package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomLandingDirectedBoss extends CastleRoomLandingDirected {
	public CastleRoomLandingDirectedBoss(int sideLength, int height, CastleRoomStaircaseDirected stairsBelow, int floor, Random rand) {
		super(sideLength, height, stairsBelow, floor, rand);
	}

	@Override
	public void generate(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		super.generate(castleOrigin, genArray, dungeon);
	}

	@Override
	public boolean canBuildInnerWallOnSide(EnumFacing side) {
		return side != this.stairStartSide;
	}
}
