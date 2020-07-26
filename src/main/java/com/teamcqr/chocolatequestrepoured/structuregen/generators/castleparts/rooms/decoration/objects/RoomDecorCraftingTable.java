package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.init.Blocks;

public class RoomDecorCraftingTable extends RoomDecorBlocksBase {
	public RoomDecorCraftingTable() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.CRAFTING_TABLE.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
	}
}
