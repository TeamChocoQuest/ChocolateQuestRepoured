package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class CastleRoomStaircaseDirected extends CastleRoomBase {
	private static final int PLATFORM_LENGTH = 2;
	private EnumFacing doorSide;
	private int numRotations;
	private int upperStairWidth;
	private int upperStairLength;
	private int centerStairWidth;
	private int centerStairLength;

	public CastleRoomStaircaseDirected(BlockPos startOffset, int sideLength, int height, EnumFacing doorSide, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.STAIRCASE_DIRECTED;
		this.doorSide = doorSide;
		this.numRotations = DungeonGenUtils.getCWRotationsBetween(EnumFacing.SOUTH, this.doorSide);
		this.defaultCeiling = false;

		this.upperStairWidth = 0;

		// Determine the width of the center stairs and the two upper side stairs. Find the largest possible
		// side width such that the center width is still greater than or equal to the length of each side.
		do {
			this.upperStairWidth++;
			this.centerStairWidth = (sideLength - 1) - this.upperStairWidth * 2;
		} while ((this.centerStairWidth - 2) >= (this.upperStairWidth + 1));

		// Each stair section should cover half the ascent
		this.upperStairLength = height / 2;
		this.centerStairLength = height + 1 - this.upperStairLength; // center section will either be same length or 1 more
	}

	@Override
	public void generateRoom(BlockStateGenArray genArray, DungeonCastle dungeon) {
		for (int x = 0; x < this.buildLengthX - 1; x++) {
			for (int z = 0; z < this.buildLengthZ - 1; z++) {
				this.buildFloorBlock(x, z, genArray, dungeon);

				if (z < 2) {
					this.buildPlatform(x, z, genArray, dungeon);
				} else if (((x < this.upperStairWidth) || (x >= this.centerStairWidth + this.upperStairWidth)) && z < this.upperStairLength + PLATFORM_LENGTH) {
					this.buildUpperStair(x, z, genArray, dungeon);
				} else if (((x >= this.upperStairWidth) || (x < this.centerStairWidth + this.upperStairWidth)) && z <= this.centerStairLength + PLATFORM_LENGTH) {
					this.buildLowerStair(x, z, genArray, dungeon);
				}
			}
		}
	}

	public void setDoorSide(EnumFacing side) {
		this.doorSide = side;
	}

	public int getUpperStairEndZ() {
		return (this.upperStairLength);
	}

	public int getUpperStairWidth() {
		return this.upperStairWidth;
	}

	public int getCenterStairWidth() {
		return this.centerStairWidth;
	}

	public EnumFacing getDoorSide() {
		return this.doorSide;
	}

	private void buildFloorBlock(int x, int z, BlockStateGenArray genArray, DungeonCastle dungeon) {
		IBlockState blockToBuild = dungeon.getFloorBlockState();
		genArray.addBlockState(this.origin.add(x, 0, z), blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
	}

	private void buildUpperStair(int x, int z, BlockStateGenArray genArray, DungeonCastle dungeon) {
		int stairHeight = this.centerStairLength + (z - PLATFORM_LENGTH);
		EnumFacing stairFacing = DungeonGenUtils.rotateFacingNTimesAboutY(EnumFacing.SOUTH, this.numRotations);
		IBlockState blockToBuild;
		for (int y = 1; y < this.height; y++) {
			if (y < stairHeight) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (y == stairHeight) {
				blockToBuild = dungeon.getStairBlockState().withProperty(BlockStairs.FACING, stairFacing);
			} else {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
			genArray.addBlockState(this.getRotatedPlacement(x, y, z, this.doorSide), blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
		}
	}

	private void buildLowerStair(int x, int z, BlockStateGenArray genArray, DungeonCastle dungeon) {
		int stairHeight = this.centerStairLength - (z - PLATFORM_LENGTH + 1);
		EnumFacing stairFacing = DungeonGenUtils.rotateFacingNTimesAboutY(EnumFacing.NORTH, this.numRotations);
		IBlockState blockToBuild;
		for (int y = 1; y < this.height; y++) {
			if (y < stairHeight) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (y == stairHeight) {
				blockToBuild = dungeon.getStairBlockState().withProperty(BlockStairs.FACING, stairFacing);
			} else {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
			genArray.addBlockState(this.getRotatedPlacement(x, y, z, this.doorSide), blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
		}
	}

	private void buildPlatform(int x, int z, BlockStateGenArray genArray, DungeonCastle dungeon) {
		IBlockState blockToBuild;
		int platformHeight = this.centerStairLength; // the stair length is also the platform height

		for (int y = 1; y < this.height; y++) {
			if (y < platformHeight) {
				blockToBuild = dungeon.getFloorBlockState();
			} else {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
			genArray.addBlockState(this.getRotatedPlacement(x, y, z, this.doorSide), blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
		}
	}

	@Override
	public boolean canBuildDoorOnSide(EnumFacing side) {
		return (side == this.doorSide);
	}

	@Override
	public boolean reachableFromSide(EnumFacing side) {
		return (side == this.doorSide);
	}

	//Only centered doors look good, as the stairs are centered in the room
	@Override
	public DoorPlacement addDoorOnSideRandom(Random random, EnumFacing side) {
		return super.addDoorOnSideCentered(side);
	}
}
