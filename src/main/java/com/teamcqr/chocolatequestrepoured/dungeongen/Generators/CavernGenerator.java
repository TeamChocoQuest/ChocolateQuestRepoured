package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CavernDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.lootchests.ELootTable;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class CavernGenerator implements IDungeonGenerator {
	
	public CavernGenerator() {};
	
	public CavernGenerator(CavernDungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	private CavernDungeon dungeon;
	private Set<BlockPos> airBlocks = Sets.newHashSet();
	private Set<BlockPos> floorBlocks = Sets.newHashSet();
	
	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		for(BlockPos bp : this.airBlocks) {
			if(!Block.isEqualTo(world.getBlockState(bp).getBlock(), this.dungeon.getAirBlock())) {
				world.setBlockState(bp, this.dungeon.getAirBlock().getDefaultState());
			}
		}
		for(BlockPos bp : this.floorBlocks) {
			if(!Block.isEqualTo(world.getBlockState(bp).getBlock(), this.dungeon.getFloorBlock())) {
				world.setBlockState(bp, this.dungeon.getFloorBlock().getDefaultState());
			}
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		System.out.println("Generated " + this.dungeon.getDungeonName() + " at X: " + x + "  Y: " + y + "  Z: " + z);
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		BlockPos start = new BlockPos(x, y, z);
		world.setBlockState(start, Blocks.CHEST.getDefaultState());
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(start);
		switch(new Random().nextInt(4)) {
			case 0: 
				chest.setLootTable(ELootTable.CQ_VANILLA_MINESHAFT.getLootTable(), world.getSeed());
				break;
			case 1: 
				chest.setLootTable(ELootTable.CQ_VANILLA_DUNGEON.getLootTable(), world.getSeed());
				break;
			case 2: 
				chest.setLootTable(ELootTable.CQ_VANILLA_STRONGHOLD_CORRIDOR.getLootTable(), world.getSeed());
				break;
			case 3: 
				chest.setLootTable(ELootTable.CQ_VANILLA_BLACKSMITH.getLootTable(), world.getSeed());
				break;
		}
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

}
