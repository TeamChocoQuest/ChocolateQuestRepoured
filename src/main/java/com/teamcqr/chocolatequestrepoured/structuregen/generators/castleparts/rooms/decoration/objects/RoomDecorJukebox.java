package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.init.Blocks;

public class RoomDecorJukebox extends RoomDecorBlocksBase {
	public RoomDecorJukebox() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.JUKEBOX.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
	}
}
