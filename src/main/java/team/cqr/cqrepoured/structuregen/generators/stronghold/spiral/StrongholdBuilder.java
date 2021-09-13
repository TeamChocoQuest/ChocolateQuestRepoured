package team.cqr.cqrepoured.structuregen.generators.stronghold.spiral;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonVolcano;
import team.cqr.cqrepoured.structuregen.generation.AbstractDungeonPart;
import team.cqr.cqrepoured.structuregen.generation.DungeonGenerator;
import team.cqr.cqrepoured.structuregen.generation.DungeonPartBlock;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.structurefile.AbstractBlockInfo;
import team.cqr.cqrepoured.util.ESkyDirection;

public class StrongholdBuilder {

	private final Random random;
	private AbstractDungeonGenerator<DungeonVolcano> generator;
	private DungeonGenerator dungeonGenerator;
	private BlockPos startPos;
	private DungeonVolcano dungeon;
	private int blocksRemainingToWall;
	private EnumFacing direction;
	private World world;
	private List<AbstractDungeonPart> strongholdParts = new ArrayList<>();

	public StrongholdBuilder(AbstractDungeonGenerator<DungeonVolcano> generator, DungeonGenerator dungeonGenerator, BlockPos start, int distanceToWall, DungeonVolcano dungeon, EnumFacing expansionDirection, World world, Random rand) {
		this.generator = generator;
		this.dungeonGenerator = dungeonGenerator;
		this.startPos = start;
		this.dungeon = dungeon;
		this.blocksRemainingToWall = distanceToWall;
		this.direction = expansionDirection;
		this.world = world;
		this.random = rand;
	}

	public void generate(int cX, int cZ, DungeonInhabitant mobType) {

		Vec3i expansionVector = new Vec3i(0, 0, 0);
		switch (this.direction) {
		case EAST:
			expansionVector = new Vec3i(3, 0, 0);
			break;
		case NORTH:
			expansionVector = new Vec3i(0, 0, -3);
			break;
		case SOUTH:
			expansionVector = new Vec3i(0, 0, 3);
			break;
		case WEST:
			expansionVector = new Vec3i(-3, 0, 0);
			break;
		default:
			break;
		}
		// DONE: Place fire pots and "porch"
		BlockPos pos = this.startPos;// .add(expansionV);

		List<AbstractBlockInfo> blockInfoList = new ArrayList<>();

		for (int i = 0; i < (this.blocksRemainingToWall / 4) + 2; i++) {
			//this.buildSegment(pos.subtract(this.startPos), blockInfoList);
			EntranceBuilderHelper.buildEntranceSegment(pos.subtract(this.startPos), blockInfoList, this.direction);
			pos = pos.add(expansionVector);
		}
		this.strongholdParts.add(new DungeonPartBlock(this.world, this.dungeonGenerator, this.startPos, blockInfoList, new PlacementSettings(), mobType));
		this.buildStronghold(pos.add(0, -1, 0), this.world, cX, cZ, mobType);
	}

	private void buildStronghold(BlockPos pos, World world2, int cX, int cZ, DungeonInhabitant mobType) {
		SpiralStrongholdBuilder stronghold = new SpiralStrongholdBuilder(this.generator, this.dungeonGenerator, ESkyDirection.fromFacing(this.direction), this.dungeon, this.random);
		stronghold.calculateFloors(pos, world2, mobType);
		stronghold.buildFloors(pos.add(0, -1, 0), this.world, mobType);
		this.strongholdParts.addAll(stronghold.getStrongholdParts());
	}

	public List<AbstractDungeonPart> getStrongholdParts() {
		return this.strongholdParts;
	}

}
