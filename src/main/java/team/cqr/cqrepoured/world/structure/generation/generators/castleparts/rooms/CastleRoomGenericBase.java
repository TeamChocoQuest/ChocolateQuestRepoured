//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;
//
//import net.minecraft.util.math.BlockPos;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//
//import java.util.Random;
//
//public abstract class CastleRoomGenericBase extends CastleRoomDecoratedBase {
//	public CastleRoomGenericBase(int sideLength, int height, int floor, Random rand) {
//		super(sideLength, height, floor, rand);
//	}
//
//	@Override
//	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
//		// No special generation - decorations only
//	}
//
//	@Override
//	boolean shouldBuildEdgeDecoration() {
//		return true;
//	}
//
//	@Override
//	boolean shouldBuildWallDecoration() {
//		return true;
//	}
//
//	@Override
//	boolean shouldBuildMidDecoration() {
//		return true;
//	}
//
//	@Override
//	boolean shouldAddSpawners() {
//		return true;
//	}
//
//	@Override
//	boolean shouldAddChests() {
//		return true;
//	}
//}
