package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RoomDecorUnlitTorch extends RoomDecorBlocksBase {
	public RoomDecorUnlitTorch() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockRotating(0, 2, 0, ModBlocks.UNLIT_TORCH.getDefaultState(), BlockTorch.FACING, EnumFacing.SOUTH));

		this.schematic.add(new DecoBlockBase(0, 1, 0, Blocks.AIR.getDefaultState()));
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.AIR.getDefaultState()));

	}
}
