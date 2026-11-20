package kitsuyadev.bewitchment.validation;

import com.mojang.logging.LogUtils;
import kitsuyadev.bewitchment.ForgePortGameTests;
import kitsuyadev.bewitchment.common.registry.BWObjects;
import kitsuyadev.bewitchment.common.registry.BWRecipeTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.GameTestRegistry;
import net.minecraft.gametest.framework.GameTestRunner;
import net.minecraft.gametest.framework.GameTestTicker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Mod(ValidationMod.MOD_ID)
@Mod.EventBusSubscriber(modid = ValidationMod.MOD_ID)
public final class ValidationMod {
	public static final String MOD_ID = "bewitchment_validation";
	static final Logger LOGGER = LogUtils.getLogger();
	private static int occupiedTicks;
	private static final GameTestTicker TEST_TICKER = new GameTestTicker();
	private static Collection<GameTestInfo> tests = List.of();
	private static boolean reportedTests;
	private static int ticksAfterTests;
	static volatile boolean requestedRespawn;
	static volatile boolean worldChecksFinished;
	static volatile boolean bookOpened;

	@SubscribeEvent
	public static void inspectWorld(TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.END || System.getProperty("bewitchment.validation.world") == null) {
			return;
		}
		MinecraftServer server = event.getServer();
		if (!server.getWorldData().getLevelName().equals(System.getProperty("bewitchment.validation.world"))
				|| server.getPlayerList().getPlayerCount() == 0) {
			return;
		}
		if (!tests.isEmpty() && !reportedTests) {
			TEST_TICKER.tick();
			if (tests.stream().allMatch(GameTestInfo::isDone)) {
				reportedTests = true;
				tests.forEach(test -> LOGGER.info("BW_VALIDATION GameTest {} {} {}", test.getTestName(),
						test.hasSucceeded() ? "PASS" : "FAIL", test.getError()));
				ServerPlayer player = server.getPlayerList().getPlayers().get(0);
				player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(BWObjects.BOOK_OF_SHADOWS));
				try {
					LOGGER.info("BW_VALIDATION guidebook use result={}",
							BWObjects.BOOK_OF_SHADOWS.use(player.level(), player, InteractionHand.MAIN_HAND).getResult());
				} catch (RuntimeException error) {
					LOGGER.error("BW_VALIDATION guidebook use failed", error);
				}
			}
		}
		if (reportedTests && !worldChecksFinished) {
			ticksAfterTests++;
			if (!requestedRespawn && (bookOpened || ticksAfterTests >= 100)) {
				requestedRespawn = true;
				server.getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).set(true, server);
				server.getGameRules().getRule(GameRules.RULE_DO_IMMEDIATE_RESPAWN).set(true, server);
				ServerPlayer player = server.getPlayerList().getPlayers().get(0);
				player.setGameMode(GameType.SURVIVAL);
				player.setHealth(1);
				boolean accepted = player.hurt(player.damageSources().fall(), 20);
				LOGGER.info("BW_VALIDATION survival fall damage accepted={} alive={} health={}",
						accepted, player.isAlive(), player.getHealth());
			}
			if (ticksAfterTests == 300) {
				LOGGER.error("BW_VALIDATION timed out waiting for the connected player to respawn");
				worldChecksFinished = true;
			}
		}
		if (++occupiedTicks != 200) {
			return;
		}
		ServerLevel level = server.overworld();
		ChunkPos center = new ChunkPos(level.getSharedSpawnPos());
		Map<String, Integer> generated = new TreeMap<>();
		int chunks = 0;
		for (int x = center.x - 10; x <= center.x + 10; x++) {
			for (int z = center.z - 10; z <= center.z + 10; z++) {
				LevelChunk chunk = level.getChunkSource().getChunkNow(x, z);
				if (chunk == null) {
					continue;
				}
				chunks++;
				for (var section : chunk.getSections()) {
					section.getStates().count((state, count) -> {
						var id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
						if (id.getNamespace().equals("bewitchment")) {
							generated.merge(id.getPath(), count, Integer::sum);
						}
					});
				}
			}
		}
		LOGGER.info("BW_VALIDATION world={} seed={} chunks={} naturalBlocks={} mspt={}",
				server.getWorldData().getLevelName(), level.getSeed(), chunks, generated, server.getAverageTickTime());
		var recipes = level.getRecipeManager();
		LOGGER.info("BW_VALIDATION recipes rituals={} cauldronBrews={} selectedPackCount={}",
				recipes.getAllRecipesFor(BWRecipeTypes.RITUAL_RECIPE_TYPE.get()).size(),
				recipes.getAllRecipesFor(BWRecipeTypes.CAULDRON_BREWING_RECIPE_TYPE.get()).size(),
				server.getPackRepository().getSelectedIds().size());
		server.getPlayerList().getPlayers().forEach(ValidationPlayers::seed);
		GameTestRegistry.register(ForgePortGameTests.class);
		var spawn = level.getSharedSpawnPos().offset(20, 0, 20);
		var fixture = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, spawn);
		// The integrated scenario below exercises the same clone rules using a real connection.
		var inPackTests = GameTestRegistry.getAllTestFunctions().stream()
				.filter(test -> !test.getTestName().equals("playerclonefollowscomponentcopyrules")).toList();
		tests = GameTestRunner.runTests(inPackTests, fixture, Rotation.NONE, level, TEST_TICKER, 3);
		LOGGER.info("BW_VALIDATION started {} in-pack GameTests", tests.size());
	}

	@SubscribeEvent
	public static void afterRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (requestedRespawn && !worldChecksFinished && event.getEntity() instanceof ServerPlayer player) {
			LOGGER.info("BW_VALIDATION connected player respawned");
			ValidationPlayers.logServer(player);
			player.getServer().saveEverything(false, true, false);
			worldChecksFinished = true;
		}
	}
}
