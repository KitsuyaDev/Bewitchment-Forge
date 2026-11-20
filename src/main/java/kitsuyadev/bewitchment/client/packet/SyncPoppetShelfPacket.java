/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.packet;

import io.netty.buffer.Unpooled;
import kitsuyadev.bewitchment.common.packet.BWNetwork;
import net.minecraftforge.network.PacketDistributor;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.block.entity.PoppetShelfBlockEntity;
import kitsuyadev.bewitchment.common.world.BWWorldState;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public class SyncPoppetShelfPacket {
	public static final ResourceLocation ID = Bewitchment.id("sync_poppet_shelf");

	public static void send(ServerPlayer player, BlockPos pos) {
		send(PacketDistributor.PLAYER.with(() -> player), player.serverLevel(), pos);
	}

	public static void send(PacketDistributor.PacketTarget target, ServerLevel level, BlockPos pos) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeLong(pos.asLong());
		CompoundTag nbt = new CompoundTag();
		NonNullList<ItemStack> inventory = BWWorldState.get(level).poppetShelves.get(pos.asLong());
		if (inventory != null) {
			ContainerHelper.saveAllItems(nbt, inventory);
		}
		buf.writeNbt(nbt);
		BWNetwork.sendToClient(target, ID, buf);
	}

	@SuppressWarnings({"ConstantConditions", "Convert2Lambda"})
	public static class Receiver {
		public static void receive(FriendlyByteBuf buf) {
			Minecraft client = Minecraft.getInstance();
			BlockPos pos = BlockPos.of(buf.readLong());
			NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
			CompoundTag nbt = buf.readNbt();
			if (nbt != null && !nbt.isEmpty()) {
				ContainerHelper.loadAllItems(nbt, inventory);
			}
			client.execute(new Runnable() {
				@Override
				public void run() {
					if (client.level != null && client.level.getBlockEntity(pos) instanceof PoppetShelfBlockEntity poppetShelfBlockEntity) {
						poppetShelfBlockEntity.clientInventory = inventory;
					}
				}
			});
		}
	}
}
