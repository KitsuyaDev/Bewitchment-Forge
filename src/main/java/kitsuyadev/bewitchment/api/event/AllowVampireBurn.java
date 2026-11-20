/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class AllowVampireBurn extends PlayerEvent {
	public AllowVampireBurn(Player player) {
		super(player);
	}

	public static boolean allowBurn(Player player) {
		return !MinecraftForge.EVENT_BUS.post(new AllowVampireBurn(player));
	}
}
