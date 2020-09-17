package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import java.util.ArrayList;
import java.util.HashSet;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonRandomizedCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomBase;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RoomDecorPillar extends RoomDecorBlocksBase {
	@Override
	protected void makeSchematic() {

	}

	@Override
	public boolean wouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap, CastleRoomBase room) {
		this.schematic = this.getSizedSchematic(room);
		return super.wouldFit(start, side, decoArea, decoMap, room);
	}

	@Override
	public void build(World world, BlockStateGenArray genArray, CastleRoomBase room, DungeonRandomizedCastle dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap) {
		this.schematic = this.getSizedSchematic(room);
		super.build(world, genArray, room, dungeon, start, side, decoMap);
	}

	private ArrayList<DecoBlockBase> getSizedSchematic(CastleRoomBase room) {
		ArrayList<DecoBlockBase> sizedSchematic = new ArrayList<>();
		int height = room.getDecorationLengthY();
		final IBlockState lowerStairs = Blocks.STONE_BRICK_STAIRS.getDefaultState();
		final IBlockState upperStairs = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);
		IBlockState stairs;
		final BlockStateGenArray.GenerationPhase genPhase = BlockStateGenArray.GenerationPhase.MAIN;

		stairs = lowerStairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
		sizedSchematic.add(new DecoBlockBase(0, 0, 0, stairs.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT), genPhase));
		sizedSchematic.add(new DecoBlockBase(1, 0, 0, stairs, genPhase));
		sizedSchematic.add(new DecoBlockBase(2, 0, 0, stairs.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT), genPhase));

		stairs = lowerStairs.withProperty(BlockStairs.FACING, EnumFacing.EAST);
		sizedSchematic.add(new DecoBlockBase(0, 0, 1, stairs, genPhase));

		stairs = lowerStairs.withProperty(BlockStairs.FACING, EnumFacing.WEST);
		sizedSchematic.add(new DecoBlockBase(2, 0, 1, stairs, genPhase));

		stairs = lowerStairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH);
		sizedSchematic.add(new DecoBlockBase(0, 0, 2, stairs.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT), genPhase));
		sizedSchematic.add(new DecoBlockBase(1, 0, 2, stairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH), genPhase));
		sizedSchematic.add(new DecoBlockBase(2, 0, 2, stairs.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT), genPhase));

		for (int y = 0; y < height; y++) {
			sizedSchematic.add(new DecoBlockBase(1, y, 1, Blocks.STONEBRICK.getDefaultState(), genPhase));
		}

		stairs = upperStairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
		sizedSchematic.add(new DecoBlockBase(0, (height - 1), 0, stairs.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT), genPhase));
		sizedSchematic.add(new DecoBlockBase(1, (height - 1), 0, stairs, genPhase));
		sizedSchematic.add(new DecoBlockBase(2, (height - 1), 0, stairs.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT), genPhase));

		stairs = upperStairs.withProperty(BlockStairs.FACING, EnumFacing.EAST);
		sizedSchematic.add(new DecoBlockBase(0, (height - 1), 1, stairs, genPhase));

		stairs = upperStairs.withProperty(BlockStairs.FACING, EnumFacing.WEST);
		sizedSchematic.add(new DecoBlockBase(2, (height - 1), 1, stairs, genPhase));

		stairs = upperStairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH);
		sizedSchematic.add(new DecoBlockBase(0, (height - 1), 2, stairs.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT), genPhase));
		sizedSchematic.add(new DecoBlockBase(1, (height - 1), 2, stairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH), genPhase));
		sizedSchematic.add(new DecoBlockBase(2, (height - 1), 2, stairs.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT), genPhase));

		return sizedSchematic;
	}
}
