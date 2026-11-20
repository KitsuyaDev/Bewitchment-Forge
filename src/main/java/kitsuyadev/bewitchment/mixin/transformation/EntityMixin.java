/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin.transformation;

import kitsuyadev.bewitchment.api.BewitchmentAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public abstract class EntityMixin {
	@ModifyVariable(method = "setPose", at = @At("HEAD"), argsOnly = true)
	private Pose modifySetPose(Pose pose) {
		if ((pose == Pose.FALL_FLYING || pose == Pose.SWIMMING) && (Object) this instanceof Player player
				&& (BewitchmentAPI.isVampire(player, false) || BewitchmentAPI.isWerewolf(player, false))) {
			return Pose.STANDING;
		}
		return pose;
	}
}
