package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class CastleRoomWalkableRoofTower extends CastleRoomWalkableRoof {
	public CastleRoomWalkableRoofTower(int sideLength, int height, CastleRoomTowerSquare tower, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.WALKABLE_TOWER_ROOF;
		this.pathable = false;
		this.offsetX = tower.getOffsetX();
		this.offsetZ = tower.getOffsetZ();
		this.roomLengthX = tower.getRoomLengthX();
		this.roomLengthZ = tower.getRoomLengthZ();
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		super.generateRoom(castleOrigin, genArray, dungeon);
	}

	@Override
	protected boolean hasFloor() {
		return false;
	}
}
