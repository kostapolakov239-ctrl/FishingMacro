package com.fishingmacro.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import com.fishingmacro.config.Config;
import com.fishingmacro.KillAura;

public class CommandManager {
	
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
		// Основная команда /fm
		dispatcher.register(ClientCommandManager.literal("fm")
			.then(ClientCommandManager.literal("hud")
				.then(ClientCommandManager.literal("x")
					.then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0, 500))
						.executes(ctx -> setHudX(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "value")))))
				.then(ClientCommandManager.literal("y")
					.then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0, 500))
						.executes(ctx -> setHudY(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "value"))))))
			.then(ClientCommandManager.literal("killaura")
				.then(ClientCommandManager.literal("range")
					.then(ClientCommandManager.argument("value", DoubleArgumentType.doubleArg(1.0, 50.0))
						.executes(ctx -> setKillauraRange(ctx.getSource(), DoubleArgumentType.getDouble(ctx, "value")))))
				.then(ClientCommandManager.literal("smooth")
					.then(ClientCommandManager.argument("value", DoubleArgumentType.doubleArg(1.0, 45.0))
						.executes(ctx -> setCameraSpeed(ctx.getSource(), DoubleArgumentType.getDouble(ctx, "value")))))
				.then(ClientCommandManager.literal("delay")
					.then(ClientCommandManager.argument("value", IntegerArgumentType.integer(50, 500))
						.executes(ctx -> setAttackDelay(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "value"))))))
			.then(ClientCommandManager.literal("info")
				.executes(ctx -> showInfo(ctx.getSource())))
		);
	}
	
	private static int setHudX(FabricClientCommandSource source, int value) {
		Config.setHudX(value);
		source.sendFeedback(Text.of("§a✓ HUD X position set to " + value));
		return 1;
	}
	
	private static int setHudY(FabricClientCommandSource source, int value) {
		Config.setHudY(value);
		source.sendFeedback(Text.of("§a✓ HUD Y position set to " + value));
		return 1;
	}
	
	private static int setKillauraRange(FabricClientCommandSource source, double value) {
		Config.setKillauraRange(value);
		source.sendFeedback(Text.of("§a✓ Kill Aura range set to " + value + " blocks"));
		return 1;
	}
	
	private static int setCameraSpeed(FabricClientCommandSource source, double value) {
		Config.setCameraRotationSpeed(value);
		source.sendFeedback(Text.of("§a✓ Camera rotation speed (smoothness) set to " + value + "°/tick"));
		source.sendFeedback(Text.of("§7(Lower = smoother, Higher = faster)"));
		return 1;
	}
	
	private static int setAttackDelay(FabricClientCommandSource source, int value) {
		Config.setAttackDelay(value);
		source.sendFeedback(Text.of("§a✓ Attack delay set to " + value + "ms"));
		return 1;
	}
	
	private static int showInfo(FabricClientCommandSource source) {
		Config.Settings settings = Config.getSettings();
		source.sendFeedback(Text.of("§6=== Fishing Macro Config ==="));
		source.sendFeedback(Text.of("§eHUD Position: X=" + settings.hudX + " Y=" + settings.hudY));
		source.sendFeedback(Text.of("§eKill Aura Range: " + settings.killauraRange + " blocks"));
		source.sendFeedback(Text.of("§eCamera Smoothness: " + settings.cameraRotationSpeed + "°/tick"));
		source.sendFeedback(Text.of("§eAttack Delay: " + settings.attackDelay + "ms"));
		source.sendFeedback(Text.of("§7Use /fm hud x [value] to move HUD"));
		source.sendFeedback(Text.of("§7Use /fm killaura range [value] to change range"));
		source.sendFeedback(Text.of("§7Use /fm killaura smooth [value] to change smoothness"));
		return 1;
	}
}
