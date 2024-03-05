package team.cqr.cqrepoured.world.structure.generation.generators.stronghold.spiral;

import java.util.Random;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece;
import team.cqr.cqrepoured.util.ESkyDirection;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonVolcano;

public class StrongholdBuilder {

	private final Random random;
	private CQRStructurePiece.Builder dungeonBuilder;
	private BlockPos startPos;
	private DungeonVolcano dungeon;
	private int blocksRemainingToWall;
	private Direction direction;
	private final CQRStructurePiece.Builder builder;

	public StrongholdBuilder(CQRStructurePiece.Builder dungeonBuilder, BlockPos start, int distanceToWall, DungeonVolcano dungeon, Direction expansionDirection, CQRStructurePiece.Builder builder, Random rand) {
		this.dungeonBuilder = dungeonBuilder;
		this.startPos = start;
		this.dungeon = dungeon;
		this.blocksRemainingToWall = distanceToWall;
		this.direction = expansionDirection;
		this.builder = builder;
		this.random = rand;
	}

	public void generate() {

		Vector3i expansionVector = new Vector3i(0, 0, 0);
		switch (this.direction) {
		case EAST:
			expansionVector = new Vector3i(3, 0, 0);
			break;
		case NORTH:
			expansionVector = new Vector3i(0, 0, -3);
			break;
		case SOUTH:
			expansionVector = new Vector3i(0, 0, 3);
			break;
		case WEST:
			expansionVector = new Vector3i(-3, 0, 0);
			break;
		default:
			break;
		}

		BlockPos pos = this.startPos;
		for (int i = 0; i < (this.blocksRemainingToWall / 4) + 2; i++) {

			// this.buildSegment(pos.subtract(this.startPos), blockInfoList);
			// Old way: EntranceBuilderHelper.buildEntranceSegment(pos.subtract(this.startPos), blockInfoList, this.direction);
			// new way:
			// this.buildSegment(pos.subtract(this.startPos), partBuilder);
			EntranceBuilderHelper.buildEntranceSegment(pos, this.builder, this.direction);

			pos = pos.offset(expansionVector);
		}

		this.buildStronghold(pos.offset(0, -1, 0), this.builder);
	}

	private void buildStronghold(BlockPos pos, CQRStructurePiece.Builder builder) {
		SpiralStrongholdBuilder stronghold = new SpiralStrongholdBuilder(this.dungeonBuilder, ESkyDirection.fromFacing(this.direction), this.dungeon, this.random);
		stronghold.calculateFloors(pos, builder);
		stronghold.buildFloors(pos.offset(0, -1, 0), builder);
	}

}
