package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * LootUtils supplies several methods for easing the process of modifying loot tables during the LootTableLoadEvent.
 * 
 * @author Draco18s
 *
 */
public class LootUtils {
	/***
	 * Removes the specified item from the indicated loot table
	 * 
	 * @param table
	 * @param toRemove
	 * @return returns if any entries were removed
	 */
	public static boolean removeLootFromTable(LootTable table, Item toRemove) {
		List<LootPool> pools = ReflectionHelper.getPrivateValue(LootTable.class, table, "pools", "field_186466_c");
		for (LootPool pool : pools) {
			List<LootEntry> entries = ReflectionHelper.getPrivateValue(LootPool.class, pool, "lootEntries", "field_186453_a");
			Iterator<LootEntry> it = entries.iterator();
			while (it.hasNext()) {
				LootEntry entry = it.next();
				if (entry instanceof LootEntryItem) {
					LootEntryItem lei = (LootEntryItem) entry;
					Item i = ReflectionHelper.getPrivateValue(LootEntryItem.class, lei, "item", "field_186368_a");
					if (i == toRemove) {
						it.remove();
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void addItemToTable(LootTable table, Item item, int weight, float numRolls, float probability, int minQuantity, int maxQuantity, float minLootBonus, float maxLootBonus, String name) {
		addItemToTable(table, item, 0, 0, weight, numRolls, probability, minQuantity, maxQuantity, minLootBonus, maxLootBonus, name);
	}

	public static void addItemToTable(LootTable table, Item item, int weight, float numRolls, float probability, int minQuantity, int maxQuantity, float minLootBonus, float maxLootBonus, String name, IMethod LootCallbacks,
			ICondition LootConditions) {
		addItemToTable(table, item, 0, 0, weight, numRolls, probability, minQuantity, maxQuantity, minLootBonus, maxLootBonus, name, LootCallbacks, LootConditions);
	}

	public static void addItemToTable(LootTable table, Item item, int weight, float numRolls, float probability, int minQuantity, int maxQuantity, float minLootBonus, float maxLootBonus, String name, IMethod LootCallbacks) {
		addItemToTable(table, item, 0, 0, weight, numRolls, probability, minQuantity, maxQuantity, minLootBonus, maxLootBonus, name, LootCallbacks, new ICondition() {
			@Override
			public void FunctionsCallback(ArrayList<LootCondition> lootconds) {
			}
		});
	}

	public static void addItemToTable(LootTable table, Item item, int weight, float numRolls, float probability, int minQuantity, int maxQuantity, float minLootBonus, float maxLootBonus, String name, ICondition LootConditions) {
		addItemToTable(table, item, 0, 0, weight, numRolls, probability, minQuantity, maxQuantity, minLootBonus, maxLootBonus, name, new IMethod() {
			@Override
			public void FunctionsCallback(ArrayList<LootFunction> lootfuncs) {
			}
		}, LootConditions);
	}

	public static void addItemToTable(LootTable table, Item item, int minMeta, int maxMeta, int weight, float numRolls, float probability, int minQuantity, int maxQuantity, float minLootBonus, float maxLootBonus, String name) {
		addItemToTable(table, item, 0, 0, weight, numRolls, probability, minQuantity, maxQuantity, minLootBonus, maxLootBonus, name, new IMethod() {
			@Override
			public void FunctionsCallback(ArrayList<LootFunction> lootfuncs) {
			}
		}, new ICondition() {
			@Override
			public void FunctionsCallback(ArrayList<LootCondition> lootconds) {
			}
		});
	}

	public static void addItemToTable(LootTable table, Item item, int minMeta, int maxMeta, int weight, float numRolls, float probability, int minQuantity, int maxQuantity, float minLootBonus, float maxLootBonus, String name,
			IMethod LootCallbacks, ICondition LootConditions) {
		ArrayList<LootCondition> _conditions = new ArrayList<LootCondition>();
		_conditions.add(new RandomChance(probability));
		LootConditions.FunctionsCallback(_conditions);
		LootCondition[] lchance = _conditions.toArray(new LootCondition[0]);
		/* LootCondition[] lchance = {new RandomChance(probability)}; */

		ArrayList<LootFunction> _functions = new ArrayList<LootFunction>();
		if (item.getItemStackLimit() > 1) {
			_functions.add(new SetCount(lchance, new RandomValueRange(minQuantity, maxQuantity)));
		}
		if (minLootBonus >= 0 && (maxLootBonus > minLootBonus || maxLootBonus > 0)) {
			_functions.add(new LootingEnchantBonus(new LootCondition[] {}, new RandomValueRange(minLootBonus, maxLootBonus), 0));
		}
		_functions.add(new SetMetadata(new LootCondition[] {}, new RandomValueRange(minMeta, maxMeta)));
		LootCallbacks.FunctionsCallback(_functions);
		LootFunction[] lcount = _functions.toArray(new LootFunction[0]);

		/*
		 * ArrayList<LootEntryItem> _entries = new ArrayList<LootEntryItem>(); _entries.add(new LootEntryItem(item, weight, 1, lcount, lchance, name)); LootEntryItem[] leitem = _entries.toArray(new LootEntryItem[0]);
		 */
		LootEntryItem[] leitem = { new LootEntryItem(item, weight, 1, lcount, lchance, name) };

		LootPool newPool = createLootPool(leitem, lcount, lchance, new RandomValueRange(numRolls), new RandomValueRange(0), name);

		addItemToTable(table, newPool);
	}

	public static LootPool createLootPool(LootEntryItem[] leitem, LootFunction[] lcount, LootCondition[] lchance, RandomValueRange numRolls, RandomValueRange bonusRolls, String name) {
		return new LootPool(leitem, lchance, numRolls, bonusRolls, name);
	}

	public static void addItemToTable(LootTable table, LootPool pool) {
		table.addPool(pool);
	}

	public static interface IMethod {
		public void FunctionsCallback(ArrayList<LootFunction> lootfuncs);
	}

	public static interface ICondition {
		public void FunctionsCallback(ArrayList<LootCondition> lootconds);
	}
}
