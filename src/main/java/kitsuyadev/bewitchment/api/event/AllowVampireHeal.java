/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class AllowVampireHeal extends PlayerEvent {
	private final boolean pledgedToLilith;

	public AllowVampireHeal(Player player, boolean pledgedToLilith) {
		super(player);
		this.pledgedToLilith = pledgedToLilith;
	}

	public boolean isPledgedToLilith() {
		return pledgedToLilith;
	}

	public static boolean allowHeal(Player player, boolean pledgedToLilith) {
		return !MinecraftForge.EVENT_BUS.post(new AllowVampireHeal(player, pledgedToLilith));
	}
}
