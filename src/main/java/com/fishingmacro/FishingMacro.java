package com.fishingmacro;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;

public class FishingMacro {
	private static long lastClickTime = 0;
	private static final long CLICK_DELAY = 50; // ms between clicks

	public static void tick(MinecraftClient client) {
		if (client.player == null || client.screen instanceof InventoryScreen) {
			return;
		}

		// Check if we need to click (detect "тяни" or "Bite!" text)
		if (shouldClick(client)) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastClickTime >= CLICK_DELAY) {
				rightClick(client);
				lastClickTime = currentTime;
			}
		}
	}

	private static boolean shouldClick(MinecraftClient client) {
		// This checks if fishing bobber is moving (fishing line is active)
		// In real scenario, you might need to detect text or use other methods
		
		if (client.player.getMainHandStack().isEmpty()) {
			return false;
		}

		// Check for fishing rod in hand
		String itemName = client.player.getMainHandStack().getName().getString().toLowerCase();
		if (!itemName.contains("rod") && !itemName.contains("удочка")) {
			return false;
		}

		// Simple bobber detection - you can improve this
		return true;
	}

	private static void rightClick(MinecraftClient client) {
		// Send right-click interaction
		if (client.interactionManager != null && client.player != null) {
			client.interactionManager.interactItem(client.player, client.world, net.minecraft.util.Hand.MAIN_HAND);
		}
	}
}
