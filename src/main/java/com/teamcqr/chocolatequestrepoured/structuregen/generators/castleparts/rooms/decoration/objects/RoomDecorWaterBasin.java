package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RoomDecorWaterBasin extends RoomDecorBlocksBase {
	public RoomDecorWaterBasin() {
		super();
	}

	@Override
	protected void makeSchematic() {
		final IBlockState chiseledStone = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
		final IBlockState stairs = Blocks.STONE_BRICK_STAIRS.getDefaultState();

		this.schematic.add(new DecoBlockBase(0, 0, 0, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
		this.schematic.add(new DecoBlockRotating(1, 0, 0, stairs, BlockStairs.FACING, EnumFacing.SOUTH, BlockStateGenArray.GenerationPhase.MAIN));
		this.schematic.add(new DecoBlockBase(2, 0, 0, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));

		this.schematic.add(new DecoBlockRotating(0, 0, 1, stairs, BlockStairs.FACING, EnumFacing.EAST, BlockStateGenArray.GenerationPhase.MAIN));
		this.schematic.add(new DecoBlockBase(1, 0, 1, Blocks.WATER.getDefaultState(), BlockStateGenArray.GenerationPhase.POST));
		this.schematic.add(new DecoBlockRotating(2, 0, 1, stairs, BlockStairs.FACING, EnumFacing.WEST, BlockStateGenArray.GenerationPhase.MAIN));

		this.schematic.add(new DecoBlockBase(0, 0, 2, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
		this.schematic.add(new DecoBlockRotating(1, 0, 2, stairs, BlockStairs.FACING, EnumFacing.NORTH, BlockStateGenArray.GenerationPhase.MAIN));
		this.schematic.add(new DecoBlockBase(2, 0, 2, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
	}
}
