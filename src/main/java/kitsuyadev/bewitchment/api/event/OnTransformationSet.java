/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.api.event;

import kitsuyadev.bewitchment.api.registry.Transformation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class OnTransformationSet extends PlayerEvent {
	private final Transformation transformation;

	public OnTransformationSet(Player player, Transformation transformation) {
		super(player);
		this.transformation = transformation;
	}

	public Transformation getTransformation() {
		return transformation;
	}
}
