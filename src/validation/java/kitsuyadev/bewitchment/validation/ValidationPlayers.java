package kitsuyadev.bewitchment.validation;

import kitsuyadev.bewitchment.api.registry.Curse;
import kitsuyadev.bewitchment.common.registry.BWComponents;
import kitsuyadev.bewitchment.common.registry.BWCurses;
import kitsuyadev.bewitchment.common.registry.BWObjects;
import kitsuyadev.bewitchment.common.registry.BWRegistries;
import kitsuyadev.bewitchment.common.registry.BWTransformations;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

@Mod.EventBusSubscriber(modid = ValidationMod.MOD_ID)
public final class ValidationPlayers {
	@SubscribeEvent
	public static void register(RegisterCommandsEvent event) {
		if (!Boolean.getBoolean("bewitchment.validation.players")) {
			return;
		}
		event.getDispatcher().register(Commands.literal("bwqa")
				.requires(source -> source.hasPermission(2))
				.then(Commands.argument("player", EntityArgument.player())
						.executes(context -> {
							logServer(EntityArgument.getPlayer(context, "player"));
							return 1;
						})
						.then(Commands.literal("seed").executes(context -> {
							seed(EntityArgument.getPlayer(context, "player"));
							return 1;
						}))));
	}

	static void seed(ServerPlayer player) {
		if (!player.getGameProfile().getName().matches("BewitchmentQA[0-9]*")) {
			throw new IllegalArgumentException("Expected an isolated validation player");
		}
		ValidationMod.LOGGER.info("BW_VALIDATION before scenario");
		logServer(player);
		BWComponents.TRANSFORMATION_COMPONENT.get(player).setTransformation(BWTransformations.VAMPIRE);
		BWComponents.MAGIC_COMPONENT.get(player).setMagic(37);
		BWComponents.CURSES_COMPONENT.get(player).addCurse(new Curse.Instance(BWCurses.OUTRAGE, 24000));
		ItemStack nazar = new ItemStack(BWObjects.NAZAR);
		boolean valid = CuriosApi.isStackValid(new SlotContext("necklace", player, 0, false, true), nazar);
		CuriosApi.getCuriosInventory(player).resolve().filter(inventory -> valid).ifPresent(inventory ->
				inventory.setEquippedCurio("necklace", 0, nazar));
		ValidationMod.LOGGER.info("BW_VALIDATION Nazar slot validity {}", valid);
		logServer(player);
	}

	static void logServer(ServerPlayer player) {
		ValidationMod.LOGGER.info("BW_VALIDATION server {} curse={}", describe(player),
				BWComponents.CURSES_COMPONENT.get(player).hasCurse(BWCurses.OUTRAGE));
	}

	static String describe(Player player) {
		var transformation = BWComponents.TRANSFORMATION_COMPONENT.get(player).getTransformation();
		return player.getGameProfile().getName()
				+ " transformation=" + BWRegistries.TRANSFORMATION.get().getKey(transformation)
				+ " magic=" + BWComponents.MAGIC_COMPONENT.get(player).getMagic()
				+ " nazar=" + CuriosApi.getCuriosInventory(player).resolve().map(items -> items.isEquipped(BWObjects.NAZAR)).orElse(false);
	}
}
