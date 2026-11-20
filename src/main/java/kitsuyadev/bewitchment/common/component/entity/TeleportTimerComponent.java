/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.component.entity;

import kitsuyadev.bewitchment.api.component.EntityComponent;
import net.minecraft.nbt.CompoundTag;

public class TeleportTimerComponent implements EntityComponent {
	private int teleportTimer = 0;

	@Override
	public void readFromNbt(CompoundTag tag) {
		setTeleportTimer(tag.getInt("TeleportTimer"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("TeleportTimer", getTeleportTimer());
	}

	@Override
	public void serverTick() {
		if (getTeleportTimer() > 0) {
			setTeleportTimer(getTeleportTimer() - 1);
		}
	}

	public int getTeleportTimer() {
		return teleportTimer;
	}

	public void setTeleportTimer(int teleportTimer) {
		this.teleportTimer = teleportTimer;
	}
}
