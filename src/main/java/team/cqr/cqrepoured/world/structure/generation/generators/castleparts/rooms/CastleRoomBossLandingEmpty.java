//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;
//
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//
//import java.util.Random;
//
//public class CastleRoomBossLandingEmpty extends CastleRoomDecoratedBase {
//	private Direction doorSide;
//
//	public CastleRoomBossLandingEmpty(int sideLength, int height, Direction doorSide, int floor, Random rand) {
//		super(sideLength, height, floor, rand);
//		this.roomType = EnumRoomType.LANDING_BOSS;
//		this.pathable = false;
//		this.doorSide = doorSide;
//		this.defaultCeiling = true;
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
//		return true;
//	}
//
//	@Override
//	boolean shouldAddChests() {
//		return false;
//	}
//}
