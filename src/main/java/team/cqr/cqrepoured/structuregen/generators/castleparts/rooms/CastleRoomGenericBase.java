package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public abstract class CastleRoomGenericBase extends CastleRoomDecoratedBase {
	public CastleRoomGenericBase(int sideLength, int height, int floor, Random rand) {
		super(sideLength, height, floor, rand);
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		// No special generation - decorations only
	}

	@Override
	boolean shouldBuildEdgeDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildWallDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildMidDecoration() {
		return true;
	}

	@Override
	boolean shouldAddSpawners() {
		return true;
	}

	@Override
	boolean shouldAddChests() {
		return true;
	}
}
