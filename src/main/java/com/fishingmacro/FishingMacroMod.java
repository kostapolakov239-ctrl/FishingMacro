package com.fishingmacro;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import com.fishingmacro.commands.CommandManager;
import com.fishingmacro.config.Config;
import com.fishingmacro.hud.HudRenderer;
import com.fishingmacro.InventoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FishingMacroMod implements ClientModInitializer {
	public static final String MOD_ID = "fishingmacro";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Toggle states
	public static boolean fishingEnabled = false;
	public static boolean killauraEnabled = false;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing Fishing Macro...");
		
		// Load config
		Config.load();
		
		// Register commands
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> 
			CommandManager.register(dispatcher, registryAccess)
		);
		
		// Register client tick for macros
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			
			// Run Kill Aura if enabled
			if (killauraEnabled) {
				KillAura.tick(client);
			}
			
			// Auto-switch to fishing rod if fishing is enabled
			if (fishingEnabled) {
				int fishingSlot = InventoryHelper.findFishingRodSlot(client);
				if (fishingSlot != -1) {
					client.player.getInventory().selectedSlot = fishingSlot;
				}
			}
		});
		
		// Register HUD render
		HudRenderCallback.EVENT.register((context, tickCounter) -> 
			HudRenderer.render(context, tickCounter)
		);
		
		LOGGER.info("Fishing Macro loaded successfully!");
	}

	/**
	 * Toggle fishing macro on/off
	 */
	public static void toggleFishing() {
		fishingEnabled = !fishingEnabled;
		String state = fishingEnabled ? "§aON" : "§cOFF";
		MinecraftClient.getInstance().player.sendMessage(
			net.minecraft.text.Text.of("§6Fishing Macro: " + state),
			true
		);
	}

	/**
	 * Toggle kill aura on/off
	 */
	public static void toggleKillAura() {
		killauraEnabled = !killauraEnabled;
		String state = killauraEnabled ? "§aON" : "§cOFF";
		MinecraftClient.getInstance().player.sendMessage(
			net.minecraft.text.Text.of("§6Kill Aura: " + state),
			true
		);
	}
}
