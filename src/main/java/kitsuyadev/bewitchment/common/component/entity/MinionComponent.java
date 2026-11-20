/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.component.entity;

import kitsuyadev.bewitchment.api.component.EntityComponent;
import kitsuyadev.bewitchment.client.packet.SpawnSmokeParticlesPacket;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import java.util.UUID;

public class MinionComponent implements EntityComponent {
	private final Mob obj;
	private UUID master = null;

	public MinionComponent(Mob obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		setMaster(tag.getString("MasterUUID").isEmpty() ? null : UUID.fromString(tag.getString("MasterUUID")));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putString("MasterUUID", getMaster() == null ? "" : getMaster().toString());
	}

	@Override
	public void serverTick() {
		if (getMaster() != null) {
			Entity master = ((ServerLevel) obj.level()).getEntity(getMaster());
			if (master instanceof Mob mob && !mob.isDeadOrDying() && mob.getTarget() != null) {
				obj.setTarget(mob.getTarget());
			} else {
				SpawnSmokeParticlesPacket.send(PacketDistributor.TRACKING_ENTITY.with(() -> obj), obj);
				obj.remove(Entity.RemovalReason.DISCARDED);
			}
		}
	}

	public UUID getMaster() {
		return master;
	}

	public void setMaster(UUID master) {
		this.master = master;
		BWComponents.MINION_COMPONENT.sync(obj);
	}
}
