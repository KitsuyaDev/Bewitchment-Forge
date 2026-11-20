/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin.brew;

import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(ThrownPotion.class)
public abstract class PotionEntityMixin extends ThrowableItemProjectile {
	public PotionEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
		super(entityType, world);
	}

	@ModifyArg(method = "makeAreaOfEffectCloud", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/AreaEffectCloud;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)V"))
	private MobEffectInstance bewitchment$scaleLingeringEffect(MobEffectInstance effect) {
		ItemStack stack = getItemRaw();
		if (stack.hasTag() && stack.getTag().getBoolean("BewitchmentBrew")) {
			return new MobEffectInstance(effect.getEffect(), effect.getDuration() / 4, effect.getAmplifier(),
					effect.isAmbient(), effect.isVisible(), effect.showIcon());
		}
		return effect;
	}

	@Inject(method = "applySplash", at = @At("HEAD"))
	private void applySplashPotion(List<MobEffectInstance> statusEffects, @Nullable Entity entity, CallbackInfo callbackInfo) {
		if (getItemRaw().hasTag() && getItemRaw().getTag().contains("PolymorphUUID")) {
			UUID uuid = getItemRaw().getTag().getUUID("PolymorphUUID");
			String name = getItemRaw().getTag().getString("PolymorphName");
			for (LivingEntity livingEntity : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4, 2, 4))) {
				BWComponents.POLYMORPH_COMPONENT.maybeGet(livingEntity).ifPresent(polymorphComponent -> {
					polymorphComponent.setUuid(uuid);
					polymorphComponent.setName(name);
				});
			}
		}
	}

	@ModifyArg(method = "makeAreaOfEffectCloud", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
	private Entity bewitchment$setLingeringPolymorph(Entity cloud) {
		ItemStack stack = getItemRaw();
		if (stack.hasTag() && stack.getTag().contains("PolymorphUUID")) {
			BWComponents.POLYMORPH_COMPONENT.maybeGet(cloud).ifPresent(polymorphComponent -> {
				polymorphComponent.setUuid(stack.getTag().getUUID("PolymorphUUID"));
				polymorphComponent.setName(stack.getTag().getString("PolymorphName"));
			});
		}
		return cloud;
	}
}
