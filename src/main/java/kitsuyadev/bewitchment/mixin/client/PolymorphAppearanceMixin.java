/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.mixin.client;

import kitsuyadev.bewitchment.client.misc.PolymorphAppearance;
import kitsuyadev.bewitchment.common.component.entity.PolymorphComponent;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(AbstractClientPlayer.class)
public class PolymorphAppearanceMixin {
	@Unique
	private Optional<PolymorphAppearance> bewitchment$appearance() {
		return BWComponents.POLYMORPH_COMPONENT.maybeGet((AbstractClientPlayer) (Object) this)
				.map(PolymorphComponent::getActiveProfile).map(PolymorphAppearance::get);
	}

	@Inject(method = "getSkinTextureLocation", at = @At("HEAD"), cancellable = true)
	private void bewitchment$skin(CallbackInfoReturnable<ResourceLocation> callback) {
		bewitchment$appearance().ifPresent(appearance -> callback.setReturnValue(appearance.skin()));
	}

	@Inject(method = "getCloakTextureLocation", at = @At("HEAD"), cancellable = true)
	private void bewitchment$cape(CallbackInfoReturnable<ResourceLocation> callback) {
		bewitchment$appearance().ifPresent(appearance -> callback.setReturnValue(appearance.cape()));
	}

	@Inject(method = "getElytraTextureLocation", at = @At("HEAD"), cancellable = true)
	private void bewitchment$elytra(CallbackInfoReturnable<ResourceLocation> callback) {
		bewitchment$appearance().ifPresent(appearance -> callback.setReturnValue(appearance.elytra()));
	}

	@Inject(method = "getModelName", at = @At("HEAD"), cancellable = true)
	private void bewitchment$model(CallbackInfoReturnable<String> callback) {
		bewitchment$appearance().ifPresent(appearance -> callback.setReturnValue(appearance.model()));
	}
}
