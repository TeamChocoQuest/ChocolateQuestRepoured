package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorTableSmall extends RoomDecorBlocksBase {
	public RoomDecorTableSmall() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, CQRBlocks.TABLE_OAK.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
	}

}
