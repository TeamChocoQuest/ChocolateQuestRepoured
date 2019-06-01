package com.teamcqr.chocolatequestrepoured.dungeongen.lootchests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public enum ELootTable {
	
	CQ_VANILLA_MINESHAFT(ModBlocks.EXPORTER_CHEST_VANILLA_MINESHAFT, 4, "vanilla_mineshaft", LootTableList.CHESTS_ABANDONED_MINESHAFT),
	CQ_VANILLA_PYRAMID(ModBlocks.EXPORTER_CHEST_VANILLA_PYRAMID, 5, "vanilla_pyramid", LootTableList.CHESTS_DESERT_PYRAMID),
	CQ_VANILLA_END_CITY(ModBlocks.EXPORTER_CHEST_VANILLA_END_CITY, 6, "vanilla_end_city", LootTableList.CHESTS_END_CITY_TREASURE),
	CQ_VANILLA_IGLOO(ModBlocks.EXPORTER_CHEST_VANILLA_IGLOO, 7, "vanilla_igloo", LootTableList.CHESTS_IGLOO_CHEST),
	CQ_VANILLA_JUNGLE(ModBlocks.EXPORTER_CHEST_VANILLA_JUNGLE, 8, "vanilla_jungle", LootTableList.CHESTS_JUNGLE_TEMPLE),
	CQ_VANILLA_JUNGLE_DISPENSER(ModBlocks.EXPORTER_CHEST_VANILLA_JUNGLE_DISPENSER, 9, "vanilla_jungle_dispenser", LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER),
	CQ_VANILLA_NETHER_FORTRESS(ModBlocks.EXPORTER_CHEST_VANILLA_NETHER, 10, "vanilla_nether", LootTableList.CHESTS_NETHER_BRIDGE),
	CQ_VANILLA_DUNGEON(ModBlocks.EXPORTER_CHEST_VANILLA_DUNGEON, 11, "vanilla_simple_dungeon", LootTableList.CHESTS_SIMPLE_DUNGEON),
	CQ_VANILLA_BONUS_CHEST(ModBlocks.EXPORTER_CHEST_VANILLA_BONUS, 12, "vanilla_bonus", LootTableList.CHESTS_SPAWN_BONUS_CHEST),
	CQ_VANILLA_STRONGHOLD_CORRIDOR(ModBlocks.EXPORTER_CHEST_VANILLA_STRONGHOLD, 13, "vanilla_stronghold_corridor", LootTableList.CHESTS_STRONGHOLD_CORRIDOR),
	CQ_VANILLA_STRONGHOLD_STOREROOM(ModBlocks.EXPORTER_CHEST_VANILLA_STRONGHOLD_STOREROOM, 14, "vanilla_stronghold_storeroom", LootTableList.CHESTS_STRONGHOLD_CROSSING),
	CQ_VANILLA_STRONGHOLD_LIBRARY(ModBlocks.EXPORTER_CHEST_VANILLA_STRONGHOLD_LIBRARY, 15, "vanilla_stronghold_library", LootTableList.CHESTS_STRONGHOLD_LIBRARY),
	CQ_VANILLA_BLACKSMITH(ModBlocks.EXPORTER_CHEST_VANILLA_BLACKSMITH, 16, "vanilla_blacksmith", LootTableList.CHESTS_VILLAGE_BLACKSMITH),
	CQ_VANILLA_WOODLAND_MANSION(ModBlocks.EXPORTER_CHEST_VANILLA_MANSION, 17, "vanilla_mansion", LootTableList.CHESTS_WOODLAND_MANSION),
	
	//TODO: LOAD LOOT TABLES
	CQ_TREASURE(ModBlocks.EXPORTER_CHEST_TREASURE, 0, "cq_treasure", registerChest("cq_treasure")),
	CQ_EQUIPMENT(ModBlocks.EXPORTER_CHEST_EQUIPMENT, 1, "cq_equipment", registerChest("cq_equipment")),
	CQ_FOOD(ModBlocks.EXPORTER_CHEST_FOOD, 2, "cq_food", registerChest("cq_food")),
	CQ_MATERIAL(ModBlocks.EXPORTER_CHEST_MATERIAL, 3, "cq_material", registerChest("cq_material")),
	
	CQ_CUSTOM_1(ModBlocks.EXPORTER_CHEST_CUSTOM_1, 18, "cq_custom_1", registerChest("custom/cq_1")),
	CQ_CUSTOM_2(ModBlocks.EXPORTER_CHEST_CUSTOM_2, 19, "cq_custom_2", registerChest("custom/cq_2")),
	CQ_CUSTOM_3(ModBlocks.EXPORTER_CHEST_CUSTOM_3, 20, "cq_custom_3", registerChest("custom/cq_3")),
	CQ_CUSTOM_4(ModBlocks.EXPORTER_CHEST_CUSTOM_4, 21, "cq_custom_4", registerChest("custom/cq_4")),
	CQ_CUSTOM_5(ModBlocks.EXPORTER_CHEST_CUSTOM_5, 22, "cq_custom_5", registerChest("custom/cq_5")),
	CQ_CUSTOM_6(ModBlocks.EXPORTER_CHEST_CUSTOM_6, 23, "cq_custom_6", registerChest("custom/cq_6")),
	CQ_CUSTOM_7(ModBlocks.EXPORTER_CHEST_CUSTOM_7, 24, "cq_custom_7", registerChest("custom/cq_7")),
	CQ_CUSTOM_8(ModBlocks.EXPORTER_CHEST_CUSTOM_8, 25, "cq_custom_8", registerChest("custom/cq_8")),
	CQ_CUSTOM_9(ModBlocks.EXPORTER_CHEST_CUSTOM_9, 26, "cq_custom_9", registerChest("custom/cq_9")),
	CQ_CUSTOM_10(ModBlocks.EXPORTER_CHEST_CUSTOM_10, 27, "cq_custom_10", registerChest("custom/cq_10")),
	CQ_CUSTOM_11(ModBlocks.EXPORTER_CHEST_CUSTOM_11, 28, "cq_custom_11", registerChest("custom/cq_11")),
	CQ_CUSTOM_12(ModBlocks.EXPORTER_CHEST_CUSTOM_12, 29, "cq_custom_12", registerChest("custom/cq_12")),
	CQ_CUSTOM_13(ModBlocks.EXPORTER_CHEST_CUSTOM_13, 30, "cq_custom_13", registerChest("custom/cq_13")),
	CQ_CUSTOM_14(ModBlocks.EXPORTER_CHEST_CUSTOM_14, 31, "cq_custom_14", registerChest("custom/cq_14"));
	
	//CQ used:
	//treasure -->diamond
	//material/ores --> iron ingot
	//food --> porkchop
	//tools/weapons/armor --> pickaxe iron
	
	
	private Block block;
	private int ID;
	private String name;
	private ResourceLocation resourceLocation;
	private LootTable loottable;
	
	ELootTable(Block block, int id, String name, ResourceLocation loottable) {
		this.block = block;
		this.ID = id;
		this.name = name;
		this.resourceLocation = loottable;
		this.loottable = null;
	}
	
	public ResourceLocation getLootTable() {
		return this.resourceLocation;
	}
	public String getName() {
		return this.name;
	}
	public int getID() {
		return this.ID;
	}
	public Block getAssignedExporterBlock() {
		return this.block;
	}
	
	public static ELootTable valueOf(Block b) {
		if(DungeonGenUtils.isLootChest(b)) {
			for(ELootTable elt : ELootTable.values()) {
				if(Block.isEqualTo(b, elt.block)) {
					return elt;
				}
			}
		}
		return null;
	}
	
	public static ELootTable valueOf(int ID) {
		if(0 <= ID && ID < ELootTable.values().length) {
			for(ELootTable elt : ELootTable.values()) {
				if(elt.ID == ID) {
					return elt;
				}
			}
		}
		return null;
	}
	
	public static ELootTable valueOf(ResourceLocation resLoc) {
		if(resLoc != null && resLoc.getResourceDomain().equalsIgnoreCase("cqrepoured")) {
			for(ELootTable elt : ELootTable.values()) {
				if(elt.getLootTable().equals(resLoc)) {
					return elt;
				}
			}
		}
		return null;
	}
	
	private static ResourceLocation registerChest(String id) {
		ResourceLocation ret = register("chest/" + id);
		if(ret != null) {
			CQRMain.CQ_DUNGEON_LOOT.add(ret);
		}
		return ret;
	}
	private static ResourceLocation register(String id) {
		return register(new ResourceLocation("cqrepoured", id));
	}
	private static ResourceLocation register(ResourceLocation id) {
		if(CQRMain.CQ_LOOT_TABLES == null) {
			CQRMain.CQ_LOOT_TABLES = new ArrayList<ResourceLocation>();
		}
		if (id != null && CQRMain.CQ_LOOT_TABLES != null && CQRMain.CQ_LOOT_TABLES.add(id))
        {
            return id;
        }
        else
        {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
	}
	
	public void exchangeFileInJar(File newFile, boolean isCustomChest) {
		if(newFile != null && newFile.exists() && newFile.isFile()) {
			System.err.println("NYI: We need to find a way to either override the files in the mod's jar OR to generate loot tables from JSON files and override them when they are loaded OR we create the loot table object with the json file and use it instead");
		}
	}
	
	public File getJSONFile() {
		File file = null;
		
		File jsonFileDir = new File(CQRMain.CQ_CHEST_FOLDER.getAbsolutePath() + "/.generatedJSON/");
		if(!jsonFileDir.exists()) {
			jsonFileDir.mkdirs();
		}
		
		file = new File(jsonFileDir.getAbsolutePath(), getName() + ".json");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Failed to create JSON file for chest " + getName() + "!");
				e.printStackTrace();
			}
		}
		
		return file;
	}

	public LootTable getLoottable() {
		return loottable;
	}

	public void setLoottable(LootTable loottable) {
		this.loottable = loottable;
	}
	
	static ELootTable getAssignedLootTable(String fileName) {
		fileName = fileName.replaceAll(".properties", "");
		fileName = fileName.replaceAll(".prop", "");
		fileName = fileName.toLowerCase();
		
		if(LootTableLoader.isNameValid(fileName)) {
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
		return null;
	}

}
