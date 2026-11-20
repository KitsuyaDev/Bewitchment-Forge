/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.common.Bewitchment;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BWParticleTypes {
	private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
			DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Bewitchment.MOD_ID);

	public static final RegistryObject<SimpleParticleType> CAULDRON_BUBBLE =
			PARTICLE_TYPES.register("cauldron_bubble", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> INCENSE_SMOKE =
			PARTICLE_TYPES.register("incense_smoke", () -> new SimpleParticleType(false));

	public static void register(IEventBus modBus) {
		PARTICLE_TYPES.register(modBus);
	}
}
