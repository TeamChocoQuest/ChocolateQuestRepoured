package team.cqr.cqrepoured.world.structure.generation.lootchests;

import net.minecraft.item.Item;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.StandaloneLootEntry;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetDamage;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class WeightedItemStack implements IItemProvider{

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
		ILootCondition.IBuilder conditionPool = RandomChance.randomChance(this.weight / 100F);

		//LootPool pool = new LootPool(entryA, conditionA, new RandomValueRange(1), new RandomValueRange(0), "item_" + indx);
		LootPool.Builder lootPoolBuilder = LootPool.lootPool().setRolls(RandomValueRange.between(1, 1));
		lootPoolBuilder = lootPoolBuilder.name("item_" + indx).when(conditionPool);
		lootPoolBuilder.add(this.getAsLootEntry(indx));
		
		return lootPoolBuilder.build();
	}

	public StandaloneLootEntry.Builder<?> getAsLootEntry(int indx) {
		ILootCondition.IBuilder condition = RandomChance.randomChance(this.weight / 100F);
		// LootCondition condition2 = new
		List<ILootFunction.IBuilder> functions = new ArrayList<>();
		functions.add(SetCount.setCount(RandomValueRange.between(this.minCount, this.maxCount)));
		if (this.enchant) {
			ILootCondition.IBuilder enchConds = null;
			if (this.enchantChance > 0) {
				enchConds = RandomChance.randomChance(this.enchantChance / 100F);
			}
			if (this.treasure) {
				functions.add(
						EnchantWithLevels
							.enchantWithLevels(RandomValueRange.between(this.minLvl * 2, this.maxLvl * 2))
							.allowTreasure()
							.when(enchConds)
						);
			} else {
				functions.add(
						EnchantWithLevels
							.enchantWithLevels(RandomValueRange.between(this.minLvl, this.maxLvl))
							.when(enchConds)
						);
			}
		}
		if (this.damage != 0 && this.damage > 0) {
			functions.add(SetDamage.setDamage(RandomValueRange.between(this.damage, this.damage)));
		}

		StandaloneLootEntry.Builder<?> result = ItemLootEntry.lootTableItem(this).setWeight(this.weight).setQuality(0);
		
		for(ILootFunction.IBuilder func : functions) {
			result = result.apply(func);
		}
		result = result.when(condition);
		 
		return result;
	}
	
	public Item asItem() {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.itemName));
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
