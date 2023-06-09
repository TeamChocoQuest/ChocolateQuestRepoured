//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;
//
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.StairsBlock;
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
//public class CastleRoomNetherPortal extends CastleRoomDecoratedBase {
//	private enum Alignment {
//		HORIZONTAL, VERTICAL
//	}
//
//	private Alignment portalAlignment;
//
//	public CastleRoomNetherPortal(int sideLength, int height, int floor, Random rand) {
//		super(sideLength, height, floor, rand);
//		this.roomType = EnumRoomType.PORTAL;
//		this.maxSlotsUsed = 1;
//		this.defaultCeiling = true;
//		this.defaultFloor = true;
//		this.portalAlignment = this.random.nextBoolean() ? Alignment.HORIZONTAL : Alignment.VERTICAL;
//	}
//
//	@Override
//	protected void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
//		int endX = this.getDecorationLengthX() - 1;
//		int endZ = this.getDecorationLengthZ() - 1;
//		int halfX = endX / 2;
//		int halfZ = endZ / 2;
//
//		int xStart = halfX - 2;
//		int xEnd = halfX + 3;
//		int zStart = halfZ - 2;
//		int zEnd = halfZ + 2;
//
//		Predicate<Vector3i> firstLayer = (v -> (v.getY() == 0));
//		Predicate<Vector3i> northEdge = firstLayer.and(v -> (v.getX() >= xStart) && (v.getX() <= xEnd) && (v.getZ() == zStart));
//		Predicate<Vector3i> southEdge = firstLayer.and(v -> (v.getX() >= xStart) && (v.getX() <= xEnd) && (v.getZ() == zEnd));
//		Predicate<Vector3i> westEdge = firstLayer.and(v -> (v.getZ() >= zStart) && (v.getZ() <= zEnd) && (v.getX() == xStart));
//		Predicate<Vector3i> eastEdge = firstLayer.and(v -> (v.getZ() >= zStart) && (v.getZ() <= zEnd) && (v.getX() == xEnd));
//		Predicate<Vector3i> portalBot = (v -> (v.getY() == 0) && (v.getZ() == halfZ) && (v.getX() >= xStart + 1) && (v.getX() <= xEnd - 1));
//		Predicate<Vector3i> portalTop = (v -> (v.getY() == 4) && (v.getZ() == halfZ) && (v.getX() >= xStart + 1) && (v.getX() <= xEnd - 1));
//		Predicate<Vector3i> portalSides = (v -> (v.getY() > 0) && (v.getY() < 4) && (v.getZ() == halfZ) && ((v.getX() == xStart + 1) || (v.getX() == xEnd - 1)));
//		Predicate<Vector3i> portalMid = (v -> (v.getY() > 0) && (v.getY() < 4) && (v.getZ() == halfZ) && ((v.getX() > xStart + 1) && (v.getX() < xEnd - 1)));
//		Predicate<Vector3i> portal = portalBot.or(portalTop).or(portalSides);
//		Predicate<Vector3i> platform = portal.negate().and(firstLayer.and(v -> (v.getX() >= xStart + 1) && (v.getX() <= xEnd - 1) && (v.getZ() >= zStart + 1) && (v.getZ() <= zEnd - 1)));
//
//		GenerationTemplate portalRoomTemplate = new GenerationTemplate(this.getDecorationLengthX(), this.getDecorationLengthY(), this.getDecorationLengthZ());
//		portalRoomTemplate.addRule(northEdge, dungeon.getWoodStairBlockState().withProperty(StairsBlock.FACING, Direction.SOUTH));
//		portalRoomTemplate.addRule(southEdge, dungeon.getWoodStairBlockState().withProperty(StairsBlock.FACING, Direction.NORTH));
//		portalRoomTemplate.addRule(westEdge, dungeon.getWoodStairBlockState().withProperty(StairsBlock.FACING, Direction.EAST));
//		portalRoomTemplate.addRule(eastEdge, dungeon.getWoodStairBlockState().withProperty(StairsBlock.FACING, Direction.WEST));
//		portalRoomTemplate.addRule(platform, dungeon.getMainBlockState());
//		portalRoomTemplate.addRule(portal, Blocks.OBSIDIAN.getDefaultState());
//		portalRoomTemplate.addRule(portalMid, Blocks.PORTAL.getDefaultState());
//
//		Map<BlockPos, BlockState> genMap = portalRoomTemplate.getGenerationMap(this.getDecorationStartPos(), true);
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
