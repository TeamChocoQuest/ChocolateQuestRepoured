package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RoomDecorTableMedium extends RoomDecorBlocksBase {

	public RoomDecorTableMedium() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, ModBlocks.TABLE_OAK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(1, 0, 0, ModBlocks.TABLE_OAK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(0, 0, 1, ModBlocks.TABLE_OAK.getDefaultState()));
		this.schematic.add(new DecoBlockBase(1, 0, 1, ModBlocks.TABLE_OAK.getDefaultState()));
	}

}
