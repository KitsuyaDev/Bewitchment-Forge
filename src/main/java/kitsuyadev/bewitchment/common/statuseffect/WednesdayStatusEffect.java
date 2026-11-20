/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.statuseffect;

import kitsuyadev.bewitchment.client.packet.SpawnExplosionParticlesPacket;
import kitsuyadev.bewitchment.common.entity.living.ToadEntity;
import kitsuyadev.bewitchment.common.registry.BWDamageSources;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class WednesdayStatusEffect extends MobEffect {
	public WednesdayStatusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		super.removeAttributeModifiers(entity, attributes, amplifier);
		entity.level().playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1, 1);
		SpawnExplosionParticlesPacket.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), entity);
		if (entity instanceof ServerPlayer player) {
			SpawnExplosionParticlesPacket.send(player, entity);
		}
		if (entity instanceof ToadEntity toad && toad.isFromWednesdayRitual) {
			entity.remove(Entity.RemovalReason.DISCARDED);
		} else {
			entity.hurt(BWDamageSources.create(entity.level(), BWDamageSources.WEDNESDAY), Float.MAX_VALUE);
		}
	}
}
