/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.ritualfunction;

import kitsuyadev.bewitchment.api.registry.RitualFunction;
import kitsuyadev.bewitchment.common.entity.living.DemonEntity;
import kitsuyadev.bewitchment.common.registry.BWEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import java.util.function.Predicate;

public class SummonDemonRitualFunction extends RitualFunction {
	public SummonDemonRitualFunction(ParticleType<?> startParticle, Predicate<LivingEntity> sacrifice) {
		super(startParticle, sacrifice);
	}

	@Override
	public void start(ServerLevel world, BlockPos glyphPos, BlockPos effectivePos, Container inventory, boolean catFamiliar) {
		DemonEntity entity = BWEntityTypes.DEMON.get().create(world);
		if (entity != null) {
			entity.finalizeSpawn(world, world.getCurrentDifficultyAt(effectivePos), MobSpawnType.EVENT, null, null);
			entity.absMoveTo(effectivePos.getX() + 0.5, effectivePos.getY(), effectivePos.getZ() + 0.5, world.random.nextFloat() * 360, 0);
			world.addFreshEntity(entity);
		}
		super.start(world, glyphPos, effectivePos, inventory, catFamiliar);
	}
}
