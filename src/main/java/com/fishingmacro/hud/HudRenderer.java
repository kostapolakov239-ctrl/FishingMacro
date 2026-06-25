package com.fishingmacro.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import com.fishingmacro.FishingMacroMod;
import com.fishingmacro.config.Config;
import com.fishingmacro.KillAura;

public class HudRenderer {
    
    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        Config.Settings settings = Config.getSettings();
        
        int x = settings.hudX;
        int y = settings.hudY;
        
        // HUD Background
        context.fill(x - 2, y - 2, x + 120, y + 65, 0x80000000);
        context.drawBorder(x - 2, y - 2, x + 120, y + 65, 0xFF00FF00);
        
        // Fishing Macro Status
        String fishingStatus = FishingMacroMod.fishingEnabled ? "§aON" : "§cOFF";
        context.drawTextWithBackground(
            null,
            Text.of("§6Fishing: " + fishingStatus),
            x + 5,
            y + 5,
            0xFFFFFF,
            0x00000000
        );
        
        // Kill Aura Status
        String killauraStatus = FishingMacroMod.killauraEnabled ? "§aON" : "§cOFF";
        context.drawTextWithBackground(
            null,
            Text.of("§6Kill Aura: " + killauraStatus),
            x + 5,
            y + 18,
            0xFFFFFF,
            0x00000000
        );
        
        // Current Target (if Kill Aura is on)
        if (FishingMacroMod.killauraEnabled && KillAura.getCurrentTarget() != null) {
            context.drawTextWithBackground(
                null,
                Text.of("§e✓ Target Found"),
                x + 5,
                y + 31,
                0xFFFFFF,
                0x00000000
            );
        } else if (FishingMacroMod.killauraEnabled) {
            context.drawTextWithBackground(
                null,
                Text.of("§7No target"),
                x + 5,
                y + 31,
                0xFFFFFF,
                0x00000000
            );
        }
        
        // Help text
        context.drawTextWithBackground(
            null,
            Text.of("§7Press F - Toggle Fishing"),
            x + 5,
            y + 48,
            0xAAAAAA,
            0x00000000
        );
    }
}
