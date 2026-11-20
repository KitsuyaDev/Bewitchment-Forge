/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.packet;

import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.block.entity.WitchAltarBlockEntity;
import kitsuyadev.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import kitsuyadev.bewitchment.common.misc.BWUtil;
import kitsuyadev.bewitchment.common.registry.BWPledges;
import kitsuyadev.bewitchment.common.world.BWWorldState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Map;

public record CauldronTeleportPacket(BlockPos cauldronPos, String keyword) {
	private static final int MAX_KEYWORD_LENGTH = 256;

	static void register(SimpleChannel channel) {
		channel.messageBuilder(CauldronTeleportPacket.class, 2, NetworkDirection.PLAY_TO_SERVER)
				.encoder(CauldronTeleportPacket::encode)
				.decoder(CauldronTeleportPacket::decode)
				.consumerMainThread((packet, context) -> packet.handle(context.get().getSender()))
				.add();
	}

	public static void send(BlockPos cauldronPos, String keyword) {
		if (keyword.length() <= MAX_KEYWORD_LENGTH) {
			BWNetwork.sendToServer(new CauldronTeleportPacket(cauldronPos, keyword));
		}
	}

	private void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(cauldronPos);
		buffer.writeUtf(keyword, MAX_KEYWORD_LENGTH);
	}

	private static CauldronTeleportPacket decode(FriendlyByteBuf buffer) {
		return new CauldronTeleportPacket(buffer.readBlockPos(), buffer.readUtf(MAX_KEYWORD_LENGTH));
	}

	private void handle(ServerPlayer player) {
		if (player == null || !player.isAlive() || !cauldronPos.equals(player.blockPosition())) {
			return;
		}
		ServerLevel level = player.serverLevel();
		if (!level.hasChunkAt(cauldronPos)
				|| !(level.getBlockEntity(cauldronPos) instanceof WitchCauldronBlockEntity cauldron)
				|| cauldron.mode != WitchCauldronBlockEntity.Mode.TELEPORTATION) {
			return;
		}

		BlockPos closest = null;
		double closestDistance = Double.MAX_VALUE;
		for (Map.Entry<Long, String> entry : BWWorldState.get(level).witchCauldrons.entrySet()) {
			if (!keyword.equals(entry.getValue())) {
				continue;
			}
			BlockPos destination = BlockPos.of(entry.getKey());
			double distance = destination.distToCenterSqr(player.position());
			if (distance < closestDistance && level.getWorldBorder().isWithinBounds(destination)) {
				closest = destination;
				closestDistance = distance;
			}
		}
		if (closest == null) {
			player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.invalid_cauldron", keyword), true);
			return;
		}

		boolean hasPower = BewitchmentAPI.isPledged(player, BWPledges.LEONARD);
		BlockPos altarPos = cauldron.getAltarPos();
		if (!hasPower && altarPos != null && level.hasChunkAt(altarPos)
				&& level.getBlockEntity(altarPos) instanceof WitchAltarBlockEntity altar) {
			hasPower = altar.drain((int) Math.sqrt(closestDistance) / 2, false);
		}
		if (hasPower) {
			BWUtil.teleport(player, closest.getX() + 0.5, closest.getY() - 0.4375, closest.getZ() + 0.5, true);
		} else {
			player.displayClientMessage(Component.translatable(Bewitchment.MOD_ID + ".message.insufficent_altar_power", keyword), true);
		}
	}
}
