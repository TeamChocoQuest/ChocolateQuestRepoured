package com.teamcqr.chocolatequestrepoured.init;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.init.base.BlockBase;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockDungeonBrick;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockNull;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockPillarDungeonBrick;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockTable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks 
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
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
	public static final Block EXPORTER = new BlockBase("exporter", Material.WOOD);
	public static final Block NULL_BLOCK = new BlockNull("null_block", Material.GLASS, true);  //#TODO implement one click deactivating
	
	//Loot Chests
	public static final Block EXPORTER_CHEST_VALUABLE = new BlockBase("exporter_chest_valuable", Material.WOOD);
	public static final Block EXPORTER_CHEST_FOOD = new BlockBase("exporter_chest_food", Material.WOOD);
	public static final Block EXPORTER_CHEST_EQUIPMENT = new BlockBase("exporter_chest_equipment", Material.WOOD);
	public static final Block EXPORTER_CHEST_UTILITY = new BlockBase("exporter_chest_utility", Material.WOOD);
}