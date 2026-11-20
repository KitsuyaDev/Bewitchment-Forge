/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin.integration.apoli;

import kitsuyadev.bewitchment.api.BewitchmentAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Method;

@Pseudo
@Mixin(targets = "io.github.edwinmindcraft.apoli.common.power.PreventItemActionPower", remap = false)
public class PreventItemUsePowerMixin {
	@Unique
	private static final ResourceLocation VEGETARIAN = new ResourceLocation("origins", "vegetarian");

	@Inject(method = {"lambda$isUsagePrevented$0", "lambda$getPreventingForDisplay$1"},
			at = @At("RETURN"), cancellable = true)
	private static void bewitchment$werewolfDiet(Entity entity, ItemStack stack, @Coerce Object power,
												CallbackInfoReturnable<Boolean> callback) {
		if (callback.getReturnValueZ() && entity instanceof LivingEntity living && BewitchmentAPI.isWerewolf(living, true)) {
			try {
				Method getId = power.getClass().getMethod("getRegistryName");
				if (VEGETARIAN.equals(getId.invoke(power))) {
					callback.setReturnValue(false);
				}
			} catch (ReflectiveOperationException exception) {
				throw new IllegalStateException("Could not read the Origins Forge power ID", exception);
			}
		}
	}
}
