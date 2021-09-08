package team.cqr.cqrepoured.structuregen.lootchests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.Queues;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.util.Reference;
import team.cqr.cqrepoured.util.reflection.ReflectionConstructor;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class LootTableLoader {

	private static final ReflectionField LOOT_CONTEXT = new ReflectionField(ForgeHooks.class, "lootContext", "lootContext");
	private static final ReflectionConstructor<?> LOOT_TABLE_CONTEXT = new ReflectionConstructor<>("net.minecraftforge.common.ForgeHooks$LootTableContext", ResourceLocation.class, Boolean.TYPE);
	private static final ReflectionField GSON_INSTANCE = new ReflectionField(LootTableManager.class, "field_186526_b", "GSON_INSTANCE");
	private static final ReflectionField FIELD_REGISTERED_LOOT_TABLES = new ReflectionField(LootTableManager.class, "field_186527_c", "registeredLootTables");

	private static LootTable loadingLootTable;

	private static List<WeightedItemStack> getItemList(Properties propFile) {
		List<WeightedItemStack> items = new ArrayList<>();
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
			String item;
			int damage;
			int minCount;
			int maxCount;
			int chance;
			boolean enchant = false;
			int minLvl = 1;
			int maxLvl = 10;
			boolean treasure = false;
			int enchChance = 0;

			item = ((String) tokenizer.nextElement()).trim();
			if (item.isEmpty()) {
				CQRMain.logger.error("Can't parse argument 1 (item) of:\n{}", entry);
				return null;
			}
			try {
				damage = Integer.parseInt(((String) tokenizer.nextElement()).trim());
			} catch (NumberFormatException e) {
				CQRMain.logger.error("Can't parse argument 2 (item damage) of:\n{}", entry);
				return null;
			}
			try {
				minCount = Integer.parseInt(((String) tokenizer.nextElement()).trim());
			} catch (NumberFormatException e) {
				CQRMain.logger.error("Can't parse argument 3 (min item count) of:\n{}", entry);
				return null;
			}
			try {
				maxCount = Integer.parseInt(((String) tokenizer.nextElement()).trim());
			} catch (NumberFormatException e) {
				CQRMain.logger.error("Can't parse argument 4 (max item count) of:\n{}", entry);
				return null;
			}
			try {
				chance = Integer.parseInt(((String) tokenizer.nextElement()).trim());
			} catch (NumberFormatException e) {
				CQRMain.logger.error("Can't parse argument 5 (item chance) of:\n{}", entry);
				return null;
			}

			if (tokenCount >= 6) {
				enchant = Boolean.parseBoolean(((String) tokenizer.nextElement()).trim());
				try {
					minLvl = Integer.parseInt(((String) tokenizer.nextElement()).trim());
				} catch (NumberFormatException e) {
					CQRMain.logger.error("Can't parse argument 7 (min enchant level) of:\n{}", entry);
					return null;
				}
				try {
					maxLvl = Integer.parseInt(((String) tokenizer.nextElement()).trim());
				} catch (NumberFormatException e) {
					CQRMain.logger.error("Can't parse argument 8 (max enchant level) of:\n{}", entry);
					return null;
				}
				if (tokenCount >= 9) {
					treasure = Boolean.parseBoolean(((String) tokenizer.nextElement()).trim());
					if (tokenCount >= 10) {
						try {
							enchChance = Integer.parseInt(((String) tokenizer.nextElement()).trim());
						} catch (NumberFormatException e) {
							CQRMain.logger.error("Can't parse argument 10 (enchanting chance) of:\n{}", entry);
							return null;
						}
					}
				}
			}

			return new WeightedItemStack(item, damage, minCount, maxCount, chance, enchant, minLvl, maxLvl, treasure, enchChance);
		} else {
			CQRMain.logger.error("Config string {} is invalid! Not enough arguments!", entry);
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LootTable fillLootTable(ResourceLocation name, LootTable defaultLootTable) {
		File jsonFile = new File(CQRMain.CQ_CHEST_FOLDER, name.getPath() + ".json");
		File propFile = new File(CQRMain.CQ_CHEST_FOLDER, name.getPath() + ".properties");

		if (jsonFile.exists()) {
			// Load json loot table
			try (InputStream inputStream = new FileInputStream(jsonFile)) {
				String s = Files.toString(jsonFile, StandardCharsets.UTF_8);

				ThreadLocal<Deque<?>> lootContext = LOOT_CONTEXT.get(null);
				Deque que = lootContext.get();
				if (que == null) {
					que = Queues.newArrayDeque();
					lootContext.set(que);
				}

				que.push(LOOT_TABLE_CONTEXT.newInstance(name, true));
				LootTable newLootTable = GSON_INSTANCE.<Gson>get(null).fromJson(s, LootTable.class);
				que.pop();

				if (newLootTable != null) {
					loadingLootTable = newLootTable;
				}
				return newLootTable;
			} catch (IOException | JsonSyntaxException e) {
				CQRMain.logger.error("Failed to read json loot table {}", jsonFile.getName(), e);
			}
		} else if (propFile.exists()) {
			// Load prop file and fill loot table
			try (InputStream inputStream = new FileInputStream(propFile)) {
				Properties properties = new Properties();
				properties.load(inputStream);

				List<WeightedItemStack> items = getItemList(properties);
				LootTable newLootTable = new LootTable(new LootPool[0]);

				if (CQRConfig.general.singleLootPoolPerLootTable) {
					LootEntry[] entries = new LootEntry[items.size()];
					for (int i = 0; i < items.size(); i++) {
						entries[i] = items.get(i).getAsLootEntry(i);
					}

					return new LootTable(new LootPool[] {
							new LootPool(entries, new LootCondition[] {},
									new RandomValueRange(Math.min(CQRConfig.general.minItemsPerLootChest, CQRConfig.general.maxItemsPerLootChest),
											Math.min(Math.max(CQRConfig.general.minItemsPerLootChest, CQRConfig.general.maxItemsPerLootChest), items.size())),
									new RandomValueRange(0), name.getPath() + "_pool") });
				} else {
					for (int i = 0; i < items.size(); i++) {
						newLootTable.addPool(items.get(i).getAsSingleLootPool(i));
					}
				}

				return newLootTable;
			} catch (IOException e) {
				CQRMain.logger.error("Failed to read prop loot table {}", propFile.getName(), e);
			}
		}

		return defaultLootTable;
	}

	public static void freezeLootTable() {
		if (loadingLootTable != null) {
			loadingLootTable.freeze();
			loadingLootTable = null;
		}
	}

	public static void registerCustomLootTables(WorldServer worldServer) {
		Collection<File> files = FileUtils.listFiles(new File(CQRMain.CQ_CHEST_FOLDER, "chests"), new String[] { "json", "properties" }, false);
		Set<ResourceLocation> cqrChestLootTables = CQRLoottables.getChestLootTables();
		LootTableManager lootTableManager = worldServer.getLootTableManager();
		LoadingCache<ResourceLocation, LootTable> registeredLootTables = FIELD_REGISTERED_LOOT_TABLES.get(lootTableManager);

		for (File file : files) {
			String s = file.getName();
			ResourceLocation name = new ResourceLocation(Reference.MODID, "chests/" + s.substring(0, s.lastIndexOf('.')));

			if (cqrChestLootTables.contains(name)) {
				continue;
			}

			LootTable table = new LootTable(new LootPool[0]);
			ForgeEventFactory.loadLootTable(name, table, lootTableManager);
			registeredLootTables.put(name, table);
		}
	}

}
