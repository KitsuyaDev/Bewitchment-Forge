/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client;

import com.mojang.blaze3d.platform.InputConstants;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import kitsuyadev.bewitchment.api.client.model.BroomEntityModel;
import kitsuyadev.bewitchment.client.event.CauldronTeleportEvent;
import kitsuyadev.bewitchment.client.event.TransformationAbilityEvent;
import kitsuyadev.bewitchment.client.model.ContributorHornsModel;
import kitsuyadev.bewitchment.client.model.entity.living.*;
import kitsuyadev.bewitchment.client.model.equipment.armor.WitchArmorModel;
import kitsuyadev.bewitchment.client.model.equipment.trinket.DruidBandModel;
import kitsuyadev.bewitchment.client.model.equipment.trinket.PricklyBeltModel;
import kitsuyadev.bewitchment.client.model.equipment.trinket.SpecterBangleModel;
import kitsuyadev.bewitchment.client.model.equipment.trinket.ZephyrHarnessModel;
import kitsuyadev.bewitchment.client.particle.CauldronBubbleParticle;
import kitsuyadev.bewitchment.client.particle.IncenseSmokeParticle;
import kitsuyadev.bewitchment.client.render.blockentity.BrazierBlockEntityRenderer;
import kitsuyadev.bewitchment.client.render.blockentity.PoppetShelfBlockEntityRenderer;
import kitsuyadev.bewitchment.client.render.blockentity.WitchAltarBlockEntityRenderer;
import kitsuyadev.bewitchment.client.render.blockentity.WitchCauldronBlockEntityRenderer;
import kitsuyadev.bewitchment.client.render.entity.*;
import kitsuyadev.bewitchment.client.render.entity.living.*;
import kitsuyadev.bewitchment.client.render.trinket.*;
import kitsuyadev.bewitchment.client.screen.DemonScreen;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.item.TaglockItem;
import kitsuyadev.bewitchment.common.registry.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModList;
import kitsuyadev.bewitchment.client.integration.appleskin.BWAppleskinIntegration;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@Mod.EventBusSubscriber(modid = Bewitchment.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BewitchmentClient {
	public static final KeyMapping TRANSFORMATION_ABILITY = new KeyMapping("key." + Bewitchment.MOD_ID + ".transformation_ability", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "itemGroup." + Bewitchment.MOD_ID);

	public static final ModelLayerLocation CONTRIBUTOR_HORNS_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("contributor_horns"), "main");
	public static final ModelLayerLocation WITCH_ARMOR_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("witch_armor"), "main");
	public static final ModelLayerLocation SPECTER_BANGLE_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("specter_bangle"), "main");
	public static final ModelLayerLocation PRICKLY_BELT_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("prickly_belt"), "main");
	public static final ModelLayerLocation DRUID_BAND_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("druid_band"), "main");
	public static final ModelLayerLocation ZEPHYR_HARNESS_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("zephyr_harness"), "main");

	public static final ModelLayerLocation BROOM_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("broom"), "main");
	public static final ModelLayerLocation OWL_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("owl"), "main");
	public static final ModelLayerLocation RAVEN_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("raven"), "main");
	public static final ModelLayerLocation SNAKE_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("snake"), "main");
	public static final ModelLayerLocation TOAD_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("toad"), "main");
	public static final ModelLayerLocation GHOST_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("ghost"), "main");
	public static final ModelLayerLocation VAMPIRE_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("vampire"), "main");
	public static final ModelLayerLocation WEREWOLF_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("werewolf"), "main");
	public static final ModelLayerLocation HELLHOUND_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("hellhound"), "main");
	public static final ModelLayerLocation MALE_DEMON_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("male_demon"), "main");
	public static final ModelLayerLocation FEMALE_DEMON_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("female_demon"), "main");
	public static final ModelLayerLocation LEONARD_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("leonard"), "main");
	public static final ModelLayerLocation BAPHOMET_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("baphomet"), "main");
	public static final ModelLayerLocation LILITH_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("lilith"), "main");
	public static final ModelLayerLocation HERNE_MODEL_LAYER = new ModelLayerLocation(Bewitchment.id("herne"), "main");

	@SubscribeEvent
	public static void registerKey(RegisterKeyMappingsEvent event) {
		event.register(TRANSFORMATION_ABILITY);
	}

	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(BWParticleTypes.CAULDRON_BUBBLE.get(), CauldronBubbleParticle.Factory::new);
		event.registerSpriteSet(BWParticleTypes.INCENSE_SMOKE.get(), IncenseSmokeParticle.Factory::new);
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register((state, world, pos, tintIndex) -> 0xffff00, BWObjects.GOLDEN_GLYPH);
		event.register((state, world, pos, tintIndex) -> 0xc00000, BWObjects.FIERY_GLYPH);
		event.register((state, world, pos, tintIndex) -> 0x8000a0, BWObjects.ELDRITCH_GLYPH);
		event.register((state, world, pos, tintIndex) -> tintIndex == 1 ? ((BedBlock) state.getBlock()).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN, BWObjects.ORANGE_COFFIN, BWObjects.MAGENTA_COFFIN, BWObjects.LIGHT_BLUE_COFFIN, BWObjects.YELLOW_COFFIN, BWObjects.LIME_COFFIN, BWObjects.PINK_COFFIN, BWObjects.GRAY_COFFIN, BWObjects.LIGHT_GRAY_COFFIN, BWObjects.CYAN_COFFIN, BWObjects.PURPLE_COFFIN, BWObjects.BLUE_COFFIN, BWObjects.BROWN_COFFIN, BWObjects.GREEN_COFFIN, BWObjects.RED_COFFIN, BWObjects.BLACK_COFFIN);
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, tintIndex) -> tintIndex == 1 ? ((BedBlock) Block.byItem(stack.getItem())).getColor().getFireworkColor() : 0xffffff, BWObjects.WHITE_COFFIN, BWObjects.ORANGE_COFFIN, BWObjects.MAGENTA_COFFIN, BWObjects.LIGHT_BLUE_COFFIN, BWObjects.YELLOW_COFFIN, BWObjects.LIME_COFFIN, BWObjects.PINK_COFFIN, BWObjects.GRAY_COFFIN, BWObjects.LIGHT_GRAY_COFFIN, BWObjects.CYAN_COFFIN, BWObjects.PURPLE_COFFIN, BWObjects.BLUE_COFFIN, BWObjects.BROWN_COFFIN, BWObjects.GREEN_COFFIN, BWObjects.RED_COFFIN, BWObjects.BLACK_COFFIN);
		event.register((stack, tintIndex) -> tintIndex == 0 ? 0x7f0000 : 0xffffff, BWObjects.BOTTLE_OF_BLOOD);
	}

	@SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CONTRIBUTOR_HORNS_MODEL_LAYER, ContributorHornsModel::getTexturedModelData);
		event.registerLayerDefinition(WITCH_ARMOR_MODEL_LAYER, WitchArmorModel::getTexturedModelData);
		event.registerLayerDefinition(SPECTER_BANGLE_MODEL_LAYER, SpecterBangleModel::getTexturedModelData);
		event.registerLayerDefinition(PRICKLY_BELT_MODEL_LAYER, PricklyBeltModel::getTexturedModelData);
		event.registerLayerDefinition(DRUID_BAND_MODEL_LAYER, DruidBandModel::getTexturedModelData);
		event.registerLayerDefinition(ZEPHYR_HARNESS_MODEL_LAYER, ZephyrHarnessModel::getTexturedModelData);
		event.registerLayerDefinition(BROOM_MODEL_LAYER, BroomEntityModel::getTexturedModelData);
		event.registerLayerDefinition(OWL_MODEL_LAYER, OwlEntityModel::getTexturedModelData);
		event.registerLayerDefinition(RAVEN_MODEL_LAYER, RavenEntityModel::getTexturedModelData);
		event.registerLayerDefinition(SNAKE_MODEL_LAYER, SnakeEntityModel::getTexturedModelData);
		event.registerLayerDefinition(TOAD_MODEL_LAYER, ToadEntityModel::getTexturedModelData);
		event.registerLayerDefinition(GHOST_MODEL_LAYER, GhostEntityModel::getTexturedModelData);
		event.registerLayerDefinition(VAMPIRE_MODEL_LAYER, VampireEntityModel::getTexturedModelData);
		event.registerLayerDefinition(WEREWOLF_MODEL_LAYER, WerewolfEntityModel::getTexturedModelData);
		event.registerLayerDefinition(HELLHOUND_MODEL_LAYER, HellhoundEntityModel::getTexturedModelData);
		event.registerLayerDefinition(MALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataMale);
		event.registerLayerDefinition(FEMALE_DEMON_MODEL_LAYER, DemonEntityModel::getTexturedModelDataFemale);
		event.registerLayerDefinition(LEONARD_MODEL_LAYER, LeonardEntityModel::getTexturedModelData);
		event.registerLayerDefinition(BAPHOMET_MODEL_LAYER, BaphometEntityModel::getTexturedModelData);
		event.registerLayerDefinition(LILITH_MODEL_LAYER, LilithEntityModel::getTexturedModelData);
		event.registerLayerDefinition(HERNE_MODEL_LAYER, HerneEntityModel::getTexturedModelData);
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		for (BWBoatTypes.Wood wood : BWBoatTypes.Wood.values()) {
			event.registerEntityRenderer(wood.entity(false), context -> new BWBoatRenderer(context, wood, false));
			event.registerEntityRenderer(wood.entity(true), context -> new BWBoatRenderer(context, wood, true));
		}
		event.registerBlockEntityRenderer(BWBlockEntityTypes.BW_CHEST.get(), ChestRenderer::new);
		event.registerBlockEntityRenderer(BWBlockEntityTypes.WITCH_ALTAR.get(), ctx -> new WitchAltarBlockEntityRenderer());
		event.registerBlockEntityRenderer(BWBlockEntityTypes.WITCH_CAULDRON.get(), ctx -> new WitchCauldronBlockEntityRenderer());
		event.registerBlockEntityRenderer(BWBlockEntityTypes.BRAZIER.get(), ctx -> new BrazierBlockEntityRenderer());
		event.registerBlockEntityRenderer(BWBlockEntityTypes.POPPET_SHELF.get(), ctx -> new PoppetShelfBlockEntityRenderer());
		event.registerBlockEntityRenderer(BWBlockEntityTypes.JUNIPER_CHEST.get(), ChestRenderer::new);
		event.registerBlockEntityRenderer(BWBlockEntityTypes.ELDER_CHEST.get(), ChestRenderer::new);
		event.registerBlockEntityRenderer(BWBlockEntityTypes.DRAGONS_BLOOD_CHEST.get(), ChestRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.JUNIPER_BROOM.get(), JuniperBroomEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.CYPRESS_BROOM.get(), CypressBroomEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.ELDER_BROOM.get(), ElderBroomEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.DRAGONS_BLOOD_BROOM.get(), DragonsBloodBroomEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.SILVER_ARROW.get(), SilverArrowEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.HORNED_SPEAR.get(), HornedSpearEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.OWL.get(), OwlEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.RAVEN.get(), RavenEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.SNAKE.get(), SnakeEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.TOAD.get(), ToadEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.GHOST.get(), GhostEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.VAMPIRE.get(), VampireEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.WEREWOLF.get(), WerewolfEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.HELLHOUND.get(), HellhoundEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.DEMON.get(), DemonEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.LEONARD.get(), LeonardEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.BAPHOMET.get(), BaphometEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.LILITH.get(), LilithEntityRenderer::new);
		event.registerEntityRenderer(BWEntityTypes.HERNE.get(), HerneEntityRenderer::new);
	}

	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			if (ModList.get().isLoaded("appleskin")) {
				BWAppleskinIntegration.registerEvents();
			}
			ItemProperties.register(BWObjects.HEDGEWITCH_HAT, Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase().contains("faith") ? 1 : 0);
			ItemProperties.register(BWObjects.ALCHEMIST_HAT, Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase().contains("faith") ? 1 : 0);
			ItemProperties.register(BWObjects.BESMIRCHED_HAT, Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase().contains("faith") ? 1 : 0);
			ItemProperties.register(BWObjects.NAZAR, Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.hasTag() && stack.getTag().getBoolean("Worn") ? 1 : 0);
			ItemProperties.register(BWObjects.PRICKLY_BELT, Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.hasTag() && stack.getTag().getInt("PotionUses") > 0 ? 1 : 0);
			ItemProperties.register(BWObjects.HORNED_SPEAR, Bewitchment.id("variant"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1 : 0);
			ItemProperties.register(BWObjects.TAGLOCK, Bewitchment.id("variant"), (stack, world, entity, seed) -> TaglockItem.hasTaglock(stack) ? 1 : 0);
			ItemProperties.register(BWObjects.WAYSTONE, Bewitchment.id("variant"), (stack, world, entity, seed) -> stack.hasTag() && stack.getOrCreateTag().contains("LocationPos") ? 1 : 0);
			setRenderType(RenderType.cutout(), BWObjects.SALT_LINE, BWObjects.TEMPORARY_COBWEB, BWObjects.GLYPH, BWObjects.GOLDEN_GLYPH, BWObjects.FIERY_GLYPH, BWObjects.ELDRITCH_GLYPH, BWObjects.SIGIL);
			setRenderType(RenderType.cutout(), BWObjects.ACONITE_CROP, BWObjects.BELLADONNA_CROP, BWObjects.GARLIC_CROP, BWObjects.MANDRAKE_CROP);
			setRenderType(RenderType.cutout(), BWObjects.JUNIPER_SAPLING, BWObjects.POTTED_JUNIPER_SAPLING, BWObjects.JUNIPER_DOOR, BWObjects.JUNIPER_TRAPDOOR);
			setRenderType(RenderType.cutout(), BWObjects.CYPRESS_SAPLING, BWObjects.POTTED_CYPRESS_SAPLING, BWObjects.CYPRESS_DOOR, BWObjects.CYPRESS_TRAPDOOR);
			setRenderType(RenderType.cutout(), BWObjects.ELDER_SAPLING, BWObjects.POTTED_ELDER_SAPLING, BWObjects.ELDER_DOOR, BWObjects.ELDER_TRAPDOOR);
			setRenderType(RenderType.cutout(), BWObjects.DRAGONS_BLOOD_SAPLING, BWObjects.POTTED_DRAGONS_BLOOD_SAPLING, BWObjects.DRAGONS_BLOOD_DOOR, BWObjects.DRAGONS_BLOOD_TRAPDOOR);
			setRenderType(RenderType.cutout(), BWObjects.GLOWING_BRAMBLE, BWObjects.ENDER_BRAMBLE, BWObjects.FRUITING_BRAMBLE, BWObjects.SCORCHED_BRAMBLE, BWObjects.THICK_BRAMBLE, BWObjects.FLEETING_BRAMBLE);
			setRenderType(RenderType.cutout(), BWObjects.STONE_WITCH_ALTAR);
			setRenderType(RenderType.cutout(), BWObjects.MOSSY_COBBLESTONE_WITCH_ALTAR);
			setRenderType(RenderType.cutout(), BWObjects.PRISMARINE_WITCH_ALTAR);
			setRenderType(RenderType.cutout(), BWObjects.NETHER_BRICK_WITCH_ALTAR);
			setRenderType(RenderType.cutout(), BWObjects.BLACKSTONE_WITCH_ALTAR);
			setRenderType(RenderType.cutout(), BWObjects.GOLDEN_WITCH_ALTAR);
			setRenderType(RenderType.cutout(), BWObjects.END_STONE_WITCH_ALTAR);
			setRenderType(RenderType.cutout(), BWObjects.OBSIDIAN_WITCH_ALTAR);
			setRenderType(RenderType.cutout(), BWObjects.PURPUR_WITCH_ALTAR);
			setRenderType(RenderType.translucent(), BWObjects.CRYSTAL_BALL);
			setRenderType(RenderType.cutout(), BWObjects.BRAZIER);
			MenuScreens.register(BWScreenHandlerTypes.DEMON_SCREEN_HANDLER.get(), DemonScreen::new);
			MenuScreens.register(BWScreenHandlerTypes.BAPHOMET_SCREEN_HANDLER.get(), DemonScreen::new);
			CuriosRendererRegistry.register(BWObjects.NAZAR, NazarRenderer::new);
			CuriosRendererRegistry.register(BWObjects.SPECTER_BANGLE, SpecterBangleRenderer::new);
			CuriosRendererRegistry.register(BWObjects.PRICKLY_BELT, PricklyBeltRenderer::new);
			CuriosRendererRegistry.register(BWObjects.HELLISH_BAUBLE, HellishBaubleRenderer::new);
			CuriosRendererRegistry.register(BWObjects.DRUID_BAND, DruidBandRenderer::new);
			CuriosRendererRegistry.register(BWObjects.ZEPHYR_HARNESS, ZephyrHarnessRenderer::new);
		});
	}

	private static void setRenderType(RenderType type, Block... blocks) {
		for (Block block : blocks) {
			ItemBlockRenderTypes.setRenderLayer(block, type);
		}
	}

	public static ModelPart getPart(ModelLayerLocation layer) {
		return Minecraft.getInstance().getEntityModels().bakeLayer(layer);
	}
}
