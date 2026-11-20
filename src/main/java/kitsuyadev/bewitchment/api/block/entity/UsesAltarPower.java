/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.api.block.entity;

import net.minecraft.core.BlockPos;

public interface UsesAltarPower {
	BlockPos getAltarPos();

	void setAltarPos(BlockPos pos);
}
