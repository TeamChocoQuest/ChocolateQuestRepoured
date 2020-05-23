package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.SpiralStaircaseBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomTowerSquare extends CastleRoomBase {
	private static final int MIN_SIZE = 5;
	private EnumFacing connectedSide;
	private int stairYOffset;
	private BlockPos pillarStart;
	private EnumFacing firstStairSide;

	public CastleRoomTowerSquare(BlockPos startOffset, int sideLength, int height, EnumFacing connectedSide,
								 int towerSize, CastleRoomTowerSquare towerBelow, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.TOWER_SQUARE;
		this.connectedSide = connectedSide;
		this.buildLengthX = towerSize;
		this.buildLengthZ = towerSize;
		this.defaultFloor = false;
		this.defaultCeiling = false;
		this.pathable = false;
		this.isTower = true;

		if (connectedSide == EnumFacing.NORTH || connectedSide == EnumFacing.SOUTH) {
			this.offsetX += (sideLength - this.buildLengthX) / 2;
			if (connectedSide == EnumFacing.SOUTH) {
				this.offsetZ += sideLength - this.buildLengthZ;
			}
		}
		if (connectedSide == EnumFacing.WEST || connectedSide == EnumFacing.EAST) {
			this.offsetZ += (sideLength - this.buildLengthZ) / 2;
			if (connectedSide == EnumFacing.EAST) {
				this.offsetX += sideLength - this.buildLengthX;
			}
		}

		if (towerBelow != null) {
			this.firstStairSide = towerBelow.getLastStairSide().rotateY();
			this.stairYOffset = 0; // stairs must continue from room below so start building in the floor
		} else {
			this.firstStairSide = this.connectedSide.rotateY(); // makes stairs face door
			this.stairYOffset = 1; // account for 1 layer of floor
		}

		for (EnumFacing side : EnumFacing.HORIZONTALS) {
			this.walls.addOuter(side);
		}

		this.pillarStart = this.getNonWallStartPos().add((this.buildLengthX / 2), this.stairYOffset, (this.buildLengthZ / 2));
	}

	@Override
	public void generateRoom(BlockStateGenArray genArray, DungeonCastle dungeon) {
		SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(this.pillarStart, this.firstStairSide, dungeon.getMainBlockState(), dungeon.getWoodStairBlockState());

		BlockPos pos;
		IBlockState blockToBuild;

		for (int x = 0; x < this.buildLengthX - 1; x++) {
			for (int z = 0; z < this.buildLengthX - 1; z++) {
				for (int y = 0; y < this.height; y++) {
					blockToBuild = Blocks.AIR.getDefaultState();
					pos = this.getNonWallStartPos().add(x, y, z);

					if (stairs.isPartOfStairs(pos)) {
						blockToBuild = stairs.getBlock(pos);
					} else if (y == 0) {
						blockToBuild = dungeon.getFloorBlockState();
					} else if (y == this.height - 1) {
						blockToBuild = dungeon.getMainBlockState();
					}

					if (blockToBuild.getBlock() != Blocks.AIR) {
						this.usedDecoPositions.add(pos);
					}

					genArray.addBlockState(pos, blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
				}
			}
		}
	}

	public EnumFacing getLastStairSide() {
		EnumFacing result = this.firstStairSide;
		for (int i = this.stairYOffset; i < this.height - 1; i++) {
			result = result.rotateY();
		}
		return result;
	}

	@Override
	public boolean reachableFromSide(EnumFacing side) {
		return side == this.connectedSide;
	}

	@Override
	public boolean isTower() {
		return true;
	}

}
