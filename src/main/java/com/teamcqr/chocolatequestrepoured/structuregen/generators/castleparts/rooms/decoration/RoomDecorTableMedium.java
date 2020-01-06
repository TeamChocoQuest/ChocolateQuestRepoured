package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RoomDecorTableMedium extends RoomDecorBlocks {

	public RoomDecorTableMedium() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockOffset(0, 0, 0, Blocks.OAK_FENCE));
		this.schematic.add(new DecoBlockOffset(1, 0, 0, Blocks.OAK_FENCE));
		this.schematic.add(new DecoBlockOffset(0, 0, 1, Blocks.OAK_FENCE));
		this.schematic.add(new DecoBlockOffset(1, 0, 1, Blocks.OAK_FENCE));

		this.schematic.add(new DecoBlockOffset(0, 1, 0, Blocks.WOODEN_SLAB));
		this.schematic.add(new DecoBlockOffset(1, 1, 0, Blocks.WOODEN_SLAB));
		this.schematic.add(new DecoBlockOffset(0, 1, 1, Blocks.WOODEN_SLAB));
		this.schematic.add(new DecoBlockOffset(1, 1, 1, Blocks.WOODEN_SLAB));
	}

	@Override
	protected IBlockState getRotatedBlockState(Block block, EnumFacing side) {
		IBlockState result = block.getDefaultState();

		if (block == Blocks.WOODEN_SLAB) {
			result = result.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
		}

		return result;
	}

}
