/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.registry;

import kitsuyadev.bewitchment.api.component.*;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.component.entity.*;
import kitsuyadev.bewitchment.common.component.EntityComponentData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.LargeFireball;

import java.util.Optional;

public class BWComponents {
	public static final Key<ContractsComponent> CONTRACTS_COMPONENT = new Key<>(Bewitchment.id("contracts"), ContractsComponent.class, false, CopyStrategy.ALWAYS);
	public static final Key<FortuneComponent> FORTUNE_COMPONENT = new Key<>(Bewitchment.id("fortune"), FortuneComponent.class, false, CopyStrategy.ALWAYS);
	public static final Key<MagicComponent> MAGIC_COMPONENT = new Key<>(Bewitchment.id("magic"), MagicComponent.class, true, CopyStrategy.RETURN_ONLY);
	public static final Key<PledgeComponent> PLEDGE_COMPONENT = new Key<>(Bewitchment.id("pledge"), PledgeComponent.class, true, CopyStrategy.ALWAYS);
	public static final Key<TransformationComponent> TRANSFORMATION_COMPONENT = new Key<>(Bewitchment.id("transformation"), TransformationComponent.class, true, CopyStrategy.ALWAYS);
	public static final Key<BloodComponent> BLOOD_COMPONENT = new Key<>(Bewitchment.id("blood"), BloodComponent.class, true, CopyStrategy.RETURN_ONLY);
	public static final Key<CursesComponent> CURSES_COMPONENT = new Key<>(Bewitchment.id("curses"), CursesComponent.class, false, CopyStrategy.ALWAYS);

	public static final Key<AdditionalWerewolfDataComponent> ADDITIONAL_WEREWOLF_DATA_COMPONENT = new Key<>(Bewitchment.id("additional_werewolf_data"), AdditionalWerewolfDataComponent.class, true, CopyStrategy.ALWAYS);
	public static final Key<RespawnTimerComponent> RESPAWN_TIMER_COMPONENT = new Key<>(Bewitchment.id("respawn_timer"), RespawnTimerComponent.class, false, CopyStrategy.RETURN_ONLY);
	public static final Key<TeleportTimerComponent> TELEPORT_TIMER_COMPONENT = new Key<>(Bewitchment.id("teleport_timer"), TeleportTimerComponent.class, false, CopyStrategy.RETURN_ONLY);
	public static final Key<FullInvisibilityComponent> FULL_INVISIBILITY_COMPONENT = new Key<>(Bewitchment.id("full_invisibility"), FullInvisibilityComponent.class, true, CopyStrategy.RETURN_ONLY);
	public static final Key<BroomUserComponent> BROOM_USER_COMPONENT = new Key<>(Bewitchment.id("broom_user"), BroomUserComponent.class, true, CopyStrategy.NEVER);
	public static final Key<PolymorphComponent> POLYMORPH_COMPONENT = new Key<>(Bewitchment.id("polymorph"), PolymorphComponent.class, true, CopyStrategy.RETURN_ONLY);
	public static final Key<AdditionalWaterDataComponent> ADDITIONAL_WATER_DATA_COMPONENT = new Key<>(Bewitchment.id("additional_water_data"), AdditionalWaterDataComponent.class, true, CopyStrategy.RETURN_ONLY);
	public static final Key<FamiliarComponent> FAMILIAR_COMPONENT = new Key<>(Bewitchment.id("familiar"), FamiliarComponent.class, true, CopyStrategy.ALWAYS);
	public static final Key<MinionComponent> MINION_COMPONENT = new Key<>(Bewitchment.id("minion"), MinionComponent.class, false, CopyStrategy.RETURN_ONLY);
	public static final Key<FakeMobComponent> FAKE_MOB_COMPONENT = new Key<>(Bewitchment.id("fake_mob"), FakeMobComponent.class, true, CopyStrategy.RETURN_ONLY);
	public static final Key<WerewolfVillagerComponent> WEREWOLF_VILLAGER_COMPONENT = new Key<>(Bewitchment.id("werewolf_villager"), WerewolfVillagerComponent.class, false, CopyStrategy.RETURN_ONLY);
	public static final Key<CaduceusFireballComponent> CADUCEUS_FIREBALL_COMPONENT = new Key<>(Bewitchment.id("caduceus_fireball"), CaduceusFireballComponent.class, false, CopyStrategy.RETURN_ONLY);

	public static void attach(Entity entity, EntityComponentData data) {
		data.add(ADDITIONAL_WATER_DATA_COMPONENT, new AdditionalWaterDataComponent(entity));

		if (entity instanceof LivingEntity living) {
			data.add(BLOOD_COMPONENT, new BloodComponent(living));
			data.add(CURSES_COMPONENT, new CursesComponent(living));
			data.add(FAMILIAR_COMPONENT, new FamiliarComponent(living));
		}
		if (entity instanceof Player player) {
			data.add(CONTRACTS_COMPONENT, new ContractsComponent(player));
			data.add(FORTUNE_COMPONENT, new FortuneComponent(player));
			data.add(MAGIC_COMPONENT, new MagicComponent(player));
			data.add(PLEDGE_COMPONENT, new PledgeComponent(player));
			data.add(TRANSFORMATION_COMPONENT, new TransformationComponent(player));
			data.add(ADDITIONAL_WEREWOLF_DATA_COMPONENT, new AdditionalWerewolfDataComponent(player));
			data.add(BROOM_USER_COMPONENT, new BroomUserComponent(player));
			data.add(FULL_INVISIBILITY_COMPONENT, new FullInvisibilityComponent(player));
			data.add(RESPAWN_TIMER_COMPONENT, new RespawnTimerComponent());
			data.add(TELEPORT_TIMER_COMPONENT, new TeleportTimerComponent());
		}
		if (entity instanceof Player || entity instanceof Arrow || entity instanceof AreaEffectCloud) {
			data.add(POLYMORPH_COMPONENT, new PolymorphComponent(entity));
		}
		if (entity instanceof Mob mob) {
			data.add(MINION_COMPONENT, new MinionComponent(mob));
			data.add(FAKE_MOB_COMPONENT, new FakeMobComponent(mob));
		}
		if (entity instanceof Villager villager) {
			data.add(WEREWOLF_VILLAGER_COMPONENT, new WerewolfVillagerComponent(villager));
		}
		if (entity instanceof LargeFireball) {
			data.add(CADUCEUS_FIREBALL_COMPONENT, new CaduceusFireballComponent());
		}
	}

	public record Key<T extends EntityComponent>(ResourceLocation id, Class<T> componentClass, boolean synced,
												 CopyStrategy copyStrategy) {
		public T get(Entity entity) {
			return maybeGet(entity).orElseThrow(() ->
					new IllegalStateException("Missing Bewitchment component " + id + " on " + entity.getType()));
		}

		public Optional<T> maybeGet(Entity entity) {
			return EntityComponentData.get(entity).resolve().flatMap(data -> data.find(this));
		}

		public void sync(Entity entity) {
			if (!entity.level().isClientSide && synced) {
				EntityComponentData.get(entity).ifPresent(data -> data.sync(entity, this));
			}
		}

		public boolean copyOnDeath() {
			return copyStrategy == CopyStrategy.ALWAYS;
		}

		public boolean copyOnReturn() {
			return copyStrategy != CopyStrategy.NEVER;
		}
	}

	public enum CopyStrategy {
		ALWAYS, RETURN_ONLY, NEVER
	}
}
