/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin.pledge;

import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import kitsuyadev.bewitchment.common.registry.BWPledges;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "getMobType", at = @At("HEAD"), cancellable = true)
	private void getGroup(CallbackInfoReturnable<MobType> callbackInfo) {
		if ((Object) this instanceof Player player && !BWComponents.PLEDGE_COMPONENT.get(player).getPledge().equals(BWPledges.NONE) && !BewitchmentAPI.isVampire(this, true)) {
			callbackInfo.setReturnValue(BewitchmentAPI.DEMON);
		}
	}
}
