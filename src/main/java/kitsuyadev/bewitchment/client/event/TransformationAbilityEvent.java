package kitsuyadev.bewitchment.client.event;

import kitsuyadev.bewitchment.api.entity.BroomEntity;
import kitsuyadev.bewitchment.client.BewitchmentClient;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.packet.TogglePressingForwardPacket;
import kitsuyadev.bewitchment.common.packet.TransformationAbilityPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bewitchment.MOD_ID, value = Dist.CLIENT)
public class TransformationAbilityEvent {
	private static int transformationAbilityCooldown;
	private static boolean wasPressingForward;

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		Minecraft client = Minecraft.getInstance();
		LocalPlayer player = client.player;
		if (player == null) {
			transformationAbilityCooldown = 0;
			wasPressingForward = false;
			return;
		}
		if (transformationAbilityCooldown > 0) {
			transformationAbilityCooldown--;
		} else if (BewitchmentClient.TRANSFORMATION_ABILITY.isDown()) {
			transformationAbilityCooldown = 20;
			TransformationAbilityPacket.send();
		}
		boolean pressingForward = client.options.keyUp.isDown() && player.getVehicle() instanceof BroomEntity;
		if (pressingForward != wasPressingForward || (pressingForward && player.tickCount % 10 == 0)) {
			TogglePressingForwardPacket.send(pressingForward);
		}
		wasPressingForward = pressingForward;
	}
}
