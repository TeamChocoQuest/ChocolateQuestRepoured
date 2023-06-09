//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;
//
//import net.minecraft.world.level.block.BlockState;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.StairsBlock;
//import net.minecraft.util.Direction;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.math.vector.Vector3i;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.util.GenerationTemplate;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//
//import java.util.Map;
//import java.util.Random;
//import java.util.function.Predicate;
//
//public class CastleRoomPool extends CastleRoomDecoratedBase {
//	public CastleRoomPool(int sideLength, int height, int floor, Random rand) {
//		super(sideLength, height, floor, rand);
//		this.roomType = EnumRoomType.POOL;
//		this.maxSlotsUsed = 1;
//		this.defaultCeiling = true;
//		this.defaultFloor = true;
//	}
//
//	@Override
//	protected void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
//		int endX = this.getDecorationLengthX() - 1;
//		int endZ = this.getDecorationLengthZ() - 1;
//		Predicate<Vector3i> northRow = (v -> ((v.getY() == 0) && (v.getZ() == 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
//		Predicate<Vector3i> southRow = (v -> ((v.getY() == 0) && (v.getZ() == endZ - 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
//		Predicate<Vector3i> westRow = (v -> ((v.getY() == 0) && (v.getX() == 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));
//		Predicate<Vector3i> eastRow = (v -> ((v.getY() == 0) && (v.getX() == endX - 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));
//		Predicate<Vector3i> water = (v -> ((v.getY() == 0) && (v.getX() > 1) && (v.getX() < endX - 1) && (v.getZ() > 1) && (v.getZ() < endZ - 1)));
//
//		GenerationTemplate poolRoomTemplate = new GenerationTemplate(this.getDecorationLengthX(), this.getDecorationLengthY(), this.getDecorationLengthZ());
//		poolRoomTemplate.addRule(northRow, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(StairsBlock.FACING, Direction.SOUTH));
//		poolRoomTemplate.addRule(southRow, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(StairsBlock.FACING, Direction.NORTH));
//		poolRoomTemplate.addRule(westRow, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(StairsBlock.FACING, Direction.EAST));
//		poolRoomTemplate.addRule(eastRow, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(StairsBlock.FACING, Direction.WEST));
//		poolRoomTemplate.addRule(water, Blocks.WATER.getDefaultState());
//
//		Map<BlockPos, BlockState> genMap = poolRoomTemplate.getGenerationMap(this.getDecorationStartPos(), true);
//		genArray.addBlockStateMap(genMap, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
//		for (Map.Entry<BlockPos, BlockState> entry : genMap.entrySet()) {
//			if (entry.getValue().getBlock() != Blocks.AIR) {
//				this.usedDecoPositions.add(entry.getKey());
//			}
//		}
//
//	}
//
//	@Override
//	protected BlockState getFloorBlock(DungeonRandomizedCastle dungeon) {
//		return dungeon.getMainBlockState();
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
