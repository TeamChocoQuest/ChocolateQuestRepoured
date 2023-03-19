//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;
//
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//
//import java.util.Random;
//
//public class CastleRoomBossStairEmpty extends CastleRoomDecoratedBase {
//	private Direction doorSide;
//
//	public CastleRoomBossStairEmpty(int sideLength, int height, Direction doorSide, int floor, Random rand) {
//		super(sideLength, height, floor, rand);
//		this.roomType = EnumRoomType.STAIRCASE_BOSS;
//		this.pathable = true;
//		this.doorSide = doorSide;
//	}
//
//	@Override
//	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
//	}
//
//	@Override
//	boolean shouldBuildEdgeDecoration() {
//		return false;
//	}
//
//	@Override
//	boolean shouldBuildWallDecoration() {
//		return true;
//	}
//
//	@Override
//	boolean shouldBuildMidDecoration() {
//		return false;
//	}
//
//	@Override
//	boolean shouldAddSpawners() {
//		return false;
//	}
//
//	@Override
//	boolean shouldAddChests() {
//		return false;
//	}
//}
