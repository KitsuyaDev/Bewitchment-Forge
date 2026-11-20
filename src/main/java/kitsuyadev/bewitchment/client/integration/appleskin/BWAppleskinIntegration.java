/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.integration.appleskin;

import kitsuyadev.bewitchment.api.BewitchmentAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.common.MinecraftForge;
import squeek.appleskin.api.event.HUDOverlayEvent;

public class BWAppleskinIntegration {
	public static void registerEvents() {
		MinecraftForge.EVENT_BUS.addListener((HUDOverlayEvent.Saturation event) -> cancelForVampire(event));
		MinecraftForge.EVENT_BUS.addListener((HUDOverlayEvent.Exhaustion event) -> cancelForVampire(event));
	}

	private static void cancelForVampire(HUDOverlayEvent event) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player != null && BewitchmentAPI.isVampire(player, true)) {
			event.setCanceled(true);
		}
	}
}
