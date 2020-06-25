package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.util.math.BlockPos;

public class CastleRoomRoofBossEmpty extends CastleRoomBase {
	public CastleRoomRoofBossEmpty(int sideLength, int height, int floor) {
		super(sideLength, height, floor);
		this.roomType = EnumRoomType.ROOF_BOSS_EMPTY;
		this.pathable = false;
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
	}
}
