package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomBossLandingEmpty extends CastleRoomDecoratedBase {
	private EnumFacing doorSide;

	public CastleRoomBossLandingEmpty(int sideLength, int height, EnumFacing doorSide, int floor) {
		super(sideLength, height, floor);
		this.roomType = EnumRoomType.LANDING_BOSS;
		this.pathable = false;
		this.doorSide = doorSide;
		this.defaultCeiling = true;
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
	}

	@Override
	boolean shouldBuildEdgeDecoration() {
		return false;
	}

	@Override
	boolean shouldBuildWallDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildMidDecoration() {
		return false;
	}

	@Override
	boolean shouldAddSpawners() {
		return true;
	}

	@Override
	boolean shouldAddChests() {
		return false;
	}
}
