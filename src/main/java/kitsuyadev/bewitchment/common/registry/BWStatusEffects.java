/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.statuseffect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BWStatusEffects {
	private static final DeferredRegister<MobEffect> STATUS_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Bewitchment.MOD_ID);

	public static final RegistryObject<MobEffect> CLIMBING = STATUS_EFFECTS.register("climbing", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x605448));
	public static final RegistryObject<MobEffect> CORROSION = STATUS_EFFECTS.register("corrosion", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x6fc536).addAttributeModifier(Attributes.ARMOR, "5b5df91d-001a-464f-a773-ab4ccd62e09f", -3, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEffect> CORRUPTION = STATUS_EFFECTS.register("corruption", () -> new CorruptionStatusEffect(MobEffectCategory.HARMFUL, 0x600000));
	public static final RegistryObject<MobEffect> DEAFENED = STATUS_EFFECTS.register("deafened", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x676c77));
	public static final RegistryObject<MobEffect> DEFLECTION = STATUS_EFFECTS.register("deflection", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x969696));
	public static final RegistryObject<MobEffect> DISJUNCTION = STATUS_EFFECTS.register("disjunction", () -> new DisjunctionStatusEffect(MobEffectCategory.NEUTRAL, 0xb154a9));
	public static final RegistryObject<MobEffect> ENCHANTED = STATUS_EFFECTS.register("enchanted", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xf5ce8c));
	public static final RegistryObject<MobEffect> ETHEREAL = STATUS_EFFECTS.register("ethereal", () -> new EmptyStatusEffect(MobEffectCategory.NEUTRAL, 0x9a9ebf));
	public static final RegistryObject<MobEffect> GILLS = STATUS_EFFECTS.register("gills", () -> new GillsStatusEffect(MobEffectCategory.HARMFUL, 0x2f5e7b));
	public static final RegistryObject<MobEffect> HARDENING = STATUS_EFFECTS.register("hardening", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x3fa442).addAttributeModifier(Attributes.ARMOR, "7f7f1ce3-6383-49a8-a9f1-af0f5c7ab38f", 3, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEffect> IGNITION = STATUS_EFFECTS.register("ignition", () -> new IgnitionStatusEffect(MobEffectCategory.HARMFUL, 0xd68a0c));
	public static final RegistryObject<MobEffect> INHIBITED = STATUS_EFFECTS.register("inhibited", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x383a41));
	public static final RegistryObject<MobEffect> INVIGORATING = STATUS_EFFECTS.register("invigorating", () -> new InvigoratingStatusEffect(MobEffectCategory.BENEFICIAL, 0xaa64b3));
	public static final RegistryObject<MobEffect> LEECHING = STATUS_EFFECTS.register("leeching", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x4a0000));
	public static final RegistryObject<MobEffect> MAGIC_SPONGE = STATUS_EFFECTS.register("magic_sponge", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0xa264aa));
	public static final RegistryObject<MobEffect> MORTAL_COIL = STATUS_EFFECTS.register("mortal_coil", () -> new MortalCoilStatusEffect(MobEffectCategory.HARMFUL, 0x2d2d2d));
	public static final RegistryObject<MobEffect> NOURISHING = STATUS_EFFECTS.register("nourishing", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x71bc78));
	public static final RegistryObject<MobEffect> PACT = STATUS_EFFECTS.register("pact", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0x7d0000).addAttributeModifier(Attributes.MAX_HEALTH, "be0cf4d3-6e4b-43e6-9831-9e7d14aa14a7", -1, AttributeModifier.Operation.ADDITION));
	public static final RegistryObject<MobEffect> POLYMORPH = STATUS_EFFECTS.register("polymorph", () -> new PolymorphStatusEffect(MobEffectCategory.NEUTRAL, 0xb154a9));
	public static final RegistryObject<MobEffect> PURITY = STATUS_EFFECTS.register("purity", () -> new PurityStatusEffect(MobEffectCategory.BENEFICIAL, 0xded3d5));
	public static final RegistryObject<MobEffect> SINKING = STATUS_EFFECTS.register("sinking", () -> new SinkingStatusEffect(MobEffectCategory.HARMFUL, 0x6c6c6c));
	public static final RegistryObject<MobEffect> SYNCHRONIZED = STATUS_EFFECTS.register("synchronized", () -> new SynchronizedStatusEffect(MobEffectCategory.NEUTRAL, 0x990404));
	public static final RegistryObject<MobEffect> THEFT = STATUS_EFFECTS.register("theft", () -> new TheftStatusEffect(MobEffectCategory.BENEFICIAL, 0x773b89));
	public static final RegistryObject<MobEffect> THORNS = STATUS_EFFECTS.register("thorns", () -> new EmptyStatusEffect(MobEffectCategory.BENEFICIAL, 0x527d26));
	public static final RegistryObject<MobEffect> VOLATILITY = STATUS_EFFECTS.register("volatility", () -> new EmptyStatusEffect(MobEffectCategory.HARMFUL, 0xb76b00));
	public static final RegistryObject<MobEffect> WEBBED = STATUS_EFFECTS.register("webbed", () -> new WebbedStatusEffect(MobEffectCategory.HARMFUL, 0x2d312f));
	public static final RegistryObject<MobEffect> WEDNESDAY = STATUS_EFFECTS.register("wednesday", () -> new WednesdayStatusEffect(MobEffectCategory.BENEFICIAL, 0x00ff00));

	public static void register(IEventBus modBus) {
		STATUS_EFFECTS.register(modBus);
	}
}
