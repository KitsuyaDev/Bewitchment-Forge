/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.common.component;

import kitsuyadev.bewitchment.api.component.EntityComponent;
import kitsuyadev.bewitchment.common.Bewitchment;
import kitsuyadev.bewitchment.common.packet.BWNetwork;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class EntityComponentData {
	private static final ResourceLocation ID = Bewitchment.id("entity_components");
	private static final Capability<EntityComponentData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

	private final Map<BWComponents.Key<?>, EntityComponent> components = new LinkedHashMap<>();
	private boolean reading;

	private EntityComponentData(Entity entity) {
		BWComponents.attach(entity, this);
	}

	public static void register(IEventBus modBus, IEventBus forgeBus) {
		modBus.addListener((RegisterCapabilitiesEvent event) -> event.register(EntityComponentData.class));
		forgeBus.addGenericListener(Entity.class, EntityComponentData::attachCapabilities);
		forgeBus.addListener(EntityComponentData::copyPlayer);
		forgeBus.addListener(EntityComponentData::startTracking);
		forgeBus.addListener((PlayerEvent.PlayerLoggedInEvent event) -> syncPlayer(event.getEntity()));
		forgeBus.addListener((PlayerEvent.PlayerRespawnEvent event) -> syncPlayer(event.getEntity()));
		forgeBus.addListener((PlayerEvent.PlayerChangedDimensionEvent event) -> syncPlayer(event.getEntity()));
	}

	private static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Provider provider = new Provider(event.getObject());
		event.addCapability(ID, provider);
		event.addListener(provider::invalidate);
	}

	public static LazyOptional<EntityComponentData> get(Entity entity) {
		return entity.getCapability(CAPABILITY);
	}

	public static void readFabricSave(Entity entity, CompoundTag entityTag) {
		if (entityTag.contains("cardinal_components", Tag.TAG_COMPOUND)
				&& !entityTag.getCompound("ForgeCaps").contains(ID.toString(), Tag.TAG_COMPOUND)) {
			get(entity).ifPresent(data -> data.read(entityTag.getCompound("cardinal_components"), false));
		}
	}

	public <T extends EntityComponent> void add(BWComponents.Key<T> key, T component) {
		components.put(key, component);
	}

	public <T extends EntityComponent> Optional<T> find(BWComponents.Key<T> key) {
		return Optional.ofNullable(key.componentClass().cast(components.get(key)));
	}

	public static void tick(Entity entity) {
		get(entity).ifPresent(data -> {
			if (entity.level().isClientSide) {
				data.components.values().forEach(EntityComponent::clientTick);
			} else {
				data.components.values().forEach(EntityComponent::serverTick);
			}
		});
	}

	public void sync(Entity entity, BWComponents.Key<?> key) {
		if (reading || !key.synced() || !entity.isAddedToWorld() || entity.level().isClientSide) {
			return;
		}
		EntityComponent component = components.get(key);
		if (component != null) {
			CompoundTag update = new CompoundTag();
			writeComponent(update, key, component);
			BWNetwork.syncComponents(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), entity, update);
		}
	}

	private static void startTracking(PlayerEvent.StartTracking event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			syncTo(player, event.getTarget());
		}
	}

	private static void syncPlayer(Entity entity) {
		if (entity instanceof ServerPlayer player) {
			syncTo(player, player);
		}
	}

	private static void syncTo(ServerPlayer player, Entity entity) {
		get(entity).ifPresent(data -> BWNetwork.syncComponents(
				PacketDistributor.PLAYER.with(() -> player), entity, data.serialize(true)));
	}

	private static void copyPlayer(PlayerEvent.Clone event) {
		Entity original = event.getOriginal();
		boolean removed = original.isRemoved();
		if (removed) {
			original.reviveCaps();
		}
		try {
			get(original).ifPresent(from -> get(event.getEntity()).ifPresent(to ->
					from.components.forEach((key, component) -> {
						if (key.copyOnDeath() || (!event.isWasDeath() && key.copyOnReturn())) {
							CompoundTag state = new CompoundTag();
							writeComponent(state, key, component);
							to.read(state, false);
						}
					})));
		} finally {
			if (removed) {
				original.invalidateCaps();
			}
		}
	}

	private CompoundTag serialize(boolean syncedOnly) {
		CompoundTag tag = new CompoundTag();
		components.forEach((key, component) -> {
			if (!syncedOnly || key.synced()) {
				writeComponent(tag, key, component);
			}
		});
		return tag;
	}

	private static void writeComponent(CompoundTag tag, BWComponents.Key<?> key, EntityComponent component) {
		CompoundTag state = new CompoundTag();
		component.writeToNbt(state);
		tag.put(key.id().toString(), state);
	}

	public void readSync(CompoundTag tag) {
		read(tag, true);
	}

	private void read(CompoundTag tag, boolean syncedOnly) {
		reading = true;
		try {
			components.forEach((key, component) -> {
				String name = key.id().toString();
				if ((!syncedOnly || key.synced()) && tag.contains(name, Tag.TAG_COMPOUND)) {
					component.readFromNbt(tag.getCompound(name));
				}
			});
		} finally {
			reading = false;
		}
	}

	private static final class Provider implements ICapabilitySerializable<CompoundTag> {
		private final EntityComponentData data;
		private LazyOptional<EntityComponentData> optional;

		private Provider(Entity entity) {
			data = new EntityComponentData(entity);
			optional = LazyOptional.of(() -> data);
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
			return CAPABILITY.orEmpty(capability, optional);
		}

		@Override
		public CompoundTag serializeNBT() {
			return data.serialize(false);
		}

		@Override
		public void deserializeNBT(CompoundTag tag) {
			data.read(tag, false);
		}

		private void invalidate() {
			optional.invalidate();
			// A player clone can briefly revive the old entity's capabilities to copy their state.
			optional = LazyOptional.of(() -> data);
		}
	}
}
