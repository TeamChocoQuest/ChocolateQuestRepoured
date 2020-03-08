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
		this.schematic.add(new DecoBlockRotating(0, 2, 0, Blocks.TORCH.getDefaultState(), BlockTorch.FACING, EnumFacing.SOUTH));

		this.schematic.add(new DecoBlockBase(0, 1, 0, Blocks.AIR.getDefaultState()));
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.AIR.getDefaultState()));

	}
}
