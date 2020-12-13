package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorTableMedium extends RoomDecorBlocksBase {

	public RoomDecorTableMedium() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, CQRBlocks.TABLE_OAK.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
		this.schematic.add(new DecoBlockBase(1, 0, 0, CQRBlocks.TABLE_OAK.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
		this.schematic.add(new DecoBlockBase(0, 0, 1, CQRBlocks.TABLE_OAK.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
		this.schematic.add(new DecoBlockBase(1, 0, 1, CQRBlocks.TABLE_OAK.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
	}

}
