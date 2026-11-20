/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.component.entity;

import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.api.component.EntityComponent;
import kitsuyadev.bewitchment.api.entity.BroomEntity;
import kitsuyadev.bewitchment.common.registry.BWEntityTypes;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class BroomUserComponent implements EntityComponent {
	private final Player obj;
	private boolean pressingForward;
	private boolean forwardInput;

	public BroomUserComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		setPressingForward(tag.getBoolean("PressingForward"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("PressingForward", isPressingForward());
	}

	public boolean isPressingForward() {
		return pressingForward;
	}

	public void setForwardInput(boolean forwardInput) {
		this.forwardInput = forwardInput;
	}

	@Override
	public void serverTick() {
		if (!(obj.getVehicle() instanceof BroomEntity broom) || broom.getFirstPassenger() != obj) {
			forwardInput = false;
		}
		boolean hasForward = forwardInput;
		if (hasForward && BewitchmentAPI.getFamiliar(obj) != BWEntityTypes.OWL.get()) {
			hasForward = BewitchmentAPI.drainMagic(obj, 1, obj.tickCount % 60 != 0);
		}
		setPressingForward(hasForward);
	}

	private void setPressingForward(boolean pressingForward) {
		if (this.pressingForward == pressingForward) {
			return;
		}
		this.pressingForward = pressingForward;
		BWComponents.BROOM_USER_COMPONENT.sync(obj);
	}
}
