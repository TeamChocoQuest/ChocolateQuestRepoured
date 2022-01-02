package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.util.SpiralStaircaseBuilder;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;

public class CastleRoomTowerSquare extends CastleRoomBase {
	private static final int MIN_SIZE = 5;
	private Direction connectedSide;
	private int stairYOffset;
	private Vector3i pillarOffset;
	private Direction firstStairSide;

	public CastleRoomTowerSquare(int sideLength, int height, Direction connectedSide, int towerSize, CastleRoomTowerSquare towerBelow, int floor, Random rand) {
		super(sideLength, height, floor, rand);
		this.roomType = EnumRoomType.TOWER_SQUARE;
		this.connectedSide = connectedSide;
		this.defaultFloor = false;
		this.defaultCeiling = false;
		this.pathable = false;
		this.isTower = true;

		if (towerBelow != null) {
			this.firstStairSide = towerBelow.getLastStairSide().rotateY();
			this.stairYOffset = 0; // stairs must continue from room below so start building in the floor
		} else {
			this.firstStairSide = this.connectedSide.rotateY(); // makes stairs face door
			this.stairYOffset = 1; // account for 1 layer of floor
		}

		this.pillarOffset = new Vector3i((this.roomLengthX / 2), this.stairYOffset, (this.roomLengthZ / 2));
	}

	@Override
	public void generateRoom(BlockPos castleOrigin, BlockStateGenArray genArray, DungeonRandomizedCastle dungeon) {
		BlockPos stairCenter = this.roomOrigin.add(this.pillarOffset);
		SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(stairCenter, this.firstStairSide, dungeon.getMainBlockState(), dungeon.getStairBlockState());

		BlockPos pos;
		BlockState blockToBuild;

		for (int x = 0; x < this.getDecorationLengthX(); x++) {
			for (int z = 0; z < this.getDecorationLengthZ(); z++) {
				for (int y = 0; y < this.height; y++) {
					blockToBuild = Blocks.AIR.getDefaultState();
					pos = this.getNonWallStartPos().add(x, y, z);

					if (stairs.isPartOfStairs(pos)) {
						blockToBuild = stairs.getBlock(pos);
					} else if (y == 0) {
						blockToBuild = dungeon.getFloorBlockState();
					} else if (y == this.height - 1) {
						blockToBuild = dungeon.getMainBlockState();
					}

					if (blockToBuild.getBlock() != Blocks.AIR) {
						this.usedDecoPositions.add(pos);
					}

					genArray.addBlockState(pos, blockToBuild, BlockStateGenArray.GenerationPhase.MAIN, BlockStateGenArray.EnumPriority.MEDIUM);
				}
			}
		}
	}

	public Direction getLastStairSide() {
		Direction result = this.firstStairSide;
		for (int i = this.stairYOffset; i < this.height - 1; i++) {
			result = result.rotateY();
		}
		return result;
	}

	@Override
	public boolean isTower() {
		return true;
	}

}
