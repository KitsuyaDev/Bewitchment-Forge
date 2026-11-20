/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import com.google.common.collect.Sets;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.block.entity.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;

public class BWBlockEntityTypes {
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Bewitchment.MOD_ID);

	public static final RegistryObject<BlockEntityType<BWChestBlockEntity>> BW_CHEST = BLOCK_ENTITY_TYPES.register("bw_chest", () -> BlockEntityType.Builder.of(BWChestBlockEntity::new, BWObjects.JUNIPER_CHEST, BWObjects.TRAPPED_JUNIPER_CHEST, BWObjects.CYPRESS_CHEST, BWObjects.TRAPPED_CYPRESS_CHEST, BWObjects.ELDER_CHEST, BWObjects.TRAPPED_ELDER_CHEST).build(null));
	public static final RegistryObject<BlockEntityType<WitchAltarBlockEntity>> WITCH_ALTAR = BLOCK_ENTITY_TYPES.register("witch_altar", () -> BlockEntityType.Builder.of(WitchAltarBlockEntity::new, merge(BWObjects.STONE_WITCH_ALTAR, BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR, BWObjects.PRISMARINE_WITCH_ALTAR, BWObjects.NETHER_BRICK_WITCH_ALTAR, BWObjects.BLACKSTONE_WITCH_ALTAR, BWObjects.GOLDEN_WITCH_ALTAR, BWObjects.END_STONE_WITCH_ALTAR, BWObjects.OBSIDIAN_WITCH_ALTAR, BWObjects.PURPUR_WITCH_ALTAR)).build(null));
	public static final RegistryObject<BlockEntityType<WitchCauldronBlockEntity>> WITCH_CAULDRON = BLOCK_ENTITY_TYPES.register("witch_cauldron", () -> BlockEntityType.Builder.of(WitchCauldronBlockEntity::new, BWObjects.WITCH_CAULDRON).build(null));
	public static final RegistryObject<BlockEntityType<GlyphBlockEntity>> GLYPH = BLOCK_ENTITY_TYPES.register("glyph", () -> BlockEntityType.Builder.of(GlyphBlockEntity::new, BWObjects.GOLDEN_GLYPH).build(null));
	public static final RegistryObject<BlockEntityType<BrazierBlockEntity>> BRAZIER = BLOCK_ENTITY_TYPES.register("brazier", () -> BlockEntityType.Builder.of(BrazierBlockEntity::new, BWObjects.BRAZIER).build(null));
	public static final RegistryObject<BlockEntityType<TaglockHolderBlockEntity>> TAGLOCK_HOLDER = BLOCK_ENTITY_TYPES.register("taglock_holder", () -> BlockEntityType.Builder.of(TaglockHolderBlockEntity::new, BWObjects.JUNIPER_FENCE_GATE, BWObjects.JUNIPER_PRESSURE_PLATE, BWObjects.JUNIPER_BUTTON, BWObjects.JUNIPER_TRAPDOOR, BWObjects.JUNIPER_DOOR).build(null));
	public static final RegistryObject<BlockEntityType<LockableBlockEntity>> LOCKABLE = BLOCK_ENTITY_TYPES.register("lockable", () -> BlockEntityType.Builder.of(LockableBlockEntity::new, BWObjects.ELDER_FENCE_GATE, BWObjects.ELDER_PRESSURE_PLATE, BWObjects.ELDER_BUTTON, BWObjects.ELDER_TRAPDOOR, BWObjects.ELDER_DOOR).build(null));
	public static final RegistryObject<BlockEntityType<SigilBlockEntity>> SIGIL = BLOCK_ENTITY_TYPES.register("sigil", () -> BlockEntityType.Builder.of(SigilBlockEntity::new, BWObjects.SIGIL, BWObjects.DRAGONS_BLOOD_FENCE_GATE, BWObjects.DRAGONS_BLOOD_PRESSURE_PLATE, BWObjects.DRAGONS_BLOOD_BUTTON, BWObjects.DRAGONS_BLOOD_TRAPDOOR, BWObjects.DRAGONS_BLOOD_DOOR).build(null));
	public static final RegistryObject<BlockEntityType<PoppetShelfBlockEntity>> POPPET_SHELF = BLOCK_ENTITY_TYPES.register("poppet_shelf", () -> BlockEntityType.Builder.of(PoppetShelfBlockEntity::new, BWObjects.OAK_POPPET_SHELF, BWObjects.SPRUCE_POPPET_SHELF, BWObjects.BIRCH_POPPET_SHELF, BWObjects.JUNGLE_POPPET_SHELF, BWObjects.ACACIA_POPPET_SHELF, BWObjects.DARK_OAK_POPPET_SHELF, BWObjects.CRIMSON_POPPET_SHELF, BWObjects.WARPED_POPPET_SHELF, BWObjects.JUNIPER_POPPET_SHELF, BWObjects.CYPRESS_POPPET_SHELF, BWObjects.ELDER_POPPET_SHELF, BWObjects.DRAGONS_BLOOD_POPPET_SHELF).build(null));
	public static final RegistryObject<BlockEntityType<JuniperChestBlockEntity>> JUNIPER_CHEST = BLOCK_ENTITY_TYPES.register("juniper_chest", () -> BlockEntityType.Builder.of(JuniperChestBlockEntity::new, BWObjects.JUNIPER_CHEST, BWObjects.TRAPPED_JUNIPER_CHEST).build(null));
	public static final RegistryObject<BlockEntityType<ElderChestBlockEntity>> ELDER_CHEST = BLOCK_ENTITY_TYPES.register("elder_chest", () -> BlockEntityType.Builder.of(ElderChestBlockEntity::new, BWObjects.ELDER_CHEST, BWObjects.TRAPPED_ELDER_CHEST).build(null));
	public static final RegistryObject<BlockEntityType<DragonsBloodChestBlockEntity>> DRAGONS_BLOOD_CHEST = BLOCK_ENTITY_TYPES.register("dragons_blood_chest", () -> BlockEntityType.Builder.of(DragonsBloodChestBlockEntity::new, BWObjects.DRAGONS_BLOOD_CHEST, BWObjects.TRAPPED_DRAGONS_BLOOD_CHEST).build(null));

	private static Block[] merge(Block[]... blockArrays) {
		Set<Block> merged = new HashSet<>();
		for (Block[] blockArray : blockArrays) {
			merged.addAll(Sets.newHashSet(blockArray));
		}
		return merged.toArray(new Block[0]);
	}

	public static void register(IEventBus modBus) {
		BLOCK_ENTITY_TYPES.register(modBus);
	}
}
