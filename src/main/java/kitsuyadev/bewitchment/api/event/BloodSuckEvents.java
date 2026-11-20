/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public abstract class BloodSuckEvents extends PlayerEvent {
	private final LivingEntity target;

	protected BloodSuckEvents(Player player, LivingEntity target) {
		super(player);
		this.target = target;
	}

	public LivingEntity getTarget() {
		return target;
	}

	public static class OnBloodSuck extends BloodSuckEvents {
		private final int amount;

		public OnBloodSuck(Player player, LivingEntity target, int amount) {
			super(player, target);
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}
	}

	public static class SetBloodAmount extends BloodSuckEvents {
		private int amount;

		public SetBloodAmount(Player player, LivingEntity target, int amount) {
			super(player, target);
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}

		public void increaseAmountTo(int amount) {
			this.amount = Math.max(this.amount, amount);
		}
	}

	public static int bloodAmount(Player player, LivingEntity target, int amount) {
		SetBloodAmount event = new SetBloodAmount(player, target, amount);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getAmount();
	}
}
