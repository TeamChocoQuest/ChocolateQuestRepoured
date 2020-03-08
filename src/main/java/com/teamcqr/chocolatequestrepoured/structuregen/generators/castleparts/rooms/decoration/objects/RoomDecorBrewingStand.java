package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.init.Blocks;

public class RoomDecorBrewingStand extends RoomDecorBlocksBase {
	public RoomDecorBrewingStand() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockOffset(0, 0, 0, Blocks.BREWING_STAND));
	}
}
