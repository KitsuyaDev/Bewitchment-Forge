/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

public abstract class ReviveEvents extends PlayerEvent {
	private final DamageSource source;
	private final ItemStack poppet;

	protected ReviveEvents(Player player, DamageSource source, ItemStack poppet) {
		super(player);
		this.source = source;
		this.poppet = poppet;
	}

	public DamageSource getSource() {
		return source;
	}

	public ItemStack getPoppet() {
		return poppet;
	}

	@Cancelable
	public static class CancelRevive extends ReviveEvents {
		public CancelRevive(Player player, DamageSource source, ItemStack poppet) {
			super(player, source, poppet);
		}
	}

	public static class OnRevive extends ReviveEvents {
		public OnRevive(Player player, DamageSource source, ItemStack poppet) {
			super(player, source, poppet);
		}
	}
}
