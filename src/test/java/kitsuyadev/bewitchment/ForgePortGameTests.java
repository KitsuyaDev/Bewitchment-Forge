/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment;

import com.mojang.authlib.GameProfile;
import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.api.registry.Contract;
import kitsuyadev.bewitchment.api.registry.Curse;
import kitsuyadev.bewitchment.api.registry.Fortune;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.block.entity.GlyphBlockEntity;
import kitsuyadev.bewitchment.common.block.entity.WitchAltarBlockEntity;
import kitsuyadev.bewitchment.common.registry.BWBoatTypes;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import kitsuyadev.bewitchment.common.registry.BWContracts;
import kitsuyadev.bewitchment.common.registry.BWCurses;
import kitsuyadev.bewitchment.common.registry.BWEntityTypes;
import kitsuyadev.bewitchment.common.registry.BWFortunes;
import kitsuyadev.bewitchment.common.registry.BWObjects;
import kitsuyadev.bewitchment.common.registry.BWRecipeTypes;
import kitsuyadev.bewitchment.common.registry.BWRitualFunctions;
import kitsuyadev.bewitchment.common.registry.BWTransformations;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.UUID;
import java.util.stream.LongStream;

@GameTestHolder(Bewitchment.MOD_ID)
@PrefixGameTestTemplate(false)
public class ForgePortGameTests {
	@GameTest(template = "port_empty")
	public static void registryAndRecipeDataLoad(GameTestHelper helper) {
		helper.assertTrue(ForgeRegistries.ITEMS.getValue(Bewitchment.id("book_of_shadows")) == BWObjects.BOOK_OF_SHADOWS,
				"The Book of Shadows must retain its item registry ID");
		helper.assertTrue(ForgeRegistries.ENTITY_TYPES.getValue(Bewitchment.id("toad")) == BWEntityTypes.TOAD.get(),
				"The toad must retain its entity type ID");
		var recipes = helper.getLevel().getRecipeManager();
		helper.assertTrue(!recipes.getAllRecipesFor(BWRecipeTypes.RITUAL_RECIPE_TYPE.get()).isEmpty(),
				"Ritual recipes must load with their Forge serializers");
		helper.assertTrue(!recipes.getAllRecipesFor(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE.get()).isEmpty(),
				"Cauldron brewing recipes must load with their Forge serializers");
		helper.succeed();
	}

	@GameTest(template = "port_empty")
	public static void familiarStateSurvivesNbtRoundTrip(GameTestHelper helper) {
		Entity original = helper.spawn(BWEntityTypes.TOAD.get(), 1, 1, 1);
		BWComponents.FAMILIAR_COMPONENT.get(original).setFamiliar(true);
		CompoundTag saved = original.saveWithoutId(new CompoundTag());
		Entity loaded = BWEntityTypes.TOAD.get().create(helper.getLevel());
		helper.assertTrue(loaded != null, "The toad entity type must create a toad");
		loaded.load(saved);
		helper.assertTrue(BWComponents.FAMILIAR_COMPONENT.get(loaded).isFamiliar(),
				"Familiar state must survive Forge capability serialization");
		helper.succeed();
	}

	@GameTest(template = "port_empty")
	public static void legacyFamiliarStateImports(GameTestHelper helper) {
		Entity loaded = BWEntityTypes.TOAD.get().create(helper.getLevel());
		helper.assertTrue(loaded != null, "The toad entity type must create a toad");
		CompoundTag saved = loaded.saveWithoutId(new CompoundTag());
		saved.remove("ForgeCaps");
		CompoundTag familiar = new CompoundTag();
		familiar.putBoolean("Familiar", true);
		CompoundTag components = new CompoundTag();
		components.put("bewitchment:familiar", familiar);
		saved.put("cardinal_components", components);
		loaded.load(saved);
		helper.assertTrue(BWComponents.FAMILIAR_COMPONENT.get(loaded).isFamiliar(),
				"A legacy CCA familiar payload must be read from the entity NBT");
		helper.succeed();
	}

	@GameTest(template = "port_empty")
	public static void altarPowerIsSpentOnlyForRealDrains(GameTestHelper helper) {
		BlockPos origin = new BlockPos(1, 0, 1);
		helper.setBlock(origin, BWObjects.STONE_WITCH_ALTAR[1]);
		BlockEntity blockEntity = helper.getLevel().getBlockEntity(helper.absolutePos(origin));
		helper.assertTrue(blockEntity instanceof WitchAltarBlockEntity, "The formed altar block must create its block entity");
		WitchAltarBlockEntity altar = (WitchAltarBlockEntity) blockEntity;
		altar.maxPower = 50;
		altar.power = 50;
		helper.assertTrue(altar.drain(20, true) && altar.power == 50, "Simulating a drain must preserve altar power");
		helper.assertTrue(altar.drain(20, false) && altar.power == 30, "A successful drain must spend altar power");
		helper.assertTrue(!altar.drain(31, false) && altar.power == 30, "An unaffordable ritual must preserve altar power");
		helper.succeed();
	}

	@GameTest(template = "port_empty")
	public static void terraformChestBoatPreservesIdentityAndCargo(GameTestHelper helper) {
		ChestBoat boat = (ChestBoat) BWBoatTypes.Wood.CYPRESS.entity(true).create(helper.getLevel());
		boat.setItem(9, new ItemStack(Items.DIAMOND, 7));
		CompoundTag legacy = boat.saveWithoutId(new CompoundTag());
		legacy.putString("id", "terraform:chest_boat");
		legacy.putString("TerraformBoat", "bewitchment:cypress_boat");

		var loaded = EntityType.create(legacy, helper.getLevel()).orElseThrow();
		helper.assertTrue(loaded.getType() == BWBoatTypes.Wood.CYPRESS.entity(true), "The legacy chest boat must retain its wood and chest variant");
		helper.assertTrue(loaded.getUUID().equals(boat.getUUID()), "The boat UUID must stay the same");
		ItemStack cargo = ((ChestBoat) loaded).getItem(9);
		helper.assertTrue(cargo.is(Items.DIAMOND) && cargo.getCount() == 7, "The same slot must contain all seven diamonds");
		helper.assertTrue(legacy.getString("id").equals("terraform:chest_boat"), "Loading must preserve the input NBT");

		legacy.putString("TerraformBoat", "othermod:cypress_boat");
		helper.assertTrue(BWBoatTypes.convertTerraformBoat(legacy) == legacy, "Foreign Terraform variants must remain outside this conversion");
		helper.succeed();
	}

	@GameTest(template = "port_empty")
	public static void trinketsMigrationQueuesItemsAndDoesNotReplay(GameTestHelper helper) {
		ServerPlayer player = new ServerPlayer(helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.randomUUID(), "TrinketsFixture"));
		for (int slot = 0; slot < player.getInventory().items.size(); slot++) {
			player.getInventory().setItem(slot, new ItemStack(Items.COBBLESTONE));
		}
		CompoundTag legacy = player.saveWithoutId(new CompoundTag());
		ItemStack trinket = new ItemStack(Items.DIAMOND, 2);
		trinket.getOrCreateTag().putString("Example", "preserve");
		ListTag items = new ListTag();
		items.add(trinket.save(new CompoundTag()));
		CompoundTag unavailable = trinket.save(new CompoundTag());
		unavailable.putString("id", "unavailable:legacy_accessory");
		items.add(unavailable);
		CompoundTag belt = new CompoundTag();
		belt.put("Items", items);
		CompoundTag chest = new CompoundTag();
		chest.put("belt", belt);
		CompoundTag trinkets = new CompoundTag();
		trinkets.put("chest", chest);
		CompoundTag components = new CompoundTag();
		components.put("trinkets:trinkets", trinkets);
		legacy.put("cardinal_components", components);

		player.load(legacy);
		helper.assertTrue(player.getInventory().getItem(0).is(Items.COBBLESTONE), "A full inventory must stay intact");
		player.getInventory().removeItemNoUpdate(0);
		CompoundTag queued = player.saveWithoutId(new CompoundTag());
		player.load(queued);
		ItemStack migrated = player.getInventory().getItem(0);
		helper.assertTrue(ItemStack.matches(migrated, trinket), "The queued stack must survive save and load with its count and tag");

		player.getInventory().removeItemNoUpdate(0);
		CompoundTag saved = player.saveWithoutId(new CompoundTag());
		saved.put("cardinal_components", components);
		player.load(saved);
		helper.assertTrue(player.getInventory().getItem(0).isEmpty(), "An already imported legacy payload must not replay");
		ListTag remaining = player.saveWithoutId(new CompoundTag()).getCompound("ForgeData").getCompound("PlayerPersisted")
				.getCompound("bewitchment:trinkets_migration").getList("Pending", Tag.TAG_COMPOUND);
		helper.assertTrue(remaining.size() == 1 && remaining.getCompound(0).equals(unavailable),
				"An unknown legacy item must retain its exact recovery data after multiple saves");
		helper.succeed();
	}

	@GameTest(template = "port_empty")
	public static void brewerProjectilesPreserveDurationAndPolymorph(GameTestHelper helper) {
		var level = helper.getLevel();
		UUID identity = UUID.randomUUID();
		ItemStack tipped = brew(Items.TIPPED_ARROW.getDefaultInstance(), identity);
		var arrow = EntityType.ARROW.create(level);
		arrow.setEffectsFromItem(tipped);
		ListTag arrowEffects = arrow.saveWithoutId(new CompoundTag()).getList("CustomPotionEffects", Tag.TAG_COMPOUND);
		helper.assertTrue(arrowEffects.getCompound(0).getInt("Duration") == 20, "A Bewitchment arrow must divide the duration by eight");
		helper.assertTrue(identity.equals(BWComponents.POLYMORPH_COMPONENT.get(arrow).getUuid()), "The arrow must keep the brew's identity");

		BlockPos impact = new BlockPos(1, 0, 1);
		helper.setBlock(impact, Blocks.STONE);
		var thrown = EntityType.POTION.create(level);
		thrown.setItem(brew(Items.LINGERING_POTION.getDefaultInstance(), identity));
		thrown.setPos(helper.absoluteVec(new Vec3(1.5, 2.5, 1.5)));
		thrown.setDeltaMovement(0, -0.4, 0);
		level.addFreshEntity(thrown);
		helper.succeedWhen(() -> {
			var clouds = level.getEntitiesOfClass(AreaEffectCloud.class,
					new net.minecraft.world.phys.AABB(helper.absolutePos(impact)).inflate(2));
			helper.assertTrue(!clouds.isEmpty(), "The lingering potion must create a cloud on impact");
			AreaEffectCloud cloud = clouds.get(0);
			ListTag effects = cloud.saveWithoutId(new CompoundTag()).getList("Effects", Tag.TAG_COMPOUND);
			helper.assertTrue(effects.getCompound(0).getInt("Duration") == 40, "A Bewitchment cloud must divide the duration by four");
			helper.assertTrue(identity.equals(BWComponents.POLYMORPH_COMPONENT.get(cloud).getUuid()), "The cloud must keep the brew's identity");
		});
	}

	@GameTest(template = "port_empty", batch = "ritual", timeoutTicks = 160)
	public static void droughtRitualConsumesReagentsAndClearsRain(GameTestHelper helper) {
		var level = helper.getLevel();
		BlockPos center = new BlockPos(3, 1, 3);
		BlockPos absolute = helper.absolutePos(center);
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				int major = Math.max(Math.abs(x), Math.abs(z));
				int minor = Math.min(Math.abs(x), Math.abs(z));
				if ((major == 3 && minor <= 1) || (major == 2 && minor == 2)) {
					helper.setBlock(center.offset(x, -1, z), Blocks.STONE);
					helper.setBlock(center.offset(x, 0, z), BWObjects.FIERY_GLYPH);
				}
			}
		}
		helper.setBlock(center.below(), Blocks.STONE);
		helper.setBlock(center, BWObjects.GOLDEN_GLYPH);
		BlockPos altarPos = center.above();
		helper.setBlock(altarPos, BWObjects.STONE_WITCH_ALTAR[1]);
		var altar = (WitchAltarBlockEntity) level.getBlockEntity(helper.absolutePos(altarPos));
		var glyph = (GlyphBlockEntity) level.getBlockEntity(absolute);
		altar.maxPower = 200;
		altar.power = 200;
		glyph.setAltarPos(helper.absolutePos(altarPos));

		level.setWeatherParameters(0, 1200, true, false);
		level.setRainLevel(1);
		var serum = new net.minecraft.world.entity.item.ItemEntity(level, absolute.getX() + 0.4, absolute.getY(), absolute.getZ() + 0.5,
				new ItemStack(BWObjects.FIERY_SERUM));
		var coal = new net.minecraft.world.entity.item.ItemEntity(level, absolute.getX() + 0.6, absolute.getY(), absolute.getZ() + 0.5,
				new ItemStack(Items.COAL));
		level.addFreshEntity(serum);
		level.addFreshEntity(coal);
		glyph.onUse(level, absolute, helper.makeMockPlayer(), InteractionHand.MAIN_HAND, null);
		helper.assertTrue(glyph.ritualFunction == BWRitualFunctions.STOP_RAIN, "The drought recipe and chalk ring must start the ritual");
		helper.assertTrue(altar.power == 50 && serum.getItem().isEmpty() && coal.getItem().isEmpty(),
				"The ritual must spend 150 power and consume one of each reagent");
		helper.succeedWhen(() -> helper.assertTrue(glyph.ritualFunction == null && !level.getLevelData().isRaining(),
				"The ritual must finish and clear the rain"));
	}

	@GameTest(template = "port_empty")
	public static void playerCloneFollowsComponentCopyRules(GameTestHelper helper) {
		var level = helper.getLevel();
		var profile = new GameProfile(UUID.randomUUID(), "ComponentFixture");
		ServerPlayer original = new ServerPlayer(level.getServer(), level, profile);
		var transformation = BWComponents.TRANSFORMATION_COMPONENT.get(original);
		transformation.setTransformation(BWTransformations.VAMPIRE);
		transformation.setAlternateForm(true);
		original.setPose(Pose.SWIMMING);
		helper.assertTrue(original.getPose() == Pose.STANDING, "An active vampire form must still block the swimming pose");
		BWComponents.MAGIC_COMPONENT.get(original).setMagic(37);
		BWComponents.CURSES_COMPONENT.get(original).addCurse(new Curse.Instance(BWCurses.OUTRAGE, 600));
		List.of(BWContracts.LUST, BWContracts.GREED, BWContracts.WRATH).forEach(contract ->
				BWComponents.CONTRACTS_COMPONENT.get(original).addContract(new Contract.Instance(contract, 600, 1)));
		ServerPlayer reloaded = new ServerPlayer(level.getServer(), level, profile);
		reloaded.load(original.saveWithoutId(new CompoundTag()));
		helper.assertTrue(BWComponents.MAGIC_COMPONENT.get(reloaded).getMagic() == 37, "Magic must survive save and load");
		helper.assertTrue(BWComponents.CURSES_COMPONENT.get(reloaded).hasCurse(BWCurses.OUTRAGE), "Curses must survive save and load");

		// Forge invalidates the old player's capabilities before its connection stops ticking it.
		reloaded.remove(Entity.RemovalReason.KILLED);
		helper.assertTrue(BWComponents.TRANSFORMATION_COMPONENT.maybeGet(reloaded).isEmpty(), "The dead player's capabilities must be invalid");
		reloaded.setPose(Pose.STANDING);
		reloaded.setPose(Pose.SWIMMING);
		helper.assertTrue(reloaded.getPose() == Pose.SWIMMING, "An unavailable transformation must leave the vanilla pose alone");
		helper.assertTrue(!BewitchmentAPI.isVampire(reloaded, true) && !BewitchmentAPI.isWerewolf(reloaded, true),
				"Transformation queries must tolerate capability invalidation");

		ServerPlayer respawned = new ServerPlayer(level.getServer(), level, profile);
		MinecraftForge.EVENT_BUS.post(new PlayerEvent.Clone(respawned, reloaded, true));
		helper.assertTrue(BWComponents.TRANSFORMATION_COMPONENT.get(respawned).getTransformation() == BWTransformations.VAMPIRE,
				"The vampire transformation must survive death");
		helper.assertTrue(BWComponents.CURSES_COMPONENT.get(respawned).hasCurse(BWCurses.OUTRAGE), "The curse must survive death");
		helper.assertTrue(BWComponents.MAGIC_COMPONENT.get(respawned).getMagic() == 0, "Magic must reset on death");
		var contracts = BWComponents.CONTRACTS_COMPONENT.get(respawned);
		helper.assertTrue(contracts.getContracts().size() == 3 && contracts.hasContract(BWContracts.LUST)
				&& contracts.hasContract(BWContracts.GREED) && contracts.hasContract(BWContracts.WRATH),
				"All three contracts must survive respawn without duplication");
		helper.succeed();
	}

	@GameTest(template = "port_empty")
	public static void powerFortuneSurvivesSaveAndTriggersInCombat(GameTestHelper helper) {
		var level = helper.getLevel();
		var profile = new GameProfile(UUID.randomUUID(), "FortuneFixture");
		ServerPlayer original = new ServerPlayer(level.getServer(), level, profile);
		BWComponents.FORTUNE_COMPONENT.get(original).setFortune(new Fortune.Instance(BWFortunes.POWER, 200));
		var reloaded = helper.makeMockPlayer();
		reloaded.load(original.saveWithoutId(new CompoundTag()));
		var fortune = BWComponents.FORTUNE_COMPONENT.get(reloaded);
		helper.assertTrue(fortune.getFortune() != null && fortune.getFortune().duration == 200,
				"The Power fortune and its time remaining must survive save and load");
		reloaded.setLastHurtByMob(helper.spawn(BWEntityTypes.TOAD.get(), 1, 1, 1));
		fortune.serverTick();
		helper.assertTrue(reloaded.hasEffect(MobEffects.DAMAGE_BOOST) && reloaded.hasEffect(MobEffects.DAMAGE_RESISTANCE),
				"Combat must grant both Power fortune effects");
		helper.assertTrue(fortune.getFortune() == null, "The triggered fortune must be consumed");
		helper.succeed();
	}

	@GameTest(template = "port_empty")
	public static void grassAndFortressLootOfferBewitchmentPlants(GameTestHelper helper) {
		var level = helper.getLevel();
		var origin = helper.absoluteVec(new Vec3(1.5, 1, 1.5));
		var loot = level.getServer().getLootData();
		var grass = loot.getLootTable(new ResourceLocation("minecraft", "blocks/grass"));
		var openHand = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, origin)
				.withParameter(LootContextParams.BLOCK_STATE, Blocks.GRASS.defaultBlockState())
				.withParameter(LootContextParams.TOOL, ItemStack.EMPTY).create(LootContextParamSets.BLOCK);
		helper.assertTrue(LongStream.rangeClosed(1, 256).anyMatch(seed -> grass.getRandomItems(openHand, seed).stream()
				.anyMatch(stack -> stack.is(BWObjects.ACONITE_SEEDS) || stack.is(BWObjects.BELLADONNA_SEEDS)
						|| stack.is(BWObjects.GARLIC) || stack.is(BWObjects.MANDRAKE_SEEDS))),
				"Grass loot must offer Bewitchment herbs or seeds without shears");
		var chest = new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, origin).create(LootContextParamSets.CHEST);
		var fortress = loot.getLootTable(new ResourceLocation("minecraft", "chests/nether_bridge"));
		helper.assertTrue(fortress.getRandomItems(chest, 1L).stream().anyMatch(stack -> stack.is(BWObjects.DRAGONS_BLOOD_SAPLING.asItem())),
				"Nether fortress loot must offer a dragon's blood sapling");
		helper.succeed();
	}

	private static ItemStack brew(ItemStack stack, UUID identity) {
		PotionUtils.setCustomEffects(stack, List.of(new MobEffectInstance(MobEffects.POISON, 160)));
		stack.getOrCreateTag().putBoolean("BewitchmentBrew", true);
		stack.getOrCreateTag().putUUID("PolymorphUUID", identity);
		stack.getOrCreateTag().putString("PolymorphName", "Validation");
		return stack;
	}
}
