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

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Queues;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.ForgeHooks;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class LootTableLoader {

	private static List<WeightedItemStack> getItemList(Properties propFile) {
		List<WeightedItemStack> items = new ArrayList<WeightedItemStack>();
		Enumeration<Object> fileEntries = propFile.elements();
		while (fileEntries.hasMoreElements()) {
			String entry = (String) fileEntries.nextElement();
			if (!entry.startsWith("#")) {
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

	public static LootTable fillLootTable(ResourceLocation name, LootTable lootTable) {
		File jsonFile = new File(CQRMain.CQ_CHEST_FOLDER, ELootTable.valueOf(name).getFileName() + ".json");
		File propFile = new File(CQRMain.CQ_CHEST_FOLDER, ELootTable.valueOf(name).getFileName() + ".prop");
		InputStream inputStream = null;

		if (jsonFile.exists()) {
			// Load json loot table
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
			} catch (IOException | JsonSyntaxException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InstantiationException
					| InvocationTargetException e) {
				CQRMain.logger.error("Failed to read json loot table " + jsonFile.getName(), e);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		} else if (propFile.exists()) {
			// Load prop file and fill loot table
			try {
				inputStream = new FileInputStream(propFile);
				Properties properties = new Properties();
				properties.load(inputStream);

				List<WeightedItemStack> items = getItemList(properties);

				if (CQRConfig.general.singleLootPoolPerLootTable) {
					LootEntry[] entries = new LootEntry[items.size()];
					for (int i = 0; i < items.size(); i++) {
						entries[i] = items.get(i).getAsLootEntry(i);
					}

					lootTable.addPool(new LootPool(entries, new LootCondition[] {}, new RandomValueRange(Math.min(CQRConfig.general.minItemsPerLootChest, CQRConfig.general.maxItemsPerLootChest), Math.min(Math.max(
							CQRConfig.general.minItemsPerLootChest, CQRConfig.general.maxItemsPerLootChest), items.size())), new RandomValueRange(0), name.getResourceDomain() + "_pool"));
				} else {
					for (int i = 0; i < items.size(); i++) {
						lootTable.addPool(items.get(i).getAsSingleLootPool(i));
					}
				}
			} catch (IOException e) {
				CQRMain.logger.error("Failed to read prop loot table " + propFile.getName(), e);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		}

		return lootTable;

	}

	private static ThreadLocal<Deque> getLootContext() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = ForgeHooks.class.getDeclaredField("lootContext");
		f.setAccessible(true);
		return (ThreadLocal<Deque>) f.get(null);
	}

	private static Object createLootTableContext(ResourceLocation name) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor c = Class.forName("net.minecraftforge.common.ForgeHooks$LootTableContext").getDeclaredConstructor(ResourceLocation.class, Boolean.TYPE);
		c.setAccessible(true);
		return c.newInstance(name, true);
	}

	private static Gson getGsonInstance() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = LootTableManager.class.getDeclaredField("GSON_INSTANCE");
		f.setAccessible(true);
		return (Gson) f.get(null);
	}

}
