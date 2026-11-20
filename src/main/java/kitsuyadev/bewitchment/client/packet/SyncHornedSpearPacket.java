/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import kitsuyadev.bewitchment.common.packet.BWNetwork;
import net.minecraftforge.network.PacketDistributor;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.entity.projectile.HornedSpearEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SyncHornedSpearPacket {
	public static final ResourceLocation ID = Bewitchment.id("sync_horned_spear");

	public static void send(PacketDistributor.PacketTarget target, HornedSpearEntity entity) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getId());
		buf.writeItem(entity.spear);
		BWNetwork.sendToClient(target, ID, buf);
	}

	@SuppressWarnings("ConstantConditions")
	public static class Receiver {
		public static void receive(FriendlyByteBuf buf) {
			Minecraft client = Minecraft.getInstance();
			int entityId = buf.readInt();
			ItemStack spear = buf.readItem();
			if (client.level != null && client.level.getEntity(entityId) instanceof HornedSpearEntity entity) {
				entity.spear = spear;
			}
		}
	}
}
