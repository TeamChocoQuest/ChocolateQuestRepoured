package com.teamcqr.chocolatequestrepoured.structuregen.lootchests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.google.common.collect.Queues;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.teamcqr.chocolatequestrepoured.CQRMain;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.common.ForgeHooks;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class LootTableLoader {

	// private static final WeightedItemStack airEntryBase = new WeightedItemStack("minecraft:air", 0, 1, 2, 100, false, 1, 2, false);

	// These are all valid file names for the chests!
	final static String[] validFileNames = {
			"treasure_chest",
			"material_chest",
			"food_chest",
			"tools_chest",
			"custom_1",
			"custom_2",
			"custom_3",
			"custom_4",
			"custom_5",
			"custom_6",
			"custom_7",
			"custom_8",
			"custom_9",
			"custom_10",
			"custom_11",
			"custom_12",
			"custom_13",
			"custom_14" };

	public void loadConfigs() {
		if (CQRMain.CQ_CHEST_FOLDER.exists()) {
			File[] files = CQRMain.CQ_CHEST_FOLDER.listFiles();
			CQRMain.logger.info("Loading " + files.length + " loot chest configs.");
			for (File f : files) {
				ELootTable.getAssignedLootTable(f.getName());
			}
		}
	}

	private static List<WeightedItemStack> getItemList(Properties propFile) {
		List<WeightedItemStack> items = new ArrayList<WeightedItemStack>();
		Enumeration<Object> fileEntries = propFile.elements();
		while (fileEntries.hasMoreElements()) {
			String entry = (String) fileEntries.nextElement();
			if(!entry.startsWith("#")) {
				WeightedItemStack stack = createWeightedItemStack(entry);
				if (stack != null) {
					items.add(stack);
				}
			}
		}
		return items;
	}

	private static WeightedItemStack createWeightedItemStack(String entry) {
		// String format: ID = ITEM, DAMAGE, MIN_COUNT, MAX_COUNT, CHANCE, ENCHANT, MIN_LVL, MAX_LVL, TREASURE
		StringTokenizer tokenizer = new StringTokenizer(entry, ",");
		int tokenCount = tokenizer.countTokens();
		if (tokenCount >= 5) {
			String item = "minecraft:stone";
			int damage = 0;
			int min_count = 0;
			int max_count = 1;
			int chance = 25;
			boolean enchant = false;
			int min_lvl = 1;
			int max_lvl = 10;
			boolean treasure = false;

			item = ((String) tokenizer.nextElement()).trim();
			damage = Integer.parseInt(((String) tokenizer.nextElement()).trim());
			min_count = Integer.parseInt(((String) tokenizer.nextElement()).trim());
			max_count = Integer.parseInt(((String) tokenizer.nextElement()).trim());
			chance = Integer.parseInt(((String) tokenizer.nextElement()).trim());

			if (tokenCount >= 6) {
				enchant = Boolean.parseBoolean(((String) tokenizer.nextElement()).trim());
				min_lvl = Integer.parseInt(((String) tokenizer.nextElement()).trim());
				max_lvl = Integer.parseInt(((String) tokenizer.nextElement()).trim());
				if (tokenCount >= 9) {
					treasure = Boolean.parseBoolean(((String) tokenizer.nextElement()).trim());
				}
			}

			WeightedItemStack itemstack = new WeightedItemStack(item, damage, min_count, max_count, chance, enchant, min_lvl, max_lvl, treasure);
			return itemstack;
		} else {
			CQRMain.logger.error("Config string is invalid! Not enough arguments!");
			return null;
		}
	}

	static boolean isNameValid(String fileName) {
		for (int i = 0; i < validFileNames.length; i++) {
			if (validFileNames[i].equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		return false;
	}

	public static LootTable fillLootTable(ResourceLocation name, LootTable lootTable) {
		File jsonFile = new File(CQRMain.CQ_CHEST_FOLDER, ELootTable.getAssignedFileName(ELootTable.valueOf(name)) + ".json");
		File propFile = new File(CQRMain.CQ_CHEST_FOLDER, ELootTable.getAssignedFileName(ELootTable.valueOf(name)) + ".prop");
		InputStream inputStream = null;

		if (jsonFile.exists()) {
			try {
				inputStream = new FileInputStream(jsonFile);
				String s = Files.toString(jsonFile, StandardCharsets.UTF_8);

				ThreadLocal<Deque> lootContext = getLootContext();
				Deque que = lootContext.get();
				if (que == null) {
					que = Queues.newArrayDeque();
					lootContext.set(que);
				}

				que.push(createLootTableContext(name));
				lootTable = getGsonInstance().fromJson(s, LootTable.class);
				que.pop();

				if (lootTable != null) {
					lootTable.freeze();
				}
			} catch (IOException | JsonParseException e) {
				CQRMain.logger.error(e);
			}

			try {
				inputStream.close();
			} catch (IOException e) {
				CQRMain.logger.error(e);
			}
		} else if (propFile.exists()) {
			try {
				inputStream = new FileInputStream(propFile);
				Properties properties = new Properties();
				properties.load(inputStream);

				List<WeightedItemStack> items = getItemList(properties);

				for (int i = 0; i < items.size(); i++) {
					items.get(i).addToTable(lootTable, i);
				}
			} catch (IOException e) {
				CQRMain.logger.error(e);
			}

			try {
				inputStream.close();
			} catch (IOException e) {
				CQRMain.logger.error(e);
			}
		}

		return lootTable;
	}

	private static ThreadLocal<Deque> lootContext = null;

	private static ThreadLocal<Deque> getLootContext() {
		if (lootContext != null) {
			return lootContext;
		}
		try {
			Field f = ForgeHooks.class.getDeclaredField("lootContext");
			f.setAccessible(true);
			return (ThreadLocal<Deque>) f.get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error(e);
		}
		return null;
	}

	private static Object createLootTableContext(ResourceLocation name) {
		try {
			Constructor c = Class.forName("net.minecraftforge.common.ForgeHooks$LootTableContext").getDeclaredConstructor(ResourceLocation.class, Boolean.TYPE);
			c.setAccessible(true);
			return c.newInstance(name, true);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			CQRMain.logger.error(e);
		}
		return null;
	}

	private static Gson GsonInstance = null;

	private static Gson getGsonInstance() {
		if (GsonInstance != null) {
			return GsonInstance;
		}
		try {
			Field f = LootTableManager.class.getDeclaredField("GSON_INSTANCE");
			f.setAccessible(true);
			return (Gson) f.get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error(e);
		}
		return null;
	}

}
