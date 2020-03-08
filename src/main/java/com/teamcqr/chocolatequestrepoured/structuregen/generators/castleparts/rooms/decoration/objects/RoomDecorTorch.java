package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RoomDecorTorch extends RoomDecorBlocksBase {
	public RoomDecorTorch() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 2, 0, Blocks.TORCH));

		this.schematic.add(new DecoBlockBase(0, 1, 0, Blocks.AIR));
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.AIR));

	}

	@Override
	protected IBlockState getRotatedBlockState(Block block, EnumFacing side) {
		IBlockState result = block.getDefaultState();

		if (block == Blocks.TORCH) {
			result = result.withProperty(BlockTorch.FACING, side.getOpposite());
		}

		return result;
	}
}
