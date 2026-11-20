/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.api.registry.*;
import kitsuyadev.bewitchment.common.Bewitchment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class BWRegistries {
	static final DeferredRegister<RitualFunction> RITUAL_FUNCTION_ENTRIES = DeferredRegister.create(Bewitchment.id("ritual_function"), Bewitchment.MOD_ID);
	public static final Supplier<IForgeRegistry<RitualFunction>> RITUAL_FUNCTION = RITUAL_FUNCTION_ENTRIES.makeRegistry(RegistryBuilder::new);

	static final DeferredRegister<Sigil> SIGIL_ENTRIES = DeferredRegister.create(Bewitchment.id("sigil"), Bewitchment.MOD_ID);
	public static final Supplier<IForgeRegistry<Sigil>> SIGIL = SIGIL_ENTRIES.makeRegistry(RegistryBuilder::new);

	static final DeferredRegister<Fortune> FORTUNE_ENTRIES = DeferredRegister.create(Bewitchment.id("fortune"), Bewitchment.MOD_ID);
	public static final Supplier<IForgeRegistry<Fortune>> FORTUNE = FORTUNE_ENTRIES.makeRegistry(RegistryBuilder::new);

	static final DeferredRegister<Transformation> TRANSFORMATION_ENTRIES = DeferredRegister.create(Bewitchment.id("transformation"), Bewitchment.MOD_ID);
	public static final Supplier<IForgeRegistry<Transformation>> TRANSFORMATION = TRANSFORMATION_ENTRIES.makeRegistry(RegistryBuilder::new);

	static final DeferredRegister<Contract> CONTRACT_ENTRIES = DeferredRegister.create(Bewitchment.id("contract"), Bewitchment.MOD_ID);
	public static final Supplier<IForgeRegistry<Contract>> CONTRACT = CONTRACT_ENTRIES.makeRegistry(RegistryBuilder::new);

	static final DeferredRegister<Curse> CURSE_ENTRIES = DeferredRegister.create(Bewitchment.id("curse"), Bewitchment.MOD_ID);
	public static final Supplier<IForgeRegistry<Curse>> CURSE = CURSE_ENTRIES.makeRegistry(RegistryBuilder::new);

	public static void register(IEventBus modBus) {
		BWRitualFunctions.register();
		RITUAL_FUNCTION_ENTRIES.register(modBus);
		BWSigils.register();
		SIGIL_ENTRIES.register(modBus);
		BWFortunes.register();
		FORTUNE_ENTRIES.register(modBus);
		BWTransformations.register();
		TRANSFORMATION_ENTRIES.register(modBus);
		BWContracts.register();
		CONTRACT_ENTRIES.register(modBus);
		BWCurses.register();
		CURSE_ENTRIES.register(modBus);
	}
}
