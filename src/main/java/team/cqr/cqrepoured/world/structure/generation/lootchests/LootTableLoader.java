package team.cqr.cqrepoured.world.structure.generation.lootchests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Queues;
import com.google.common.io.Files;
import com.google.gson.JsonSyntaxException;

import meldexun.reflectionutil.ReflectionConstructor;
import meldexun.reflectionutil.ReflectionField;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class LootTableLoader {

	private static final ReflectionField<ThreadLocal<Deque<?>>> LOOT_CONTEXT = new ReflectionField<>(ForgeHooks.class, "lootContext", "lootContext");
	private static final ReflectionConstructor<?> LOOT_TABLE_CONTEXT = new ReflectionConstructor<>(Stream.of("net.minecraftforge.common.ForgeHooks$LootTableContext")
			.map(className -> {
				try {
					return Class.forName(className);
				} catch (ClassNotFoundException e) {
					return null;
				}
			})
			.filter(Objects::nonNull)
			.map(Class::getDeclaredConstructors)
			.flatMap(Arrays::stream)
			.filter(constructor -> constructor.getParameterCount() == 2)
			.filter(constructor -> "net.minecraft.util.ResourceLocation".equals(constructor.getParameterTypes()[0].getTypeName()))
			.filter(constructor -> "boolean".equals(constructor.getParameterTypes()[1].getTypeName()))
			.findFirst()
			.orElse(null));

	private static List<WeightedItemStack> getItemList(Properties propFile) {
		return propFile.values()
				.stream()
				.filter(String.class::isInstance)
				.map(String.class::cast)
				.map(LootTableLoader::createWeightedItemStack)
				.collect(Collectors.toList());
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
					enchChance = chance;
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
				LootTable newLootTable;
				try {
					newLootTable = LootTableManager.GSON.fromJson(s, LootTable.class);
				} finally {
					que.pop();
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
				LootTable newLootTable = LootTable.EMPTY;

				if (CQRConfig.SERVER_CONFIG.general.singleLootPoolPerLootTable.get()) {
					StandaloneLootEntry.Builder[] entries = new StandaloneLootEntry.Builder[items.size()];
					for (int i = 0; i < items.size(); i++) {
						entries[i] = items.get(i).getAsLootEntry(i);
					}

					LootPool.Builder poolBuilder = LootPool.lootPool()
							.setRolls(RandomValueRange.between(
									Math.min(
											CQRConfig.SERVER_CONFIG.general.minItemsPerLootChest.get(), CQRConfig.SERVER_CONFIG.general.maxItemsPerLootChest.get()
											), 
									Math.min(
											Math.max(
													CQRConfig.SERVER_CONFIG.general.minItemsPerLootChest.get(), CQRConfig.SERVER_CONFIG.general.maxItemsPerLootChest.get()
													),
											items.size()
											)
									)
								)
							.name(name.getPath() + "_pool");
					for(StandaloneLootEntry.Builder builder : entries) {
						poolBuilder = poolBuilder.add(builder);
					}
					
					return LootTable.lootTable().withPool(poolBuilder).build();
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

}
