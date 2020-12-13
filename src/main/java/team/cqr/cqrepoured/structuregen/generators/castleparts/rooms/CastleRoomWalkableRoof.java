package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class CastleRoomWalkableRoof extends CastleRoomBase {
	public CastleRoomWalkableRoof(int sideLength, int height, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.WALKABLE_ROOF;
		this.pathable = false;
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		for (BlockPos pos : this.getDecorationArea()) {
			genArray.addBlockState(pos, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.LOWEST);
		}
	}

	@Override
	protected boolean hasFloor() {
		return false;
	}
}
