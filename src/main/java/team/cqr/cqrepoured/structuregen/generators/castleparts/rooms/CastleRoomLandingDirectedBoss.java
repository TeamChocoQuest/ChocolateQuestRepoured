package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.util.BlockStateGenArray;

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
