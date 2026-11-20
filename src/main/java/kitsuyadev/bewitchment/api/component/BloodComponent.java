/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.api.component;

import net.minecraftforge.common.MinecraftForge;
import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.api.event.BloodSetEvents;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import kitsuyadev.bewitchment.common.registry.BWTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class BloodComponent implements EntityComponent {
	public static final int MAX_BLOOD = 100;

	private final LivingEntity obj;
	private int blood = MAX_BLOOD;

	public BloodComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		if (tag.contains("Blood")) {
			setBlood(tag.getInt("Blood"));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putInt("Blood", getBlood());
	}

	@Override
	public void serverTick() {
		if (obj.getType().is(BWTags.HAS_BLOOD) && !BewitchmentAPI.isVampire(obj, true) && obj.getRandom().nextFloat() < (obj.isSleeping() ? 1 / 50f : 1 / 500f)) {
			fillBlood(1, false);
		}
	}

	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		MinecraftForge.EVENT_BUS.post(new BloodSetEvents.OnSetBlood(obj, blood));
		this.blood = blood;
		BWComponents.BLOOD_COMPONENT.sync(obj);
	}

	public boolean fillBlood(int amount, boolean simulate) {
		MinecraftForge.EVENT_BUS.post(new BloodSetEvents.OnFillBlood(obj, amount, simulate));
		if (getBlood() < MAX_BLOOD) {
			if (!simulate) {
				setBlood(Math.min(MAX_BLOOD, getBlood() + amount));
			}
			return true;
		}
		return false;
	}

	public boolean drainBlood(int amount, boolean simulate) {
		MinecraftForge.EVENT_BUS.post(new BloodSetEvents.OnDrainBlood(obj, amount, simulate));
		if (getBlood() - amount >= 0) {
			if (!simulate) {
				setBlood(getBlood() - amount);
			}
			return true;
		}
		return false;
	}
}
