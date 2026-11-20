/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin;

import kitsuyadev.bewitchment.common.registry.BWComponents;
import kitsuyadev.bewitchment.common.component.EntityComponentData;
import kitsuyadev.bewitchment.common.component.LegacyTrinketsInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract UUID getUUID();

	@Shadow
	private Level level;

	@Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
	private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (!callbackInfo.getReturnValueZ() && !level.isClientSide && (Object) this instanceof Mob mob) {
			if (source.getEntity() instanceof LivingEntity living) {
				if (living.getUUID().equals(BWComponents.MINION_COMPONENT.get(mob).getMaster())) {
					callbackInfo.setReturnValue(true);
				} else if (living instanceof Mob mobAttacker && getUUID().equals(BWComponents.MINION_COMPONENT.get(mobAttacker).getMaster())) {
					callbackInfo.setReturnValue(true);
				}
			}
		}
	}

	@Inject(method = "load", at = @At("RETURN"))
	private void bewitchment$loadFabricComponents(CompoundTag nbt, CallbackInfo callback) {
		Entity entity = (Entity) (Object) this;
		EntityComponentData.readFabricSave(entity, nbt);
		if (entity instanceof ServerPlayer player) {
			LegacyTrinketsInventory.load(player, nbt);
		}
	}
}
