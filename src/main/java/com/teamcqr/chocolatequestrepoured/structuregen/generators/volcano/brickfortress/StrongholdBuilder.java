package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockUnlitTorch;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.StairCaseHelper;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class StrongholdBuilder {

	private BlockPos startPos;
	private VolcanoDungeon dungeon;
	private int blocksRemainingToWall;
	private EnumFacing direction;
	private World world;

	public StrongholdBuilder(BlockPos start, int distanceToWall, VolcanoDungeon dungeon, EnumFacing expansionDirection, World world) {
		this.startPos = start;
		this.dungeon = dungeon;
		this.blocksRemainingToWall = distanceToWall;
		this.direction = expansionDirection;
		this.world = world;
	}

	public void generate() {

		Vec3i expansionVector = new Vec3i(0, 0, 0);
		switch (this.direction) {
		case EAST:
			expansionVector = new Vec3i(3, 0, 0);
			break;
		case NORTH:
			expansionVector = new Vec3i(0, 0, -3);
			break;
		case SOUTH:
			expansionVector = new Vec3i(0, 0, 3);
			break;
		case WEST:
			expansionVector = new Vec3i(-3, 0, 0);
			break;
		default:
			break;
		}
		// DONE: Place fire pots and "porch"
		BlockPos pos = this.startPos;// .add(expansionV);

		for (int i = 0; i < (this.blocksRemainingToWall / 6) + 2; i++) {
			this.buildSegment(pos);
			pos = pos.add(expansionVector);
		}
		
		SpiralStrongholdBuilder stronghold = new SpiralStrongholdBuilder(ESkyDirection.fromFacing(this.direction), this.dungeon, new Random(WorldDungeonGenerator.getSeed(this.world, pos.getX() /16, pos.getZ() /16)));
		//try {
			stronghold.calculateFloors(pos);
			stronghold.buildFloors(pos, world);
		/*} catch(Exception ex) {
			//IGnore
		}*/
	}

	private void buildSegment(BlockPos startPosCentered) {
		BlockPos corner1, corner2, pillar1, pillar2, torch1, torch2, air1, air2;
		corner1 = null;
		corner2 = null;
		pillar1 = null;
		pillar2 = null;
		torch1 = null;
		torch2 = null;
		air1 = null;
		air2 = null;
		switch (this.direction) {
		case EAST:
			corner1 = startPosCentered.add(0, 0, -3);
			corner2 = startPosCentered.add(3, 0, 3);
			air1 = startPosCentered.add(0, 1, -2);
			air2 = startPosCentered.add(3, 5, -2);
			pillar1 = startPosCentered.add(1, 0, 2);
			pillar2 = startPosCentered.add(1, 0, -2);
			torch1 = startPosCentered.add(1, 4, 1);
			torch2 = startPosCentered.add(1, 4, -1);
			break;
		case NORTH:
			corner1 = startPosCentered.add(-3, 0, 0);
			corner2 = startPosCentered.add(3, 0, -3);
			air1 = startPosCentered.add(-2, 1, 0);
			air2 = startPosCentered.add(2, 5, -3);
			pillar1 = startPosCentered.add(2, 0, -1);
			pillar2 = startPosCentered.add(-2, 0, -1);
			torch1 = startPosCentered.add(1, 4, -1);
			torch2 = startPosCentered.add(-1, 4, -1);
			break;
		case SOUTH:
			corner1 = startPosCentered.add(3, 0, 0);
			corner2 = startPosCentered.add(-3, 0, 3);
			air1 = startPosCentered.add(2, 1, 0);
			air2 = startPosCentered.add(-2, 5, 3);
			pillar1 = startPosCentered.add(-2, 0, 1);
			pillar2 = startPosCentered.add(2, 0, 1);
			torch1 = startPosCentered.add(-1, 4, 1);
			torch2 = startPosCentered.add(1, 4, 1);
			break;
		case WEST:
			corner1 = startPosCentered.add(0, 0, 3);
			corner2 = startPosCentered.add(-3, 0, -3);
			air1 = startPosCentered.add(0, 1, 2);
			air2 = startPosCentered.add(-3, 5, 2);
			pillar1 = startPosCentered.add(-1, 0, -2);
			pillar1 = startPosCentered.add(-1, 0, 2);
			torch1 = startPosCentered.add(-1, 4, -1);
			torch2 = startPosCentered.add(-1, 4, 1);
			break;
		default:
			break;
		}
		if (corner1 != null && corner2 != null && pillar1 != null && pillar2 != null) {
			for (BlockPos airPos : BlockPos.getAllInBox(air1, air2)) {
				this.world.setBlockToAir(airPos);
			}
			this.buildFloorAndCeiling(corner1, corner2, 5);

			// Left torch -> Facing side: rotate right (90.0°)
			this.buildPillar(pillar1);
			this.world.setBlockState(torch1, ModBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockUnlitTorch.FACING, StairCaseHelper.getFacingWithRotation(this.direction, Rotation.COUNTERCLOCKWISE_90)));
			// Right torch -> Facing side: rotate left (-90.0°)
			this.buildPillar(pillar2);
			this.world.setBlockState(torch2, ModBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockUnlitTorch.FACING, StairCaseHelper.getFacingWithRotation(this.direction, Rotation.CLOCKWISE_90)));
		}
	}

	private void buildPillar(BlockPos bottom) {
		for (int iY = 1; iY <= 4; iY++) {
			BlockPos pos = bottom.add(0, iY, 0);
			this.world.setBlockState(pos, ModBlocks.GRANITE_PILLAR.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
		}
		this.world.setBlockState(bottom.add(0, 5, 0), ModBlocks.GRANITE_CARVED.getDefaultState());
	}

	private void buildFloorAndCeiling(BlockPos start, BlockPos end, int ceilingHeight) {
		BlockPos endP = new BlockPos(end.getX(), start.getY(), end.getZ());

		// Floor
		for (BlockPos p : BlockPos.getAllInBox(start, endP)) {
			this.world.setBlockState(p, ModBlocks.GRANITE_SMALL.getDefaultState());
		}

		// Ceiling
		for (BlockPos p : BlockPos.getAllInBox(start.add(0, ceilingHeight + 1, 0), endP.add(0, ceilingHeight + 1, 0))) {
			this.world.setBlockState(p, ModBlocks.GRANITE_SQUARE.getDefaultState());
		}
	}

}
