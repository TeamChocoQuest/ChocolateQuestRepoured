package com.teamcqr.chocolatequestrepoured.structuregen.lootchests;

import java.util.ArrayList;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public enum ELootTable {
	
	CQ_VANILLA_MINESHAFT(ModBlocks.EXPORTER_CHEST_VANILLA_MINESHAFT, 4, "vanilla_mineshaft", LootTableList.CHESTS_ABANDONED_MINESHAFT, false),
	CQ_VANILLA_PYRAMID(ModBlocks.EXPORTER_CHEST_VANILLA_PYRAMID, 5, "vanilla_pyramid", LootTableList.CHESTS_DESERT_PYRAMID, false),
	CQ_VANILLA_END_CITY(ModBlocks.EXPORTER_CHEST_VANILLA_END_CITY, 6, "vanilla_end_city", LootTableList.CHESTS_END_CITY_TREASURE, false),
	CQ_VANILLA_IGLOO(ModBlocks.EXPORTER_CHEST_VANILLA_IGLOO, 7, "vanilla_igloo", LootTableList.CHESTS_IGLOO_CHEST, false),
	CQ_VANILLA_JUNGLE(ModBlocks.EXPORTER_CHEST_VANILLA_JUNGLE, 8, "vanilla_jungle", LootTableList.CHESTS_JUNGLE_TEMPLE, false),
	CQ_VANILLA_JUNGLE_DISPENSER(ModBlocks.EXPORTER_CHEST_VANILLA_JUNGLE_DISPENSER, 9, "vanilla_jungle_dispenser", LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER, false),
	CQ_VANILLA_NETHER_FORTRESS(ModBlocks.EXPORTER_CHEST_VANILLA_NETHER, 10, "vanilla_nether", LootTableList.CHESTS_NETHER_BRIDGE, false),
	CQ_VANILLA_DUNGEON(ModBlocks.EXPORTER_CHEST_VANILLA_DUNGEON, 11, "vanilla_simple_dungeon", LootTableList.CHESTS_SIMPLE_DUNGEON, false),
	CQ_VANILLA_BONUS_CHEST(ModBlocks.EXPORTER_CHEST_VANILLA_BONUS, 12, "vanilla_bonus", LootTableList.CHESTS_SPAWN_BONUS_CHEST, false),
	CQ_VANILLA_STRONGHOLD_CORRIDOR(ModBlocks.EXPORTER_CHEST_VANILLA_STRONGHOLD, 13, "vanilla_stronghold_corridor", LootTableList.CHESTS_STRONGHOLD_CORRIDOR, false),
	CQ_VANILLA_STRONGHOLD_STOREROOM(ModBlocks.EXPORTER_CHEST_VANILLA_STRONGHOLD_STOREROOM, 14, "vanilla_stronghold_storeroom", LootTableList.CHESTS_STRONGHOLD_CROSSING, false),
	CQ_VANILLA_STRONGHOLD_LIBRARY(ModBlocks.EXPORTER_CHEST_VANILLA_STRONGHOLD_LIBRARY, 15, "vanilla_stronghold_library", LootTableList.CHESTS_STRONGHOLD_LIBRARY, false),
	CQ_VANILLA_BLACKSMITH(ModBlocks.EXPORTER_CHEST_VANILLA_BLACKSMITH, 16, "vanilla_blacksmith", LootTableList.CHESTS_VILLAGE_BLACKSMITH, false),
	CQ_VANILLA_WOODLAND_MANSION(ModBlocks.EXPORTER_CHEST_VANILLA_MANSION, 17, "vanilla_mansion", LootTableList.CHESTS_WOODLAND_MANSION, false),
	
	//DONE: LOAD LOOT TABLES
	CQ_TREASURE(ModBlocks.EXPORTER_CHEST_TREASURE, 0, "cq_treasure", registerChest("cq_treasure"), false),
	CQ_EQUIPMENT(ModBlocks.EXPORTER_CHEST_EQUIPMENT, 1, "cq_equipment", registerChest("cq_equipment"), false),
	CQ_FOOD(ModBlocks.EXPORTER_CHEST_FOOD, 2, "cq_food", registerChest("cq_food"), false),
	CQ_MATERIAL(ModBlocks.EXPORTER_CHEST_MATERIAL, 3, "cq_material", registerChest("cq_material"), false),
	
	CQ_CUSTOM_1(ModBlocks.EXPORTER_CHEST_CUSTOM_1, 18, "cq_custom_1", registerChest("custom/cq_1"), true),
	CQ_CUSTOM_2(ModBlocks.EXPORTER_CHEST_CUSTOM_2, 19, "cq_custom_2", registerChest("custom/cq_2"), true),
	CQ_CUSTOM_3(ModBlocks.EXPORTER_CHEST_CUSTOM_3, 20, "cq_custom_3", registerChest("custom/cq_3"), true),
	CQ_CUSTOM_4(ModBlocks.EXPORTER_CHEST_CUSTOM_4, 21, "cq_custom_4", registerChest("custom/cq_4"), true),
	CQ_CUSTOM_5(ModBlocks.EXPORTER_CHEST_CUSTOM_5, 22, "cq_custom_5", registerChest("custom/cq_5"), true),
	CQ_CUSTOM_6(ModBlocks.EXPORTER_CHEST_CUSTOM_6, 23, "cq_custom_6", registerChest("custom/cq_6"), true),
	CQ_CUSTOM_7(ModBlocks.EXPORTER_CHEST_CUSTOM_7, 24, "cq_custom_7", registerChest("custom/cq_7"), true),
	CQ_CUSTOM_8(ModBlocks.EXPORTER_CHEST_CUSTOM_8, 25, "cq_custom_8", registerChest("custom/cq_8"), true),
	CQ_CUSTOM_9(ModBlocks.EXPORTER_CHEST_CUSTOM_9, 26, "cq_custom_9", registerChest("custom/cq_9"), true),
	CQ_CUSTOM_10(ModBlocks.EXPORTER_CHEST_CUSTOM_10, 27, "cq_custom_10", registerChest("custom/cq_10"), true),
	CQ_CUSTOM_11(ModBlocks.EXPORTER_CHEST_CUSTOM_11, 28, "cq_custom_11", registerChest("custom/cq_11"), true),
	CQ_CUSTOM_12(ModBlocks.EXPORTER_CHEST_CUSTOM_12, 29, "cq_custom_12", registerChest("custom/cq_12"), true),
	CQ_CUSTOM_13(ModBlocks.EXPORTER_CHEST_CUSTOM_13, 30, "cq_custom_13", registerChest("custom/cq_13"), true),
	CQ_CUSTOM_14(ModBlocks.EXPORTER_CHEST_CUSTOM_14, 31, "cq_custom_14", registerChest("custom/cq_14"), true);
	
	//CQ used:
	//treasure -->diamond
	//material/ores --> iron ingot
	//food --> porkchop
	//tools/weapons/armor --> pickaxe iron
	
	
	private Block block;
	private int ID;
	private String name;
	private ResourceLocation resourceLocation;
	private boolean isCustomChest;
	
	ELootTable(Block block, int id, String name, ResourceLocation loottable, boolean customChest) {
		this.block = block;
		this.ID = id;
		this.name = name;
		this.resourceLocation = loottable;
		//if this is a new CQ loottable, we need to add it to the LootTableList so that MC/Forge know about it
		if(id < 4 || id > 17) {
			LootTableList.register(this.resourceLocation);
		}
		this.isCustomChest = customChest;
	}
	
	public ResourceLocation getResourceLocation() {
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
	public boolean isCustomChest() {
		return this.isCustomChest;
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
				if(elt.getResourceLocation().equals(resLoc)) {
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
	
	static String getAssignedFileName(ELootTable table) {
		switch(table) {
		case CQ_CUSTOM_1:
			return "custom_1" + ".prop";
		case CQ_CUSTOM_10:
			return "custom_10" + ".prop";
		case CQ_CUSTOM_11:
			return "custom_11" + ".prop";
		case CQ_CUSTOM_12:
			return "custom_12" + ".prop";
		case CQ_CUSTOM_13:
			return "custom_13" + ".prop";
		case CQ_CUSTOM_14:
			return "custom_14" + ".prop";
		case CQ_CUSTOM_2:
			return "custom_2" + ".prop";
		case CQ_CUSTOM_3:
			return "custom_3" + ".prop";
		case CQ_CUSTOM_4:
			return "custom_4" + ".prop";
		case CQ_CUSTOM_5:
			return "custom_5" + ".prop";
		case CQ_CUSTOM_6:
			return "custom_6" + ".prop";
		case CQ_CUSTOM_7:
			return "custom_7" + ".prop";
		case CQ_CUSTOM_8:
			return "custom_8" + ".prop";
		case CQ_CUSTOM_9:
			return "custom_9" + ".prop";
		case CQ_EQUIPMENT:
			return "tools_chest" + ".prop";
		case CQ_FOOD:
			return "food_chest" + ".prop";
		case CQ_MATERIAL:
			return "material_chest" + ".prop";
		case CQ_TREASURE:
			return "treasure_chest" + ".prop";
		default:
			return null;
		}
	}

}
