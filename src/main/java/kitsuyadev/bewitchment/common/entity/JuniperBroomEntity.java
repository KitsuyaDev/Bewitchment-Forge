/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.entity;

import kitsuyadev.bewitchment.api.entity.BroomEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class JuniperBroomEntity extends BroomEntity {
	public JuniperBroomEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	protected float getSpeed() {
		return 1.5f;
	}
}
