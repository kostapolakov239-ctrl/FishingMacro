package com.fishingmacro;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FishingMacroMod implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("fishingmacro");
	public static final String MOD_ID = "fishingmacro";

	public static KeyBinding toggleFishingKey;
	public static KeyBinding toggleKillauraKey;

	public static boolean fishingEnabled = false;
	public static boolean killauraEnabled = false;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Fishing Macro initialized!");

		// Register keybindings
		toggleFishingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.fishingmacro.toggle_fishing",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_F,
			"category.fishingmacro.main"
		));

		toggleKillauraKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.fishingmacro.toggle_killaura",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_K,
			"category.fishingmacro.main"
		));

		// Register tick event for auto-clicking
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			// Toggle fishing
			if (toggleFishingKey.wasPressed()) {
				fishingEnabled = !fishingEnabled;
				String status = fishingEnabled ? "§aENABLED" : "§cDISABLED";
				client.player.sendMessage(
					net.minecraft.text.Text.of("§6Fishing Macro: " + status),
					true
				);
			}

			// Toggle killaura
			if (toggleKillauraKey.wasPressed()) {
				killauraEnabled = !killauraEnabled;
				String status = killauraEnabled ? "§aENABLED" : "§cDISABLED";
				client.player.sendMessage(
					net.minecraft.text.Text.of("§6Kill Aura: " + status),
					true
				);
			}

			// Run fishing logic
			if (fishingEnabled) {
				FishingMacro.tick(client);
			}

			// Run killaura logic
			if (killauraEnabled) {
				KillAura.tick(client);
			}
		});
	}
}
