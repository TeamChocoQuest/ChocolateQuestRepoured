package com.teamcqr.chocolatequestrepoured.dungeongen.lootchests;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class LootTableLoader {
	
	//These are all valid file names for the chests!
	String[] validFileNames = {"treasure_chest", "loot_chest", "material_chest", "food_chest", "tools_chest", "custom_1", "custom_2", "custom_3", "custom_4", "custom_5", "custom_6", "custom_7", "custom_9", "custom_10", "custom_11", "custom_12", "custom_13"}; 
	
	public void load(File config, int tableID, World world) {
		if(config != null && config.exists()) {
			if(isFileNameValid(config)) {
				ResourceLocation lootTableResFile = ELootTable.valueOf(tableID).getLootTable();
				
				Properties propFile = new Properties();
				boolean success = false;
				try {
					FileInputStream inStream = new FileInputStream(config);
					propFile.load(inStream);
					success = true;
				} catch(Exception ex) {
					System.err.println("Failed to load file " + config.getAbsolutePath());
				}
				if(success) {
					List<WeightedItemStack> items = getItemList(propFile);
					if(!items.isEmpty()) {
						JsonObject json = getJSON(items);
						//TODO: modify json file of resourcelocation
					}
				}
			}
		}
	}
	
	private JsonObject getJSON(List<WeightedItemStack> items) {
		JsonObject json = new JsonObject();
		
		JsonArray pools = new JsonArray();
		JsonObject poolOBJ = new JsonObject();
		
		JsonObject rollEntry = new JsonObject();
		//TODO: add config option for min and max rolls / chest items
		rollEntry.addProperty("min", 6);
		rollEntry.addProperty("max", 12);
		
		poolOBJ.add("rolls", rollEntry);
		
		JsonArray entries = createEntryArray(items);
		
		poolOBJ.add("entries", entries);
		
		pools.add(poolOBJ);
		
		json.add("pools", pools);
		
		return json;
	}
	
	private JsonArray createEntryArray(List<WeightedItemStack> items) {
		JsonArray entryARR = new JsonArray();
		
		for(WeightedItemStack item : items) {
			if(item != null) {
				JsonObject itemJSON = item.toJSON();
				if(itemJSON != null) {
					entryARR.add(itemJSON);
				}
			}
		}
		return entryARR;
	}
	
	private List<WeightedItemStack> getItemList(Properties propFile) {
		List<WeightedItemStack> items = new ArrayList<WeightedItemStack>();
		Enumeration<Object> fileEntries = propFile.elements();
		while(fileEntries.hasMoreElements()) {
			String entry = (String) fileEntries.nextElement();
			WeightedItemStack stack = createWeightedItemStack(entry);
			if(stack != null) {
				items.add(stack);
			}
		}
		return items;
	}
	
	private WeightedItemStack createWeightedItemStack(String entry) {
		//		  1     2       3          4          5       6        7        8        9
		//String format: ID = ITEM, DAMAGE, MIN_COUNT, MAX_COUNT, CHANCE, ENCHANT, MIN_LVL, MAX_LVL, TREASURE
		StringTokenizer tokenizer = new StringTokenizer(entry, ",");
		int tokenCount = tokenizer.countTokens();
		if(tokenCount >= 5) {
			String item = "minecraft:stone";
			int damage = 0;
			int min_count = 0;
			int max_count = 1;
			int chance = 25;
			boolean enchant = false;
			int min_lvl = 1;
			int max_lvl = 10;
			boolean treasure = false;
			
			switch(tokenCount) {
				default:
					item = ((String)tokenizer.nextElement()).trim();
					damage = Integer.parseInt(((String)tokenizer.nextElement()).trim());
					min_count = Integer.parseInt(((String)tokenizer.nextElement()).trim());
					max_count = Integer.parseInt(((String)tokenizer.nextElement()).trim());
					chance = Integer.parseInt(((String)tokenizer.nextElement()).trim());
				case 6:
					enchant = Boolean.parseBoolean(((String)tokenizer.nextElement()).trim());
					min_lvl = Integer.parseInt(((String)tokenizer.nextElement()).trim());
					max_lvl = Integer.parseInt(((String)tokenizer.nextElement()).trim());
				case 9:
					treasure = Boolean.parseBoolean(((String)tokenizer.nextElement()).trim());
					break;
			}
			WeightedItemStack itemstack = new WeightedItemStack(item, damage, min_count, max_count, chance, enchant, min_lvl, max_lvl, treasure);
			return itemstack;
		} else {
			System.err.println("Config string is invalid! Not enough arguments!");
			return null;
		}
	}
	
	private boolean isFileNameValid(File file) {
		String fileName = file.getName().replaceAll(".properties", "");
		fileName = fileName.toLowerCase();
		for(int i = 0; i < validFileNames.length; i++) {
			if(validFileNames[i].equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		return false;
	}
	
}
