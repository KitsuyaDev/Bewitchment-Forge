package kitsuyadev.bewitchment.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = ValidationMod.MOD_ID, value = Dist.CLIENT)
public final class ValidationClient {
	private static boolean requested;
	private static int finishedTicks;
	private static boolean sentRespawn;
	private static List<String> previousPlayers = List.of();

	@SubscribeEvent
	public static void openDisposableWorld(TickEvent.ClientTickEvent event) {
		String world = System.getProperty("bewitchment.validation.world");
		Minecraft client = Minecraft.getInstance();
		if (event.phase != TickEvent.Phase.END || client.getOverlay() != null) {
			return;
		}
		if ((world != null || Boolean.getBoolean("bewitchment.validation.players")) && client.level != null && client.player != null) {
			List<String> players = client.level.players().stream()
					.filter(player -> player.getGameProfile().getName().matches("BewitchmentQA[0-9]*"))
					.map(ValidationPlayers::describe).sorted().toList();
			if (!players.equals(previousPlayers)) {
				previousPlayers = players;
				ValidationMod.LOGGER.info("BW_VALIDATION client {} observes {}", client.player.getGameProfile().getName(), players);
			}
		}
		if (world == null) {
			return;
		}
		if (ValidationMod.worldChecksFinished && ++finishedTicks == 100) {
			ValidationMod.LOGGER.info("BW_VALIDATION shutting down the isolated client");
			client.stop();
			return;
		}
		if (client.player != null && client.gameMode != null) {
			if (ValidationMod.requestedRespawn && !sentRespawn && client.player.isDeadOrDying()) {
				sentRespawn = true;
				client.player.respawn();
			}
			if (!ValidationMod.bookOpened && client.screen != null
					&& client.screen.getClass().getName().startsWith("vazkii.patchouli.client.book.gui.")) {
				ValidationMod.bookOpened = true;
				ValidationMod.LOGGER.info("BW_VALIDATION guidebook screen={}", client.screen.getClass().getSimpleName());
				client.setScreen(null);
			}
		}
		if (requested || !(client.screen instanceof TitleScreen)) {
			return;
		}
		if (!world.matches("BewitchmentQA-[a-zA-Z0-9-]+")) {
			throw new IllegalArgumentException("Expected a disposable BewitchmentQA world name");
		}
		requested = true;
		if (client.getLevelSource().levelExists(world)) {
			ValidationMod.LOGGER.info("BW_VALIDATION reopening {}", world);
			client.createWorldOpenFlows().loadLevel(client.screen, world);
		} else {
			ValidationMod.LOGGER.info("BW_VALIDATION creating {}", world);
			LevelSettings settings = new LevelSettings(world, GameType.CREATIVE, false, Difficulty.NORMAL,
					true, new GameRules(), WorldDataConfiguration.DEFAULT);
			client.createWorldOpenFlows().createFreshLevel(world, settings, new WorldOptions(20261119L, true, false),
					WorldPresets::createNormalWorldDimensions);
		}
	}
}
