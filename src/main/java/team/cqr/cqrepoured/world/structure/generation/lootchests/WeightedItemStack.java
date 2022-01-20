package team.cqr.cqrepoured.world.structure.generation.lootchests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.loot.ILootGenerator;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetDamage;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
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
	private int enchantChance;

	public WeightedItemStack(String itemName, int damage, int minItems, int maxItems, int weight, boolean enchant, int minEnchantLevel, int maxEnchantLevel, boolean isTreasure, int enchantChance) {
		this.itemName = itemName;
		this.damage = damage;
		this.minCount = minItems;
		this.maxCount = maxItems;
		this.weight = weight;
		this.enchant = enchant;
		this.minLvl = minEnchantLevel;
		this.maxLvl = maxEnchantLevel;
		this.treasure = isTreasure;
		this.enchantChance = enchantChance;
	}

	public int getWeight() {
		return this.weight;
	}

	public LootPool getAsSingleLootPool(int indx) {
		ILootCondition condition = new RandomChance(this.weight / 100F);
		ILootCondition[] conditionA = new ILootCondition[] { condition };

		// LootCondition condition2 = new RandomChance(1F - (new Float(this.weight) / 100F));
		// LootCondition[] conditionB = new LootCondition[] { condition2 };

		ILootCondition condition3 = new RandomChance(1F);
		ILootCondition[] conditionC = new ILootCondition[] { condition3 };

		List<ILootFunction> functions = new ArrayList<>();
		functions.add(new SetCount(null, new RandomValueRange(this.minCount, this.maxCount)));
		if (this.enchant) {
			ILootCondition[] enchConds = null;
			if (this.enchantChance > 0) {
				enchConds = new ILootCondition[] { new RandomChance(this.enchantChance / 100F) };
			}
			if (this.treasure) {
				functions.add(new EnchantWithLevels(enchConds, new RandomValueRange(this.minLvl * 2, this.maxLvl * 2), true));
			} else {
				functions.add(new EnchantWithLevels(enchConds, new RandomValueRange(this.minLvl, this.maxLvl), false));
			}
		}
		if (this.damage != 0 && this.damage > 0) {
			functions.add(new SetDamage(null, new RandomValueRange(this.damage)));
		}

		ILootGenerator entry = new ItemLootEntry(Item.getByNameOrId(this.itemName), this.weight, 0, functions.toArray(new ILootFunction[0]), conditionC, "entry_" + indx + this.itemName);
		// LootEntry entryEmpty = new LootEntryEmpty(100 - this.weight, 0, conditionB, "entry_empty");

		ILootGenerator[] entryA = new ILootGenerator[] { entry/* , entryEmpty */ };

		LootPool pool = new LootPool(entryA, conditionA, new RandomValueRange(1), new RandomValueRange(0), "item_" + indx);
		return pool;
	}

	public ILootGenerator getAsLootEntry(int indx) {
		ILootCondition condition = new RandomChance(this.weight / 100F);
		// LootCondition condition2 = new
		ILootCondition[] conditionA = new ILootCondition[] { condition };
		List<ILootFunction> functions = new ArrayList<>();
		functions.add(new SetCount(null, new RandomValueRange(this.minCount, this.maxCount)));
		if (this.enchant) {
			ILootCondition[] enchConds = null;
			if (this.enchantChance > 0) {
				enchConds = new ILootCondition[] { new RandomChance(this.enchantChance / 100F) };
			}
			if (this.treasure) {
				functions.add(new EnchantWithLevels(enchConds, new RandomValueRange(this.minLvl * 2, this.maxLvl * 2), this.treasure));
			} else {
				functions.add(new EnchantWithLevels(enchConds, new RandomValueRange(this.minLvl, this.maxLvl), false));
			}
		}
		if (this.damage != 0 && this.damage > 0) {
			functions.add(new SetDamage(null, new RandomValueRange(this.damage)));
		}

		ILootGenerator entry = new ItemLootEntry(Item.getByNameOrId(this.itemName), this.weight, 0, functions.toArray(new ILootFunction[0]), conditionA, "entry_" + indx + this.itemName);
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
