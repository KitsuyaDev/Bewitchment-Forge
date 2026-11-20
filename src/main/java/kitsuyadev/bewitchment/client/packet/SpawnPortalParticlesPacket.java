/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import kitsuyadev.bewitchment.common.packet.BWNetwork;
import net.minecraftforge.network.PacketDistributor;
import kitsuyadev.bewitchment.common.Bewitchment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class SpawnPortalParticlesPacket {
	public static final ResourceLocation ID = Bewitchment.id("spawn_portal_particles");

	public static void send(ServerPlayer player, Entity entity) {
		send(PacketDistributor.PLAYER.with(() -> player), entity);
	}

	public static void send(PacketDistributor.PacketTarget target, Entity entity) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		BWNetwork.sendToClient(target, ID, buf);
	}

	public static class Receiver {
		public static void receive(FriendlyByteBuf buf) {
			Minecraft client = Minecraft.getInstance();
			int id = buf.readInt();
			client.execute(() -> {
				ClientLevel world = client.level;
				if (world != null) {
					Entity entity = world.getEntity(id);
					if (entity != null) {
						for (int i = 0; i < 32; i++) {
							world.addParticle(ParticleTypes.PORTAL, entity.getRandomX(1), entity.getRandomY(), entity.getRandomZ(1), 0, 0, 0);
						}
					}
				}
			});
		}
	}
}
