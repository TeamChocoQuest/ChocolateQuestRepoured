package team.cqr.cqrepoured.world.structure.generation.lootchests;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonParseException;

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
public class LootTableLoader {

	private static final ReflectionField<Boolean> LootTable_isFrozen = new ReflectionField<>(LootTable.class, "isFrozen", null);
	private static final ReflectionField<Boolean> LootPool_isFrozen = new ReflectionField<>(LootPool.class, "isFrozen", null);
	// ATs don't work in dev for some reason
	private static final ReflectionField<List<LootPool>> LootTable_pools = new ReflectionField<>(LootTable.class, "field_186466_c", "pools");

	public static void loadLootTableFromConfig(LootTableLoadEvent event) {
		LootTable lootTable = loadJsonLootTable(event.getName(), event.getLootTableManager());
		if (lootTable == null) {
			lootTable = loadPropertiesLootTable(event.getName());
		}

		if (lootTable != null) {
			event.setTable(lootTable);
		}
	}

	private static LootTable loadJsonLootTable(ResourceLocation name, LootTableManager lootTableManager) {
		Path jsonFile = new File(CQRMain.CQ_CHEST_FOLDER, name.getPath() + ".json").toPath();
		if (!Files.exists(jsonFile)) {
			return null;
		}

		try {
			String data = new String(Files.readAllBytes(jsonFile), StandardCharsets.UTF_8);
			LootTable lootTable = ForgeHooks.loadLootTable(LootTableManager.GSON_INSTANCE, name, data, true, lootTableManager);

			// unfreeze to fix crash when another mod wants to modify the loot table
			if (lootTable != null) {
				LootTable_isFrozen.setBoolean(lootTable, false);
				LootTable_pools.get(lootTable).forEach(pool -> LootPool_isFrozen.setBoolean(pool, false));
			}

			return lootTable;
		} catch (IOException | JsonParseException e) {
			CQRMain.logger.error("Failed to read json loot table {}", Launch.minecraftHome.toPath().relativize(jsonFile), e);
			return null;
		}
	}

	private static LootTable loadPropertiesLootTable(ResourceLocation name) {
		Path propertiesFile = new File(CQRMain.CQ_CHEST_FOLDER, name.getPath() + ".properties").toPath();
		if (!Files.exists(propertiesFile)) {
			return null;
		}

		try {
			LootPool[] pools;
			if (CQRConfig.general.singleLootPoolPerLootTable) {
				pools = new LootPool[] { new LootPool(LootTableLoader.parseLootEntries(propertiesFile).toArray(LootEntry[]::new), new LootCondition[0], new RandomValueRange(CQRConfig.general.minItemsPerLootChest, CQRConfig.general.maxItemsPerLootChest), new RandomValueRange(0), name.getPath()) };
			} else {
				pools = LootTableLoader.parseLootEntries(propertiesFile).map(entry -> new LootPool(new LootEntry[] { entry }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), entry.getEntryName())).toArray(LootPool[]::new);
			}
			return new LootTable(pools);
		} catch (IOException e) {
			CQRMain.logger.error("Failed to read prop loot table {}", Launch.minecraftHome.toPath().relativize(propertiesFile), e);
			return null;
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
		int meta = parseInt(tokenizer, 0);
		int countMin = parseInt(tokenizer, 1);
		int countMax = parseInt(tokenizer, 1);
		int chance = parseInt(tokenizer, 100);
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
			functionsBuilder.add(new EnchantWithLevels(enchantChance < 100 ? new LootCondition[] { new RandomChance(enchantChance / 100.0F) } : new LootCondition[0], new RandomValueRange(enchantMin * (enchantTreasure ? 2 : 1), enchantMax * (enchantTreasure ? 2 : 1)), enchantTreasure));
		}
		if (meta != 0) {
			functionsBuilder.add(new SetMetadata(new LootCondition[0], new RandomValueRange(meta)));
		}
		LootFunction[] functions = functionsBuilder.toArray(new LootFunction[functionsBuilder.size()]);
		LootCondition[] conditions = chance < 100 ? new LootCondition[] { new RandomChance(chance / 100.0F) } : new LootCondition[0];

		return new LootEntryItem(item, chance, 0, functions, conditions, name);
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
