package team.cqr.cqrepoured.structuregen.generators.stronghold.spiral;

import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.gentest.GeneratableDungeon;
import team.cqr.cqrepoured.gentest.part.BlockDungeonPart;
import team.cqr.cqrepoured.gentest.preparable.PreparableBlockInfo;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.structurefile.AbstractBlockInfo;
import team.cqr.cqrepoured.structuregen.generators.volcano.StairCaseHelper;
import team.cqr.cqrepoured.util.ESkyDirection;

public class StrongholdBuilder {

	private final Random random;
	private AbstractDungeonGenerator<DungeonVolcano> generator;
	private GeneratableDungeon.Builder dungeonBuilder;
	private BlockPos startPos;
	private DungeonVolcano dungeon;
	private int blocksRemainingToWall;
	private EnumFacing direction;
	private World world;

	public StrongholdBuilder(AbstractDungeonGenerator<DungeonVolcano> generator, GeneratableDungeon.Builder dungeonBuilder, BlockPos start, int distanceToWall, DungeonVolcano dungeon, EnumFacing expansionDirection, World world, Random rand) {
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

		BlockPos pos = this.startPos;
		BlockDungeonPart.Builder partBuilder = new BlockDungeonPart.Builder();
		for (int i = 0; i < (this.blocksRemainingToWall / 4) + 2; i++) {

			//this.buildSegment(pos.subtract(this.startPos), blockInfoList);
			// Old way: EntranceBuilderHelper.buildEntranceSegment(pos.subtract(this.startPos), blockInfoList, this.direction);
			//new way:
			this.buildSegment(pos.subtract(this.startPos), partBuilder);

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

	public List<AbstractDungeonPart> getStrongholdParts() {
		return this.strongholdParts;
	}

	private void buildSegment(BlockPos startPosCentered, BlockDungeonPart.Builder partBuilder) {
		// COrner 2 is always the reference location for the part (!)
		BlockPos corner1, corner2, pillar1, pillar2, torch1, torch2, air1, air2;
		corner1 = null;
		corner2 = null;
		// Pillars are in the middle of the part (on the expansion axis)
		pillar1 = null;
		pillar2 = null;
		// marks the positions of the torches
		torch1 = null;
		torch2 = null;
		// these mark the corners of the complete part
		air1 = null;
		air2 = null;
		switch (this.direction) {
		case EAST:
			corner1 = startPosCentered.add(0, 0, -3);
			corner2 = startPosCentered.add(3, 0, 3);
			air1 = startPosCentered.add(0, 1, -2);
			air2 = startPosCentered.add(3, 5, -2);
			pillar1 = startPosCentered.add(1, 0, 2);
			pillar2 = startPosCentered.add(1, 0, -2);
			torch1 = startPosCentered.add(1, 4, 1);
			torch2 = startPosCentered.add(1, 4, -1);
			break;
		case NORTH:
			corner1 = startPosCentered.add(-3, 0, 0);
			corner2 = startPosCentered.add(3, 0, -3);
			air1 = startPosCentered.add(-2, 1, 0);
			air2 = startPosCentered.add(2, 5, -3);
			pillar1 = startPosCentered.add(2, 0, -1);
			pillar2 = startPosCentered.add(-2, 0, -1);
			torch1 = startPosCentered.add(1, 4, -1);
			torch2 = startPosCentered.add(-1, 4, -1);
			break;
		case SOUTH:
			corner1 = startPosCentered.add(3, 0, 0);
			corner2 = startPosCentered.add(-3, 0, 3);
			air1 = startPosCentered.add(2, 1, 0);
			air2 = startPosCentered.add(-2, 5, 3);
			pillar1 = startPosCentered.add(-2, 0, 1);
			pillar2 = startPosCentered.add(2, 0, 1);
			torch1 = startPosCentered.add(-1, 4, 1);
			torch2 = startPosCentered.add(1, 4, 1);
			break;
		case WEST:
			corner1 = startPosCentered.add(0, 0, 3);
			corner2 = startPosCentered.add(-3, 0, -3);
			air1 = startPosCentered.add(0, 1, 2);
			air2 = startPosCentered.add(-3, 5, 2);
			pillar1 = startPosCentered.add(-1, 0, -2);
			pillar2 = startPosCentered.add(-1, 0, 2);
			torch1 = startPosCentered.add(-1, 4, -1);
			torch2 = startPosCentered.add(-1, 4, 1);
			break;
		default:
			break;
		}
		if (corner1 != null && corner2 != null && pillar1 != null && pillar2 != null) {
			/*
			 * for (BlockPos airPos : BlockPos.getAllInBox(air1, air2)) { blockInfoList.add(new PreparableBlockInfo(airPos, Blocks.AIR.getDefaultState(), null)); }
			 */
			this.buildFloorAndCeiling(corner1, corner2, 5, partBuilder);

			// Left torch -> Facing side: rotate right (90.0°)
			this.buildPillar(pillar1, partBuilder);
			partBuilder.add(new PreparableBlockInfo(torch1, CQRBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockTorch.FACING, StairCaseHelper.getFacingWithRotation(this.direction, Rotation.COUNTERCLOCKWISE_90)), null));
			// Right torch -> Facing side: rotate left (-90.0°)
			this.buildPillar(pillar2, partBuilder);
			partBuilder.add(new PreparableBlockInfo(torch2, CQRBlocks.UNLIT_TORCH.getDefaultState().withProperty(BlockTorch.FACING, StairCaseHelper.getFacingWithRotation(this.direction, Rotation.CLOCKWISE_90)), null));
		}
	}

	private void buildPillar(BlockPos bottom, BlockDungeonPart.Builder partBuilder) {
		for (int iY = 1; iY <= 4; iY++) {
			BlockPos pos = bottom.add(0, iY, 0);
			partBuilder.add(new PreparableBlockInfo(pos, CQRBlocks.GRANITE_PILLAR.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y), null));
		}
		partBuilder.add(new PreparableBlockInfo(bottom.add(0, 5, 0), CQRBlocks.GRANITE_CARVED.getDefaultState(), null));
	}

	private void buildFloorAndCeiling(BlockPos start, BlockPos end, int ceilingHeight, BlockDungeonPart.Builder partBuilder) {
		BlockPos endP = new BlockPos(end.getX(), start.getY(), end.getZ());

		// Floor
		for (BlockPos p : BlockPos.getAllInBox(start, endP)) {
			partBuilder.add(new PreparableBlockInfo(p, CQRBlocks.GRANITE_SMALL.getDefaultState(), null));
		}

		// Ceiling
		for (BlockPos p : BlockPos.getAllInBox(start.add(0, ceilingHeight + 1, 0), endP.add(0, ceilingHeight + 1, 0))) {
			partBuilder.add(new PreparableBlockInfo(p, CQRBlocks.GRANITE_SQUARE.getDefaultState(), null));
		}
	}

}
