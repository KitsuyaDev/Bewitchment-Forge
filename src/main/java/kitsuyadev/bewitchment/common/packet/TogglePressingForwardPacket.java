/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.packet;

import kitsuyadev.bewitchment.api.entity.BroomEntity;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

public record TogglePressingForwardPacket(boolean pressingForward) {
	static void register(SimpleChannel channel) {
		channel.messageBuilder(TogglePressingForwardPacket.class, 3, NetworkDirection.PLAY_TO_SERVER)
				.encoder((packet, buffer) -> buffer.writeBoolean(packet.pressingForward))
				.decoder(TogglePressingForwardPacket::decode)
				.consumerMainThread((packet, context) -> packet.handle(context.get().getSender()))
				.add();
	}

	public static void send(boolean pressingForward) {
		BWNetwork.sendToServer(new TogglePressingForwardPacket(pressingForward));
	}

	private static TogglePressingForwardPacket decode(FriendlyByteBuf buffer) {
		return new TogglePressingForwardPacket(buffer.readBoolean());
	}

	private void handle(ServerPlayer player) {
		if (player != null && player.isAlive() && player.getVehicle() instanceof BroomEntity broom
				&& broom.getFirstPassenger() == player) {
			BWComponents.BROOM_USER_COMPONENT.get(player).setForwardInput(pressingForward);
		}
	}
}
