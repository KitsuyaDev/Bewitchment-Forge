package kitsuyadev.bewitchment.client.event;

import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.block.entity.WitchCauldronBlockEntity;
import kitsuyadev.bewitchment.common.packet.CauldronTeleportPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bewitchment.MOD_ID, value = Dist.CLIENT)
public class CauldronTeleportEvent {
	@SubscribeEvent
	public static void onChat(ClientChatEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null && !event.getMessage().startsWith("/")
				&& player.level().getBlockEntity(player.blockPosition()) instanceof WitchCauldronBlockEntity cauldron
				&& cauldron.mode == WitchCauldronBlockEntity.Mode.TELEPORTATION) {
			CauldronTeleportPacket.send(cauldron.getBlockPos(), event.getMessage());
			event.setCanceled(true);
		}
	}
}
