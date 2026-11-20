/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.packet;

import kitsuyadev.bewitchment.common.packet.BWNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.Consumer;

public final class ClientPacketHandler {
	private static final Map<ResourceLocation, Consumer<FriendlyByteBuf>> RECEIVERS = Map.of(
			SpawnExplosionParticlesPacket.ID, SpawnExplosionParticlesPacket.Receiver::receive,
			SpawnPortalParticlesPacket.ID, SpawnPortalParticlesPacket.Receiver::receive,
			SpawnSmokeParticlesPacket.ID, SpawnSmokeParticlesPacket.Receiver::receive,
			SpawnSpecterBangleParticlesPacket.ID, SpawnSpecterBangleParticlesPacket.Receiver::receive,
			SyncContractsPacket.ID, SyncContractsPacket.Receiver::receive,
			SyncDemonTradesPacket.ID, SyncDemonTradesPacket.Receiver::receive,
			SyncHornedSpearPacket.ID, SyncHornedSpearPacket.Receiver::receive,
			SyncPoppetShelfPacket.ID, SyncPoppetShelfPacket.Receiver::receive);

	private ClientPacketHandler() {
	}

	public static void handle(BWNetwork.ClientPacket packet) {
		Consumer<FriendlyByteBuf> receiver = RECEIVERS.get(packet.id());
		if (receiver != null) {
			packet.read(receiver);
		}
	}
}
