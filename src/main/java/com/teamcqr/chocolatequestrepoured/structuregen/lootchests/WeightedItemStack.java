package com.teamcqr.chocolatequestrepoured.structuregen.lootchests;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.*;
import net.minecraft.world.storage.loot.functions.*;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class WeightedItemStack {

	private String itemName;
	private int minCount;
	private int maxCount;
	private int weight;
	private boolean enchant = false;
	private boolean treasure;
	private int damage;
	private int minLvl;
	private int maxLvl;

	public WeightedItemStack(String itemName, int damage, int minItems, int maxItems, int weight, boolean enchant, int minEnchantLevel, int maxEnchantLevel, boolean isTreasure) {
		this.itemName = itemName;
		this.damage = damage;
		this.minCount = minItems;
		this.maxCount = maxItems;
		this.weight = weight;
		this.enchant = enchant;
		this.minLvl = minEnchantLevel;
		this.maxLvl = maxEnchantLevel;
		this.treasure = isTreasure;
	}

	public int getWeight() {
		return this.weight;
	}

	public LootPool getAsSingleLootPool(int indx) {
		LootCondition condition = new RandomChance(new Float(this.weight) / 100F);
		LootCondition[] conditionA = new LootCondition[] { condition };

		//LootCondition condition2 = new RandomChance(1F - (new Float(this.weight) / 100F));
		//LootCondition[] conditionB = new LootCondition[] { condition2 };

		LootCondition condition3 = new RandomChance(1F);
		LootCondition[] conditionC = new LootCondition[] { condition3 };

		ArrayList<LootFunction> functions = new ArrayList<>();
		functions.add(new SetCount(null, new RandomValueRange(this.minCount, this.maxCount)));
		if (this.enchant) {
			if (this.treasure) {
				functions.add(new EnchantWithLevels(null, new RandomValueRange(this.minLvl * 2, this.maxLvl * 2), true));
			} else {
				functions.add(new EnchantWithLevels(null, new RandomValueRange(this.minLvl, this.maxLvl), false));
			}
		}
		if(this.damage != 0 && this.damage > 0) {
			functions.add(new SetMetadata(null, new RandomValueRange(damage)));
		}

		LootEntry entry = new LootEntryItem(Item.getByNameOrId(this.itemName), this.weight, 0, functions.toArray(new LootFunction[0]), conditionC, "entry_" + indx + this.itemName);
		//LootEntry entryEmpty = new LootEntryEmpty(100 - this.weight, 0, conditionB, "entry_empty");

		LootEntry[] entryA = new LootEntry[] { entry/*, entryEmpty */};

		LootPool pool = new LootPool(entryA, conditionA, new RandomValueRange(1), new RandomValueRange(0), "item_" + indx);
		return pool;
	}
	
	public LootEntry getAsLootEntry(int indx) {
		LootCondition condition = new RandomChance(new Float(this.weight) / 100F);
		//LootCondition condition2 = new 
		LootCondition[] conditionA = new LootCondition[] { condition };
		ArrayList<LootFunction> functions = new ArrayList<>();
		functions.add(new SetCount(null, new RandomValueRange(this.minCount, this.maxCount)));
		if (this.enchant) {
			if (this.treasure) {
				functions.add(new EnchantWithLevels(null, new RandomValueRange(this.minLvl * 2, this.maxLvl * 2), true));
			} else {
				functions.add(new EnchantWithLevels(null, new RandomValueRange(this.minLvl, this.maxLvl), false));
			}
		}
		if(this.damage != 0 && this.damage > 0) {
			functions.add(new SetMetadata(null, new RandomValueRange(damage)));
		}

		LootEntry entry = new LootEntryItem(Item.getByNameOrId(this.itemName), this.weight, 0, functions.toArray(new LootFunction[0]), conditionA, "entry_" + indx + this.itemName);
		return entry;
	}

	public WeightedItemStack setChance(int chance) {
		if (chance != this.weight) {
			this.weight = Math.abs(chance);
		}
		return this;
	}

	public String getItemName() {
		return this.itemName;
	}

}
