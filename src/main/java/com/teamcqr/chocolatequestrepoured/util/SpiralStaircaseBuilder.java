package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SpiralStaircaseBuilder {
	private static final int STAIR_WIDTH = 2;

	private BlockPos start;
	private EnumFacing firstSide;
	private Block platformBlock;
	private Block stairBlock;

	public SpiralStaircaseBuilder(BlockPos pillarStart, EnumFacing firstStairSide, Block platformBlock, Block stairBlock) {
		this.start = pillarStart;
		this.firstSide = firstStairSide;
		this.platformBlock = platformBlock;
		this.stairBlock = stairBlock;
	}

	// returns true if a position is within this staircase, meaning that it is within
	// the 3x3 grid of the spiral and at or above the starting Y
	public boolean isPartOfStairs(BlockPos position) {
		return ((Math.abs(position.getX() - this.start.getX()) <= STAIR_WIDTH) && (Math.abs(position.getZ() - this.start.getZ()) <= STAIR_WIDTH) && (position.getY() >= this.start.getY()));
	}

	public IBlockState getBlock(BlockPos position) {
		EnumFacing stairSide;
		int startX = this.start.getX();
		int startZ = this.start.getZ();
		int posX = position.getX();
		int posZ = position.getZ();

		if (position.getX() == this.start.getX() && position.getZ() == this.start.getZ()) {
			return this.platformBlock.getDefaultState();
		}

		// The side of the stairs rotates each level from the bottom
		stairSide = this.rotateFacingNTimesCW(this.firstSide, Math.abs(position.getY() - this.start.getY()));
		EnumFacing stairFacing = this.rotateFacingNTimesCW(stairSide, 1);

		switch (stairSide) {
		case NORTH:
			if (posX == startX && this.inBoundsNoZero(posZ, startZ, -STAIR_WIDTH)) {
				return this.stairBlock.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
			} else if (this.inBoundsNoZero(posX, startX, STAIR_WIDTH) && this.inBoundsWithZero(posZ, startZ, -STAIR_WIDTH)) {
				return this.platformBlock.getDefaultState();
			}
			break;

		case SOUTH:
			if (posX == startX && this.inBoundsNoZero(posZ, startZ, STAIR_WIDTH)) {
				return this.stairBlock.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
			} else if (this.inBoundsNoZero(posX, startX, -STAIR_WIDTH) && this.inBoundsWithZero(posZ, startZ, STAIR_WIDTH)) {
				return this.platformBlock.getDefaultState();
			}
			break;

		case WEST:
			if (this.inBoundsNoZero(posX, startX, -STAIR_WIDTH) && posZ == startZ) {
				return this.stairBlock.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
			} else if (this.inBoundsWithZero(posX, startX, -STAIR_WIDTH) && this.inBoundsNoZero(posZ, startZ, -STAIR_WIDTH)) {
				return this.platformBlock.getDefaultState();
			}
			break;

		case EAST:
			if (this.inBoundsNoZero(posX, startX, STAIR_WIDTH) && posZ == startZ) {
				return this.stairBlock.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
			} else if (this.inBoundsWithZero(posX, startX, STAIR_WIDTH) && this.inBoundsNoZero(posZ, startZ, STAIR_WIDTH)) {
				return this.platformBlock.getDefaultState();
			}
			break;
		default:
			break;
		}
		return Blocks.AIR.getDefaultState();
	}

	private EnumFacing rotateFacingNTimesCW(EnumFacing facing, int n) {
		n = n % 4; // cap at 0-3 rotations, any more is redundant
		while (n != 0) {
			facing = facing.rotateY();
			n--;
		}

		return facing;
	}

	private boolean inBoundsNoZero(int pos, int start, int distance) {
		int diff = pos - start;
		if (distance > 0) {
			return (diff > 0 && diff <= distance);
		} else {
			return (diff < 0 && diff >= distance);
		}
	}

	private boolean inBoundsWithZero(int pos, int start, int distance) {
		int diff = pos - start;
		if (distance > 0) {
			return (diff >= 0 && diff <= distance);
		} else {
			return (diff <= 0 && diff >= distance);
		}
	}
}
