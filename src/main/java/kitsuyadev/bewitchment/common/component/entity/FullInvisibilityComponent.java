/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.component.entity;

import kitsuyadev.bewitchment.api.component.EntityComponent;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class FullInvisibilityComponent implements EntityComponent {
	private final Player obj;
	private boolean fullInvisible = false;

	public FullInvisibilityComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		setFullInvisible(tag.getBoolean("FullInvisible"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("FullInvisible", isFullInvisible());
	}

	@Override
	public void serverTick() {
		if (isFullInvisible() && !obj.isShiftKeyDown()) {
			setFullInvisible(false);
			obj.setInvisible(false);
			obj.removeEffect(MobEffects.INVISIBILITY);
		}
	}

	public boolean isFullInvisible() {
		return fullInvisible;
	}

	public void setFullInvisible(boolean fullInvisible) {
		this.fullInvisible = fullInvisible;
		BWComponents.FULL_INVISIBILITY_COMPONENT.sync(obj);
	}
}
