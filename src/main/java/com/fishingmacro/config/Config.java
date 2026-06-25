package com.fishingmacro.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger("fishingmacro");
    private static final Path CONFIG_DIR = Paths.get(System.getProperty("user.home"), ".minecraft", "config", "fishingmacro");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("config.properties");
    
    public static class Settings {
        public int hudX = 10;
        public int hudY = 10;
        public double killauraRange = 8.0;
        public double cameraRotationSpeed = 15.0;
        public int attackDelay = 200;
        public int fishingSlot = 2;
        public int swordSlot = 1;
    }
    
    private static Settings settings = new Settings();
    private static Properties properties = new Properties();
    
    static {
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            LOGGER.warn("Failed to create config directory", e);
        }
    }
    
    public static void load() {
        try {
            if (Files.exists(CONFIG_FILE)) {
                try (InputStream input = new FileInputStream(CONFIG_FILE.toFile())) {
                    properties.load(input);
                    settings.hudX = Integer.parseInt(properties.getProperty("hud.x", "10"));
                    settings.hudY = Integer.parseInt(properties.getProperty("hud.y", "10"));
                    settings.killauraRange = Double.parseDouble(properties.getProperty("killaura.range", "8.0"));
                    settings.cameraRotationSpeed = Double.parseDouble(properties.getProperty("killaura.smooth", "15.0"));
                    settings.attackDelay = Integer.parseInt(properties.getProperty("killaura.delay", "200"));
                    settings.fishingSlot = Integer.parseInt(properties.getProperty("inventory.fishing.slot", "2"));
                    settings.swordSlot = Integer.parseInt(properties.getProperty("inventory.sword.slot", "1"));
                    LOGGER.info("Config loaded successfully");
                }
            } else {
                save();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
    }
    
    public static void save() {
        try {
            properties.setProperty("hud.x", String.valueOf(settings.hudX));
            properties.setProperty("hud.y", String.valueOf(settings.hudY));
            properties.setProperty("killaura.range", String.valueOf(settings.killauraRange));
            properties.setProperty("killaura.smooth", String.valueOf(settings.cameraRotationSpeed));
            properties.setProperty("killaura.delay", String.valueOf(settings.attackDelay));
            properties.setProperty("inventory.fishing.slot", String.valueOf(settings.fishingSlot));
            properties.setProperty("inventory.sword.slot", String.valueOf(settings.swordSlot));
            
            try (OutputStream output = new FileOutputStream(CONFIG_FILE.toFile())) {
                properties.store(output, "Fishing Macro Configuration");
                LOGGER.info("Config saved successfully");
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }
    
    public static Settings getSettings() {
        return settings;
    }
    
    public static void setHudX(int value) {
        settings.hudX = value;
        save();
    }
    
    public static void setHudY(int value) {
        settings.hudY = value;
        save();
    }
    
    public static void setKillauraRange(double value) {
        settings.killauraRange = value;
        save();
    }
    
    public static void setCameraRotationSpeed(double value) {
        settings.cameraRotationSpeed = value;
        save();
    }
    
    public static void setAttackDelay(int value) {
        settings.attackDelay = value;
        save();
    }
    
    public static void setFishingSlot(int slot) {
        settings.fishingSlot = slot;
        save();
    }
    
    public static void setSwordSlot(int slot) {
        settings.swordSlot = slot;
        save();
    }
}
