/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.entity.BWBoat;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.HitResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BWBoatTypes {
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bewitchment.MOD_ID);
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Bewitchment.MOD_ID);

	public static void register(IEventBus modBus) {
		// Initialize boat variants before wiring their deferred registers to the mod bus.
		for (Wood wood : Wood.values()) {
			registerBoat(wood, false);
			registerBoat(wood, true);
		}
		ITEMS.register(modBus);
		ENTITIES.register(modBus);
	}

	public static void addToTab(CreativeModeTab.Output output) {
		ITEMS.getEntries().forEach(item -> output.accept(item.get()));
	}

	public static CompoundTag convertTerraformBoat(CompoundTag tag) {
		String entityId = tag.getString("id");
		boolean chest = entityId.equals("terraform:chest_boat");
		if (!chest && !entityId.equals("terraform:boat")) {
			return tag;
		}

		String legacyType = tag.getString("TerraformBoat");
		for (Wood wood : Wood.values()) {
			if (legacyType.equals(Bewitchment.id(wood.name + "_boat").toString())) {
				CompoundTag converted = tag.copy();
				converted.putString("id", Bewitchment.id(wood.name + (chest ? "_chest_boat" : "_boat")).toString());
				converted.remove("TerraformBoat");
				return converted;
			}
		}
		return tag;
	}

	private static void registerBoat(Wood wood, boolean chest) {
		String id = wood.name + (chest ? "_chest_boat" : "_boat");
		RegistryObject<EntityType<Boat>> entity = ENTITIES.register(id, () -> EntityType.Builder.<Boat>of(
						(type, level) -> chest ? new BWBoat.Chest(type, level, wood) : new BWBoat.Open(type, level, wood),
						MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(Bewitchment.id(id).toString()));
		RegistryObject<Item> item = ITEMS.register(id, () -> new BoatItem(chest, Boat.Type.OAK, new Item.Properties().stacksTo(1)) {
			@Override
			protected Boat getBoat(Level level, HitResult hit) {
				Boat boat = entity.get().create(level);
				boat.setPos(hit.getLocation());
				boat.xo = boat.getX();
				boat.yo = boat.getY();
				boat.zo = boat.getZ();
				return boat;
			}
		});
		if (chest) {
			wood.chestEntity = entity;
			wood.chestItem = item;
		} else {
			wood.boatEntity = entity;
			wood.boatItem = item;
		}
	}

	public enum Wood {
		JUNIPER("juniper", () -> BWObjects.JUNIPER_PLANKS),
		CYPRESS("cypress", () -> BWObjects.CYPRESS_PLANKS),
		ELDER("elder", () -> BWObjects.ELDER_PLANKS),
		DRAGONS_BLOOD("dragons_blood", () -> BWObjects.DRAGONS_BLOOD_PLANKS);

		private final String name;
		private final Supplier<Block> planks;
		private RegistryObject<EntityType<Boat>> boatEntity;
		private RegistryObject<EntityType<Boat>> chestEntity;
		private RegistryObject<Item> boatItem;
		private RegistryObject<Item> chestItem;

		Wood(String name, Supplier<Block> planks) {
			this.name = name;
			this.planks = planks;
		}

		public String woodName() {
			return name;
		}

		public Block planks() {
			return planks.get();
		}

		public EntityType<Boat> entity(boolean chest) {
			return (chest ? chestEntity : boatEntity).get();
		}

		public Item item(boolean chest) {
			return (chest ? chestItem : boatItem).get();
		}
	}
}
