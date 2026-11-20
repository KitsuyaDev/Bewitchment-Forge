/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.api.entity.BroomEntity;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.entity.DragonsBloodBroomEntity;
import kitsuyadev.bewitchment.common.entity.ElderBroomEntity;
import kitsuyadev.bewitchment.common.entity.JuniperBroomEntity;
import kitsuyadev.bewitchment.common.entity.living.*;
import kitsuyadev.bewitchment.common.entity.projectile.HornedSpearEntity;
import kitsuyadev.bewitchment.common.entity.projectile.SilverArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BWEntityTypes {
	private static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Bewitchment.MOD_ID);

	public static final RegistryObject<EntityType<JuniperBroomEntity>> JUNIPER_BROOM =
			create("juniper_broom", JuniperBroomEntity::new, MobCategory.MISC, () -> EntityType.ARROW.getDimensions(), false);

	public static final RegistryObject<EntityType<BroomEntity>> CYPRESS_BROOM =
			create("cypress_broom", BroomEntity::new, MobCategory.MISC, () -> EntityType.ARROW.getDimensions(), false);

	public static final RegistryObject<EntityType<ElderBroomEntity>> ELDER_BROOM =
			create("elder_broom", ElderBroomEntity::new, MobCategory.MISC, () -> EntityType.ARROW.getDimensions(), false);

	public static final RegistryObject<EntityType<DragonsBloodBroomEntity>> DRAGONS_BLOOD_BROOM =
			create("dragons_blood_broom", DragonsBloodBroomEntity::new, MobCategory.MISC, () -> EntityType.ARROW.getDimensions(), false);

	public static final RegistryObject<EntityType<SilverArrowEntity>> SILVER_ARROW =
			create("silver_arrow", SilverArrowEntity::new, MobCategory.MISC, () -> EntityType.ARROW.getDimensions(), false);

	public static final RegistryObject<EntityType<HornedSpearEntity>> HORNED_SPEAR =
			create("horned_spear", HornedSpearEntity::new, MobCategory.MISC, () -> EntityType.TRIDENT.getDimensions(), false);

	public static final RegistryObject<EntityType<OwlEntity>> OWL =
			create("owl", OwlEntity::new, MobCategory.CREATURE, () -> EntityDimensions.fixed(0.5f, 0.75f), false);

	public static final RegistryObject<EntityType<RavenEntity>> RAVEN =
			create("raven", RavenEntity::new, MobCategory.CREATURE, () -> EntityDimensions.fixed(0.4f, 0.4f), false);

	public static final RegistryObject<EntityType<SnakeEntity>> SNAKE =
			create("snake", SnakeEntity::new, MobCategory.CREATURE, () -> EntityDimensions.fixed(0.75f, 0.25f), false);

	public static final RegistryObject<EntityType<ToadEntity>> TOAD =
			create("toad", ToadEntity::new, MobCategory.CREATURE, () -> EntityDimensions.fixed(0.5f, 0.5f), false);

	public static final RegistryObject<EntityType<GhostEntity>> GHOST =
			create("ghost", GhostEntity::new, MobCategory.MONSTER, () -> EntityDimensions.fixed(0.6f, 1.8f), true);

	public static final RegistryObject<EntityType<VampireEntity>> VAMPIRE =
			create("vampire", VampireEntity::new, MobCategory.MONSTER, () -> EntityType.PILLAGER.getDimensions(), false);

	public static final RegistryObject<EntityType<WerewolfEntity>> WEREWOLF =
			create("werewolf", WerewolfEntity::new, MobCategory.MONSTER, () -> EntityDimensions.fixed(0.8f, 2.8f), false);

	public static final RegistryObject<EntityType<HellhoundEntity>> HELLHOUND =
			create("hellhound", HellhoundEntity::new, MobCategory.MONSTER, () -> EntityType.WOLF.getDimensions(), true);

	public static final RegistryObject<EntityType<DemonEntity>> DEMON =
			create("demon", DemonEntity::new, MobCategory.MONSTER, () -> EntityDimensions.fixed(0.8f, 2.4f), true);

	public static final RegistryObject<EntityType<LeonardEntity>> LEONARD =
			create("leonard", LeonardEntity::new, MobCategory.MONSTER, () -> EntityDimensions.fixed(0.8f, 2.8f), true);

	public static final RegistryObject<EntityType<BaphometEntity>> BAPHOMET =
			create("baphomet", BaphometEntity::new, MobCategory.MONSTER, () -> EntityDimensions.fixed(0.8f, 2.8f), true);

	public static final RegistryObject<EntityType<LilithEntity>> LILITH =
			create("lilith", LilithEntity::new, MobCategory.MONSTER, () -> EntityDimensions.fixed(0.8f, 2.8f), true);

	public static final RegistryObject<EntityType<HerneEntity>> HERNE =
			create("herne", HerneEntity::new, MobCategory.MONSTER, () -> EntityDimensions.fixed(0.8f, 2.8f), true);

	private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.EntityFactory<T> factory,
																		   MobCategory category, Supplier<EntityDimensions> dimensions,
																		   boolean fireImmune) {
		return ENTITY_TYPES.register(name, () -> {
			EntityType.Builder<T> builder = EntityType.Builder.of(factory, category);
			builder.dimensions = dimensions.get();
			if (fireImmune) {
				builder.fireImmune();
			}
			return builder.build(Bewitchment.id(name).toString());
		});
	}

	public static void register(IEventBus modBus) {
		ENTITY_TYPES.register(modBus);
		modBus.addListener(BWEntityTypes::registerAttributes);
	}

	private static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(OWL.get(), OwlEntity.createAttributes().build());
		event.put(RAVEN.get(), RavenEntity.createAttributes().build());
		event.put(SNAKE.get(), SnakeEntity.createAttributes().build());
		event.put(TOAD.get(), ToadEntity.createAttributes().build());
		event.put(GHOST.get(), GhostEntity.createAttributes().build());
		event.put(VAMPIRE.get(), VampireEntity.createAttributes().build());
		event.put(WEREWOLF.get(), WerewolfEntity.createAttributes().build());
		event.put(HELLHOUND.get(), HellhoundEntity.createAttributes().build());
		event.put(DEMON.get(), DemonEntity.createAttributes().build());
		event.put(LEONARD.get(), LeonardEntity.createAttributes().build());
		event.put(BAPHOMET.get(), BaphometEntity.createAttributes().build());
		event.put(LILITH.get(), LilithEntity.createAttributes().build());
		event.put(HERNE.get(), HerneEntity.createAttributes().build());
	}
}
