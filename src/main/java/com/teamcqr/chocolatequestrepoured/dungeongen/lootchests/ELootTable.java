package com.teamcqr.chocolatequestrepoured.dungeongen.lootchests;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;

public enum ELootTable {
	
	CQ_VANILLA_MINESHAFT(ModBlocks.EXPORTER_CHEST_VANILLA_MINESHAFT, 0, "vanilla_mineshaft", LootTableList.CHESTS_ABANDONED_MINESHAFT),
	CQ_VANILLA_PYRAMID(ModBlocks.EXPORTER_CHEST_VANILLA_PYRAMID, 1, "vanilla_pyramid", LootTableList.CHESTS_DESERT_PYRAMID),
	CQ_VANILLA_END_CITY(ModBlocks.EXPORTER_CHEST_VANILLA_END_CITY, 2, "vanilla_end_city", LootTableList.CHESTS_END_CITY_TREASURE),
	CQ_VANILLA_IGLOO(ModBlocks.EXPORTER_CHEST_VANILLA_IGLOO, 3, "vanilla_igloo", LootTableList.CHESTS_IGLOO_CHEST),
	CQ_VANILLA_JUNGLE(ModBlocks.EXPORTER_CHEST_VANILLA_JUNGLE, 4, "vanilla_jungle", LootTableList.CHESTS_JUNGLE_TEMPLE),
	CQ_VANILLA_JUNGLE_DISPENSER(ModBlocks.EXPORTER_CHEST_VANILLA_JUNGLE_DISPENSER, 5, "vanilla_jungle_dispenser", LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER),
	CQ_VANILLA_NETHER_FORTRESS(ModBlocks.EXPORTER_CHEST_VANILLA_NETHER, 6, "vanilla_nether", LootTableList.CHESTS_NETHER_BRIDGE),
	CQ_VANILLA_DUNGEON(ModBlocks.EXPORTER_CHEST_VANILLA_DUNGEON, 7, "vanilla_simple_dungeon", LootTableList.CHESTS_SIMPLE_DUNGEON),
	CQ_VANILLA_BONUS_CHEST(ModBlocks.EXPORTER_CHEST_VANILLA_BONUS, 8, "vanilla_bonus", LootTableList.CHESTS_SPAWN_BONUS_CHEST),
	CQ_VANILLA_STRONGHOLD_CORRIDOR(ModBlocks.EXPORTER_CHEST_VANILLA_STRONGHOLD, 9, "vanilla_stronghold_corridor", LootTableList.CHESTS_STRONGHOLD_CORRIDOR),
	CQ_VANILLA_STRONGHOLD_STOREROOM(ModBlocks.EXPORTER_CHEST_VANILLA_STRONGHOLD_STOREROOM, 10, "vanilla_stronghold_storeroom", LootTableList.CHESTS_STRONGHOLD_CROSSING),
	CQ_VANILLA_STRONGHOLD_LIBRARY(ModBlocks.EXPORTER_CHEST_VANILLA_STRONGHOLD_LIBRARY, 11, "vanilla_stronghold_library", LootTableList.CHESTS_STRONGHOLD_LIBRARY),
	CQ_VANILLA_BLACKSMITH(ModBlocks.EXPORTER_CHEST_VANILLA_BLACKSMITH, 12, "vanilla_blacksmith", LootTableList.CHESTS_VILLAGE_BLACKSMITH),
	CQ_VANILLA_WOODLAND_MANSION(ModBlocks.EXPORTER_CHEST_VANILLA_MANSION, 13, "vanilla_mansion", LootTableList.CHESTS_WOODLAND_MANSION);
	
	//CQ used:
	//treasure ->diamond
	//loot - boss --> emerald
	//material --> iron ingot
	//food --> porkchop
	//tools --> pickaxe iron
	
	
	private Block block;
	private int ID;
	private String name;
	private ResourceLocation loottable;
	
	ELootTable(Block block, int id, String name, ResourceLocation loottable) {
		this.block = block;
		this.ID = id;
		this.name = name;
		this.loottable = loottable;
	}
	
	public ResourceLocation getLootTable() {
		return this.loottable;
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
	
	/*private static ResourceLocation registerChest(String id) {
		ResourceLocation ret = register("chest/" + id);
		/*if(ret != null) {
			CQ_DUNGEON_LOOT.add(ret);
		}*/
		//return ret;
//	}
	//private static ResourceLocation register(String id) {
		//return register(new ResourceLocation("chocolatequestrepoured", id));
	//}
	/*private static ResourceLocation register(ResourceLocation id) {
		if (CQ_LOOT_TABLES.add(id))
        {
            return id;
        }
        else
        {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
	}*/

}
