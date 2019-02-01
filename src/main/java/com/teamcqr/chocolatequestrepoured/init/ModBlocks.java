package com.teamcqr.chocolatequestrepoured.init;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockDungeonBrick;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockTable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks 
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	//Dungeon Blocks
	public static final Block DUNGEONBRICK1 = new BlockDungeonBrick("dungeon_bricks_1", Material.ROCK);
	public static final Block DUNGEONBRICK2 = new BlockDungeonBrick("dungeon_bricks_2", Material.ROCK);
	public static final Block DUNGEONBRICK3 = new BlockDungeonBrick("dungeon_bricks_3", Material.ROCK);
	public static final Block DUNGEONBRICK4 = new BlockDungeonBrick("dungeon_bricks_4", Material.ROCK);
	public static final Block DUNGEONBRICK5 = new BlockDungeonBrick("dungeon_bricks_5", Material.ROCK);
	public static final Block DUNGEONBRICK6 = new BlockDungeonBrick("dungeon_bricks_6", Material.ROCK);
	public static final Block DUNGEONBRICK7 = new BlockDungeonBrick("dungeon_bricks_7", Material.ROCK);
	public static final Block DUNGEONBRICK8 = new BlockDungeonBrick("dungeon_bricks_8", Material.ROCK);
/*	public static final Block DungeonBrick9 = new BlockDungeonBrick("dungeon_brick_9", Material.ROCK);
	public static final Block DungeonBrick10 = new BlockDungeonBrick("dungeon_brick_10", Material.ROCK);
	public static final Block DungeonBrick11 = new BlockDungeonBrick("dungeon_brick_11", Material.ROCK);
	public static final Block DungeonBrick12 = new BlockDungeonBrick("dungeon_brick_12", Material.ROCK);
	public static final Block DungeonBrick13 = new BlockDungeonBrick("dungeon_brick_13", Material.ROCK);
	public static final Block DungeonBrick14 = new BlockDungeonBrick("dungeon_brick_14", Material.ROCK);
	public static final Block DungeonBrick15 = new BlockDungeonBrick("dungeon_brick_15", Material.ROCK);
	public static final Block DungeonBrick16 = new BlockDungeonBrick("dungeon_brick_16", Material.ROCK); */
	
	//Other
	public static final Block TABLE_OAK = new BlockTable("table_oak", Material.WOOD);
	public static final Block TABLE_SPRUCE = new BlockTable("table_spruce", Material.WOOD);
	public static final Block TABLE_BIRCH = new BlockTable("table_birch", Material.WOOD);
	public static final Block TABLE_JUNGLE = new BlockTable("table_jungle", Material.WOOD);
	public static final Block TABLE_ACACIA = new BlockTable("table_acacia", Material.WOOD);
	public static final Block TABLE_DARK = new BlockTable("table_dark", Material.WOOD);
}