package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CastleRoomWalkableRoofTower extends CastleRoomWalkableRoof {
	public CastleRoomWalkableRoofTower(int sideLength, int height, CastleRoomTowerSquare tower, int floor) {
		super(sideLength, height, floor);
		this.roomType = EnumRoomType.WALKABLE_TOWER_ROOF;
		this.pathable = false;
		this.offsetX = tower.getOffsetX();
		this.offsetZ = tower.getOffsetZ();
		this.buildLengthX = tower.getBuildLengthX();
		this.buildLengthZ = tower.getBuildLengthZ();
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonCastle dungeon) {
		super.generateRoom(castleOrigin, genArray, dungeon);
	}

	@Override
	protected boolean hasFloor()
	{
		return false;
	}

	@Override
	protected BlockPos getNonWallStartPos() {
		//Normal rooms start their decoration area at (offsetX, 0, offsety)
		//But towers always have north/west walls, so adjust 1 square for those
		return super.getNonWallStartPos().add(1, 0, 1);
	}
}
