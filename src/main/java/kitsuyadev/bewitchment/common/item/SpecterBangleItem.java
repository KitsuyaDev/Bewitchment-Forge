/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.item;

import top.theillusivec4.curios.api.SlotContext;
import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.client.packet.SpawnSpecterBangleParticlesPacket;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpecterBangleItem extends BWAccessoryItem {
	public SpecterBangleItem(Properties settings) {
		super(settings);
	}

	@Override
	public void curioTick(SlotContext slot, ItemStack stack) {
		LivingEntity entity = slot.entity();
		if (entity instanceof ServerPlayer player && entity.isShiftKeyDown() && BewitchmentAPI.drainMagic(player, 1, true)) {
			if (entity.getRandom().nextFloat() < 1 / 40f) {
				BewitchmentAPI.drainMagic(player, 1, false);
			}
			BWComponents.FULL_INVISIBILITY_COMPONENT.maybeGet(player).ifPresent(fullInvisibilityComponent -> {
				if (!fullInvisibilityComponent.isFullInvisible()) {
					fullInvisibilityComponent.setFullInvisible(true);
					entity.setInvisible(true);
				}
			});
			if (entity.getRandom().nextFloat() < 1 / 32f) {
				SpawnSpecterBangleParticlesPacket.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), player);
			}
		}
	}
}
