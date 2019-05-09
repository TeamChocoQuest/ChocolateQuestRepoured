package com.teamcqr.chocolatequestrepoured.dungeongen.lootchests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.teamcqr.chocolatequestrepoured.CQRMain;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class LootTableLoader {
	
	private static final WeightedItemStack airEntryBase = new WeightedItemStack("minecraft:air", 0, 1, 2, 100, false, 1, 2, false);
	
	//These are all valid file names for the chests!
	String[] validFileNames = {"treasure_chest", "material_chest", "food_chest", "tools_chest", "custom_1", "custom_2", "custom_3", "custom_4", "custom_5", "custom_6", "custom_7", "custom_8", "custom_9", "custom_10", "custom_11", "custom_12", "custom_13", "custom_14"}; 
	
	public void loadConfigs() {
		int files = -1;
		if(CQRMain.CQ_CHEST_FOLDER != null && CQRMain.CQ_CHEST_FOLDER.exists()) {
			files = CQRMain.CQ_CHEST_FOLDER.listFiles().length -1;
		}
		if(files > 0) {
			System.out.println("Found " + files + " loot chest configs! Loading...");
			for(File f : CQRMain.CQ_CHEST_FOLDER.listFiles()) {
				if(f.isFile()) {
					ELootTable table = null;
					
					table = getAssignedLootTable(f.getName());
					
					if(table != null) {
						System.out.println("Loading loot config " + f.getName() + "...");
						createJSONFile(f, table);
					}
				}
			}
		}
	}
	
	public void exchangeJarFiles() {
		for(ELootTable table : ELootTable.values()) {
			if(table.getID() < 4 || table.getID() > 17) {
				File jsonFile = table.getJSONFile();
				table.exchangeFileInJar(jsonFile, table.getID() > 17);
			}
		}
	}
	
	private void createJSONFile(File config, ELootTable table) {
		System.out.println("Checking existance of file " +config.getName() + "..."); 
		if(config != null && config.exists()) {
			System.out.println("File exists! Checking if name is valid...");
			if(isFileNameValid(config)) {
				System.out.println("Name is valid! Loading...");
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
						//DONE: modify json file of resourcelocation --> exchangeJarFiles() method, is done by ELootTable class, but not finished!!
						File jsonFileDir = new File(CQRMain.CQ_CHEST_FOLDER.getAbsolutePath() + "/.generatedJSON/");
						if(!jsonFileDir.exists()) {
							jsonFileDir.mkdirs();
						}
						File jsonFile = new File(jsonFileDir.getAbsolutePath(), table.getName() + ".json");
						if(jsonFile.exists()) {
							jsonFile.delete();
						}
						if(!jsonFile.exists()) {
							try {
								jsonFile.createNewFile();
							} catch (IOException e) {
								System.err.println("Failed to create JSON file for chest " + config.getName() + "!");
								e.printStackTrace();
							}
						}
						Gson gson = new Gson();
						
						FileWriter fileWriter = null;
						try {
							fileWriter = new FileWriter(jsonFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
						if(fileWriter != null) {
							JsonWriter jsonWriter = new JsonWriter(fileWriter);
							gson.toJson(json, jsonWriter);
							try {
								jsonWriter.close();
								fileWriter.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}					
					}
				}
			}
		}
	}
	
	private JsonObject getJSON(List<WeightedItemStack> items) {
		JsonObject json = new JsonObject();
		
		JsonArray pools = new JsonArray();
		for(WeightedItemStack wes : items) {
			JsonObject poolForItem = new JsonObject();
			poolForItem.addProperty("name", "CQ_Loot_" + wes.getItemName().replaceAll("minecraft:", ""));
			poolForItem.addProperty("rolls", 1);
			
			JsonArray entries = new JsonArray();
			entries.add(wes.toJSON());
			entries.add(getAirEntry(100-wes.getWeight()).toJSON());
			
			poolForItem.add("entries", entries);
			
			pools.add(poolForItem);
		}
		
		json.add("pools", pools);
		
		return json;
	}
	private WeightedItemStack getAirEntry(int chance) {
		return airEntryBase.setChance(chance);
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
			
			item = ((String)tokenizer.nextElement()).trim();
			//System.out.println("Item: " + item);
			damage = Integer.parseInt(((String)tokenizer.nextElement()).trim());
			min_count = Integer.parseInt(((String)tokenizer.nextElement()).trim());
			max_count = Integer.parseInt(((String)tokenizer.nextElement()).trim());
			chance = Integer.parseInt(((String)tokenizer.nextElement()).trim());
			
			if(tokenCount >= 6) {
				enchant = Boolean.parseBoolean(((String)tokenizer.nextElement()).trim());
				min_lvl = Integer.parseInt(((String)tokenizer.nextElement()).trim());
				max_lvl = Integer.parseInt(((String)tokenizer.nextElement()).trim());
				if(tokenCount >= 9) {
					treasure = Boolean.parseBoolean(((String)tokenizer.nextElement()).trim());
				}
			}

			WeightedItemStack itemstack = new WeightedItemStack(item, damage, min_count, max_count, chance, enchant, min_lvl, max_lvl, treasure);
			return itemstack;
		} else {
			System.err.println("Config string is invalid! Not enough arguments!");
			return null;
		}
	}
	
	private boolean isFileNameValid(File file) {
		String name = file.getName();
		name = name.replaceAll(".properties", "");
		name = name.replaceAll(".prop", "");
		name = name.toLowerCase();
		return isNameValid(name);
	}
	private boolean isNameValid(String fileName) {
		for(int i = 0; i < validFileNames.length; i++) {
			if(validFileNames[i].equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		return false;
	}
	
	private ELootTable getAssignedLootTable(String fileName) {
		fileName = fileName.replaceAll(".properties", "");
		fileName = fileName.replaceAll(".prop", "");
		fileName = fileName.toLowerCase();
		
		if(isNameValid(fileName)) {
			System.out.println("Name is valid, getting enum...");
			switch(fileName) {
			case "treasure_chest":
				return ELootTable.CQ_TREASURE;
			case "material_chest":
				return ELootTable.CQ_MATERIAL;
			case "food_chest":
				return ELootTable.CQ_FOOD;
			case "tools_chest":
				return ELootTable.CQ_EQUIPMENT;
			case "custom_1":
				return ELootTable.CQ_CUSTOM_1;
			case "custom_2":
				return ELootTable.CQ_CUSTOM_2;
			case "custom_3":
				return ELootTable.CQ_CUSTOM_3;
			case "custom_4":
				return ELootTable.CQ_CUSTOM_4;
			case "custom_5":
				return ELootTable.CQ_CUSTOM_5;
			case "custom_6":
				return ELootTable.CQ_CUSTOM_6;
			case "custom_7":
				return ELootTable.CQ_CUSTOM_7;
			case "custom_8":
				return ELootTable.CQ_CUSTOM_8;
			case "custom_9":
				return ELootTable.CQ_CUSTOM_9;
			case "custom_10":
				return ELootTable.CQ_CUSTOM_10;
			case "custom_11":
				return ELootTable.CQ_CUSTOM_11;
			case "custom_12":
				return ELootTable.CQ_CUSTOM_12;
			case "custom_13":
				return ELootTable.CQ_CUSTOM_13;
			case "custom_14":
				return ELootTable.CQ_CUSTOM_14;
			default:
				break;
			}
		}
		//System.out.println("File does not exist");
		return null;
	}
	
}
