/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public abstract class BloodSetEvents extends LivingEvent {
	private final int amount;

	protected BloodSetEvents(LivingEntity entity, int amount) {
		super(entity);
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public static class OnSetBlood extends BloodSetEvents {
		public OnSetBlood(LivingEntity entity, int amount) {
			super(entity, amount);
		}
	}

	public abstract static class BloodTransfer extends BloodSetEvents {
		private final boolean simulate;

		protected BloodTransfer(LivingEntity entity, int amount, boolean simulate) {
			super(entity, amount);
			this.simulate = simulate;
		}

		public boolean isSimulated() {
			return simulate;
		}
	}

	public static class OnFillBlood extends BloodTransfer {
		public OnFillBlood(LivingEntity entity, int amount, boolean simulate) {
			super(entity, amount, simulate);
		}
	}

	public static class OnDrainBlood extends BloodTransfer {
		public OnDrainBlood(LivingEntity entity, int amount, boolean simulate) {
			super(entity, amount, simulate);
		}
	}
}
