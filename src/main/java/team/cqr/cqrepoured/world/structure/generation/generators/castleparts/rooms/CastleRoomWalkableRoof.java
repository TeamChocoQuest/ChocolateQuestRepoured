//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;
//
//import net.minecraft.block.Blocks;
//import net.minecraft.util.math.BlockPos;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//
//import java.util.Random;
//
//public class CastleRoomWalkableRoof extends CastleRoomBase {
//	public CastleRoomWalkableRoof(int sideLength, int height, int floor, Random rand) {
//		super(sideLength, height, floor, rand);
//		this.roomType = EnumRoomType.WALKABLE_ROOF;
//		this.pathable = false;
//	}
//
//	@Override
//	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
//		for (BlockPos pos : this.getDecorationArea()) {
//			genArray.addBlockState(pos, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.LOWEST);
//		}
//	}
//
//	@Override
//	protected boolean hasFloor() {
//		return false;
//	}
//}
