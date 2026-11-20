/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import kitsuyadev.bewitchment.common.packet.BWNetwork;
import net.minecraftforge.network.PacketDistributor;
import kitsuyadev.bewitchment.api.registry.Contract;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import kitsuyadev.bewitchment.common.registry.BWRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SyncContractsPacket {
	public static final ResourceLocation ID = Bewitchment.id("sync_contracts");

	public static void send(ServerPlayer player) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		CompoundTag contracts = new CompoundTag();
		contracts.put("Contracts", BWComponents.CONTRACTS_COMPONENT.get(player).toNbtContract());
		buf.writeNbt(contracts);
		BWNetwork.sendToClient(PacketDistributor.PLAYER.with(() -> player), ID, buf);
	}

	@SuppressWarnings({"ConstantConditions", "Convert2Lambda"})
	public static class Receiver {
		public static void receive(FriendlyByteBuf buf) {
			Minecraft client = Minecraft.getInstance();
			CompoundTag contractsCompound = buf.readNbt();
			client.execute(new Runnable() {
				@Override
				public void run() {
					if (client.player != null) {
						BWComponents.CONTRACTS_COMPONENT.maybeGet(client.player).ifPresent(contractsComponent -> {
							contractsComponent.getContracts().clear();
							ListTag contractsList = contractsCompound.getList("Contracts", Tag.TAG_COMPOUND);
							for (int i = 0; i < contractsList.size(); i++) {
								CompoundTag contractCompound = contractsList.getCompound(i);
								contractsComponent.addContract(new Contract.Instance(BWRegistries.CONTRACT.get().getValue(new ResourceLocation(contractCompound.getString("Contract"))), contractCompound.getInt("Duration"), contractCompound.getInt("Cost")));
							}
						});
					}
				}
			});
		}
	}
}
