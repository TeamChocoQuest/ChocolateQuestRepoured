package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RoomDecorBed extends RoomDecorBlocksBase {
	public RoomDecorBed() {
		super();
	}

	@Override
	protected void makeSchematic() {
		IBlockState head = Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
		this.schematic.add(new DecoBlockRotating(0, 0, 0, head, BlockBed.FACING, EnumFacing.NORTH));
		IBlockState foot = Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
		this.schematic.add(new DecoBlockRotating(0, 0, 1, foot, BlockBed.FACING, EnumFacing.NORTH));
	}
}
