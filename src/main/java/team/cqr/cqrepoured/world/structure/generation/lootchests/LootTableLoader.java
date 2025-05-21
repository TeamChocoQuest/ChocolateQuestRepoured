package team.cqr.cqrepoured.world.structure.generation.lootchests;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import meldexun.reflectionutil.ReflectionConstructor;
import meldexun.reflectionutil.ReflectionField;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.LootTableLoadEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.init.CQRLoottables;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
@SuppressWarnings("unchecked")
public class LootTableLoader {

	private static final ReflectionField<List<JsonElement>> JsonArray_elements = new ReflectionField<>(JsonArray.class, "elements", null);
	private static final ThreadLocal<Deque<?>> lootContext = new ReflectionField<ThreadLocal<Deque<?>>>(ForgeHooks.class, "lootContext", null).get(null);
	private static final ReflectionConstructor<?> LootTableContext = new ReflectionConstructor<>("net.minecraftforge.common.ForgeHooks$LootTableContext", ResourceLocation.class, boolean.class);

	public static void loadLootTableFromConfig(LootTableLoadEvent event) {
		if (Files.exists(jsonFile(event.getName()))) {
			loadJsonLootTable(event);
		} else if (Files.exists(propertiesFile(event.getName()))) {
			loadPropertiesLootTable(event);
		}
	}

	private static Path jsonFile(ResourceLocation name) {
		return new File(CQRMain.CQ_CHEST_FOLDER, name.getPath() + ".json").toPath();
	}

	private static Path propertiesFile(ResourceLocation name) {
		return new File(CQRMain.CQ_CHEST_FOLDER, name.getPath() + ".properties").toPath();
	}

	@SuppressWarnings("rawtypes")
	private static void loadJsonLootTable(LootTableLoadEvent event) {
		Path jsonFile = jsonFile(event.getName());

		try {
			JsonObject json;
			try (Reader reader = Files.newBufferedReader(jsonFile)) {
				json = LootTableManager.GSON_INSTANCE.fromJson(reader, JsonObject.class);
			}

			// remove invalid entries and empty pools
			if (json.has("pools")) {
				JsonArray_elements.get(json.get("pools")).removeIf(pool -> {
					if (!pool.getAsJsonObject().has("entries")) {
						return true;
					}
					JsonArray entries = pool.getAsJsonObject().getAsJsonArray("entries");
					JsonArray_elements.get(entries).removeIf(entry -> {
						return entry.getAsJsonObject().get("type").getAsString().equals("item") && Item.getByNameOrId(entry.getAsJsonObject().get("name").getAsString()) == null;
					});
					return entries.size() == 0;
				});
			}

			Deque que = lootContext.get();
			if (que == null) {
				lootContext.set(que = new ArrayDeque<>());
			}

			que.push(LootTableContext.newInstance(event.getName(), true));
			try {
				event.setTable(LootTableManager.GSON_INSTANCE.fromJson(json, LootTable.class));
			} finally {
				que.pop();
			}
		} catch (IOException | JsonParseException e) {
			CQRMain.logger.error("Failed to read json loot table {}", Launch.minecraftHome.toPath().relativize(jsonFile), e);
		}
	}

	private static void loadPropertiesLootTable(LootTableLoadEvent event) {
		Path propertiesFile = propertiesFile(event.getName());

		try {
			LootPool[] pools;
			if (CQRConfig.general.singleLootPoolPerLootTable) {
				pools = new LootPool[] { new LootPool(LootTableLoader.parseLootEntries(propertiesFile).toArray(LootEntry[]::new), new LootCondition[0], new RandomValueRange(CQRConfig.general.minItemsPerLootChest, CQRConfig.general.maxItemsPerLootChest), new RandomValueRange(0), event.getName().getPath()) };
			} else {
				pools = LootTableLoader.parseLootEntries(propertiesFile).map(entry -> new LootPool(new LootEntry[] { entry }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), entry.getEntryName())).toArray(LootPool[]::new);
			}
			event.setTable(new LootTable(pools));
		} catch (IOException e) {
			CQRMain.logger.error("Failed to read prop loot table {}", Launch.minecraftHome.toPath().relativize(propertiesFile), e);
		}
	}

	private static Stream<LootEntry> parseLootEntries(Path file) throws IOException {
		return Files.lines(file).map(LootTableLoader::parseLootEntry).filter(Objects::nonNull);
	}

	private static LootEntry parseLootEntry(String s) {
		if (s.startsWith("#")) {
			return null;
		}

		int i = s.indexOf('=');
		if (i < 0) {
			return null;
		}

		// Format: name = item, meta, countMin, countMax, chance, enchant, enchantMin, enchantMax, enchantTreasure, enchantChance
		String name = s.substring(0, i).trim();
		StringTokenizer tokenizer = new StringTokenizer(s.substring(i + 1), ",");
		Item item = Item.getByNameOrId(tokenizer.nextToken().trim());
		if (item == null) {
			return null;
		}
		int meta = parseInt(tokenizer, 0);
		int countMin = parseInt(tokenizer, 1);
		int countMax = parseInt(tokenizer, 1);
		int weight = parseInt(tokenizer, 100);
		boolean enchant = parseBoolean(tokenizer, false);
		int enchantMin = parseInt(tokenizer, 1);
		int enchantMax = parseInt(tokenizer, 30);
		boolean enchantTreasure = parseBoolean(tokenizer, false);
		int enchantChance = parseInt(tokenizer, 100);

		List<LootFunction> functionsBuilder = new ArrayList<>();
		if (countMin != 1 || countMax != 1) {
			functionsBuilder.add(new SetCount(new LootCondition[0], new RandomValueRange(countMin, countMax)));
		}
		if (enchant && enchantChance > 0) {
			functionsBuilder.add(new EnchantWithLevels(enchantChance < 100 ? new LootCondition[] { new RandomChance(enchantChance / 100.0F) } : new LootCondition[0], new RandomValueRange(enchantMin, enchantMax), enchantTreasure));
		}
		if (meta != 0) {
			functionsBuilder.add(new SetMetadata(new LootCondition[0], new RandomValueRange(meta)));
		}
		LootFunction[] functions = functionsBuilder.toArray(new LootFunction[functionsBuilder.size()]);

		return new LootEntryItem(item, weight, 0, functions, new LootCondition[0], name);
	}

	private static boolean parseBoolean(StringTokenizer tokenizer, boolean defaultValue) {
		return tokenizer.hasMoreTokens() ? Boolean.parseBoolean(tokenizer.nextToken().trim()) : defaultValue;
	}

	private static int parseInt(StringTokenizer tokenizer, int defaultValue) {
		if (!tokenizer.hasMoreTokens()) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(tokenizer.nextToken().trim());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static void registerCustomLootTables(WorldServer worldServer) {
		Collection<File> files = FileUtils.listFiles(new File(CQRMain.CQ_CHEST_FOLDER, "chests"), new String[] { "json", "properties" }, false);
		Set<ResourceLocation> cqrChestLootTables = CQRLoottables.getChestLootTables();
		LootTableManager lootTableManager = worldServer.getLootTableManager();

		for (File file : files) {
			String s = file.getName();
			ResourceLocation name = new ResourceLocation(CQRMain.MODID, "chests/" + s.substring(0, s.lastIndexOf('.')));

			if (cqrChestLootTables.contains(name)) {
				continue;
			}

			LootTable table = new LootTable(new LootPool[0]);
			table = ForgeEventFactory.loadLootTable(name, table, lootTableManager);
			lootTableManager.registeredLootTables.put(name, table);
		}
	}

}
