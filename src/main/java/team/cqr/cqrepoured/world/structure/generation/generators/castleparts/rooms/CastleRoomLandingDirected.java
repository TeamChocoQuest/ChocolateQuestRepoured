//package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;
//
//import net.minecraft.world.level.block.BlockState;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.StairsBlock;
//import net.minecraft.util.Direction;
//import net.minecraft.core.BlockPos;
//import team.cqr.cqrepoured.util.BlockStateGenArray;
//import team.cqr.cqrepoured.util.DungeonGenUtils;
//import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
//
//import java.util.Random;
//
//public class CastleRoomLandingDirected extends CastleRoomBase {
//	protected int openingWidth;
//	protected int openingSeparation;
//	protected int stairZ;
//	protected Direction stairStartSide;
//
//	public CastleRoomLandingDirected(int sideLength, int height, CastleRoomStaircaseDirected stairsBelow, int floor, Random rand) {
//		super(sideLength, height, floor, rand);
//		this.roomType = EnumRoomType.LANDING_DIRECTED;
//		this.openingWidth = stairsBelow.getUpperStairWidth();
//		this.stairZ = stairsBelow.getUpperStairEndZ() + 1;
//		this.openingSeparation = stairsBelow.getCenterStairWidth();
//		this.stairStartSide = stairsBelow.getDoorSide();
//		this.defaultCeiling = true;
//	}
//
//	@Override
//	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
//		BlockState blockToBuild;
//
//		// If stairs are facing to the east or west, need to flip the build lengths since we are essentially
//		// generating a room facing south and then rotating it
//		int lenX = this.stairStartSide.getAxis() == Direction.Axis.Z ? this.getDecorationLengthX() : this.getDecorationLengthZ();
//		int lenZ = this.stairStartSide.getAxis() == Direction.Axis.Z ? this.getDecorationLengthZ() : this.getDecorationLengthX();
//
//		for (int x = 0; x < lenX - 1; x++) {
//			for (int z = 0; z < lenZ - 1; z++) {
//				for (int y = 0; y < this.height - 1; y++) {
//					blockToBuild = Blocks.AIR.getDefaultState();
//					if (y == 0) {
//						if (z > this.stairZ) {
//							blockToBuild = dungeon.getFloorBlockState();
//						} else if (x < this.openingWidth || ((x >= this.openingSeparation + this.openingWidth) && (x < this.openingSeparation + this.openingWidth * 2))) {
//							if (z == this.stairZ) {
//								Direction stairFacing = DungeonGenUtils.rotateFacingNTimesAboutY(Direction.SOUTH, DungeonGenUtils.getCWRotationsBetween(Direction.SOUTH, this.stairStartSide));
//								blockToBuild = dungeon.getWoodStairBlockState().withProperty(StairsBlock.FACING, stairFacing);
//							}
//						} else {
//							blockToBuild = dungeon.getFloorBlockState();
//						}
//					}
//
//					genArray.addBlockState(this.getRotatedPlacement(x, y, z, this.stairStartSide), blockToBuild, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
//				}
//			}
//		}
//	}
//
//	@Override
//	public boolean canBuildDoorOnSide(Direction side) {
//		// Really only works on this side, could add logic to align the doors for other sides later
//		return (side == this.stairStartSide);
//	}
//
//	@Override
//	public boolean reachableFromSide(Direction side) {
//		return (side == this.stairStartSide || side == this.stairStartSide.getOpposite());
//	}
//}
