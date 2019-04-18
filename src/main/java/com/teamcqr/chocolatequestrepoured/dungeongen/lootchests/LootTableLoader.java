package com.teamcqr.chocolatequestrepoured.dungeongen.lootchests;

import java.io.File;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

public class LootTableLoader {
	
	//These are all valid file names for the chests!
	String[] validFileNames = {"treasure_chest", "loot_chest", "material_chest", "food_chest", "tools_chest", "custom_1", "custom_2", "custom_3", "custom_4", "custom_5", "custom_6", "custom_7", "custom_9", "custom_10", "custom_11", "custom_12", "custom_13"}; 
	
	public void load(File config, int tableID, World world) {
		if(config != null && config.exists()) {
			if(isFileNameValid(config)) {
				ResourceLocation lootTableResFile = ELootTable.valueOf(tableID).getLootTable();
				
				LootTable table = world.getLootTableManager().getLootTableFromLocation(lootTableResFile);
				
				if(!world.isRemote) {
					WorldServer worldServer = (WorldServer)world;
					
					LootContext lootContext = new LootContext.Builder(worldServer).build();
					
				}
			}
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
