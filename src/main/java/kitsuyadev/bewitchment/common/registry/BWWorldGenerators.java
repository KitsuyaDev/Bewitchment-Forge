/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.common.BWConfig;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.entity.living.GhostEntity;
import kitsuyadev.bewitchment.common.entity.living.HellhoundEntity;
import kitsuyadev.bewitchment.common.entity.living.VampireEntity;
import kitsuyadev.bewitchment.common.entity.living.WerewolfEntity;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import kitsuyadev.bewitchment.common.world.AddReferencedLoot;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BWWorldGenerators {
	public static final ResourceKey<ConfiguredFeature<?, ?>> JUNIPER = FeatureUtils.createKey(Bewitchment.MOD_ID + ":juniper");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CYPRESS = FeatureUtils.createKey(Bewitchment.MOD_ID + ":cypress");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ELDER = FeatureUtils.createKey(Bewitchment.MOD_ID + ":elder");
	public static final ResourceKey<ConfiguredFeature<?, ?>> DRAGONS_BLOOD = FeatureUtils.createKey(Bewitchment.MOD_ID + ":dragons_blood");

	private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS =
			DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Bewitchment.MOD_ID);
	private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
			DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Bewitchment.MOD_ID);
	private static final RegistryObject<Codec<WorldgenModifier>> WORLDGEN =
			BIOME_MODIFIERS.register("worldgen", () -> RecordCodecBuilder.create(instance -> instance.group(
					PlacedFeature.CODEC.fieldOf("juniper").forGetter(WorldgenModifier::juniper),
					PlacedFeature.CODEC.fieldOf("cypress").forGetter(WorldgenModifier::cypress),
					PlacedFeature.CODEC.fieldOf("elder").forGetter(WorldgenModifier::elder),
					PlacedFeature.LIST_CODEC.fieldOf("salt").forGetter(WorldgenModifier::salt),
					PlacedFeature.LIST_CODEC.fieldOf("silver").forGetter(WorldgenModifier::silver)
			).apply(instance, WorldgenModifier::new)));
	public static final RegistryObject<Codec<AddReferencedLoot>> REFERENCED_LOOT =
			LOOT_MODIFIERS.register("add_referenced_loot", () -> AddReferencedLoot.CODEC);

	public static void register(IEventBus modBus) {
		BIOME_MODIFIERS.register(modBus);
		LOOT_MODIFIERS.register(modBus);
		modBus.addListener(BWWorldGenerators::registerPlacements);
	}

	private static void registerPlacements(SpawnPlacementRegisterEvent event) {
		registerPlacement(event, BWEntityTypes.OWL.get(), Animal::checkAnimalSpawnRules);
		registerPlacement(event, BWEntityTypes.RAVEN.get(), Animal::checkAnimalSpawnRules);
		registerPlacement(event, BWEntityTypes.SNAKE.get(), Animal::checkAnimalSpawnRules);
		registerPlacement(event, BWEntityTypes.TOAD.get(), Animal::checkAnimalSpawnRules);
		registerPlacement(event, BWEntityTypes.GHOST.get(), GhostEntity::canSpawn);
		registerPlacement(event, BWEntityTypes.VAMPIRE.get(), VampireEntity::canSpawn);
		registerPlacement(event, BWEntityTypes.WEREWOLF.get(), WerewolfEntity::canSpawn);
		registerPlacement(event, BWEntityTypes.HELLHOUND.get(), HellhoundEntity::canSpawn);
	}

	private static <T extends net.minecraft.world.entity.Entity> void registerPlacement(SpawnPlacementRegisterEvent event,
																			EntityType<T> type, SpawnPlacements.SpawnPredicate<T> predicate) {
		event.register(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				predicate, SpawnPlacementRegisterEvent.Operation.REPLACE);
	}

	public record WorldgenModifier(Holder<PlacedFeature> juniper, Holder<PlacedFeature> cypress,
								  Holder<PlacedFeature> elder, HolderSet<PlacedFeature> salt,
								  HolderSet<PlacedFeature> silver) implements BiomeModifier {
		@Override
		public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
			if (phase != Phase.ADD) {
				return;
			}
			boolean taiga = biome.is(BiomeTags.IS_TAIGA);
			boolean forest = biome.is(BiomeTags.IS_FOREST);
			boolean plains = biome.is(Tags.Biomes.IS_PLAINS);
			boolean savanna = biome.is(BiomeTags.IS_SAVANNA);
			boolean swamp = biome.is(Tags.Biomes.IS_SWAMP);
			if (savanna) {
				builder.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, juniper);
			}
			if (taiga || swamp) {
				builder.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, cypress);
			}
			if (forest) {
				builder.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, elder);
			}
			if (biome.is(BiomeTags.IS_OVERWORLD)) {
				if (BWConfig.generateSalt) {
					salt.forEach(feature -> builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature));
				}
				if (BWConfig.generateSilver) {
					silver.forEach(feature -> builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature));
				}
				if (taiga || forest) {
					addSpawn(builder, BWEntityTypes.OWL.get(), BWConfig.owlWeight, BWConfig.owlMinGroupCount, BWConfig.owlMaxGroupCount);
				}
				if (plains || forest) {
					addSpawn(builder, BWEntityTypes.RAVEN.get(), BWConfig.ravenWeight, BWConfig.ravenMinGroupCount, BWConfig.ravenMaxGroupCount);
				}
				if (plains || savanna || biome.is(Tags.Biomes.IS_DESERT)) {
					addSpawn(builder, BWEntityTypes.SNAKE.get(), BWConfig.snakeWeight, BWConfig.snakeMinGroupCount, BWConfig.snakeMaxGroupCount);
				}
				if (biome.is(BiomeTags.IS_JUNGLE) || swamp) {
					addSpawn(builder, BWEntityTypes.TOAD.get(), BWConfig.toadWeight, BWConfig.toadMinGroupCount, BWConfig.toadMaxGroupCount);
				}
				if (!biome.value().getMobSettings().getMobs(BWEntityTypes.GHOST.get().getCategory()).isEmpty()) {
					addSpawn(builder, BWEntityTypes.GHOST.get(), BWConfig.ghostWeight, BWConfig.ghostMinGroupCount, BWConfig.ghostMaxGroupCount);
					if (taiga || plains) {
						addSpawn(builder, BWEntityTypes.VAMPIRE.get(), BWConfig.vampireWeight, BWConfig.vampireMinGroupCount, BWConfig.vampireMaxGroupCount);
					}
					if (taiga || forest) {
						addSpawn(builder, BWEntityTypes.WEREWOLF.get(), BWConfig.werewolfWeight, BWConfig.werewolfMinGroupCount, BWConfig.werewolfMaxGroupCount);
					}
				}
			}
			if (biome.is(BiomeTags.IS_NETHER)) {
				addSpawn(builder, BWEntityTypes.HELLHOUND.get(), BWConfig.hellhoundWeight, BWConfig.hellhoundMinGroupCount, BWConfig.hellhoundMaxGroupCount);
			}
		}

		@Override
		public Codec<? extends BiomeModifier> codec() {
			return WORLDGEN.get();
		}
	}

	private static void addSpawn(ModifiableBiomeInfo.BiomeInfo.Builder builder, EntityType<?> type,
								 int weight, int minGroupSize, int maxGroupSize) {
		if (weight < 0 || minGroupSize < 0 || maxGroupSize < minGroupSize) {
			throw new IllegalArgumentException("Invalid spawn settings for " + type.getDescriptionId()
					+ " (weight " + weight + ", minimum " + minGroupSize + ", maximum " + maxGroupSize + ")");
		}
		if (weight > 0 && minGroupSize > 0) {
			builder.getMobSpawnSettings().addSpawn(type.getCategory(),
					new MobSpawnSettings.SpawnerData(type, weight, minGroupSize, maxGroupSize));
		}
	}
}
