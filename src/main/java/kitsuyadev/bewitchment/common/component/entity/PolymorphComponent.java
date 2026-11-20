/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.component.entity;

import kitsuyadev.bewitchment.api.component.EntityComponent;
import com.mojang.authlib.GameProfile;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.server.level.ServerPlayer;
import kitsuyadev.bewitchment.common.registry.BWStatusEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PolymorphComponent implements EntityComponent {
	private final Entity obj;
	private UUID uuid;
	private String name;
	private boolean active;
	private GameProfile profile;

	public PolymorphComponent(Entity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		uuid = tag.hasUUID("UUID") ? tag.getUUID("UUID") : null;
		name = uuid == null ? null : tag.getString("Name");
		active = tag.getBoolean("Active") && uuid != null;
		profile = active ? new GameProfile(uuid, name) : null;
	}

	@Override
	public void writeToNbt(@NotNull CompoundTag tag) {
		if (getUuid() != null) {
			tag.putUUID("UUID", uuid);
			tag.putString("Name", name == null ? "" : name);
			tag.putBoolean("Active", active);
		}
	}

	@Override
	public void serverTick() {
		if (!(obj instanceof ServerPlayer player) || uuid == null) {
			return;
		}
		boolean hasEffect = player.hasEffect(BWStatusEffects.POLYMORPH.get());
		if (active != hasEffect || (active && !uuid.equals(profile.getId()))) {
			active = hasEffect;
			profile = active ? new GameProfile(uuid, name) : null;
			player.refreshDisplayName();
			player.refreshTabListName();
			BWComponents.POLYMORPH_COMPONENT.sync(player);
		}
		if (!hasEffect) {
			uuid = null;
			name = null;
		}
	}

	public GameProfile getActiveProfile() {
		return profile;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
