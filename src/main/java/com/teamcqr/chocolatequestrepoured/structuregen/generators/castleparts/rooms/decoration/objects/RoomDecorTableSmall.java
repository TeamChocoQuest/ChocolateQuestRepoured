package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

public class RoomDecorTableSmall extends RoomDecorBlocksBase {
	public RoomDecorTableSmall() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, ModBlocks.TABLE_OAK.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
	}

}
