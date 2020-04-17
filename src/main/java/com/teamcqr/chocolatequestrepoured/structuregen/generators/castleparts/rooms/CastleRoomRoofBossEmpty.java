package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.util.math.BlockPos;

public class CastleRoomRoofBossEmpty extends CastleRoomBase {
	public CastleRoomRoofBossEmpty(BlockPos startOffset, int sideLength, int height, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.ROOF_BOSS_EMPTY;
		this.pathable = false;
	}

	@Override
	public void generateRoom(BlockStateGenArray genArray, CastleDungeon dungeon) {
	}

	@Override
	protected void generateWalls(BlockStateGenArray genArray, CastleDungeon dungeon) {
	}
}
