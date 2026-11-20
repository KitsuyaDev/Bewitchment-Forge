/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.fortune;

import kitsuyadev.bewitchment.api.registry.Fortune;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class ExhaustedFortune extends Fortune {
	public ExhaustedFortune(boolean positive) {
		super(positive);
	}

	@Override
	public boolean finish(ServerLevel world, Player target) {
		return BWComponents.MAGIC_COMPONENT.get(target).drainMagic(50, false);
	}
}
