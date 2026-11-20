/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.packet;

import io.netty.buffer.Unpooled;
import kitsuyadev.bewitchment.client.packet.ClientComponentSync;
import kitsuyadev.bewitchment.client.packet.ClientPacketHandler;
import kitsuyadev.bewitchment.common.Bewitchment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Consumer;

public final class BWNetwork {
	private static final String VERSION = "1";
	private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			Bewitchment.id("main"), () -> VERSION, VERSION::equals, VERSION::equals);

	private BWNetwork() {
	}

	public static void register() {
		CHANNEL.messageBuilder(ComponentSync.class, 0, NetworkDirection.PLAY_TO_CLIENT)
				.encoder(ComponentSync::encode)
				.decoder(ComponentSync::decode)
				.consumerMainThread((message, context) ->
						DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientComponentSync.handle(message)))
				.add();
		CHANNEL.messageBuilder(ClientPacket.class, 1, NetworkDirection.PLAY_TO_CLIENT)
				.encoder(ClientPacket::encode)
				.decoder(ClientPacket::decode)
				.consumerMainThread((message, context) ->
						DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handle(message)))
				.add();
		CauldronTeleportPacket.register(CHANNEL);
		TogglePressingForwardPacket.register(CHANNEL);
		TransformationAbilityPacket.register(CHANNEL);
	}

	static void sendToServer(Object packet) {
		CHANNEL.sendToServer(packet);
	}

	public static void syncComponents(PacketDistributor.PacketTarget target, Entity entity, CompoundTag components) {
		if (!components.isEmpty()) {
			CHANNEL.send(target, new ComponentSync(entity.getId(), components));
		}
	}

	public static void sendToClient(PacketDistributor.PacketTarget target, ResourceLocation id, FriendlyByteBuf buffer) {
		try {
			byte[] bytes = new byte[buffer.readableBytes()];
			buffer.readBytes(bytes);
			CHANNEL.send(target, new ClientPacket(id, bytes));
		} finally {
			buffer.release();
		}
	}

	public record ClientPacket(ResourceLocation id, byte[] bytes) {
		private static final int MAX_PAYLOAD = 1_048_576;

		private void encode(FriendlyByteBuf buffer) {
			buffer.writeResourceLocation(id);
			buffer.writeByteArray(bytes);
		}

		private static ClientPacket decode(FriendlyByteBuf buffer) {
			return new ClientPacket(buffer.readResourceLocation(), buffer.readByteArray(MAX_PAYLOAD));
		}

		public void read(Consumer<FriendlyByteBuf> receiver) {
			FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.wrappedBuffer(bytes));
			try {
				receiver.accept(buffer);
			} finally {
				buffer.release();
			}
		}
	}

	public record ComponentSync(int entityId, CompoundTag components) {
		private void encode(FriendlyByteBuf buffer) {
			buffer.writeVarInt(entityId);
			buffer.writeNbt(components);
		}

		private static ComponentSync decode(FriendlyByteBuf buffer) {
			return new ComponentSync(buffer.readVarInt(), buffer.readNbt());
		}
	}
}
