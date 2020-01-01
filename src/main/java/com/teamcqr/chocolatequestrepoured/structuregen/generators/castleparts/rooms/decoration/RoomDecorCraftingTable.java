package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import net.minecraft.init.Blocks;

public class RoomDecorCraftingTable extends RoomDecorBlocks {
	public RoomDecorCraftingTable() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockOffset(0, 0, 0, Blocks.CRAFTING_TABLE));
	}
}
