/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin.pledge;

import kitsuyadev.bewitchment.api.entity.Pledgeable;
import kitsuyadev.bewitchment.common.world.BWUniversalWorldState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	private Level level;

	@Inject(method = "setRemoved", at = @At("TAIL"))
	private void bewitchment$pledge$markAsRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
		if (!level.isClientSide && reason.shouldDestroy() && this instanceof Pledgeable pledgeable) {
			BWUniversalWorldState universalWorldState = BWUniversalWorldState.get(level);
			universalWorldState.pledgesToRemove.addAll(pledgeable.getPledgedPlayerUUIDs());
			universalWorldState.setDirty();
		}
	}
}
