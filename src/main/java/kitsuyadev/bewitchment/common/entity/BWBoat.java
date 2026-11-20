/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.entity;

import kitsuyadev.bewitchment.common.registry.BWBoatTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public interface BWBoat {
	BWBoatTypes.Wood wood();

	class Open extends Boat implements BWBoat {
		private final BWBoatTypes.Wood wood;

		public Open(EntityType<? extends Boat> type, Level level, BWBoatTypes.Wood wood) {
			super(type, level);
			this.wood = wood;
		}

		@Override
		public BWBoatTypes.Wood wood() {
			return wood;
		}

		@Override
		public Item getDropItem() {
			return wood.item(false);
		}
	}

	class Chest extends ChestBoat implements BWBoat {
		private final BWBoatTypes.Wood wood;

		public Chest(EntityType<Boat> type, Level level, BWBoatTypes.Wood wood) {
			super(cast(type), level);
			this.wood = wood;
		}

		@SuppressWarnings("unchecked")
		private static EntityType<? extends ChestBoat> cast(EntityType<?> type) {
			return (EntityType<? extends ChestBoat>) type;
		}

		@Override
		public BWBoatTypes.Wood wood() {
			return wood;
		}

		@Override
		public Item getDropItem() {
			return wood.item(true);
		}
	}
}
