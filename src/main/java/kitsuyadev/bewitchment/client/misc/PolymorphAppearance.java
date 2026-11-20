/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.misc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import kitsuyadev.bewitchment.common.Bewitchment;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class PolymorphAppearance {
	private static final Logger LOGGER = LoggerFactory.getLogger(Bewitchment.MOD_ID);
	private static final Cache<UUID, PolymorphAppearance> APPEARANCES = CacheBuilder.newBuilder()
			.maximumSize(128).expireAfterAccess(10, TimeUnit.MINUTES).build();

	private ResourceLocation skin;
	private ResourceLocation cape;
	private ResourceLocation elytra;
	private String model;

	private PolymorphAppearance(GameProfile profile) {
		skin = DefaultPlayerSkin.getDefaultSkin(profile.getId());
		model = DefaultPlayerSkin.getSkinModelName(profile.getId());
		Minecraft client = Minecraft.getInstance();
		PlayerInfo online = client.getConnection() == null ? null : client.getConnection().getPlayerInfo(profile.getId());
		if (online != null) {
			readTextures(client, online.getProfile());
		} else {
			Util.backgroundExecutor().execute(() -> {
				try {
					GameProfile resolved = client.getMinecraftSessionService().fillProfileProperties(profile, false);
					client.execute(() -> readTextures(client, resolved));
				} catch (RuntimeException exception) {
					LOGGER.debug("Could not resolve polymorph profile {}", profile.getId(), exception);
				}
			});
		}
	}

	public static PolymorphAppearance get(GameProfile profile) {
		PolymorphAppearance appearance = APPEARANCES.getIfPresent(profile.getId());
		if (appearance == null) {
			appearance = new PolymorphAppearance(profile);
			APPEARANCES.put(profile.getId(), appearance);
		}
		return appearance;
	}

	private void readTextures(Minecraft client, GameProfile profile) {
		client.getSkinManager().registerSkins(profile, (type, location, metadata) -> {
			if (type == MinecraftProfileTexture.Type.SKIN) {
				skin = location;
				model = "slim".equals(metadata.getMetadata("model")) ? "slim" : "default";
			} else if (type == MinecraftProfileTexture.Type.CAPE) {
				cape = location;
			} else if (type == MinecraftProfileTexture.Type.ELYTRA) {
				elytra = location;
			}
		}, false);
	}

	public ResourceLocation skin() {
		return skin;
	}

	public ResourceLocation cape() {
		return cape;
	}

	public ResourceLocation elytra() {
		return elytra;
	}

	public String model() {
		return model;
	}
}
