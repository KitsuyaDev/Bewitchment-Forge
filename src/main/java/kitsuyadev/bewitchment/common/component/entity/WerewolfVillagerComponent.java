/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.component.entity;

import kitsuyadev.bewitchment.api.component.EntityComponent;
import kitsuyadev.bewitchment.api.BewitchmentAPI;
import kitsuyadev.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import kitsuyadev.bewitchment.common.entity.living.WerewolfEntity;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import kitsuyadev.bewitchment.common.registry.BWEntityTypes;
import kitsuyadev.bewitchment.common.registry.BWSoundEvents;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.NotNull;

public class WerewolfVillagerComponent implements EntityComponent {
	private final Villager obj;
	private CompoundTag storedWerewolf = null;
	private int despawnTimer = 2400;

	public WerewolfVillagerComponent(Villager obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		if (tag.contains("StoredWerewolf")) {
			storedWerewolf = tag.getCompound("StoredWerewolf");
		}
		if (tag.contains("DespawnTimer")) {
			despawnTimer = tag.getInt("DespawnTimer");
		}
	}

	@Override
	public void writeToNbt(@NotNull CompoundTag tag) {
		if (storedWerewolf != null) {
			tag.put("StoredWerewolf", storedWerewolf);
		}
		tag.putInt("DespawnTimer", despawnTimer);
	}

	@Override
	public void serverTick() {
		if (getStoredWerewolf() != null) {
			if (!obj.hasCustomName() && getDespawnTimer() > 0) {
				setDespawnTimer(getDespawnTimer() - 1);
				if (getDespawnTimer() == 0) {
					obj.remove(Entity.RemovalReason.DISCARDED);
				}
			}
			if (obj.tickCount % 20 == 0 && obj.level().isNight() && BewitchmentAPI.getMoonPhase(obj.level()) == 0 && (obj.hasCustomName() || obj.level().canSeeSky(obj.blockPosition()))) {
				WerewolfEntity entity = BWEntityTypes.WEREWOLF.get().create(obj.level());
				if (entity != null) {
					SpawnSmokeParticlesPacket.send(PacketDistributor.TRACKING_ENTITY.with(() -> obj), obj);
					obj.level().playSound(null, obj.getX(), obj.getY(), obj.getZ(), BWSoundEvents.ENTITY_GENERIC_TRANSFORM.get(), obj.getSoundSource(), 1, obj.getVoicePitch());
					entity.load(getStoredWerewolf());
					entity.absMoveTo(obj.getX(), obj.getY(), obj.getZ(), obj.getRandom().nextFloat() * 360, 0);
					entity.setHealth(entity.getMaxHealth() * (obj.getHealth() / obj.getMaxHealth()));
					entity.setRemainingFireTicks(obj.getRemainingFireTicks());
					entity.removeAllEffects();
					obj.getActiveEffects().forEach(entity::addEffect);
					BWComponents.CURSES_COMPONENT.maybeGet(entity).ifPresent(entityCursesComponent -> BWComponents.CURSES_COMPONENT.maybeGet(obj).ifPresent(thisCursesComponent -> {
						entityCursesComponent.getCurses().clear();
						thisCursesComponent.getCurses().forEach(entityCursesComponent::addCurse);
					}));
					if (!obj.hasCustomName() && getDespawnTimer() >= 0) {
						setDespawnTimer(2400);
					}
					entity.storedVillager = obj.saveWithoutId(new CompoundTag());
					obj.level().addFreshEntity(entity);
					obj.remove(Entity.RemovalReason.DISCARDED);
				}
			}
		}
	}

	public CompoundTag getStoredWerewolf() {
		return storedWerewolf;
	}

	public void setStoredWerewolf(CompoundTag storedWerewolf) {
		this.storedWerewolf = storedWerewolf;
	}

	public int getDespawnTimer() {
		return despawnTimer;
	}

	public void setDespawnTimer(int despawnTimer) {
		this.despawnTimer = despawnTimer;
	}
}
