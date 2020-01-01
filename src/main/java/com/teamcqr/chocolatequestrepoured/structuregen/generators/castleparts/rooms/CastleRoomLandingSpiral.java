package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.SpiralStaircaseBuilder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomLandingSpiral extends CastleRoom {
	private CastleRoomStaircaseSpiral stairsBelow;

	public CastleRoomLandingSpiral(BlockPos startPos, int sideLength, int height, CastleRoomStaircaseSpiral stairsBelow) {
		super(startPos, sideLength, height);
		this.roomType = EnumRoomType.LANDING_SPIRAL;
		this.stairsBelow = stairsBelow;
		this.defaultCeiling = true;
	}

	@Override
	public void generateRoom(World world, CastleDungeon dungeon) {
		BlockPos pos;
		IBlockState blockToBuild;
		BlockPos pillarStart = new BlockPos(this.stairsBelow.getCenterX(), this.origin.getY(), this.stairsBelow.getCenterZ());
		EnumFacing firstStairSide = this.stairsBelow.getLastStairSide().rotateY();

		SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(pillarStart, firstStairSide, dungeon.getWallBlock(), dungeon.getStairBlock());

		for (int x = 0; x < this.buildLengthX - 1; x++) {
			for (int z = 0; z < this.buildLengthZ - 1; z++) {
				for (int y = 0; y < this.height; y++) {
					blockToBuild = Blocks.AIR.getDefaultState();
					pos = this.getInteriorBuildStart().add(x, y, z);

					// continue stairs for 1 layer through floor
					if (y == 0) {
						if (stairs.isPartOfStairs(pos)) {
							blockToBuild = stairs.getBlock(pos);
						} else {
							blockToBuild = dungeon.getFloorBlock().getDefaultState();
						}
					}
					world.setBlockState(pos, blockToBuild);
				}
			}
		}
	}
}
