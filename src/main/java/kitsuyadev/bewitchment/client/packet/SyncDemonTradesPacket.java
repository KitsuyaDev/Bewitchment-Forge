/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import kitsuyadev.bewitchment.common.packet.BWNetwork;
import net.minecraftforge.network.PacketDistributor;
import kitsuyadev.bewitchment.client.screen.DemonScreenHandler;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.entity.DemonMerchant;
import kitsuyadev.bewitchment.common.entity.living.DemonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import java.util.List;

public class SyncDemonTradesPacket {
	public static final ResourceLocation ID = Bewitchment.id("sync_demon_trades");

	public static void send(ServerPlayer player, DemonMerchant merchant, int syncId) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(syncId);
		DemonEntity.DemonTradeOffer.toPacket(merchant.getOffers(), buf);
		buf.writeInt(merchant.getDemonTrader().getId());
		buf.writeBoolean(merchant.isDiscount());
		BWNetwork.sendToClient(PacketDistributor.PLAYER.with(() -> player), ID, buf);
	}

	public static class Receiver {
		public static void receive(FriendlyByteBuf buf) {
			Minecraft client = Minecraft.getInstance();
			int syncId = buf.readInt();
			List<DemonEntity.DemonTradeOffer> offers = DemonEntity.DemonTradeOffer.fromPacket(buf);
			int traderId = buf.readInt();
			boolean discount = buf.readBoolean();
			client.execute(() -> {
				if (client.player != null) {
					AbstractContainerMenu screenHandler = client.player.containerMenu;
					if (syncId == screenHandler.containerId && screenHandler instanceof DemonScreenHandler) {
						((DemonScreenHandler) screenHandler).demonMerchant.setCurrentCustomer(client.player);
						((DemonScreenHandler) screenHandler).demonMerchant.setOffersClientside(offers);
						((DemonScreenHandler) screenHandler).demonMerchant.setDemonTraderClientside((LivingEntity) client.level.getEntity(traderId));
						((DemonScreenHandler) screenHandler).demonMerchant.setDiscountClientside(discount);
					}
				}
			});
		}
	}
}
