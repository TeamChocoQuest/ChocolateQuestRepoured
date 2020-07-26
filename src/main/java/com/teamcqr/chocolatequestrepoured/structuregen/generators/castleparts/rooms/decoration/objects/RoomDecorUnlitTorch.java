package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.BlockTorch;
import net.minecraft.util.EnumFacing;

public class RoomDecorUnlitTorch extends RoomDecorBlocksBase {
	public RoomDecorUnlitTorch() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockRotating(0, 0, 0, ModBlocks.UNLIT_TORCH.getDefaultState(), BlockTorch.FACING, EnumFacing.SOUTH, BlockStateGenArray.GenerationPhase.POST));

	}
}
