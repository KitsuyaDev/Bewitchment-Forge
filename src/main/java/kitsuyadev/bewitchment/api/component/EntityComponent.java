/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.api.component;

import net.minecraft.nbt.CompoundTag;

public interface EntityComponent {
	void readFromNbt(CompoundTag tag);

	void writeToNbt(CompoundTag tag);

	default void serverTick() {
	}

	default void clientTick() {
	}
}
