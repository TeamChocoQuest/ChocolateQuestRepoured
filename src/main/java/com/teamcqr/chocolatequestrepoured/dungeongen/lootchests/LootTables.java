package com.teamcqr.chocolatequestrepoured.dungeongen.lootchests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootTables {
	
	//TODO: rewrite this as an enum and add ALL the extra  loot chests --> wait for textures being uploaded...
	//TODO: place all loot chest into their own tab
	//TODO: on loot table load: load in the configs!
	
	private static final Set<ResourceLocation> CQ_LOOT_TABLES = Sets.<ResourceLocation>newHashSet();
	
	//VANILLA LOOT
	/* ID = 0*/public static final ResourceLocation LOOT_TABLE_MINESHAFT = register(LootTableList.CHESTS_ABANDONED_MINESHAFT);
	/* ID = 1*/public static final ResourceLocation LOOT_TABLE_PYRAMID = register(LootTableList.CHESTS_DESERT_PYRAMID);
	/* ID = 2*/public static final ResourceLocation LOOT_TABLE_END_CITY = register(LootTableList.CHESTS_END_CITY_TREASURE);
	/* ID = 3*/public static final ResourceLocation LOOT_TABLE_IGLOO = register(LootTableList.CHESTS_IGLOO_CHEST);
	/* ID = 4*/public static final ResourceLocation LOOT_TABLE_JUNGLE_TEMPLE = register(LootTableList.CHESTS_JUNGLE_TEMPLE);
	/* ID = 5*/public static final ResourceLocation LOOT_TABLE_JUNGLE_TEMPLE_DISPENSER = register(LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER);
	/* ID = 6*/public static final ResourceLocation LOOT_TABLE_NEHTER_FORTRESS = register(LootTableList.CHESTS_NETHER_BRIDGE);
	/* ID = 7*/public static final ResourceLocation LOOT_TABLE_VANILLA_DUNGEON = register(LootTableList.CHESTS_SIMPLE_DUNGEON);
	/* ID = 8*/public static final ResourceLocation LOOT_TABLE_BONUS_CHEST = register(LootTableList.CHESTS_SPAWN_BONUS_CHEST);
	/* ID = 9*/public static final ResourceLocation LOOT_TABLE_STRONGHOLD_CORRIDOR_CHEST = register(LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
	/* ID = 10*/public static final ResourceLocation LOOT_TABLE_STRONGHOLD_STOREROOM_CHEST = register(LootTableList.CHESTS_STRONGHOLD_CROSSING);
	/* ID = 11*/public static final ResourceLocation LOOT_TABLE_STRONGHOLD_LIBRARY_CHEST = register(LootTableList.CHESTS_STRONGHOLD_LIBRARY);
	/* ID = 12*/public static final ResourceLocation LOOT_TABLE_VILLAGE_BLACKSMITH = register(LootTableList.CHESTS_VILLAGE_BLACKSMITH);
	/* ID = 13*/public static final ResourceLocation LOOT_TABLE_WOODLAND_MANSION_CHEST = register(LootTableList.CHESTS_WOODLAND_MANSION);

	//CQ LOOT
	private static final List<ResourceLocation> CQ_DUNGEON_LOOT = new ArrayList<ResourceLocation>();
	
	/* ID = 14*/public static final ResourceLocation LOOT_TABLE_CQ_TREASURE = registerChest("treasure"); //DIAMOND
	/* ID = 15*/public static final ResourceLocation LOOT_TABLE_CQ_BOSS_LOOT = registerChest("loot"); // EMERALD
	/* ID = 16*/public static final ResourceLocation LOOT_TABLE_CQ_VALUEABLE = registerChest("custom_1"); //GOLD INGOT
	/* ID = 17*/public static final ResourceLocation LOOT_TABLE_CQ_ORE = registerChest("material"); //IRON INGOT
	/* ID = 18*/public static final ResourceLocation LOOT_TABLE_CQ_MUSIC = registerChest("custom_2"); //MUSIC DISK
	/* ID = 19*/public static final ResourceLocation LOOT_TABLE_CQ_MISC_FOOD = registerChest("custom_3"); //CAKE
	/* ID = 20*/public static final ResourceLocation LOOT_TABLE_CQ_RAW_FOOD = registerChest("custom_4"); //POTATO
	/* ID = 21*/public static final ResourceLocation LOOT_TABLE_CQ_FOOD = registerChest("food"); //PORKCHOP
	/* ID = 22*/public static final ResourceLocation LOOT_TABLE_CQ_MOB_LOOT = registerChest("custom_5"); //ROTTEN FLESH
	/* ID = 23*/public static final ResourceLocation LOOT_TABLE_CQ_ARMOR = registerChest("custom_6"); //IRON ARMOR
	/* ID = 24*/public static final ResourceLocation LOOT_TABLE_CQ_TOOLS = registerChest("tools"); //IRON PICKAXE
	/* ID = 25*/public static final ResourceLocation LOOT_TABLE_CQ_CRAP_TOOLS = registerChest("custom_7"); //WOOD PICKAXE
	/* ID = 26*/public static final ResourceLocation LOOT_TABLE_CQ_MISC_TOOLS = registerChest("custom_8"); //IRON HOE
	/* ID = 27*/public static final ResourceLocation LOOT_TABLE_CQ_CRAP_MISC_TOOLS = registerChest("custom_9"); //FISHING ROD
	/* ID = 28*/public static final ResourceLocation LOOT_TABLE_CQ_BOOKS = registerChest("custom_10"); //BOOK
	/* ID = 29*/public static final ResourceLocation LOOT_TABLE_CQ_BREWING = registerChest("custom_11"); //BLAZE POWDER
	/* ID = 30*/public static final ResourceLocation LOOT_TABLE_CQ_POTIONS = registerChest("custom_12"); //RED POTION
	/* ID = 31*/public static final ResourceLocation LOOT_TABLE_CQ_MISC = registerChest("custom_13"); //FEATHER
	
	//CQ FURNACE LOOT
	private static final List<ResourceLocation> CQ_FURNACE_LOOT = new ArrayList<ResourceLocation>();
	private static final List<ResourceLocation> CQ_LIT_FURNACE_LOOT = new ArrayList<ResourceLocation>();
	
	public static final ResourceLocation LOOT_TABLE_FURNACE_CQ_GOLDEN_APPLE = registerFurnace("golden_apple");
	public static final ResourceLocation LOOT_TABLE_LIT_FURNACE_CQ_GOLDEN_APPLE = registerLitFurnace("golden_apple");
	public static final ResourceLocation LOOT_TABLE_FURNACE_CQ_COOKED_PORK = registerFurnace("cooked_pork");
	public static final ResourceLocation LOOT_TABLE_LIT_FURNACE_CQ_COOKED_PORK = registerLitFurnace("cooked_pork");
	public static final ResourceLocation LOOT_TABLE_FURNACE_CQ_COOKED_CHICKEN = registerFurnace("cooked_chicken");
	public static final ResourceLocation LOOT_TABLE_LIT_FURNACE_CQ_COOKED_CHICKEN = registerLitFurnace("cooked_chicken");
	public static final ResourceLocation LOOT_TABLE_FURNACE_CQ_APPLE = registerFurnace("apple");
	public static final ResourceLocation LOOT_TABLE_LIT_FURNACE_CQ_APPLE = registerLitFurnace("apple");
	public static final ResourceLocation LOOT_TABLE_FURNACE_CQ_ROTTEN_FLESH = registerFurnace("rotten_flesh");
	public static final ResourceLocation LOOT_TABLE_LIT_FURNACE_CQ_ROTTEN_FLESH = registerLitFurnace("rotten_flesh");
	public static final ResourceLocation LOOT_TABLE_FURNACE_CQ_WOOD = registerFurnace("wood");
	public static final ResourceLocation LOOT_TABLE_LIT_FURNACE_CQ_WOOD = registerLitFurnace("wood");
	public static final ResourceLocation LOOT_TABLE_LIT_FURNACE_CQ_STICK = registerLitFurnace("stick");
	public static final ResourceLocation LOOT_TABLE_LIT_FURNACE_CQ_COAL = registerLitFurnace("coal");
	public static final ResourceLocation LOOT_TABLE_LIT_FURNACE_CQ_COAL_BLOCK = registerLitFurnace("coal_block");
	
	private static ResourceLocation register(String id) {
		return register(new ResourceLocation("chocolatequestrepoured", id));
	}
	private static ResourceLocation registerFurnace(String id) {
		ResourceLocation ret = register("furnace/" + id);
		if(ret != null) {
			CQ_FURNACE_LOOT.add(ret);
		}
		return ret;
	}
	private static ResourceLocation registerLitFurnace(String id) {
		ResourceLocation ret = register("furnace/lit/" + id);
		if(ret != null) {
			CQ_LIT_FURNACE_LOOT.add(ret);
		}
		return ret;
	}
	private static ResourceLocation registerChest(String id) {
		ResourceLocation ret = register("chest/" + id);
		if(ret != null) {
			CQ_DUNGEON_LOOT.add(ret);
		}
		return ret;
	}
	private static ResourceLocation register(ResourceLocation id) {
		if (CQ_LOOT_TABLES.add(id))
        {
            return id;
        }
        else
        {
            throw new IllegalArgumentException(id + " is already a registered built-in loot table");
        }
	}
	
	public static ResourceLocation getLootTableForBlock(Block block) {
		if(Block.isEqualTo(block, ModBlocks.EXPORTER_CHEST_EQUIPMENT)) {
			return getLootTableByID(24);
		}
		if(Block.isEqualTo(block, ModBlocks.EXPORTER_CHEST_FOOD)) {
			return getLootTableByID(21);
		}
		if(Block.isEqualTo(block, ModBlocks.EXPORTER_CHEST_MATERIAL)) {
			return getLootTableByID(17);
		}
		if(Block.isEqualTo(block, ModBlocks.EXPORTER_CHEST_TREASURE)) {
			return getLootTableByID(14);
		}
		return null;
	}
	
	public static ResourceLocation getLootTableByID(int id) {
		switch (id) {
		//VANILLA
		case 0:  return LOOT_TABLE_MINESHAFT;
		case 1:  return LOOT_TABLE_PYRAMID;
		case 2:  return LOOT_TABLE_END_CITY;
		case 3:  return LOOT_TABLE_IGLOO;
		case 4:  return LOOT_TABLE_JUNGLE_TEMPLE;
		case 5:  return LOOT_TABLE_JUNGLE_TEMPLE_DISPENSER;
		case 6:  return LOOT_TABLE_NEHTER_FORTRESS;
		case 7:  return LOOT_TABLE_VANILLA_DUNGEON;
		case 8:  return LOOT_TABLE_BONUS_CHEST;
		case 9:  return LOOT_TABLE_STRONGHOLD_CORRIDOR_CHEST;
		case 10: return LOOT_TABLE_STRONGHOLD_STOREROOM_CHEST;
		case 11: return LOOT_TABLE_STRONGHOLD_LIBRARY_CHEST;
		case 12: return LOOT_TABLE_VILLAGE_BLACKSMITH;
		case 13: return LOOT_TABLE_WOODLAND_MANSION_CHEST;
		
		//DUNGEONS
		case 14: return LOOT_TABLE_CQ_TREASURE;
		case 15: return LOOT_TABLE_CQ_BOSS_LOOT;
		case 16: return LOOT_TABLE_CQ_VALUEABLE;
		case 17: return LOOT_TABLE_CQ_ORE;
		case 18: return LOOT_TABLE_CQ_MUSIC;
		case 19: return LOOT_TABLE_CQ_MISC_FOOD;
		case 20: return LOOT_TABLE_CQ_RAW_FOOD;
		case 21: return LOOT_TABLE_CQ_FOOD;
		case 22: return LOOT_TABLE_CQ_MOB_LOOT;
		case 23: return LOOT_TABLE_CQ_ARMOR;
		case 24: return LOOT_TABLE_CQ_TOOLS;
		case 25: return LOOT_TABLE_CQ_CRAP_TOOLS;
		case 26: return LOOT_TABLE_CQ_MISC_TOOLS;
		case 27: return LOOT_TABLE_CQ_CRAP_MISC_TOOLS;
		case 28: return LOOT_TABLE_CQ_BOOKS;
		case 29: return LOOT_TABLE_CQ_BREWING;
		case 30: return LOOT_TABLE_CQ_POTIONS;
		case 31: return LOOT_TABLE_CQ_MISC;

		default:
			return LOOT_TABLE_VANILLA_DUNGEON;
		}
	}
}
