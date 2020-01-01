package com.teamcqr.chocolatequestrepoured.structuregen.lootchests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.teamcqr.chocolatequestrepoured.CQRMain;

import net.minecraft.world.storage.loot.LootTable;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class LootTableLoader {
	
	//private static final WeightedItemStack airEntryBase = new WeightedItemStack("minecraft:air", 0, 1, 2, 100, false, 1, 2, false);
	
	//These are all valid file names for the chests!
	final static String[] validFileNames = {"treasure_chest", "material_chest", "food_chest", "tools_chest", "custom_1", "custom_2", "custom_3", "custom_4", "custom_5", "custom_6", "custom_7", "custom_8", "custom_9", "custom_10", "custom_11", "custom_12", "custom_13", "custom_14"}; 
	
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
					
					table = ELootTable.getAssignedLootTable(f.getName());
					
					if(table != null) {
						System.out.println("Loading loot config " + f.getName() + "...");
					}
				}
			}
		}
	}
	
	private static List<WeightedItemStack> getItemList(Properties propFile) {
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
	
	private static WeightedItemStack createWeightedItemStack(String entry) {
		//		 				 1     2       3          4          5       6        7        8        9
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

	static boolean isNameValid(String fileName) {
		for(int i = 0; i < validFileNames.length; i++) {
			if(validFileNames[i].equalsIgnoreCase(fileName)) {
				return true;
			}
		}
		return false;
	}
	
	public static void fillLootTable(ELootTable whatTable, LootTable lootTable) {
		Properties propFile = null;
		
		File file = null;
		
		try {
			file = new File(CQRMain.CQ_CHEST_FOLDER.getAbsolutePath(), ELootTable.getAssignedFileName(whatTable));
		} catch(Exception ex) {
			file = null;
			ex.printStackTrace();
		}
		if(file != null && file.exists()) {
			propFile = new Properties();
			
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				propFile.load(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				propFile = null;
				file = null;
				fis = null;
			} catch (IOException e) {
				e.printStackTrace();
				propFile = null;
				file = null;
				fis = null;
			}
			
			if(propFile != null) {
				List<WeightedItemStack> items = getItemList(propFile);
				
				for(WeightedItemStack wis : items) {
					wis.addToTable(lootTable);
				}
			}
		}
	}
	
}
