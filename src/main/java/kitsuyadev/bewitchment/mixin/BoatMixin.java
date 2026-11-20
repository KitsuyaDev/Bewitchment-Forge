/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin;

import kitsuyadev.bewitchment.common.entity.BWBoat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Boat.class)
public class BoatMixin {
	@Redirect(method = "checkFallDamage", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/vehicle/Boat$Type;getPlanks()Lnet/minecraft/world/level/block/Block;"))
	private Block bewitchment$woodSpecificPlanks(Boat.Type type) {
		return (Object) this instanceof BWBoat boat ? boat.wood().planks() : type.getPlanks();
	}
}
