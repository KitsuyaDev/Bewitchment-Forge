/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.packet;

import kitsuyadev.bewitchment.common.component.EntityComponentData;
import kitsuyadev.bewitchment.common.packet.BWNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public final class ClientComponentSync {
	private ClientComponentSync() {
	}

	public static void handle(BWNetwork.ComponentSync message) {
		Minecraft client = Minecraft.getInstance();
		if (client.level != null && message.components() != null) {
			Entity entity = client.player != null && client.player.getId() == message.entityId()
					? client.player : client.level.getEntity(message.entityId());
			if (entity != null) {
				EntityComponentData.get(entity).ifPresent(data -> data.readSync(message.components()));
			}
		}
	}
}
