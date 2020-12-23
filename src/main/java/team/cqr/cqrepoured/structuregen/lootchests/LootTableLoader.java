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
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.Reference;
import team.cqr.cqrepoured.util.reflection.ReflectionConstructor;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class LootTableLoader {

	private static final ReflectionField<ThreadLocal<Deque<?>>> LOOT_CONTEXT = new ReflectionField<>(ForgeHooks.class, "lootContext", "lootContext");
	private static final ReflectionConstructor<?> LOOT_TABLE_CONTEXT = new ReflectionConstructor<>("net.minecraftforge.common.ForgeHooks$LootTableContext", ResourceLocation.class, Boolean.TYPE);
	private static final ReflectionField<Gson> GSON_INSTANCE = new ReflectionField<>(LootTableManager.class, "field_186526_b", "GSON_INSTANCE");
	private static final ReflectionField<LoadingCache<ResourceLocation, LootTable>> FIELD_REGISTERED_LOOT_TABLES = new ReflectionField<>(LootTableManager.class, "field_186527_c", "registeredLootTables");

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
			String item = "minecraft:stone";
			int damage = 0;
			int min_count = 0;
			int max_count = 1;
			int chance = 25;
			boolean enchant = false;
			int min_lvl = 1;
			int max_lvl = 10;
			boolean treasure = false;
			int enchChance = 0;

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
					if (tokenCount >= 10) {
						enchChance = Integer.parseInt(((String) tokenizer.nextElement()).trim());
					}
				}
			}

			WeightedItemStack itemstack = new WeightedItemStack(item, damage, min_count, max_count, chance, enchant, min_lvl, max_lvl, treasure, enchChance);
			return itemstack;
		} else {
			CQRMain.logger.error("Config string (%s) is invalid! Not enough arguments!", entry);
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LootTable fillLootTable(ResourceLocation name, LootTable lootTable) {
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
				lootTable = GSON_INSTANCE.get(null).fromJson(s, LootTable.class);
				que.pop();

				if (lootTable != null) {
					loadingLootTable = lootTable;
				}
			} catch (IOException | JsonSyntaxException e) {
				CQRMain.logger.error("Failed to read json loot table " + jsonFile.getName(), e);
			}
		} else if (propFile.exists()) {
			// Load prop file and fill loot table
			try (InputStream inputStream = new FileInputStream(propFile)) {
				Properties properties = new Properties();
				properties.load(inputStream);

				List<WeightedItemStack> items = getItemList(properties);

				if (CQRConfig.general.singleLootPoolPerLootTable) {
					LootEntry[] entries = new LootEntry[items.size()];
					for (int i = 0; i < items.size(); i++) {
						entries[i] = items.get(i).getAsLootEntry(i);
					}

					lootTable.addPool(new LootPool(entries, new LootCondition[] {}, new RandomValueRange(Math.min(CQRConfig.general.minItemsPerLootChest, CQRConfig.general.maxItemsPerLootChest), Math.min(Math.max(CQRConfig.general.minItemsPerLootChest, CQRConfig.general.maxItemsPerLootChest), items.size())),
							new RandomValueRange(0), name.getPath() + "_pool"));
				} else {
					for (int i = 0; i < items.size(); i++) {
						lootTable.addPool(items.get(i).getAsSingleLootPool(i));
					}
				}
			} catch (IOException e) {
				CQRMain.logger.error("Failed to read prop loot table " + propFile.getName(), e);
			}
		}

		return lootTable;
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
