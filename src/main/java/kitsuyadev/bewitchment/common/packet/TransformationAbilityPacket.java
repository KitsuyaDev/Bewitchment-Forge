/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.packet;

import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.api.component.TransformationComponent;
import kitsuyadev.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import kitsuyadev.bewitchment.common.misc.VampireFlight;
import kitsuyadev.bewitchment.common.registry.*;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import virtuoel.pehkui.api.ScaleData;

public record TransformationAbilityPacket() {
	private static final float VAMPIRE_WIDTH = EntityType.BAT.getWidth() / EntityType.PLAYER.getWidth();
	private static final float VAMPIRE_HEIGHT = EntityType.BAT.getHeight() / EntityType.PLAYER.getHeight();

	public static void send() {
		BWNetwork.sendToServer(new TransformationAbilityPacket());
	}

	private static boolean canUseAbility(Player player) {
		TransformationComponent transformationComponent = BWComponents.TRANSFORMATION_COMPONENT.get(player);
		if (transformationComponent.getTransformation() == BWTransformations.VAMPIRE) {
			return true;
		}
		if (transformationComponent.getTransformation() == BWTransformations.WEREWOLF) {
			return !BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(player).isForcedTransformation();
		}
		return false;
	}

	public static void useAbility(Player player, boolean forced) {
		if (!(player instanceof ServerPlayer)) {
			return;
		}
		BWComponents.TRANSFORMATION_COMPONENT.maybeGet(player).ifPresent(transformationComponent -> {
			Level world = player.level();
			boolean isAlternateForm = transformationComponent.isAlternateForm();
			ScaleData width = BWScaleTypes.MODIFY_WIDTH_TYPE.getScaleData(player);
			ScaleData height = BWScaleTypes.MODIFY_HEIGHT_TYPE.getScaleData(player);
			if (transformationComponent.getTransformation() == BWTransformations.VAMPIRE && (forced || (BewitchmentAPI.isPledged(player, BWPledges.LILITH) && BWComponents.BLOOD_COMPONENT.get(player).getBlood() > 0))) {
				SpawnSmokeParticlesPacket.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), player);
				world.playSound(null, player.blockPosition(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM.get(), player.getSoundSource(), 1, 1);
				transformationComponent.setAlternateForm(!isAlternateForm);
				if (isAlternateForm) {
					width.setScale(width.getBaseScale() / VAMPIRE_WIDTH);
					height.setScale(height.getBaseScale() / VAMPIRE_HEIGHT);
					VampireFlight.setActive(player, false);
				} else {
					width.setScale(width.getBaseScale() * VAMPIRE_WIDTH);
					height.setScale(height.getBaseScale() * VAMPIRE_HEIGHT);
					VampireFlight.setActive(player, true);
				}
			} else if (transformationComponent.getTransformation() == BWTransformations.WEREWOLF && (forced || BewitchmentAPI.isPledged(player, BWPledges.HERNE))) {
				EntityType<?> werewolf = BWEntityTypes.WEREWOLF.get();
				float werewolfWidth = werewolf.getWidth() / EntityType.PLAYER.getWidth();
				float werewolfHeight = werewolf.getHeight() / EntityType.PLAYER.getHeight();
				SpawnSmokeParticlesPacket.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), player);
				world.playSound(null, player.blockPosition(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM.get(), player.getSoundSource(), 1, 1);
				transformationComponent.setAlternateForm(!isAlternateForm);
				if (isAlternateForm) {
					width.setScale(width.getBaseScale() / werewolfWidth);
					height.setScale(height.getBaseScale() / werewolfHeight);
					if (player.hasEffect(MobEffects.NIGHT_VISION) && player.getEffect(MobEffects.NIGHT_VISION).isAmbient()) {
						player.removeEffect(MobEffects.NIGHT_VISION);
					}
				} else {
					width.setScale(width.getBaseScale() * werewolfWidth);
					height.setScale(height.getBaseScale() * werewolfHeight);
				}
			}
		});
	}

	static void register(SimpleChannel channel) {
		channel.messageBuilder(TransformationAbilityPacket.class, 4, NetworkDirection.PLAY_TO_SERVER)
				.encoder((packet, buffer) -> {})
				.decoder(TransformationAbilityPacket::decode)
				.consumerMainThread((packet, context) -> packet.handle(context.get().getSender()))
				.add();
	}

	private static TransformationAbilityPacket decode(FriendlyByteBuf buffer) {
		return new TransformationAbilityPacket();
	}

	private void handle(ServerPlayer player) {
		if (player != null && player.isAlive() && canUseAbility(player)) {
			long now = player.serverLevel().getGameTime();
			String cooldownKey = "BewitchmentTransformationCooldown";
			if (player.getPersistentData().getLong(cooldownKey) <= now) {
				player.getPersistentData().putLong(cooldownKey, now + 20);
				useAbility(player, false);
			}
		}
	}
}
