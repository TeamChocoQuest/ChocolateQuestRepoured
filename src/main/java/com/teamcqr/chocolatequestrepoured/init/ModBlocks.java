package com.teamcqr.chocolatequestrepoured.init;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.init.base.BlockExporterChestBase;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockDungeonBrick;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockExporter;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockNull;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockPillarDungeonBrick;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockTable;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockUnlitTorch;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks 
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	public static final List<Block> LOOT_CHEST_BLOCKS = new ArrayList<Block>();
	
	//Dungeon Blocks
	//Andesite
	public static final Block ANDESITE_CARVED = new BlockDungeonBrick("andesite_carved", Material.ROCK);
	public static final Block ANDESITE_PILLAR = new BlockPillarDungeonBrick("andesite_pillar", Material.ROCK);
	public static final Block ANDESITE_CUBE = new BlockDungeonBrick("andesite_cube", Material.ROCK);
	public static final Block ANDESITE_SCALE = new BlockDungeonBrick("andesite_scale", Material.ROCK);
	public static final Block ANDESITE_SQUARE = new BlockDungeonBrick("andesite_square", Material.ROCK);
	public static final Block ANDESIDE_BRICK_SMALL = new BlockDungeonBrick("andesite_small", Material.ROCK);
	public static final Block ANDESITE_BRICK_LARGE = new BlockDungeonBrick("andesite_large", Material.ROCK);
	
	//Diorite
	public static final Block DIORITE_CARVED = new BlockDungeonBrick("diorite_carved", Material.ROCK);
	public static final Block DIORITE_PILLAR = new BlockPillarDungeonBrick("diorite_pillar", Material.ROCK);
	public static final Block DIORITE_CUBE = new BlockDungeonBrick("diorite_cube", Material.ROCK);
	public static final Block DIORITE_SCALE = new BlockDungeonBrick("diorite_scale", Material.ROCK);
	public static final Block DIORITE_SQUARE = new BlockDungeonBrick("diorite_square", Material.ROCK);
	public static final Block DIORITE_BRICK_SMALL = new BlockDungeonBrick("diorite_small", Material.ROCK);
	public static final Block DIORITE_BRICK_LARGE = new BlockDungeonBrick("diorite_large", Material.ROCK);
	
	//Granite
	public static final Block GRANITE_CARVED = new BlockDungeonBrick("granite_carved", Material.ROCK);
	public static final Block GRANITE_PILLAR = new BlockPillarDungeonBrick("granite_pillar", Material.ROCK);
	public static final Block GRANITE_CUBE = new BlockDungeonBrick("granite_cube", Material.ROCK);
	public static final Block GRANITE_SCALE = new BlockDungeonBrick("granite_scale", Material.ROCK);
	public static final Block GRANITE_SQUARE = new BlockDungeonBrick("granite_square", Material.ROCK);
	public static final Block GRANITE_BRICK_SMALL = new BlockDungeonBrick("granite_small", Material.ROCK);
	public static final Block GRANITE_BRICK_LARGE = new BlockDungeonBrick("granite_large", Material.ROCK);
	
	//Prismarine
	public static final Block PRISMARINE_CARVED = new BlockDungeonBrick("prismarine_carved", Material.ROCK);
	public static final Block PRISMARINE_PILLAR = new BlockPillarDungeonBrick("prismarine_pillar", Material.ROCK);
	public static final Block PRISMARINE_CUBE = new BlockDungeonBrick("prismarine_cube", Material.ROCK);
	public static final Block PRISMARINE_SQUARE = new BlockDungeonBrick("prismarine_square", Material.ROCK);
	public static final Block PRISMARINE_BRICK_SMALL = new BlockDungeonBrick("prismarine_small", Material.ROCK);
	public static final Block PRISMARINE_BRICK_LARGE = new BlockDungeonBrick("prismarine_large", Material.ROCK);
	
	//Endstone
	public static final Block ENDSTONE_CARVED = new BlockDungeonBrick("endstone_carved", Material.ROCK);
	public static final Block ENDSTONE_PILLAR = new BlockPillarDungeonBrick("endstone_pillar", Material.ROCK);
	public static final Block ENDSTONE_CUBE = new BlockDungeonBrick("endstone_cube", Material.ROCK);
	public static final Block ENDSTONE_SCALE = new BlockDungeonBrick("endstone_scale", Material.ROCK);
	public static final Block ENDSTONE_SQUARE = new BlockDungeonBrick("endstone_square", Material.ROCK);
	public static final Block ENDSTONE_BRICK_SMALL = new BlockDungeonBrick("endstone_small", Material.ROCK);
	
	//Purpur
	public static final Block PURPUR_CARVED = new BlockDungeonBrick("purpur_carved", Material.ROCK);
	public static final Block PURPUR_CUBE = new BlockDungeonBrick("purpur_cube", Material.ROCK);
	public static final Block PURPUR_SCALE = new BlockDungeonBrick("purpur_scale", Material.ROCK);
	public static final Block PURPUR_BRICK_SMALL = new BlockDungeonBrick("purpur_small", Material.ROCK);
	public static final Block PURPUR_BRICK_LARGE = new BlockDungeonBrick("purpur_large", Material.ROCK);
	
	//Red Netherbrick
	public static final Block RED_NETHERBRICK_CARVED = new BlockDungeonBrick("red_netherbrick_carved", Material.ROCK);
	public static final Block RED_NETHERBRICK_PILLAR = new BlockPillarDungeonBrick("red_netherbrick_pillar", Material.ROCK);
	public static final Block RED_NETHERBRICK_CUBE = new BlockDungeonBrick("red_netherbrick_cube", Material.ROCK);
	public static final Block RED_NETHERBRICK_SCALE = new BlockDungeonBrick("red_netherbrick_scale", Material.ROCK);
	public static final Block RED_NETHERBRICK_SQUARE = new BlockDungeonBrick("red_netherbrick_square", Material.ROCK);
	public static final Block RED_NETHERBRICK_BRICK_LARGE = new BlockDungeonBrick("red_netherbrick_large", Material.ROCK);
	
	//Stone
	public static final Block STONE_PILLAR = new BlockPillarDungeonBrick("stone_pillar", Material.ROCK);
	public static final Block STONE_CUBE = new BlockDungeonBrick("stone_cube", Material.ROCK);
	public static final Block STONE_SCALE = new BlockDungeonBrick("stone_scale", Material.ROCK);
	public static final Block STONE_SQUARE = new BlockDungeonBrick("stone_square", Material.ROCK);
	public static final Block STONE_BRICK_SMALL = new BlockDungeonBrick("stone_small", Material.ROCK);
	
	//Other
	public static final Block TABLE_OAK = new BlockTable("table_oak", Material.WOOD);
	public static final Block TABLE_SPRUCE = new BlockTable("table_spruce", Material.WOOD);
	public static final Block TABLE_BIRCH = new BlockTable("table_birch", Material.WOOD);
	public static final Block TABLE_JUNGLE = new BlockTable("table_jungle", Material.WOOD);
	public static final Block TABLE_ACACIA = new BlockTable("table_acacia", Material.WOOD);
	public static final Block TABLE_DARK = new BlockTable("table_dark", Material.WOOD);
	
	//Utility
	public static final Block EXPORTER = new BlockExporter("exporter", Material.ANVIL);
	public static final Block UNLIT_TORCH = new BlockUnlitTorch("unlit_torch", Material.WOOD);
	public static final Block NULL_BLOCK = new BlockNull("null_block", Material.GLASS, true);  //#TODO implement one click deactivating
	
	//Loot Chests
	//standard
	public static final Block EXPORTER_CHEST_TREASURE = new BlockExporterChestBase("exporter_chest_valuable", Material.WOOD);
	public static final Block EXPORTER_CHEST_FOOD = new BlockExporterChestBase("exporter_chest_food", Material.WOOD);
	public static final Block EXPORTER_CHEST_EQUIPMENT = new BlockExporterChestBase("exporter_chest_equipment", Material.WOOD);
	public static final Block EXPORTER_CHEST_MATERIAL = new BlockExporterChestBase("exporter_chest_utility", Material.WOOD);
	//Vanilla
	public static final Block EXPORTER_CHEST_VANILLA_BLACKSMITH = new BlockExporterChestBase("exporter_chest_vanilla_blacksmith", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_BONUS = new BlockExporterChestBase("exporter_chest_vanilla_bonus", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_DUNGEON = new BlockExporterChestBase("exporter_chest_vanilla_dungeon", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_END_CITY = new BlockExporterChestBase("exporter_chest_vanilla_end_city", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_IGLOO = new BlockExporterChestBase("exporter_chest_vanilla_igloo", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_JUNGLE = new BlockExporterChestBase("exporter_chest_vanilla_jungle", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_JUNGLE_DISPENSER = new BlockExporterChestBase("exporter_chest_vanilla_jungle_dispenser", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_MANSION = new BlockExporterChestBase("exporter_chest_vanilla_mansion", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_MINESHAFT = new BlockExporterChestBase("exporter_chest_vanilla_mineshaft", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_NETHER = new BlockExporterChestBase("exporter_chest_vanilla_nether", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_PYRAMID = new BlockExporterChestBase("exporter_chest_vanilla_pyramid", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_STRONGHOLD = new BlockExporterChestBase("exporter_chest_vanilla_stronghold", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_STRONGHOLD_LIBRARY = new BlockExporterChestBase("exporter_chest_vanilla_stronghold_library", Material.WOOD);
	public static final Block EXPORTER_CHEST_VANILLA_STRONGHOLD_STOREROOM = new BlockExporterChestBase("exporter_chest_vanilla_stronghold_storeroom", Material.WOOD);
	//custom / user
	public static final Block EXPORTER_CHEST_CUSTOM_1 = new BlockExporterChestBase("exporter_chest_custom_1", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_2 = new BlockExporterChestBase("exporter_chest_custom_2", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_3 = new BlockExporterChestBase("exporter_chest_custom_3", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_4 = new BlockExporterChestBase("exporter_chest_custom_4", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_5 = new BlockExporterChestBase("exporter_chest_custom_5", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_6 = new BlockExporterChestBase("exporter_chest_custom_6", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_7 = new BlockExporterChestBase("exporter_chest_custom_7", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_8 = new BlockExporterChestBase("exporter_chest_custom_8", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_9 = new BlockExporterChestBase("exporter_chest_custom_9", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_10 = new BlockExporterChestBase("exporter_chest_custom_10", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_11 = new BlockExporterChestBase("exporter_chest_custom_11", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_12 = new BlockExporterChestBase("exporter_chest_custom_12", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_13 = new BlockExporterChestBase("exporter_chest_custom_13", Material.WOOD);
	public static final Block EXPORTER_CHEST_CUSTOM_14 = new BlockExporterChestBase("exporter_chest_custom_14", Material.WOOD);
	
}