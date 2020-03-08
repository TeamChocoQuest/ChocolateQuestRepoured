package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.init.Blocks;

public class RoomDecorCauldron extends RoomDecorBlocksBase {
	public RoomDecorCauldron() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.CAULDRON.getDefaultState()));
	}
}
