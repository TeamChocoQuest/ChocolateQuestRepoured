package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.BlockStateGenArray;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonRandomizedCastle;
import team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.CastleRoomBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RoomDecorPillar extends RoomDecorBlocksBase {
	@Override
	protected void makeSchematic() {

	}

	@Override
	public boolean wouldFit(BlockPos start, Direction side, Set<BlockPos> decoArea, Set<BlockPos> decoMap, CastleRoomBase room) {
		this.schematic = this.getSizedSchematic(room);
		return super.wouldFit(start, side, decoArea, decoMap, room);
	}

	@Override
	public void build(World world, BlockStateGenArray genArray, CastleRoomBase room, DungeonRandomizedCastle dungeon, BlockPos start, Direction side, Set<BlockPos> decoMap) {
		this.schematic = this.getSizedSchematic(room);
		super.build(world, genArray, room, dungeon, start, side, decoMap);
	}

	private List<DecoBlockBase> getSizedSchematic(CastleRoomBase room) {
		List<DecoBlockBase> sizedSchematic = new ArrayList<>();
		int height = room.getDecorationLengthY();
		final BlockState lowerStairs = Blocks.STONE_BRICK_STAIRS.getDefaultState();
		final BlockState upperStairs = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.TOP);
		BlockState stairs;
		final BlockStateGenArray.GenerationPhase genPhase = BlockStateGenArray.GenerationPhase.MAIN;

		stairs = lowerStairs.withProperty(StairsBlock.FACING, Direction.SOUTH);
		sizedSchematic.add(new DecoBlockBase(0, 0, 0, stairs.withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_LEFT), genPhase));
		sizedSchematic.add(new DecoBlockBase(1, 0, 0, stairs, genPhase));
		sizedSchematic.add(new DecoBlockBase(2, 0, 0, stairs.withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_RIGHT), genPhase));

		stairs = lowerStairs.withProperty(StairsBlock.FACING, Direction.EAST);
		sizedSchematic.add(new DecoBlockBase(0, 0, 1, stairs, genPhase));

		stairs = lowerStairs.withProperty(StairsBlock.FACING, Direction.WEST);
		sizedSchematic.add(new DecoBlockBase(2, 0, 1, stairs, genPhase));

		stairs = lowerStairs.withProperty(StairsBlock.FACING, Direction.NORTH);
		sizedSchematic.add(new DecoBlockBase(0, 0, 2, stairs.withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_RIGHT), genPhase));
		sizedSchematic.add(new DecoBlockBase(1, 0, 2, stairs.withProperty(StairsBlock.FACING, Direction.NORTH), genPhase));
		sizedSchematic.add(new DecoBlockBase(2, 0, 2, stairs.withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_LEFT), genPhase));

		for (int y = 0; y < height; y++) {
			sizedSchematic.add(new DecoBlockBase(1, y, 1, Blocks.STONEBRICK.getDefaultState(), genPhase));
		}

		stairs = upperStairs.withProperty(StairsBlock.FACING, Direction.SOUTH);
		sizedSchematic.add(new DecoBlockBase(0, (height - 1), 0, stairs.withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_LEFT), genPhase));
		sizedSchematic.add(new DecoBlockBase(1, (height - 1), 0, stairs, genPhase));
		sizedSchematic.add(new DecoBlockBase(2, (height - 1), 0, stairs.withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_RIGHT), genPhase));

		stairs = upperStairs.withProperty(StairsBlock.FACING, Direction.EAST);
		sizedSchematic.add(new DecoBlockBase(0, (height - 1), 1, stairs, genPhase));

		stairs = upperStairs.withProperty(StairsBlock.FACING, Direction.WEST);
		sizedSchematic.add(new DecoBlockBase(2, (height - 1), 1, stairs, genPhase));

		stairs = upperStairs.withProperty(StairsBlock.FACING, Direction.NORTH);
		sizedSchematic.add(new DecoBlockBase(0, (height - 1), 2, stairs.withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_RIGHT), genPhase));
		sizedSchematic.add(new DecoBlockBase(1, (height - 1), 2, stairs.withProperty(StairsBlock.FACING, Direction.NORTH), genPhase));
		sizedSchematic.add(new DecoBlockBase(2, (height - 1), 2, stairs.withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_LEFT), genPhase));

		return sizedSchematic;
	}
}
