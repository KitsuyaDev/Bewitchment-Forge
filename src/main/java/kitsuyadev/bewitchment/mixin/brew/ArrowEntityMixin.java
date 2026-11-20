/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin.brew;

import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Mixin(Arrow.class)
public abstract class ArrowEntityMixin extends Entity {
	public ArrowEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Redirect(method = "setEffectsFromItem", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/item/alchemy/PotionUtils;getCustomEffects(Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"))
	private List<MobEffectInstance> bewitchment$scaleArrowEffects(ItemStack stack) {
		List<MobEffectInstance> effects = PotionUtils.getCustomEffects(stack);
		if (!stack.hasTag() || !stack.getTag().getBoolean("BewitchmentBrew")) {
			return effects;
		}
		List<MobEffectInstance> scaled = new ArrayList<>(effects.size());
		for (MobEffectInstance effect : effects) {
			scaled.add(new MobEffectInstance(effect.getEffect(), effect.getDuration() / 8, effect.getAmplifier(),
					effect.isAmbient(), effect.isVisible(), effect.showIcon()));
		}
		return scaled;
	}

	@Inject(method = "getPickupItem", at = @At(value = "RETURN", ordinal = 1))
	private void asItemStack(CallbackInfoReturnable<ItemStack> callbackInfo) {
		ItemStack stack = callbackInfo.getReturnValue();
		BWComponents.POLYMORPH_COMPONENT.maybeGet(this).ifPresent(polymorphComponent -> {
			if (polymorphComponent.getUuid() != null) {
				stack.getOrCreateTag().putUUID("PolymorphUUID", polymorphComponent.getUuid());
				stack.getOrCreateTag().putString("PolymorphName", polymorphComponent.getName());
			}
		});
	}

	@Inject(method = "doPostHurtEffects", at = @At("HEAD"))
	private void onHit(LivingEntity target, CallbackInfo callbackInfo) {
		BWComponents.POLYMORPH_COMPONENT.maybeGet(this).ifPresent(thisPolymorphComponent -> {
			if (thisPolymorphComponent.getUuid() != null) {
				BWComponents.POLYMORPH_COMPONENT.maybeGet(target).ifPresent(targetPolymorphComponent -> {
					targetPolymorphComponent.setUuid(thisPolymorphComponent.getUuid());
					targetPolymorphComponent.setName(thisPolymorphComponent.getName());
				});
			}
		});
	}

	@SuppressWarnings("ConstantConditions")
	@Inject(method = "setEffectsFromItem", at = @At("TAIL"))
	private void initFromStack(ItemStack stack, CallbackInfo callbackInfo) {
		BWComponents.POLYMORPH_COMPONENT.maybeGet(this).ifPresent(polymorphComponent -> {
			if (stack.hasTag() && stack.getTag().contains("PolymorphUUID")) {
				polymorphComponent.setUuid(stack.getTag().getUUID("PolymorphUUID"));
				polymorphComponent.setName(stack.getTag().getString("PolymorphName"));
			}
		});
	}
}
