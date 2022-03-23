package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.util.SpiralStaircaseBuilder;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;

import java.util.Random;

public class CastleRoomStaircaseSpiral extends CastleRoomDecoratedBase {
	private Direction firstStairSide;
	private Vector3i pillarOffset;

	public CastleRoomStaircaseSpiral(int sideLength, int height, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.STAIRCASE_SPIRAL;
		this.defaultCeiling = false;
		this.defaultFloor = false;

		this.firstStairSide = Direction.NORTH;
		this.recalcPillarOffset();
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		this.recalcPillarOffset();
		BlockPos stairCenter = this.roomOrigin.add(this.pillarOffset);
		SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(stairCenter, this.firstStairSide, dungeon.getMainBlockState(), dungeon.getStairBlockState());

		BlockPos pos;
		BlockState blockToBuild;

		for (int x = 0; x < this.getDecorationLengthX(); x++) {
			for (int z = 0; z < this.getDecorationLengthZ(); z++) {
				for (int y = 0; y < this.height; y++) {
					blockToBuild = Blocks.AIR.getDefaultState();
					pos = this.getInteriorBuildStart().add(x, y, z);

					if (y == 0) {
						blockToBuild = dungeon.getFloorBlockState();
					} else if (stairs.isPartOfStairs(pos)) {
						blockToBuild = stairs.getBlock(pos);
						this.usedDecoPositions.add(pos);
					} else if (y == this.height - 1) {
						blockToBuild = dungeon.getMainBlockState();
					}

					genArray.addBlockState(pos, blockToBuild, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}
			}
		}
	}

	@Override
	boolean shouldBuildEdgeDecoration() {
		return false;
	}

	@Override
	boolean shouldBuildWallDecoration() {
		return true;
	}

	@Override
	boolean shouldBuildMidDecoration() {
		return false;
	}

	@Override
	boolean shouldAddSpawners() {
		return true;
	}

	@Override
	boolean shouldAddChests() {
		return false;
	}

	public Direction getLastStairSide() {
		Direction result = Direction.NORTH;
		for (int i = 0; i < this.height - 1; i++) {
			result = result.rotateY();
		}
		return result;
	}

	public int getStairCenterOffsetX() {
		return this.pillarOffset.getX();
	}

	public int getStairCenterOffsetZ() {
		return this.pillarOffset.getZ();
	}

	private void recalcPillarOffset() {
		int centerX = (this.roomLengthX - 1) / 2;
		int centerZ = (this.roomLengthZ - 1) / 2;
		this.pillarOffset = new Vector3i(centerX, 0, centerZ);
	}
}
