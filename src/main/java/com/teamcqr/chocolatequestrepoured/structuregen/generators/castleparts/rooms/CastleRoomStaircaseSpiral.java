package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.SpiralStaircaseBuilder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomStaircaseSpiral extends CastleRoom {
	private EnumFacing firstStairSide;
	private BlockPos pillarStart;

	public CastleRoomStaircaseSpiral(BlockPos startPos, int sideLength, int height) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.STAIRCASE_SPIRAL;
		this.defaultCeiling = false;
		this.defaultFloor = false;

		this.firstStairSide = EnumFacing.NORTH;
		this.recalcPillarStart();
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
		this.recalcPillarStart();
		SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(this.pillarStart, this.firstStairSide, dungeon.getWallBlock(), dungeon.getStairBlock());

		BlockPos pos;
		IBlockState blockToBuild;

		for (int x = 0; x < this.buildLengthX - 1; x++) {
			for (int z = 0; z < this.buildLengthZ - 1; z++) {
				for (int y = 0; y < this.height; y++) {
					blockToBuild = Blocks.AIR.getDefaultState();
					pos = this.getInteriorBuildStart().add(x, y, z);

					if (y == 0) {
						blockToBuild = dungeon.getFloorBlock().getDefaultState();
					} else if (stairs.isPartOfStairs(pos)) {
						blockToBuild = stairs.getBlock(pos);
					} else if (y == this.height - 1) {
						blockToBuild = dungeon.getWallBlock().getDefaultState();
					}
					world.setBlockState(pos, blockToBuild);
				}
			}
		}
	}

	public EnumFacing getLastStairSide() {
		EnumFacing result = EnumFacing.NORTH;
		for (int i = 0; i < this.height - 1; i++) {
			result = result.rotateY();
		}
		return result;
	}

	public int getCenterX() {
		return this.pillarStart.getX();
	}

	public int getCenterZ() {
		return this.pillarStart.getZ();
	}

	@Override
	public void addInnerWall(EnumFacing side) {
		super.addInnerWall(side);
	}

	@Override
	public void addOuterWall(EnumFacing side) {
		super.addOuterWall(side);
	}

	private void recalcPillarStart() {
		int centerX = (this.buildLengthX - 1) / 2;
		int centerZ = (this.buildLengthZ - 1) / 2;
		this.pillarStart = this.getInteriorBuildStart().add(centerX, 0, centerZ);
	}
}
