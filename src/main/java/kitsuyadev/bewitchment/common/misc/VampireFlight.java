/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;

public final class VampireFlight {
	private static final String GRANT = "BewitchmentVampireFlight";

	private VampireFlight() {
	}

	public static void setActive(Player player, boolean active) {
		CompoundTag data = player.getPersistentData();
		Abilities abilities = player.getAbilities();
		boolean intrinsicFlight = player.isCreative() || player.isSpectator();
		if (active && !data.contains(GRANT)) {
			CompoundTag previous = new CompoundTag();
			previous.putBoolean("Allowed", abilities.mayfly && !intrinsicFlight);
			previous.putBoolean("Flying", abilities.flying && !intrinsicFlight);
			data.put(GRANT, previous);
			abilities.mayfly = true;
			abilities.flying = true;
			player.onUpdateAbilities();
		} else if (!active && data.contains(GRANT)) {
			CompoundTag previous = data.getCompound(GRANT);
			data.remove(GRANT);
			if (!intrinsicFlight) {
				abilities.mayfly = previous.getBoolean("Allowed");
				abilities.flying = abilities.mayfly && previous.getBoolean("Flying");
				player.onUpdateAbilities();
			}
		}
	}
}
