/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.component.entity;

import kitsuyadev.bewitchment.api.component.EntityComponent;
import net.minecraft.nbt.CompoundTag;

public class CaduceusFireballComponent implements EntityComponent {
	private boolean fromCaduceus = false;

	@Override
	public void readFromNbt(CompoundTag tag) {
		setFromCaduceus(tag.getBoolean("FromCaduceus"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putBoolean("FromCaduceus", isFromCaduceus());
	}

	public boolean isFromCaduceus() {
		return fromCaduceus;
	}

	public void setFromCaduceus(boolean fromCaduceus) {
		this.fromCaduceus = fromCaduceus;
	}
}
