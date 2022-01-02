package team.cqr.cqrepoured.world.structure.generation.generators.stronghold.spiral;

import java.util.Random;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.ESkyDirection;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonVolcano;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.part.BlockDungeonPart;
import team.cqr.cqrepoured.world.structure.generation.generators.AbstractDungeonGenerator;

public class StrongholdBuilder {

	private final Random random;
	private AbstractDungeonGenerator<DungeonVolcano> generator;
	private GeneratableDungeon.Builder dungeonBuilder;
	private BlockPos startPos;
	private DungeonVolcano dungeon;
	private int blocksRemainingToWall;
	private Direction direction;
	private World world;

	public StrongholdBuilder(AbstractDungeonGenerator<DungeonVolcano> generator, GeneratableDungeon.Builder dungeonBuilder, BlockPos start, int distanceToWall, DungeonVolcano dungeon, Direction expansionDirection, World world, Random rand) {
		this.generator = generator;
		this.dungeonBuilder = dungeonBuilder;
		this.startPos = start;
		this.dungeon = dungeon;
		this.blocksRemainingToWall = distanceToWall;
		this.direction = expansionDirection;
		this.world = world;
		this.random = rand;
	}

	public void generate(int cX, int cZ) {

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
		BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
		for (int i = 0; i < (this.blocksRemainingToWall / 4) + 2; i++) {

			// this.buildSegment(pos.subtract(this.startPos), blockInfoList);
			// Old way: EntranceBuilderHelper.buildEntranceSegment(pos.subtract(this.startPos), blockInfoList, this.direction);
			// new way:
			// this.buildSegment(pos.subtract(this.startPos), partBuilder);
			EntranceBuilderHelper.buildEntranceSegment(pos.subtract(this.startPos), partBuilder, this.direction);

			pos = pos.add(expansionVector);
		}
		this.dungeonBuilder.add(partBuilder, this.startPos);

		this.buildStronghold(pos.add(0, -1, 0), this.world, cX, cZ);
	}

	private void buildStronghold(BlockPos pos, World world2, int cX, int cZ) {
		SpiralStrongholdBuilder stronghold = new SpiralStrongholdBuilder(this.generator, this.dungeonBuilder, ESkyDirection.fromFacing(this.direction), this.dungeon, this.random);
		stronghold.calculateFloors(pos, world2);
		stronghold.buildFloors(pos.add(0, -1, 0), this.world);
	}

}
