package com.teamcqr.chocolatequestrepoured.dungeongen.lootchests;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.LootUtils;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootTable;

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
	private boolean enchant;
	@SuppressWarnings("unused")
	private boolean treasure;
	@SuppressWarnings("unused")
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
	
	public void addToTable(LootTable table) {
		LootUtils.addItemToTable(table,
				Item.getByNameOrId(this.itemName),
				this.weight,
				1 + new Random().nextInt(Reference.CONFIG_HELPER_INSTANCE.getMaxLootTablePoolRolls()),
				((float) this.weight / 100.0F),
				this.minCount,
				this.maxCount,
				this.enchant ? this.minLvl : 0,
				this.enchant ? this.maxLvl : 0,
				this.itemName);
	}
	
	public WeightedItemStack setChance(int chance) {
		if(chance != this.weight) {
			this.weight = Math.abs(chance);
		}
		return this;
	}
	
	public String getItemName() {
		return this.itemName;
	}

}
