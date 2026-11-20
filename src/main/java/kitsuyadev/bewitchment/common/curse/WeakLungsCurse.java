/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.curse;

import kitsuyadev.bewitchment.api.registry.Curse;
import net.minecraft.world.entity.LivingEntity;

public class WeakLungsCurse extends Curse {
	public WeakLungsCurse(Type type) {
		super(type);
	}

	@Override
	public void tick(LivingEntity target) {
		if (target.getAirSupply() > -15) {
			target.setAirSupply(-15);
		}
	}
}
