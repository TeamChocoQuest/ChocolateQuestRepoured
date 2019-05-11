package com.teamcqr.chocolatequestrepoured.dungeongen.lootchests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
	
	public JsonObject toJSON() {
		JsonObject jsonObj = null;
		
		try {
			jsonObj = new JsonObject();
			jsonObj.addProperty("type", "item");
			jsonObj.addProperty("name", this.itemName);
			jsonObj.addProperty("weight", this.weight);
			
			JsonArray functions = new JsonArray();
			
			JsonObject countOBJ = new JsonObject();
			countOBJ.addProperty("function", "set_count");
			JsonObject countPROP = new JsonObject();
			countPROP.addProperty("min", this.minCount);
			countPROP.addProperty("max", this.maxCount);
			countOBJ.add("count", countPROP);
			
			functions.add(countOBJ);
			
			if(this.enchant) {
				JsonObject enchantOBJ = new JsonObject();
				enchantOBJ.addProperty("function", "enchant_with_levels");
				enchantOBJ.addProperty("treasure", this.treasure);
				JsonObject levelOBJ = new JsonObject();
				levelOBJ.addProperty("min", this.minLvl);
				levelOBJ.addProperty("max", this.maxLvl);
				enchantOBJ.add("levels", levelOBJ);
				
				functions.add(enchantOBJ);
			}
			if(this.damage > 0) {
				JsonObject damgOBJ = new JsonObject();
				damgOBJ.addProperty("function", "set_data");
				damgOBJ.addProperty("data", this.damage);
				
				functions.add(damgOBJ);
			}
			jsonObj.add("functions", functions);
		} catch(Exception ex) {
			System.out.println("Failed to create lootentry!");
		}
		
		return jsonObj;
	}
	public int getWeight() {
		return this.weight;
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
