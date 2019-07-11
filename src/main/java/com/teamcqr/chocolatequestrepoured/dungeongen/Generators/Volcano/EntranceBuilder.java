package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.Volcano;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntranceBuilder {

	private BlockPos startPos;
	private VolcanoDungeon dungeon;
	private int blocksRemainingToWall;
	private EnumFacing direction;
	private World world;
	
	public EntranceBuilder(BlockPos start, int distanceToWall, VolcanoDungeon dungeon, EnumFacing expansionDirection, World world) {
		this.startPos = start;
		this.dungeon = dungeon;
		this.blocksRemainingToWall = distanceToWall;
		this.direction = expansionDirection;
		this.world = world;
	}
	
	private void buildPillar(BlockPos bottom) {
		List<BlockPos> pillarBlocks = new ArrayList<BlockPos>();
		for(int iY = 1; iY <= 4; iY++) {
			world.setBlockState(bottom.add(0,iY,0), ModBlocks.GRANITE_PILLAR.getDefaultState().withRotation(Rotation.NONE));
		}
		world.setBlockState(bottom.add(0,5,0), ModBlocks.GRANITE_CARVED.getDefaultState());
	}
	
	private void buildFloorAndCeiling(BlockPos start, BlockPos end, int ceilingHeight) {
		BlockPos endP = new BlockPos(end.getX(), start.getY(), end.getZ());
		
		//Floor
		for(BlockPos p : BlockPos.getAllInBox(start, endP)) {
			world.setBlockState(p, ModBlocks.GRANITE_BRICK_SMALL.getDefaultState());
		}
		
		//Ceiling
		for(BlockPos p : BlockPos.getAllInBox(start.add(0,ceilingHeight, 0), endP.add(0,ceilingHeight, 0))) {
			world.setBlockState(p, ModBlocks.GRANITE_SQUARE.getDefaultState());
		}
	}

}
