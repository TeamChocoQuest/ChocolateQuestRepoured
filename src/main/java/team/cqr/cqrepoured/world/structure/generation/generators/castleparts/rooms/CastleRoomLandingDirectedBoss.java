package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;

import java.util.Random;

public class CastleRoomLandingDirectedBoss extends CastleRoomLandingDirected {
	public CastleRoomLandingDirectedBoss(int sideLength, int height, CastleRoomStaircaseDirected stairsBelow, int floor, Random rand) {
		super(sideLength, height, stairsBelow, floor, rand);
	}

	@Override
	public void generate(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		super.generate(castleOrigin, genArray, dungeon);
	}

	@Override
	public boolean canBuildInnerWallOnSide(Direction side) {
		return side != this.stairStartSide;
	}
}
