package com.fishingmacro;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryHelper {
	
	// Все названия мечей/оружия
	private static final String[] MELEE_WEAPONS = {
		"Undead Sword", "Spider Sword", "End Sword", "Rogue Sword", "Cleaver",
		"Zombie Sword", "Ornate Zombie Sword", "Florid Zombie Sword",
		"Aspect of the End", "Aspect of the Dragons", "Aspect of the Void", "Void Sword",
		"Livid Dagger", "Shadow Fury", "Giant's Sword", "Dark Claymore",
		"Hyperion", "Valkyrie", "Scylla", "Astraea", "Necron's Blade",
		"Wither Cloak Sword", "Flower of Truth", "Bouquet of Lies", "Felthorn Reaper",
		"Revenant Falchion", "Reaper Falchion", "Recluse Fang", "Scorpion Foil",
		"Shaman Sword", "Pooch Sword", "Voidedge Katana", "Vorpal Katana", "Atomsplit Katana",
		"Daedalus Blade", "Pigman Sword", "Leaping Sword", "Silk-Edge Sword", "Yeti Sword",
		"Frozen Scythe", "Glacial Scythe", "Golem Sword", "Silver Fang", "Emerald Blade",
		"Midas' Sword", "Hunter Knife", "Flaming Sword", "Tactician's Sword",
		"Sword of Revelations", "Sword of Bad Health", "Zombie Knight Sword",
		"Zombie Commander Sword", "Zombie Lord Sword", "Skeleton Lord Sword",
		"Skeleton Master Sword", "Silent Death", "Super Cleaver", "Hyper Cleaver",
		"Giant Cleaver", "Earth Shard", "Conjuring", "Dreadlord Sword", "Soul Whip",
		"Fel Sword", "Blade of the Volcano", "Fire Freeze Staff", "Ink Wand"
	};
	
	// Все названия удочек
	private static final String[] FISHING_RODS = {
		"Fishing Rod", "Challenging Rod", "Rod of Champions", "Rod of Legends",
		"Rod of the Sea", "Starter Lava Rod", "Magma Rod", "Inferno Rod",
		"Hellfire Rod", "Phantom Rod", "Shredder", "Auger Rod", "Yeti Rod",
		"Chum Rod", "Ice Rod", "Sponge Rod", "Speedster Rod", "Farmer's Rod",
		"Winter Rod", "Topaz Rod", "Prismarine Rod"
	};
	
	// Мечи с абилитями, которые наносят урон (можно активировать на врага)
	private static final Map<String, String> DAMAGE_ABILITIES = new HashMap<>();
	
	static {
		DAMAGE_ABILITIES.put("Aspect of the End", "Instant Transmission");
		DAMAGE_ABILITIES.put("Aspect of the Dragons", "Dragon Rage");
		DAMAGE_ABILITIES.put("Aspect of the Void", "Instant Transmission");
		DAMAGE_ABILITIES.put("Livid Dagger", "Throw");
		DAMAGE_ABILITIES.put("Shadow Fury", "Shadow Fury");
		DAMAGE_ABILITIES.put("Giant's Sword", "Giant's Slam");
		DAMAGE_ABILITIES.put("Hyperion", "Wither Impact");
		DAMAGE_ABILITIES.put("Valkyrie", "Wither Impact");
		DAMAGE_ABILITIES.put("Scylla", "Wither Impact");
		DAMAGE_ABILITIES.put("Astraea", "Wither Impact");
		DAMAGE_ABILITIES.put("Wither Cloak Sword", "Creeper Veil");
		DAMAGE_ABILITIES.put("Flower of Truth", "Heat-Seeking Rose");
		DAMAGE_ABILITIES.put("Bouquet of Lies", "Blood-Seeking Rose");
		DAMAGE_ABILITIES.put("Felthorn Reaper", "Felthorn Slash");
		DAMAGE_ABILITIES.put("Recluse Fang", "Squash");
		DAMAGE_ABILITIES.put("Scorpion Foil", "Ticker");
		DAMAGE_ABILITIES.put("Voidedge Katana", "Soulcry");
		DAMAGE_ABILITIES.put("Vorpal Katana", "Soulcry");
		DAMAGE_ABILITIES.put("Atomsplit Katana", "Soulcry");
		DAMAGE_ABILITIES.put("Pigman Sword", "Burning Souls");
		DAMAGE_ABILITIES.put("Leaping Sword", "Leap");
		DAMAGE_ABILITIES.put("Silk-Edge Sword", "Leap");
		DAMAGE_ABILITIES.put("Yeti Sword", "Terrain Toss");
		DAMAGE_ABILITIES.put("Frozen Scythe", "Ice Bolt");
		DAMAGE_ABILITIES.put("Glacial Scythe", "Ice Bolt");
		DAMAGE_ABILITIES.put("Golem Sword", "Iron Punch");
		DAMAGE_ABILITIES.put("Earth Shard", "Earth Tremor");
		DAMAGE_ABILITIES.put("Dreadlord Sword", "Dreadlord Skulls");
		DAMAGE_ABILITIES.put("Soul Whip", "Flay");
		DAMAGE_ABILITIES.put("Fire Freeze Staff", "Fire Freeze");
		DAMAGE_ABILITIES.put("Ink Wand", "Ink Bomb");
	}

	/**
	 * Ищет удочку в инвентаре и возвращает номер слота (0-8)
	 * Возвращает -1 если удочка не найдена
	 */
	public static int findFishingRodSlot(MinecraftClient client) {
		if (client.player == null) return -1;
		
		for (int i = 0; i < 9; i++) {
			ItemStack stack = client.player.getInventory().getStack(i);
			if (!stack.isEmpty() && isFishingRod(stack)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Ищет меч/оружие в инвентаре и возвращает номер слота (0-8)
	 * Возвращает -1 если меч не найден
	 */
	public static int findSwordSlot(MinecraftClient client) {
		if (client.player == null) return -1;
		
		for (int i = 0; i < 9; i++) {
			ItemStack stack = client.player.getInventory().getStack(i);
			if (!stack.isEmpty() && isMeleeWeapon(stack)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Проверяет, является ли айтем удочкой
	 */
	public static boolean isFishingRod(ItemStack stack) {
		if (stack.isEmpty()) return false;
		
		String itemName = getCleanItemName(stack);
		
		for (String rodName : FISHING_RODS) {
			if (itemName.contains(rodName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Проверяет, является ли айтем мечом/оружием
	 */
	public static boolean isMeleeWeapon(ItemStack stack) {
		if (stack.isEmpty()) return false;
		
		String itemName = getCleanItemName(stack);
		
		for (String weaponName : MELEE_WEAPONS) {
			if (itemName.contains(weaponName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Возвращает способность меча (если она наносит урон)
	 * Возвращает null если нет способности с уроном
	 */
	public static String getWeaponAbility(ItemStack stack) {
		if (stack.isEmpty()) return null;
		
		String itemName = getCleanItemName(stack);
		
		for (Map.Entry<String, String> entry : DAMAGE_ABILITIES.entrySet()) {
			if (itemName.contains(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * Очищает название айтема от кодов Minecraft (цвета, форматирование)
	 * Пример: "§c§lHyperion§r" -> "Hyperion"
	 */
	private static String getCleanItemName(ItemStack stack) {
		String name = stack.getName().getString();
		// Удаляет все коды форматирования Minecraft (§c, §l, §r и т.д.)
		return name.replaceAll("§[0-9a-fk-or]", "").trim();
	}

	/**
	 * Автоматически переключает на удочку
	 */
	public static void switchToFishingRod(MinecraftClient client) {
		int slot = findFishingRodSlot(client);
		if (slot != -1 && client.player != null) {
			client.player.getInventory().selectedSlot = slot;
		}
	}

	/**
	 * Автоматически переключает на меч
	 */
	public static void switchToSword(MinecraftClient client) {
		int slot = findSwordSlot(client);
		if (slot != -1 && client.player != null) {
			client.player.getInventory().selectedSlot = slot;
		}
	}

	/**
	 * Получает индекс конкретного слота (0-8)
	 * Полезно для отладки
	 */
	public static String getInventoryInfo(MinecraftClient client) {
		if (client.player == null) return "No player";
		
		StringBuilder info = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			ItemStack stack = client.player.getInventory().getStack(i);
			String type = "Empty";
			if (!stack.isEmpty()) {
				if (isFishingRod(stack)) {
					type = "🎣 Fishing Rod";
				} else if (isMeleeWeapon(stack)) {
					type = "⚔️ Sword";
				} else {
					type = "Other";
				}
			}
			info.append(String.format("[%d] %s\n", i + 1, type));
		}
		return info.toString();
	}
}
