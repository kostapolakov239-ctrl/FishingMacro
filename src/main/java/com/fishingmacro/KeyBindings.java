package com.fishingmacro;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    
    public static KeyBinding fishingToggle;
    public static KeyBinding killauraToggle;
    
    public static void register() {
        // Register keybindings
        fishingToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.fishingmacro.fishing_toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F,
            "category.fishingmacro.main"
        ));
        
        killauraToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.fishingmacro.killaura_toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.fishingmacro.main"
        ));
        
        // Register tick handler
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (fishingToggle.wasPressed()) {
                FishingMacroMod.toggleFishing();
            }
            
            while (killauraToggle.wasPressed()) {
                FishingMacroMod.toggleKillAura();
            }
        });
    }
}
