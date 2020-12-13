package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms;

import java.util.Random;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class CastleRoomLandingDirected extends CastleRoomBase {
	protected int openingWidth;
	protected int openingSeparation;
	protected int stairZ;
	protected EnumFacing stairStartSide;

	public CastleRoomLandingDirected(int sideLength, int height, CastleRoomStaircaseDirected stairsBelow, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.LANDING_DIRECTED;
		this.openingWidth = stairsBelow.getUpperStairWidth();
		this.stairZ = stairsBelow.getUpperStairEndZ() + 1;
		this.openingSeparation = stairsBelow.getCenterStairWidth();
		this.stairStartSide = stairsBelow.getDoorSide();
		this.defaultCeiling = true;
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		IBlockState blockToBuild;

		// If stairs are facing to the east or west, need to flip the build lengths since we are essentially
		// generating a room facing south and then rotating it
		int lenX = this.stairStartSide.getAxis() == EnumFacing.Axis.Z ? this.getDecorationLengthX() : this.getDecorationLengthZ();
		int lenZ = this.stairStartSide.getAxis() == EnumFacing.Axis.Z ? this.getDecorationLengthZ() : this.getDecorationLengthX();

		for (int x = 0; x < lenX - 1; x++) {
			for (int z = 0; z < lenZ - 1; z++) {
				for (int y = 0; y < this.height - 1; y++) {
					blockToBuild = Blocks.AIR.getDefaultState();
					if (y == 0) {
						if (z > this.stairZ) {
							blockToBuild = dungeon.getFloorBlockState();
						} else if (x < this.openingWidth || ((x >= this.openingSeparation + this.openingWidth) && (x < this.openingSeparation + this.openingWidth * 2))) {
							if (z == this.stairZ) {
								EnumFacing stairFacing = DungeonGenUtils.rotateFacingNTimesAboutY(EnumFacing.SOUTH, DungeonGenUtils.getCWRotationsBetween(EnumFacing.SOUTH, this.stairStartSide));
								blockToBuild = dungeon.getWoodStairBlockState().withProperty(BlockStairs.FACING, stairFacing);
							}
						} else {
							blockToBuild = dungeon.getFloorBlockState();
						}
					}

					genArray.addBlockState(this.getRotatedPlacement(x, y, z, this.stairStartSide), blockToBuild, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}
			}
		}
	}

	@Override
	public boolean canBuildDoorOnSide(EnumFacing side) {
		// Really only works on this side, could add logic to align the doors for other sides later
		return (side == this.stairStartSide);
	}

	@Override
	public boolean reachableFromSide(EnumFacing side) {
		return (side == this.stairStartSide || side == this.stairStartSide.getOpposite());
	}
}
