/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common;

import eu.midnightdust.lib.config.MidnightConfig;
import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.api.component.BloodComponent;
import kitsuyadev.bewitchment.api.event.BloodSuckEvents;
import kitsuyadev.bewitchment.common.block.CoffinBlock;
import kitsuyadev.bewitchment.common.block.entity.BrazierBlockEntity;
import kitsuyadev.bewitchment.common.block.entity.GlyphBlockEntity;
import kitsuyadev.bewitchment.common.block.entity.SigilBlockEntity;
import kitsuyadev.bewitchment.common.item.AthameItem;
import kitsuyadev.bewitchment.common.misc.BWUtil;
import kitsuyadev.bewitchment.common.recipe.AthameDropRecipe;
import kitsuyadev.bewitchment.common.recipe.IncenseRecipe;
import kitsuyadev.bewitchment.common.registry.*;
import kitsuyadev.bewitchment.common.world.BWWorldState;
import kitsuyadev.bewitchment.common.statuseffect.CorruptionStatusEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import kitsuyadev.bewitchment.common.component.EntityComponentData;
import kitsuyadev.bewitchment.common.packet.BWNetwork;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Bewitchment.MOD_ID)
public class Bewitchment {
	public static final String MOD_ID = "bewitchment";

	public Bewitchment() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		BWRegistries.register(modBus);
		BWCommands.register(modBus);
		modBus.addListener(BWObjects::registerEntries);
		BWEntityTypes.register(modBus);
		BWBoatTypes.register(modBus);
		BWBlockEntityTypes.register(modBus);
		BWStatusEffects.register(modBus);
		BWEnchantments.register(modBus);
		BWSoundEvents.register(modBus);
		BWParticleTypes.register(modBus);
		BWRecipeTypes.register(modBus);
		BWScreenHandlerTypes.register(modBus);
		BWWorldGenerators.register(modBus);
		BWNetwork.register();
		modBus.addListener(Bewitchment::setup);

		forgeBus.addListener((RegisterCommandsEvent event) ->
				BWCommands.init(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection()));
		forgeBus.addListener((ServerStoppedEvent event) -> BewitchmentAPI.fakePlayer = null);
		forgeBus.addListener((PlayerEvent.PlayerRespawnEvent event) -> {
			BWComponents.TRANSFORMATION_COMPONENT.get(event.getEntity()).setAlternateForm(false);
			BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(event.getEntity()).setForcedTransformation(false);
		});
		forgeBus.addListener(EventPriority.LOWEST, Bewitchment::onDeath);
		forgeBus.addListener(Bewitchment::onEntityInteract);
		forgeBus.addListener(Bewitchment::onVoodooDrowning);
		forgeBus.addListener(Bewitchment::checkSleepingTime);
		forgeBus.addListener(Bewitchment::checkSleeping);
		forgeBus.addListener(BWObjects::onFuelBurn);
		EntityComponentData.register(modBus, forgeBus);
	}

	private static void onDeath(LivingDeathEvent event) {
		LivingEntity killedEntity = event.getEntity();
		Entity attacker = event.getSource().getEntity();
		if (killedEntity.level() instanceof ServerLevel world && attacker instanceof LivingEntity livingEntity) {
			if (livingEntity instanceof Player player && killedEntity.getMobType() == MobType.ARTHROPOD && BewitchmentAPI.getFamiliar(player) == BWEntityTypes.TOAD.get()) {
				livingEntity.heal(livingEntity.getMaxHealth() * 1 / 4f);
			}
			if (livingEntity instanceof Player player && BWComponents.CONTRACTS_COMPONENT.get(player).hasContract(BWContracts.DEATH)) {
				livingEntity.heal(livingEntity.getMaxHealth() / 4f);
			}
			if (livingEntity.getMainHandItem().getItem() instanceof AthameItem) {
				BlockPos glyphPos = BWUtil.getClosestBlockPos(killedEntity.blockPosition(), 6, currentPos -> world.getBlockEntity(currentPos) instanceof GlyphBlockEntity);
				if (glyphPos != null) {
					((GlyphBlockEntity) world.getBlockEntity(glyphPos)).onUse(world, glyphPos, livingEntity, InteractionHand.MAIN_HAND, killedEntity);
				}
				if (world.isNight()) {
					boolean chicken = killedEntity instanceof Chicken, wolf = killedEntity instanceof Wolf;
					if (chicken || wolf) {
						Holder<Biome> biome = world.getBiome(killedEntity.blockPosition());
						Mob boss = null;
						if (chicken) {
							if (biome.is(BiomeTags.IS_HILL) || biome.is(Tags.Biomes.IS_SNOWY) || biome.is(Tags.Biomes.IS_MOUNTAIN)) {
								boss = BWEntityTypes.LILITH.get().create(world);
							}
						}
						if (wolf) {
							if (biome.is(BiomeTags.IS_TAIGA) || biome.is(BiomeTags.IS_FOREST)) {
								boss = BWEntityTypes.HERNE.get().create(world);
							}
						}
						if (boss != null) {
							BlockPos brazierPos = BWUtil.getClosestBlockPos(killedEntity.blockPosition(), 8, currentPos -> world.getBlockEntity(currentPos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe != null && brazier.incenseRecipe.effect == BWStatusEffects.MORTAL_COIL.get());
							if (brazierPos != null) {
								world.destroyBlock(brazierPos, false);
								world.explode(null, brazierPos.getX() + 0.5, brazierPos.getY() + 0.5, brazierPos.getZ() + 0.5, 3, Level.ExplosionInteraction.TNT);
								boss.finalizeSpawn(world, world.getCurrentDifficultyAt(brazierPos), MobSpawnType.EVENT, null, null);
								boss.absMoveTo(brazierPos.getX() + 0.5, brazierPos.getY(), brazierPos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
								world.addFreshEntity(boss);
							}
						}
					}
				}
				for (AthameDropRecipe recipe : world.getRecipeManager().getAllRecipesFor(BWRecipeTypes.ATHAME_DROP_RECIPE_TYPE.get())) {
					if (recipe.entity_type.equals(killedEntity.getType()) && world.random.nextFloat() < recipe.chance * (EnchantmentHelper.getMobLooting(livingEntity) + 1)) {
						ItemStack drop = recipe.getResultItem(world.registryAccess()).copy();
						if (recipe.entity_type == EntityType.PLAYER) {
							drop.getOrCreateTag().putString("SkullOwner", killedEntity.getName().getString());
						}
						Containers.dropItemStack(world, killedEntity.getX() + 0.5, killedEntity.getY() + 0.5, killedEntity.getZ() + 0.5, drop);
					}
				}
				if (livingEntity instanceof Player player && livingEntity.getOffhandItem().getItem() == Items.GLASS_BOTTLE && BWComponents.BLOOD_COMPONENT.get(killedEntity).getBlood() > 20 && killedEntity.getType().is(BWTags.HAS_BLOOD)) {
					world.playSound(null, livingEntity.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1, 0.5f);
					BWUtil.addItemToInventoryAndConsume(player, InteractionHand.OFF_HAND, new ItemStack(BWObjects.BOTTLE_OF_BLOOD));
				}
			}
		}
	}

	private static void onVoodooDrowning(LivingBreatheEvent event) {
		LivingEntity entity = event.getEntity();
		if (!BWComponents.ADDITIONAL_WATER_DATA_COMPONENT.get(entity).isSubmerged()) {
			return;
		}
		event.setCanRefillAir(false);
		boolean invulnerable = entity instanceof Player player && player.getAbilities().invulnerable;
		if (!invulnerable && entity.canDrownInFluidType(ForgeMod.WATER_TYPE.get()) && !MobEffectUtil.hasWaterBreathing(entity)) {
			event.setCanBreathe(false);
		}
	}

	private static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		InteractionResult result = drinkBlood(event.getEntity(), event.getLevel(), event.getHand(), event.getTarget());
		if (result.consumesAction()) {
			event.setCancellationResult(result);
			event.setCanceled(true);
		}
	}

	private static InteractionResult drinkBlood(Player player, Level world, InteractionHand hand, Entity entity) {
		if (entity instanceof LivingEntity livingEntity && hand == InteractionHand.MAIN_HAND && player.isShiftKeyDown() && entity.isAlive() && BewitchmentAPI.isVampire(player, true) && player.getItemInHand(hand).isEmpty()) {
			int toGive = entity.getType().is(BWTags.HAS_BLOOD) ? 5 : entity instanceof Animal ? 1 : 0;
			toGive = BloodSuckEvents.bloodAmount(player, livingEntity, toGive);
			if (toGive > 0) {
				BloodComponent playerBloodComponent = BWComponents.BLOOD_COMPONENT.get(player);
				BloodComponent entityBloodComponent = BWComponents.BLOOD_COMPONENT.get(livingEntity);
				if (playerBloodComponent.fillBlood(toGive, true) && entityBloodComponent.drainBlood(10, true)) {
					if (!world.isClientSide && livingEntity.hurtTime == 0) {
						MinecraftForge.EVENT_BUS.post(new BloodSuckEvents.OnBloodSuck(player, livingEntity, toGive));
						world.playSound(null, player.blockPosition(), SoundEvents.HONEY_DRINK, player.getSoundSource(), 1, 0.5f);
						if (!livingEntity.isSleeping() || entityBloodComponent.getBlood() < BloodComponent.MAX_BLOOD / 2) {
							entity.hurt(BWDamageSources.create(entity.level(), BWDamageSources.VAMPIRE), 2);
						}
						playerBloodComponent.fillBlood(toGive, false);
						entityBloodComponent.drainBlood(10, false);
					}
					return InteractionResult.sidedSuccess(world.isClientSide);
				}
			}
		}
		return InteractionResult.PASS;
	}

	private static void checkSleepingTime(SleepingTimeCheckEvent event) {
		Level level = event.getEntity().level();
		if (level.isDay() && event.getSleepingLocation().filter(pos -> level.getBlockState(pos).getBlock() instanceof CoffinBlock).isPresent()) {
			event.setResult(Event.Result.ALLOW);
		}
	}

	private static void checkSleeping(PlayerSleepInBedEvent event) {
		Player player = event.getEntity();
		if (BWComponents.TRANSFORMATION_COMPONENT.get(player).isAlternateForm()) {
			player.displayClientMessage(Component.translatable("block.minecraft.bed.transformation"), true);
			event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
		} else if (player.level().isNight() && event.getOptionalPos()
				.filter(pos -> player.level().getBlockState(pos).getBlock() instanceof CoffinBlock).isPresent()) {
			player.displayClientMessage(Component.translatable("block.minecraft.bed.coffin"), true);
			event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
		}
	}

	public static void onWake(LivingEntity entity) {
		if (!entity.level().isClientSide) {
			BWWorldState worldState = BWWorldState.get(entity.level());
			for (BlockPos foundPos : BWUtil.getBlockPoses(entity.blockPosition(), 12)) {
				if (entity.level().getWorldBorder().isWithinBounds(foundPos) && entity.level().getBlockEntity(foundPos) instanceof BrazierBlockEntity brazier && brazier.incenseRecipe != null) {
					IncenseRecipe recipe = brazier.incenseRecipe;
					int durationMultiplier = 1;
					for (BlockPos sigilPos : BWUtil.getBlockPoses(foundPos, 16)) {
						if (entity.level().getWorldBorder().isWithinBounds(foundPos) && worldState.potentialSigils.contains(sigilPos.asLong()) && entity.level().getBlockEntity(sigilPos) instanceof SigilBlockEntity sigil && sigil.getSigil() == BWSigils.EXTENDING) {
							if (sigil.test(entity)) {
								sigil.setUses(sigil.getUses() - 1);
								sigil.setChanged();
								durationMultiplier = 2;
								break;
							}
						}
					}
					entity.addEffect(new MobEffectInstance(recipe.effect, 24000 * durationMultiplier, recipe.amplifier, true, false));
				}
			}
		}
	}

	private static void setup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			// MidnightLib mutates global configuration bookkeeping during registration.
			MidnightConfig.init(MOD_ID, BWConfig.class);
			BWScaleTypes.init();
			CorruptionStatusEffect.registerModEffectInverses();
			BWObjects.setup();
			BewitchmentAPI.registerAltarMapEntries(BWObjects.STONE_WITCH_ALTAR);
			BewitchmentAPI.registerAltarMapEntries(BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR);
			BewitchmentAPI.registerAltarMapEntries(BWObjects.PRISMARINE_WITCH_ALTAR);
			BewitchmentAPI.registerAltarMapEntries(BWObjects.NETHER_BRICK_WITCH_ALTAR);
			BewitchmentAPI.registerAltarMapEntries(BWObjects.BLACKSTONE_WITCH_ALTAR);
			BewitchmentAPI.registerAltarMapEntries(BWObjects.GOLDEN_WITCH_ALTAR);
			BewitchmentAPI.registerAltarMapEntries(BWObjects.END_STONE_WITCH_ALTAR);
			BewitchmentAPI.registerAltarMapEntries(BWObjects.OBSIDIAN_WITCH_ALTAR);
			BewitchmentAPI.registerAltarMapEntries(BWObjects.PURPUR_WITCH_ALTAR);
		});
	}

	public static ResourceLocation id(String value) {
		return new ResourceLocation(MOD_ID, value);
	}
}
