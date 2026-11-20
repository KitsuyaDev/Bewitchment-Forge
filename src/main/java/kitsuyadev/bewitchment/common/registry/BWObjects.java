/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.api.block.CandelabraBlock;
import kitsuyadev.bewitchment.api.block.PoppetShelfBlock;
import kitsuyadev.bewitchment.api.block.WitchAltarBlock;
import kitsuyadev.bewitchment.api.item.BroomItem;
import kitsuyadev.bewitchment.api.item.PoppetItem;
import kitsuyadev.bewitchment.api.item.SigilItem;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.block.*;
import kitsuyadev.bewitchment.common.block.dragonsblood.*;
import kitsuyadev.bewitchment.common.block.elder.*;
import kitsuyadev.bewitchment.common.block.juniper.*;
import kitsuyadev.bewitchment.common.block.util.BWCropBlock;
import kitsuyadev.bewitchment.common.block.util.BWSaplingBlock;
import kitsuyadev.bewitchment.common.item.*;
import kitsuyadev.bewitchment.common.item.util.BWBookItem;
import kitsuyadev.bewitchment.common.world.generator.tree.generator.CypressSaplingGenerator;
import kitsuyadev.bewitchment.common.world.generator.tree.generator.DragonsBloodSaplingGenerator;
import kitsuyadev.bewitchment.common.world.generator.tree.generator.ElderSaplingGenerator;
import kitsuyadev.bewitchment.common.world.generator.tree.generator.JuniperSaplingGenerator;
import kitsuyadev.bewitchment.mixin.BlocksAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class BWObjects {
	private static final Map<Block, ResourceLocation> BLOCKS = new LinkedHashMap<>();
	private static final Map<Item, ResourceLocation> ITEMS = new LinkedHashMap<>();

	public static CreativeModeTab GROUP;
	private static final List<Block> SIGNS = new ArrayList<>();
	private static final List<Block> HANGING_SIGNS = new ArrayList<>();

	//misc_no_item
	public static final Block SALT_LINE = create("salt_line", new SaltLineBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE)), false);
	public static final Block TEMPORARY_COBWEB = create("temporary_cobweb", new WebBlock(BlockBehaviour.Properties.copy(Blocks.COBWEB).randomTicks()) {
		@Override
		public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}, false);
	public static final Block GOLDEN_GLYPH = create("golden_glyph", new GlyphBlock(BlockBehaviour.Properties.of().sound(new ForgeSoundType(1, 1, () -> SoundEvents.STONE_BREAK, () -> SoundEvents.STONE_STEP, BWSoundEvents.BLOCK_GLPYH_PLACE, () -> SoundEvents.STONE_HIT, () -> SoundEvents.STONE_FALL)).pushReaction(PushReaction.DESTROY).noCollission().strength(1, 0)), false);
	public static final Block GLYPH = create("glyph", new GlyphBlock(BlockBehaviour.Properties.copy(GOLDEN_GLYPH)), false);
	public static final Block FIERY_GLYPH = create("fiery_glyph", new GlyphBlock(BlockBehaviour.Properties.copy(GOLDEN_GLYPH).lightLevel(state -> 4)), false);
	public static final Block ELDRITCH_GLYPH = create("eldritch_glyph", new GlyphBlock(BlockBehaviour.Properties.copy(GOLDEN_GLYPH).lightLevel(state -> 2)), false);
	public static final Block SIGIL = create("sigil", new SigilBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).sound(SoundType.BAMBOO).noCollission().instabreak()), false);
	//crop
	public static final Block ACONITE_CROP = create("aconite", new BWCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)), false);
	public static final Block BELLADONNA_CROP = create("belladonna", new BWCropBlock(BlockBehaviour.Properties.copy(ACONITE_CROP)), false);
	public static final Block GARLIC_CROP = create("garlic", new BWCropBlock(BlockBehaviour.Properties.copy(ACONITE_CROP)), false);
	public static final Block MANDRAKE_CROP = create("mandrake", new BWCropBlock(BlockBehaviour.Properties.copy(ACONITE_CROP)), false);
	//juniper
	public static final Block JUNIPER_LOG = create("juniper_log", new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)), true);
	public static final Block STRIPPED_JUNIPER_LOG = create("stripped_juniper_log", new RotatedPillarBlock(BlockBehaviour.Properties.copy(JUNIPER_LOG)), true);
	public static final Block JUNIPER_WOOD = create("juniper_wood", new RotatedPillarBlock(BlockBehaviour.Properties.copy(JUNIPER_LOG)), true);
	public static final Block STRIPPED_JUNIPER_WOOD = create("stripped_juniper_wood", new RotatedPillarBlock(BlockBehaviour.Properties.copy(JUNIPER_LOG)), true);
	public static final Block JUNIPER_LEAVES = create("juniper_leaves", BlocksAccessor.callCreateLeavesBlock(SoundType.GRASS), true);
	public static final Block JUNIPER_SAPLING = create("juniper_sapling", new BWSaplingBlock(new JuniperSaplingGenerator(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)), true);
	public static final Block POTTED_JUNIPER_SAPLING = create("potted_juniper_sapling", new FlowerPotBlock(JUNIPER_SAPLING, BlockBehaviour.Properties.copy(Blocks.POTTED_OAK_SAPLING)), false);
	public static final Block JUNIPER_PLANKS = create("juniper_planks", new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)), true);
	public static final Block JUNIPER_STAIRS = create("juniper_stairs", new StairBlock(JUNIPER_PLANKS.defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)), true);
	public static final Block JUNIPER_SLAB = create("juniper_slab", new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)), true);
	public static final Block JUNIPER_FENCE = create("juniper_fence", new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)), true);
	public static final Block JUNIPER_FENCE_GATE = create("juniper_fence_gate", new JuniperFenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE)), true);
	public static final Block JUNIPER_PRESSURE_PLATE = create("juniper_pressure_plate", new JuniperPressurePlateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE)), true);
	public static final Block JUNIPER_BUTTON = create("juniper_button", new JuniperButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)), true);
	public static final Block JUNIPER_TRAPDOOR = create("juniper_trapdoor", new JuniperTrapdoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)), true);
	public static final Block JUNIPER_DOOR = create("juniper_door", new JuniperDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)), false);
	public static final Item JUNIPER_DOOR_ITEM = create("juniper_door", new DoubleHighBlockItem(JUNIPER_DOOR, gen()));
	public static final Block JUNIPER_CHEST = create("juniper_chest", new JuniperChestBlock(BlockBehaviour.Properties.copy(Blocks.CHEST), () -> BWBlockEntityTypes.JUNIPER_CHEST.get(), false), true);
	public static final Block TRAPPED_JUNIPER_CHEST = create("trapped_juniper_chest", new JuniperChestBlock(BlockBehaviour.Properties.copy(Blocks.CHEST), () -> BWBlockEntityTypes.JUNIPER_CHEST.get(), true), true);
	public static final Tuple<ResourceLocation, ResourceLocation> JUNIPER_SIGN = createSign("juniper");
	//cypress
	public static final Block CYPRESS_LOG = create("cypress_log", new RotatedPillarBlock(BlockBehaviour.Properties.copy(JUNIPER_LOG)), true);
	public static final Block STRIPPED_CYPRESS_LOG = create("stripped_cypress_log", new RotatedPillarBlock(BlockBehaviour.Properties.copy(JUNIPER_LOG)), true);
	public static final Block CYPRESS_WOOD = create("cypress_wood", new RotatedPillarBlock(BlockBehaviour.Properties.copy(JUNIPER_LOG)), true);
	public static final Block STRIPPED_CYPRESS_WOOD = create("stripped_cypress_wood", new RotatedPillarBlock(BlockBehaviour.Properties.copy(JUNIPER_LOG)), true);
	public static final Block CYPRESS_LEAVES = create("cypress_leaves", BlocksAccessor.callCreateLeavesBlock(SoundType.GRASS), true);
	public static final Block CYPRESS_SAPLING = create("cypress_sapling", new BWSaplingBlock(new CypressSaplingGenerator(), BlockBehaviour.Properties.copy(JUNIPER_SAPLING)), true);
	public static final Block POTTED_CYPRESS_SAPLING = create("potted_cypress_sapling", new FlowerPotBlock(CYPRESS_SAPLING, BlockBehaviour.Properties.copy(POTTED_JUNIPER_SAPLING)), false);
	public static final Block CYPRESS_PLANKS = create("cypress_planks", new Block(BlockBehaviour.Properties.copy(JUNIPER_PLANKS)), true);
	public static final Block CYPRESS_STAIRS = create("cypress_stairs", new StairBlock(CYPRESS_PLANKS.defaultBlockState(), BlockBehaviour.Properties.copy(JUNIPER_STAIRS)), true);
	public static final Block CYPRESS_SLAB = create("cypress_slab", new SlabBlock(BlockBehaviour.Properties.copy(JUNIPER_SLAB)), true);
	public static final Block CYPRESS_FENCE = create("cypress_fence", new FenceBlock(BlockBehaviour.Properties.copy(JUNIPER_FENCE)), true);
	public static final Block CYPRESS_FENCE_GATE = create("cypress_fence_gate", new FenceGateBlock(BlockBehaviour.Properties.copy(JUNIPER_FENCE_GATE), WoodType.OAK), true);
	public static final Block CYPRESS_PRESSURE_PLATE = create("cypress_pressure_plate", new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(JUNIPER_PRESSURE_PLATE), BlockSetType.OAK), true);
	public static final Block CYPRESS_BUTTON = create("cypress_button", new ButtonBlock(BlockBehaviour.Properties.copy(JUNIPER_BUTTON), BlockSetType.OAK, 30, true), true);
	public static final Block CYPRESS_TRAPDOOR = create("cypress_trapdoor", new TrapDoorBlock(BlockBehaviour.Properties.copy(JUNIPER_TRAPDOOR), BlockSetType.OAK), true);
	public static final Block CYPRESS_DOOR = create("cypress_door", new DoorBlock(BlockBehaviour.Properties.copy(JUNIPER_DOOR), BlockSetType.OAK), false);
	public static final Item CYPRESS_DOOR_ITEM = create("cypress_door", new DoubleHighBlockItem(CYPRESS_DOOR, gen()));
	public static final Block CYPRESS_CHEST = create("cypress_chest", new BWChestBlock(BlockBehaviour.Properties.copy(JUNIPER_CHEST), () -> BWBlockEntityTypes.BW_CHEST.get(), false), true);
	public static final Block TRAPPED_CYPRESS_CHEST = create("trapped_cypress_chest", new BWChestBlock(BlockBehaviour.Properties.copy(JUNIPER_CHEST), () -> BWBlockEntityTypes.BW_CHEST.get(), true), true);
	public static final Tuple<ResourceLocation, ResourceLocation> CYPRESS_SIGN = createSign("cypress");
	//elder
	public static final Block ELDER_LOG = create("elder_log", new RotatedPillarBlock(BlockBehaviour.Properties.copy(JUNIPER_LOG)), true);
	public static final Block STRIPPED_ELDER_LOG = create("stripped_elder_log", new RotatedPillarBlock(BlockBehaviour.Properties.copy(ELDER_LOG)), true);
	public static final Block ELDER_WOOD = create("elder_wood", new RotatedPillarBlock(BlockBehaviour.Properties.copy(ELDER_LOG)), true);
	public static final Block STRIPPED_ELDER_WOOD = create("stripped_elder_wood", new RotatedPillarBlock(BlockBehaviour.Properties.copy(ELDER_LOG)), true);
	public static final Block ELDER_LEAVES = create("elder_leaves", BlocksAccessor.callCreateLeavesBlock(SoundType.GRASS), true);
	public static final Block ELDER_SAPLING = create("elder_sapling", new BWSaplingBlock(new ElderSaplingGenerator(), BlockBehaviour.Properties.copy(JUNIPER_SAPLING)), true);
	public static final Block POTTED_ELDER_SAPLING = create("potted_elder_sapling", new FlowerPotBlock(ELDER_SAPLING, BlockBehaviour.Properties.copy(POTTED_JUNIPER_SAPLING)), false);
	public static final Block ELDER_PLANKS = create("elder_planks", new Block(BlockBehaviour.Properties.copy(JUNIPER_PLANKS)), true);
	public static final Block ELDER_STAIRS = create("elder_stairs", new StairBlock(ELDER_PLANKS.defaultBlockState(), BlockBehaviour.Properties.copy(JUNIPER_STAIRS)), true);
	public static final Block ELDER_SLAB = create("elder_slab", new SlabBlock(BlockBehaviour.Properties.copy(JUNIPER_SLAB)), true);
	public static final Block ELDER_FENCE = create("elder_fence", new FenceBlock(BlockBehaviour.Properties.copy(JUNIPER_FENCE)), true);
	public static final Block ELDER_FENCE_GATE = create("elder_fence_gate", new ElderFenceGateBlock(BlockBehaviour.Properties.copy(JUNIPER_FENCE_GATE)), true);
	public static final Block ELDER_PRESSURE_PLATE = create("elder_pressure_plate", new ElderPressurePlateBlock(BlockBehaviour.Properties.copy(JUNIPER_PRESSURE_PLATE)), true);
	public static final Block ELDER_BUTTON = create("elder_button", new ElderButtonBlock(BlockBehaviour.Properties.copy(JUNIPER_BUTTON)), true);
	public static final Block ELDER_TRAPDOOR = create("elder_trapdoor", new ElderTrapdoorBlock(BlockBehaviour.Properties.copy(JUNIPER_TRAPDOOR)), true);
	public static final Block ELDER_DOOR = create("elder_door", new ElderDoorBlock(BlockBehaviour.Properties.copy(JUNIPER_DOOR)), false);
	public static final Item ELDER_DOOR_ITEM = create("elder_door", new DoubleHighBlockItem(ELDER_DOOR, gen()));
	public static final Block ELDER_CHEST = create("elder_chest", new ElderChestBlock(BlockBehaviour.Properties.copy(JUNIPER_CHEST), () -> BWBlockEntityTypes.ELDER_CHEST.get(), false), true);
	public static final Block TRAPPED_ELDER_CHEST = create("trapped_elder_chest", new ElderChestBlock(BlockBehaviour.Properties.copy(JUNIPER_CHEST), () -> BWBlockEntityTypes.ELDER_CHEST.get(), true), true);
	public static final Tuple<ResourceLocation, ResourceLocation> ELDER_SIGN = createSign("elder");
	//dragons_blood
	public static final Block DRAGONS_BLOOD_LOG = create("dragons_blood_log", new DragonsBloodLogBlock(BlockBehaviour.Properties.copy(JUNIPER_LOG).randomTicks()), true);
	public static final Block STRIPPED_DRAGONS_BLOOD_LOG = create("stripped_dragons_blood_log", new RotatedPillarBlock(BlockBehaviour.Properties.copy(DRAGONS_BLOOD_LOG)), true);
	public static final Block DRAGONS_BLOOD_WOOD = create("dragons_blood_wood", new RotatedPillarBlock(BlockBehaviour.Properties.copy(DRAGONS_BLOOD_LOG)), true);
	public static final Block STRIPPED_DRAGONS_BLOOD_WOOD = create("stripped_dragons_blood_wood", new RotatedPillarBlock(BlockBehaviour.Properties.copy(DRAGONS_BLOOD_LOG)), true);
	public static final Block DRAGONS_BLOOD_LEAVES = create("dragons_blood_leaves", BlocksAccessor.callCreateLeavesBlock(SoundType.GRASS), true);
	public static final Block DRAGONS_BLOOD_SAPLING = create("dragons_blood_sapling", new BWSaplingBlock(new DragonsBloodSaplingGenerator(), BlockBehaviour.Properties.copy(JUNIPER_SAPLING)), true);
	public static final Block POTTED_DRAGONS_BLOOD_SAPLING = create("potted_dragons_blood_sapling", new FlowerPotBlock(DRAGONS_BLOOD_SAPLING, BlockBehaviour.Properties.copy(POTTED_JUNIPER_SAPLING)), false);
	public static final Block DRAGONS_BLOOD_PLANKS = create("dragons_blood_planks", new Block(BlockBehaviour.Properties.copy(JUNIPER_PLANKS)), true);
	public static final Block DRAGONS_BLOOD_STAIRS = create("dragons_blood_stairs", new StairBlock(DRAGONS_BLOOD_PLANKS.defaultBlockState(), BlockBehaviour.Properties.copy(JUNIPER_STAIRS)), true);
	public static final Block DRAGONS_BLOOD_SLAB = create("dragons_blood_slab", new SlabBlock(BlockBehaviour.Properties.copy(JUNIPER_SLAB)), true);
	public static final Block DRAGONS_BLOOD_FENCE = create("dragons_blood_fence", new FenceBlock(BlockBehaviour.Properties.copy(JUNIPER_FENCE)), true);
	public static final Block DRAGONS_BLOOD_FENCE_GATE = create("dragons_blood_fence_gate", new DragonsBloodFenceGateBlock(BlockBehaviour.Properties.copy(JUNIPER_FENCE_GATE)), true);
	public static final Block DRAGONS_BLOOD_PRESSURE_PLATE = create("dragons_blood_pressure_plate", new DragonsBloodPressurePlateBlock(BlockBehaviour.Properties.copy(JUNIPER_PRESSURE_PLATE)), true);
	public static final Block DRAGONS_BLOOD_BUTTON = create("dragons_blood_button", new DragonsBloodButtonBlock(BlockBehaviour.Properties.copy(JUNIPER_BUTTON)), true);
	public static final Block DRAGONS_BLOOD_TRAPDOOR = create("dragons_blood_trapdoor", new DragonsBloodTrapdoorBlock(BlockBehaviour.Properties.copy(JUNIPER_TRAPDOOR)), true);
	public static final Block DRAGONS_BLOOD_DOOR = create("dragons_blood_door", new DragonsBloodDoorBlock(BlockBehaviour.Properties.copy(JUNIPER_DOOR)), false);
	public static final Item DRAGONS_BLOOD_DOOR_ITEM = create("dragons_blood_door", new DoubleHighBlockItem(DRAGONS_BLOOD_DOOR, gen()));
	public static final Block DRAGONS_BLOOD_CHEST = create("dragons_blood_chest", new DragonsBloodChestBlock(BlockBehaviour.Properties.copy(JUNIPER_CHEST), () -> BWBlockEntityTypes.DRAGONS_BLOOD_CHEST.get(), false), true);
	public static final Block TRAPPED_DRAGONS_BLOOD_CHEST = create("trapped_dragons_blood_chest", new DragonsBloodChestBlock(BlockBehaviour.Properties.copy(JUNIPER_CHEST), () -> BWBlockEntityTypes.DRAGONS_BLOOD_CHEST.get(), true), true);
	public static final Tuple<ResourceLocation, ResourceLocation> DRAGONS_BLOOD_SIGN = createSign("dragons_blood");
	//other_plants
	public static final Block GLOWING_BRAMBLE = create("glowing_bramble", new BrambleBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).strength(2, 3).noCollission().randomTicks().lightLevel(state -> 15)), true);
	public static final Block ENDER_BRAMBLE = create("ender_bramble", new BrambleBlock(BlockBehaviour.Properties.of().sound(SoundType.GRASS).strength(2, 3).noCollission().randomTicks()), true);
	public static final Block FRUITING_BRAMBLE = create("fruiting_bramble", new BrambleBlock.Fruiting(BlockBehaviour.Properties.copy(ENDER_BRAMBLE)), true);
	public static final Block SCORCHED_BRAMBLE = create("scorched_bramble", new BrambleBlock(BlockBehaviour.Properties.copy(ENDER_BRAMBLE)), true);
	public static final Block THICK_BRAMBLE = create("thick_bramble", new BrambleBlock(BlockBehaviour.Properties.copy(ENDER_BRAMBLE)), true);
	public static final Block FLEETING_BRAMBLE = create("fleeting_bramble", new BrambleBlock(BlockBehaviour.Properties.copy(ENDER_BRAMBLE)), true);
	//material_block
	public static final Block RAW_SILVER_BLOCK = create("raw_silver_block", new Block(BlockBehaviour.Properties.copy(Blocks.RAW_GOLD_BLOCK)) {
		@Override
		public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
			if (!world.isClientSide && entity instanceof LivingEntity && BewitchmentAPI.isWeakToSilver((LivingEntity) entity)) {
				entity.hurt(BWDamageSources.create(world, BWDamageSources.MAGIC_COPY), 1);
			}
			super.stepOn(world, pos, state, entity);
		}
	}, true);
	public static final Block SILVER_BLOCK = create("silver_block", new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)) {
		@Override
		public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
			RAW_SILVER_BLOCK.stepOn(world, pos, state, entity);
		}
	}, true);
	public static final Block SALT_BLOCK = create("salt_block", new Block(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK)), true);
	public static final Block DRAGONS_BLOOD_RESIN_BLOCK = create("dragons_blood_resin_block", new Block(BlockBehaviour.Properties.copy(Blocks.NETHER_WART_BLOCK)), true);
	public static final Block SILVER_ORE = create("silver_ore", new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)), true);
	public static final Block DEEPSLATE_SILVER_ORE = create("deepslate_silver_ore", new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_GOLD_ORE)), true);
	public static final Block SALT_ORE = create("salt_ore", new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE), UniformInt.of(0, 2)), true);
	public static final Block DEEPSLATE_SALT_ORE = create("deepslate_salt_ore", new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_COAL_ORE), UniformInt.of(0, 2)), true);
	//misc_block
	public static final Block HEDGEWITCH_WOOL = create("hedgewitch_wool", new Block(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)), true);
	public static final Block ALCHEMIST_WOOL = create("alchemist_wool", new Block(BlockBehaviour.Properties.copy(HEDGEWITCH_WOOL)), true);
	public static final Block BESMIRCHED_WOOL = create("besmirched_wool", new Block(BlockBehaviour.Properties.copy(HEDGEWITCH_WOOL)), true);
	public static final Block HEDGEWITCH_CARPET = create("hedgewitch_carpet", new CarpetBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_CARPET)), true);
	public static final Block ALCHEMIST_CARPET = create("alchemist_carpet", new CarpetBlock(BlockBehaviour.Properties.copy(HEDGEWITCH_CARPET)), true);
	public static final Block BESMIRCHED_CARPET = create("besmirched_carpet", new CarpetBlock(BlockBehaviour.Properties.copy(HEDGEWITCH_CARPET)), true);
	public static final Block IRON_CANDELABRA = create("iron_candelabra", new CandelabraBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).pushReaction(PushReaction.BLOCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0), (byte) 16), true);
	public static final Block GOLDEN_CANDELABRA = create("golden_candelabra", new CandelabraBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).pushReaction(PushReaction.BLOCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0), (byte) 32), true);
	public static final Block SILVER_CANDELABRA = create("silver_candelabra", new CandelabraBlock(BlockBehaviour.Properties.copy(SILVER_BLOCK).pushReaction(PushReaction.BLOCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0), (byte) 32), true);
	public static final Block NETHERITE_CANDELABRA = create("netherite_candelabra", new CandelabraBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).pushReaction(PushReaction.BLOCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0), (byte) 64), false);
	public static final Item NETHERITE_CANDELABRA_ITEM = create("netherite_candelabra", new BlockItem(NETHERITE_CANDELABRA, gen().fireResistant()));
	public static final Block BLESSED_STONE = create("blessed_stone", new Block(BlockBehaviour.Properties.copy(Blocks.BEDROCK)), true);
	//block_entity
	public static final Block[] STONE_WITCH_ALTAR = createAltar("stone_witch_altar", BlockBehaviour.Properties.copy(Blocks.STONE).pushReaction(PushReaction.BLOCK));
	public static final Block[] MOSSY_COBBLESTONE_WITCH_ALTAR = createAltar("mossy_cobblestone_witch_altar", BlockBehaviour.Properties.copy(Blocks.MOSSY_COBBLESTONE).pushReaction(PushReaction.BLOCK));
	public static final Block[] PRISMARINE_WITCH_ALTAR = createAltar("prismarine_witch_altar", BlockBehaviour.Properties.copy(Blocks.PRISMARINE).pushReaction(PushReaction.BLOCK));
	public static final Block[] NETHER_BRICK_WITCH_ALTAR = createAltar("nether_brick_witch_altar", BlockBehaviour.Properties.copy(Blocks.NETHER_BRICKS).pushReaction(PushReaction.BLOCK));
	public static final Block[] BLACKSTONE_WITCH_ALTAR = createAltar("blackstone_witch_altar", BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).pushReaction(PushReaction.BLOCK));
	public static final Block[] GOLDEN_WITCH_ALTAR = createAltar("golden_witch_altar", BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).pushReaction(PushReaction.BLOCK));
	public static final Block[] END_STONE_WITCH_ALTAR = createAltar("end_stone_witch_altar", BlockBehaviour.Properties.copy(Blocks.END_STONE).pushReaction(PushReaction.BLOCK));
	public static final Block[] OBSIDIAN_WITCH_ALTAR = createAltar("obsidian_witch_altar", BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).pushReaction(PushReaction.BLOCK));
	public static final Block[] PURPUR_WITCH_ALTAR = createAltar("purpur_witch_altar", BlockBehaviour.Properties.copy(Blocks.PURPUR_BLOCK).pushReaction(PushReaction.BLOCK));
	public static final Block WITCH_CAULDRON = create("witch_cauldron", new WitchCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON).pushReaction(PushReaction.BLOCK)), true);
	public static final Block CRYSTAL_BALL = create("crystal_ball", new CrystalBallBlock(BlockBehaviour.Properties.copy(SILVER_BLOCK).sound(SoundType.GLASS)), true);
	public static final Block BRAZIER = create("brazier", new BrazierBlock(BlockBehaviour.Properties.copy(SILVER_BLOCK).lightLevel(blockState -> blockState.getValue(BlockStateProperties.LIT) ? 15 : 0)), true);
	public static final Block OAK_POPPET_SHELF = create("oak_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block SPRUCE_POPPET_SHELF = create("spruce_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block BIRCH_POPPET_SHELF = create("birch_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block JUNGLE_POPPET_SHELF = create("jungle_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block ACACIA_POPPET_SHELF = create("acacia_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block DARK_OAK_POPPET_SHELF = create("dark_oak_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block CRIMSON_POPPET_SHELF = create("crimson_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block WARPED_POPPET_SHELF = create("warped_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block JUNIPER_POPPET_SHELF = create("juniper_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(JUNIPER_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block CYPRESS_POPPET_SHELF = create("cypress_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(CYPRESS_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block ELDER_POPPET_SHELF = create("elder_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(ELDER_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block DRAGONS_BLOOD_POPPET_SHELF = create("dragons_blood_poppet_shelf", new PoppetShelfBlock(BlockBehaviour.Properties.copy(DRAGONS_BLOOD_PLANKS).pushReaction(PushReaction.BLOCK).noOcclusion()), true);
	public static final Block WHITE_COFFIN = create("white_coffin", new CoffinBlock(DyeColor.WHITE, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block ORANGE_COFFIN = create("orange_coffin", new CoffinBlock(DyeColor.ORANGE, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block MAGENTA_COFFIN = create("magenta_coffin", new CoffinBlock(DyeColor.MAGENTA, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block LIGHT_BLUE_COFFIN = create("light_blue_coffin", new CoffinBlock(DyeColor.LIGHT_BLUE, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block YELLOW_COFFIN = create("yellow_coffin", new CoffinBlock(DyeColor.YELLOW, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block LIME_COFFIN = create("lime_coffin", new CoffinBlock(DyeColor.LIME, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block PINK_COFFIN = create("pink_coffin", new CoffinBlock(DyeColor.PINK, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block GRAY_COFFIN = create("gray_coffin", new CoffinBlock(DyeColor.GRAY, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block LIGHT_GRAY_COFFIN = create("light_gray_coffin", new CoffinBlock(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block CYAN_COFFIN = create("cyan_coffin", new CoffinBlock(DyeColor.CYAN, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block PURPLE_COFFIN = create("purple_coffin", new CoffinBlock(DyeColor.PURPLE, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block BLUE_COFFIN = create("blue_coffin", new CoffinBlock(DyeColor.BLUE, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block BROWN_COFFIN = create("brown_coffin", new CoffinBlock(DyeColor.BROWN, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block GREEN_COFFIN = create("green_coffin", new CoffinBlock(DyeColor.GREEN, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block RED_COFFIN = create("red_coffin", new CoffinBlock(DyeColor.RED, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	public static final Block BLACK_COFFIN = create("black_coffin", new CoffinBlock(DyeColor.BLACK, BlockBehaviour.Properties.copy(CYPRESS_PLANKS).noOcclusion()), true);
	//armor
	public static final Item HEDGEWITCH_HOOD = create("hedgewitch_hood", new WitchArmorItem(BWMaterials.HEDGEWITCH_ARMOR, ArmorItem.Type.HELMET, gen(), "hedgewitch", false));
	public static final Item HEDGEWITCH_HAT = create("hedgewitch_hat", new WitchArmorItem(BWMaterials.HEDGEWITCH_ARMOR, ArmorItem.Type.HELMET, gen(), "hedgewitch", true));
	public static final Item HEDGEWITCH_ROBES = create("hedgewitch_robes", new WitchArmorItem(BWMaterials.HEDGEWITCH_ARMOR, ArmorItem.Type.CHESTPLATE, gen(), "hedgewitch", false));
	public static final Item HEDGEWITCH_PANTS = create("hedgewitch_pants", new WitchArmorItem(BWMaterials.HEDGEWITCH_ARMOR, ArmorItem.Type.LEGGINGS, gen(), "hedgewitch", false));
	public static final Item ALCHEMIST_HOOD = create("alchemist_hood", new WitchArmorItem(BWMaterials.ALCHEMIST_ARMOR, ArmorItem.Type.HELMET, gen(), "alchemist", false));
	public static final Item ALCHEMIST_HAT = create("alchemist_hat", new WitchArmorItem(BWMaterials.ALCHEMIST_ARMOR, ArmorItem.Type.HELMET, gen(), "alchemist", true));
	public static final Item ALCHEMIST_ROBES = create("alchemist_robes", new WitchArmorItem(BWMaterials.ALCHEMIST_ARMOR, ArmorItem.Type.CHESTPLATE, gen(), "alchemist", false));
	public static final Item ALCHEMIST_PANTS = create("alchemist_pants", new WitchArmorItem(BWMaterials.ALCHEMIST_ARMOR, ArmorItem.Type.LEGGINGS, gen(), "alchemist", false));
	public static final Item BESMIRCHED_HOOD = create("besmirched_hood", new WitchArmorItem(BWMaterials.BESMIRCHED_ARMOR, ArmorItem.Type.HELMET, gen(), "besmirched", false));
	public static final Item BESMIRCHED_HAT = create("besmirched_hat", new WitchArmorItem(BWMaterials.BESMIRCHED_ARMOR, ArmorItem.Type.HELMET, gen(), "besmirched", true));
	public static final Item BESMIRCHED_ROBES = create("besmirched_robes", new WitchArmorItem(BWMaterials.BESMIRCHED_ARMOR, ArmorItem.Type.CHESTPLATE, gen(), "besmirched", false));
	public static final Item BESMIRCHED_PANTS = create("besmirched_pants", new WitchArmorItem(BWMaterials.BESMIRCHED_ARMOR, ArmorItem.Type.LEGGINGS, gen(), "besmirched", false));
	public static final Item HARBINGER = create("harbinger", new WitchArmorItem(BWMaterials.HARBINGER_ARMOR, ArmorItem.Type.BOOTS, gen().rarity(Rarity.RARE).durability(444).fireResistant(), "harbinger", false));
	//trinket
	public static final Item NAZAR = create("nazar", new BWAccessoryItem(gen().stacksTo(1)));
	public static final Item SPECTER_BANGLE = create("specter_bangle", new SpecterBangleItem(gen().stacksTo(1)));
	public static final Item PRICKLY_BELT = create("prickly_belt", new PricklyBeltItem(gen().stacksTo(1)));
	public static final Item HELLISH_BAUBLE = create("hellish_bauble", new HellishBaubleItem(gen().stacksTo(1).fireResistant()));
	public static final Item DRUID_BAND = create("druid_band", new DruidBandItem(gen().stacksTo(1)));
	public static final Item ZEPHYR_HARNESS = create("zephyr_harness", new ZephyrHarnessItem(gen().stacksTo(1)));
	//tool
	public static final Item ATHAME = create("athame", new AthameItem(BWMaterials.SILVER_TOOL, 1, -2, gen()));
	public static final Item SILVER_ARROW = create("silver_arrow", new SilverArrowItem(gen()));
	public static final Item SCEPTER = create("scepter", new ScepterItem(gen().rarity(Rarity.RARE).durability(64).fireResistant()));
	public static final Item CADUCEUS = create("caduceus", new CaduceusItem(0, -3, Tiers.NETHERITE, BlockTags.MINEABLE_WITH_PICKAXE, gen().rarity(Rarity.RARE).durability(1998).fireResistant()));
	public static final Item HORNED_SPEAR = create("horned_spear", new HornedSpearItem(Tiers.NETHERITE, 2, -2.4f, gen().rarity(Rarity.RARE).durability(1008).fireResistant()));
	public static final Item CHALK = create("chalk", new ChalkItem(gen().durability(128), GLYPH));
	public static final Item GOLDEN_CHALK = create("golden_chalk", new ChalkItem(gen().durability(128), GOLDEN_GLYPH));
	public static final Item FIERY_CHALK = create("fiery_chalk", new ChalkItem(gen().durability(128), FIERY_GLYPH));
	public static final Item ELDRITCH_CHALK = create("eldritch_chalk", new ChalkItem(gen().durability(128), ELDRITCH_GLYPH));
	public static final Item TAGLOCK = create("taglock", new TaglockItem(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final Item WAYSTONE = create("waystone", new WaystoneItem(gen().durability(3)));
	public static final Item JUNIPER_BROOM = create("juniper_broom", new BroomItem(gen().stacksTo(1), BWEntityTypes.JUNIPER_BROOM));
	public static final Item CYPRESS_BROOM = create("cypress_broom", new BroomItem(gen().stacksTo(1), BWEntityTypes.CYPRESS_BROOM));
	public static final Item ELDER_BROOM = create("elder_broom", new BroomItem(gen().stacksTo(1), BWEntityTypes.ELDER_BROOM));
	public static final Item DRAGONS_BLOOD_BROOM = create("dragons_blood_broom", new DragonsBloodBroomItem(gen().stacksTo(1), BWEntityTypes.DRAGONS_BLOOD_BROOM));
	public static final Item MENDING_SIGIL = create("mending_sigil", new SigilItem(gen(), BWSigils.MENDING));
	public static final Item CLEANSING_SIGIL = create("cleansing_sigil", new SigilItem(gen(), BWSigils.CLEANSING));
	public static final Item JUDGMENT_SIGIL = create("judgment_sigil", new SigilItem(gen(), BWSigils.JUDGMENT));
	public static final Item DECAY_SIGIL = create("decay_sigil", new SigilItem(gen(), BWSigils.DECAY));
	public static final Item SHRIEKING_SIGIL = create("shrieking_sigil", new SigilItem(gen(), BWSigils.SHRIEKING));
	public static final Item SENTINEL_SIGIL = create("sentinel_sigil", new SigilItem(gen(), BWSigils.SENTINEL));
	public static final Item SLIPPERY_SIGIL = create("slippery_sigil", new SigilItem(gen(), BWSigils.SLIPPERY));
	public static final Item SHADOWS_SIGIL = create("shadows_sigil", new SigilItem(gen(), BWSigils.SHADOWS));
	public static final Item EXTENDING_SIGIL = create("extending_sigil", new SigilItem(gen(), BWSigils.EXTENDING));
	public static final Item SMELLY_SIGIL = create("smelly_sigil", new SigilItem(gen(), BWSigils.SMELLY));
	public static final Item RUIN_SIGIL = create("ruin_sigil", new SigilItem(gen(), BWSigils.RUIN));
	public static final Item HEAVY_SIGIL = create("heavy_sigil", new SigilItem(gen(), BWSigils.HEAVY));
	public static final PoppetItem PROTECTION_POPPET = create("protection_poppet", new PoppetItem(gen().durability(128), true));
	public static final PoppetItem DEATH_PROTECTION_POPPET = create("death_protection_poppet", new PoppetItem(gen().durability(1), false));
	public static final PoppetItem VOODOO_POPPET = create("voodoo_poppet", new PoppetItem(gen().durability(128), false));
	public static final PoppetItem VOODOO_PROTECTION_POPPET = create("voodoo_protection_poppet", new PoppetItem(gen().durability(128), true));
	public static final PoppetItem MENDING_POPPET = create("mending_poppet", new PoppetItem(gen().durability(1), true));
	public static final PoppetItem CURSE_POPPET = create("curse_poppet", new CursePoppetItem(gen().durability(1), true));
	public static final PoppetItem VAMPIRIC_POPPET = create("vampiric_poppet", new PoppetItem(gen().durability(128), false));
	public static final PoppetItem JUDGMENT_POPPET = create("judgment_poppet", new PoppetItem(gen().durability(64), true));
	public static final PoppetItem FATIGUE_POPPET = create("fatigue_poppet", new PoppetItem(gen().durability(64), true));
	public static final Item DEMONIC_CONTRACT = create("demonic_contract", new ContractItem(gen().rarity(Rarity.RARE).stacksTo(1)));
	//material_item
	public static final Item CLEANSING_BALM = create("cleansing_balm", new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final Item GRIM_ELIXIR = create("grim_elixir", new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final Item AQUA_CERATE = create("aqua_cerate", new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final Item FIERY_SERUM = create("fiery_serum", new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final Item EARTH_ICHOR = create("earth_ichor", new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final Item HEAVEN_EXTRACT = create("heaven_extract", new Item(gen().craftRemainder(Items.GLASS_BOTTLE)));
	public static final Item RAW_SILVER = create("raw_silver", new Item(gen()));
	public static final Item SILVER_INGOT = create("silver_ingot", new Item(gen()));
	public static final Item SILVER_NUGGET = create("silver_nugget", new Item(gen()));
	public static final Item SALT = create("salt", new ItemNameBlockItem(SALT_LINE, gen()));
	public static final Item ACONITE = create("aconite", new Item(gen()));
	public static final Item ACONITE_SEEDS = create("aconite_seeds", new ItemNameBlockItem(ACONITE_CROP, gen()));
	public static final Item BELLADONNA = create("belladonna", new Item(gen()));
	public static final Item BELLADONNA_SEEDS = create("belladonna_seeds", new ItemNameBlockItem(BELLADONNA_CROP, gen()));
	public static final Item GARLIC = create("garlic", new ItemNameBlockItem(GARLIC_CROP, gen().food(Foods.POTATO)));
	public static final Item MANDRAKE_ROOT = create("mandrake_root", new Item(gen()));
	public static final Item MANDRAKE_SEEDS = create("mandrake_seeds", new ItemNameBlockItem(MANDRAKE_CROP, gen()));
	public static final Item OAK_BARK = create("oak_bark", new Item(gen()));
	public static final Item SPRUCE_BARK = create("spruce_bark", new Item(gen()));
	public static final Item BIRCH_BARK = create("birch_bark", new Item(gen()));
	public static final Item JUNIPER_BARK = create("juniper_bark", new Item(gen()));
	public static final Item CYPRESS_BARK = create("cypress_bark", new Item(gen()));
	public static final Item ELDER_BARK = create("elder_bark", new Item(gen()));
	public static final Item WOOD_ASH = create("wood_ash", new Item(gen()));
	public static final Item DRAGONS_BLOOD_RESIN = create("dragons_blood_resin", new Item(gen()));
	public static final Item SNAKE_TONGUE = create("snake_tongue", new Item(gen()));
	public static final Item ECTOPLASM = create("ectoplasm", new Item(gen()));
	public static final Item DEMON_HORN = create("demon_horn", new Item(gen().fireResistant()));
	public static final Item DEMON_HEART = create("demon_heart", new Item(gen().food(BWFoodComponents.DEMON_HEART).rarity(Rarity.UNCOMMON).fireResistant()));
	public static final Item BOTTLE_OF_BLOOD = create("bottle_of_blood", new BottleOfBloodItem(gen().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
	public static final Item GRILLED_GARLIC = create("grilled_garlic", new Item(gen().food(Foods.CARROT)));
	public static final Item GARLIC_BREAD = create("garlic_bread", new Item(gen().food(BWFoodComponents.GARLIC_BREAD)));
	public static final Item WITCHBERRY = create("witchberry", new Item(gen().food(BWFoodComponents.WITCHBERRY)));
	public static final Item WITCHBERRY_PIE = create("witchberry_pie", new Item(gen().food(BWFoodComponents.WITCHBERRY_PIE)));
	public static final Item WITCHBERRY_COOKIE = create("witchberry_cookie", new Item(gen().food(BWFoodComponents.WITCHBERRY_COOKIE)));
	public static final Item GROTESQUE_STEW = create("grotesque_stew", new GrotestqueStewItem(gen().food(BWFoodComponents.DEMON_HEART).stacksTo(1).craftRemainder(Items.BOWL)));
	//spawn_egg
	public static final Item OWL_SPAWN_EGG = create("owl_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.OWL, 0x7f3f00, 0xc0c0c0, gen()));
	public static final Item RAVEN_SPAWN_EGG = create("raven_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.RAVEN, 0x3f3f3f, 0x000000, gen()));
	public static final Item SNAKE_SPAWN_EGG = create("snake_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.SNAKE, 0x7f3f00, 0x3f3f3f, gen()));
	public static final Item TOAD_SPAWN_EGG = create("toad_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.TOAD, 0x3f3f00, 0x00c200, gen()));
	public static final Item GHOST_SPAWN_EGG = create("ghost_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.GHOST, 0xcacaca, 0x969696, gen()));
	public static final Item VAMPIRE_SPAWN_EGG = create("vampire_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.VAMPIRE, 0xddcca8, 0x7d0000, gen()));
	public static final Item WEREWOLF_SPAWN_EGG = create("werewolf_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.WEREWOLF, 0x9f9a96, 0x532f0f, gen()));
	public static final Item HELLHOUND_SPAWN_EGG = create("hellhound_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.HELLHOUND, 0xc82000, 0x802020, gen()));
	public static final Item DEMON_SPAWN_EGG = create("demon_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.DEMON, 0x802020, 0xc82000, gen()));
	public static final Item LEONARD_SPAWN_EGG = create("leonard_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.LEONARD, 0x5e3214, 0xa00303, gen()));
	public static final Item BAPHOMET_SPAWN_EGG = create("baphomet_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.BAPHOMET, 0xa00303, 0x5e3214, gen()));
	public static final Item LILITH_SPAWN_EGG = create("lilith_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.LILITH, 0x222621, 0xc9cbcd, gen()));
	public static final Item HERNE_SPAWN_EGG = create("herne_spawn_egg", new ForgeSpawnEggItem(BWEntityTypes.HERNE, 0x5d482d, 0x294e00, gen()));
	//books
	public static final Item BOOK_OF_SHADOWS = create("book_of_shadows", new BWBookItem(gen().stacksTo(1)));

	private static <T extends Block> T create(String name, T block, boolean createItem) {
		BLOCKS.put(block, Bewitchment.id(name));
		if (createItem) {
			ITEMS.put(block instanceof BWChestBlock chest ? new BWChestItem(chest, gen()) : new BlockItem(block, gen()), BLOCKS.get(block));
		}
		return block;
	}

	private static <T extends Item> T create(String name, T item) {
		ITEMS.put(item, Bewitchment.id(name));
		return item;
	}

	private static Item.Properties gen() {
		return new Item.Properties();
	}

	private static Tuple<ResourceLocation, ResourceLocation> createSign(String name) {
		WoodType wood = WoodType.register(new WoodType(Bewitchment.id(name).toString(), BlockSetType.OAK));
		Block sign = create(name + "_sign", new StandingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), wood), false);
		Block wallSign = create(name + "_wall_sign", new WallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN).dropsLike(sign), wood), false);
		Block hanging = create(name + "_hanging_sign", new CeilingHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN), wood), false);
		Block wallHanging = create(name + "_wall_hanging_sign", new WallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN).dropsLike(hanging), wood), false);
		SIGNS.add(sign);
		SIGNS.add(wallSign);
		HANGING_SIGNS.add(hanging);
		HANGING_SIGNS.add(wallHanging);
		create(name + "_sign", new SignItem(gen().stacksTo(16), sign, wallSign));
		create(name + "_hanging_sign", new HangingSignItem(hanging, wallHanging, gen().stacksTo(16)));
		return new Tuple<>(Bewitchment.id("entity/signs/" + name), Bewitchment.id("entity/signs/hanging/" + name));
	}

	private static Block[] createAltar(String name, BlockBehaviour.Properties settings) {
		settings = settings.lightLevel(blockState -> blockState.getValue(BlockStateProperties.LEVEL));
		Block[] altars = new Block[21];
		WitchAltarBlock unformed = create(name, new WitchAltarBlock(settings, null, false), true);
		altars[0] = unformed;
		altars[1] = create("moss_" + name, new WitchAltarBlock(settings, unformed, true), false);
		for (int i = 0; i < 16; i++) {
			altars[i + 2] = create(DyeColor.byId(i).getName() + "_" + name, new WitchAltarBlock(settings, unformed, true), false);
		}
		altars[18] = create("hedgewitch_" + name, new WitchAltarBlock(settings, unformed, true), false);
		altars[19] = create("alchemist_" + name, new WitchAltarBlock(settings, unformed, true), false);
		altars[20] = create("besmirched_" + name, new WitchAltarBlock(settings, unformed, true), false);
		return altars;
	}

	public static void registerEntries(RegisterEvent event) {
		event.register(Registries.BLOCK, helper -> BLOCKS.forEach((block, id) -> helper.register(id, block)));
		event.register(Registries.ITEM, helper -> ITEMS.forEach((item, id) -> helper.register(id, item)));
		event.register(Registries.CREATIVE_MODE_TAB, Bewitchment.id(Bewitchment.MOD_ID), BWObjects::createTab);
	}

	private static CreativeModeTab createTab() {
		GROUP = CreativeModeTab.builder().title(Component.translatable("itemGroup." + Bewitchment.MOD_ID)).icon(ATHAME::getDefaultInstance).displayItems((displayContext, entries) -> {
			ITEMS.keySet().forEach(item -> {
				if (item instanceof ContractItem) {
					BWRegistries.CONTRACT.get().forEach(contract -> {
						ItemStack stack = new ItemStack(item);
						stack.getOrCreateTag().putString("Contract", BWRegistries.CONTRACT.get().getKey(contract).toString());
						stack.getOrCreateTag().putInt("Duration", 168000);
						entries.accept(stack);
					});
				} else {
					entries.accept(item);
				}
			});
			BWBoatTypes.addToTab(entries);
		}).build();
		return GROUP;
	}

	public static void setup() {
		AxeItem.STRIPPABLES = new LinkedHashMap<>(AxeItem.STRIPPABLES);
		BlockEntityType.SIGN.validBlocks = new HashSet<>(BlockEntityType.SIGN.validBlocks);
		BlockEntityType.SIGN.validBlocks.addAll(SIGNS);
		BlockEntityType.HANGING_SIGN.validBlocks = new HashSet<>(BlockEntityType.HANGING_SIGN.validBlocks);
		BlockEntityType.HANGING_SIGN.validBlocks.addAll(HANGING_SIGNS);
		AxeItem.STRIPPABLES.put(JUNIPER_LOG, STRIPPED_JUNIPER_LOG);
		AxeItem.STRIPPABLES.put(JUNIPER_WOOD, STRIPPED_JUNIPER_WOOD);
		AxeItem.STRIPPABLES.put(CYPRESS_LOG, STRIPPED_CYPRESS_LOG);
		AxeItem.STRIPPABLES.put(CYPRESS_WOOD, STRIPPED_CYPRESS_WOOD);
		AxeItem.STRIPPABLES.put(ELDER_LOG, STRIPPED_ELDER_LOG);
		AxeItem.STRIPPABLES.put(ELDER_WOOD, STRIPPED_ELDER_WOOD);
		AxeItem.STRIPPABLES.put(DRAGONS_BLOOD_LOG, STRIPPED_DRAGONS_BLOOD_LOG);
		AxeItem.STRIPPABLES.put(DRAGONS_BLOOD_WOOD, STRIPPED_DRAGONS_BLOOD_WOOD);
		FireBlock fire = (FireBlock) Blocks.FIRE;
		fire.setFlammable(STRIPPED_JUNIPER_LOG, 5, 5);
		fire.setFlammable(STRIPPED_JUNIPER_WOOD, 5, 5);
		fire.setFlammable(JUNIPER_LOG, 5, 5);
		fire.setFlammable(JUNIPER_WOOD, 5, 5);
		fire.setFlammable(JUNIPER_LEAVES, 30, 60);
		fire.setFlammable(JUNIPER_PLANKS, 5, 20);
		fire.setFlammable(JUNIPER_STAIRS, 5, 20);
		fire.setFlammable(JUNIPER_SLAB, 5, 20);
		fire.setFlammable(JUNIPER_FENCE, 5, 20);
		fire.setFlammable(JUNIPER_FENCE_GATE, 5, 20);
		fire.setFlammable(STRIPPED_CYPRESS_LOG, 5, 5);
		fire.setFlammable(STRIPPED_CYPRESS_WOOD, 5, 5);
		fire.setFlammable(CYPRESS_LOG, 5, 5);
		fire.setFlammable(CYPRESS_WOOD, 5, 5);
		fire.setFlammable(CYPRESS_LEAVES, 30, 60);
		fire.setFlammable(CYPRESS_PLANKS, 5, 20);
		fire.setFlammable(CYPRESS_STAIRS, 5, 20);
		fire.setFlammable(CYPRESS_SLAB, 5, 20);
		fire.setFlammable(CYPRESS_FENCE, 5, 20);
		fire.setFlammable(CYPRESS_FENCE_GATE, 5, 20);
		fire.setFlammable(STRIPPED_ELDER_LOG, 5, 5);
		fire.setFlammable(STRIPPED_ELDER_WOOD, 5, 5);
		fire.setFlammable(ELDER_LOG, 5, 5);
		fire.setFlammable(ELDER_WOOD, 5, 5);
		fire.setFlammable(ELDER_LEAVES, 30, 60);
		fire.setFlammable(ELDER_PLANKS, 5, 20);
		fire.setFlammable(ELDER_STAIRS, 5, 20);
		fire.setFlammable(ELDER_SLAB, 5, 20);
		fire.setFlammable(ELDER_FENCE, 5, 20);
		fire.setFlammable(ELDER_FENCE_GATE, 5, 20);
		fire.setFlammable(STRIPPED_DRAGONS_BLOOD_LOG, 5, 5);
		fire.setFlammable(STRIPPED_DRAGONS_BLOOD_WOOD, 5, 5);
		fire.setFlammable(DRAGONS_BLOOD_LOG, 5, 5);
		fire.setFlammable(DRAGONS_BLOOD_WOOD, 5, 5);
		fire.setFlammable(DRAGONS_BLOOD_LEAVES, 30, 60);
		fire.setFlammable(DRAGONS_BLOOD_PLANKS, 5, 20);
		fire.setFlammable(DRAGONS_BLOOD_STAIRS, 5, 20);
		fire.setFlammable(DRAGONS_BLOOD_SLAB, 5, 20);
		fire.setFlammable(DRAGONS_BLOOD_FENCE, 5, 20);
		fire.setFlammable(DRAGONS_BLOOD_FENCE_GATE, 5, 20);
		fire.setFlammable(HEDGEWITCH_WOOL, 30, 60);
		fire.setFlammable(ALCHEMIST_WOOL, 30, 60);
		fire.setFlammable(BESMIRCHED_WOOL, 30, 60);
		fire.setFlammable(HEDGEWITCH_CARPET, 60, 20);
		fire.setFlammable(ALCHEMIST_CARPET, 60, 20);
		fire.setFlammable(BESMIRCHED_CARPET, 60, 20);
		ComposterBlock.COMPOSTABLES.put(JUNIPER_LEAVES, 0.3f);
		ComposterBlock.COMPOSTABLES.put(JUNIPER_SAPLING, 0.3f);
		ComposterBlock.COMPOSTABLES.put(CYPRESS_LEAVES, 0.3f);
		ComposterBlock.COMPOSTABLES.put(CYPRESS_SAPLING, 0.3f);
		ComposterBlock.COMPOSTABLES.put(ELDER_LEAVES, 0.3f);
		ComposterBlock.COMPOSTABLES.put(ELDER_SAPLING, 0.3f);
		ComposterBlock.COMPOSTABLES.put(DRAGONS_BLOOD_LEAVES, 0.3f);
		ComposterBlock.COMPOSTABLES.put(DRAGONS_BLOOD_SAPLING, 0.3f);
		ComposterBlock.COMPOSTABLES.put(ACONITE, 0.65f);
		ComposterBlock.COMPOSTABLES.put(ACONITE_SEEDS, 0.3f);
		ComposterBlock.COMPOSTABLES.put(BELLADONNA, 0.65f);
		ComposterBlock.COMPOSTABLES.put(BELLADONNA_SEEDS, 0.3f);
		ComposterBlock.COMPOSTABLES.put(GARLIC, 0.65f);
		ComposterBlock.COMPOSTABLES.put(MANDRAKE_ROOT, 0.65f);
		ComposterBlock.COMPOSTABLES.put(MANDRAKE_SEEDS, 0.3f);
		ComposterBlock.COMPOSTABLES.put(WOOD_ASH, 0.3f);
		ComposterBlock.COMPOSTABLES.put(DRAGONS_BLOOD_RESIN, 0.65f);
		ComposterBlock.COMPOSTABLES.put(GRILLED_GARLIC, 0.85f);
		ComposterBlock.COMPOSTABLES.put(GARLIC_BREAD, 1f);
		ComposterBlock.COMPOSTABLES.put(WITCHBERRY, 0.65f);
		ComposterBlock.COMPOSTABLES.put(WITCHBERRY_PIE, 1f);
		ComposterBlock.COMPOSTABLES.put(WITCHBERRY_COOKIE, 0.85f);
	}

	public static void onFuelBurn(FurnaceFuelBurnTimeEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.is(SCORCHED_BRAMBLE.asItem())) {
			event.setBurnTime(800);
		} else if (stack.is(BWTags.BARKS)) {
			event.setBurnTime(100);
		} else if (stack.is(JUNIPER_FENCE.asItem()) || stack.is(JUNIPER_FENCE_GATE.asItem())
				|| stack.is(CYPRESS_FENCE.asItem()) || stack.is(CYPRESS_FENCE_GATE.asItem())
				|| stack.is(ELDER_FENCE.asItem()) || stack.is(ELDER_FENCE_GATE.asItem())
				|| stack.is(DRAGONS_BLOOD_FENCE.asItem()) || stack.is(DRAGONS_BLOOD_FENCE_GATE.asItem())) {
			event.setBurnTime(300);
		}
	}
}
