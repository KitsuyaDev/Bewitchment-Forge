/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin;

import kitsuyadev.bewitchment.common.registry.BWBoatTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin {
	@ModifyVariable(method = "create(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;",
			at = @At("HEAD"), argsOnly = true)
	private static CompoundTag bewitchment$loadTerraformBoat(CompoundTag tag) {
		return BWBoatTypes.convertTerraformBoat(tag);
	}
}
