package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RoomDecorShelf extends RoomDecorBlocksBase {
	public RoomDecorShelf() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 2, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM)));
		this.schematic.add(new DecoBlockBase(1, 2, 0, Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM)));

		this.schematic.add(new DecoBlockBase(0, 1, 0, Blocks.AIR.getDefaultState()));
		this.schematic.add(new DecoBlockBase(1, 1, 0, Blocks.AIR.getDefaultState()));
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.AIR.getDefaultState()));
		this.schematic.add(new DecoBlockBase(1, 0, 0, Blocks.AIR.getDefaultState()));

	}
}
