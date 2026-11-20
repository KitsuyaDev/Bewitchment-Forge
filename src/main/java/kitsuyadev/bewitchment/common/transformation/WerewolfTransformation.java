/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.transformation;

import kitsuyadev.bewitchment.api.registry.Transformation;
import kitsuyadev.bewitchment.common.entity.living.WerewolfEntity;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.world.entity.player.Player;

public class WerewolfTransformation extends Transformation {
	@Override
	public void onAdded(Player player) {
		BWComponents.ADDITIONAL_WEREWOLF_DATA_COMPONENT.get(player).setVariant(player.getRandom().nextInt(WerewolfEntity.getVariantsStatic()));
	}
}
