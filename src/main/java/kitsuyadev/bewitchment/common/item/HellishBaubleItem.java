/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.item;

import top.theillusivec4.curios.api.SlotContext;
import kitsuyadev.bewitchment.api.BewitchmentAPI;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HellishBaubleItem extends BWAccessoryItem {
	public HellishBaubleItem(Properties settings) {
		super(settings);
	}

	@Override
	public void curioTick(SlotContext slot, ItemStack stack) {
		LivingEntity entity = slot.entity();
		if (entity instanceof Player player && !entity.level().isClientSide && entity.tickCount % 10 == 0 && entity.isOnFire()) {
			MobEffectInstance fireResistance = new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, true, false);
			if (entity.canBeAffected(fireResistance) && !entity.hasEffect(MobEffects.FIRE_RESISTANCE) && BewitchmentAPI.drainMagic(player, 1, false)) {
				entity.addEffect(fireResistance);
			}
		}
	}
}
