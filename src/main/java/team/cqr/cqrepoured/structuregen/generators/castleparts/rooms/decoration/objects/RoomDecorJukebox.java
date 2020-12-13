package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.init.Blocks;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorJukebox extends RoomDecorBlocksBase {
	public RoomDecorJukebox() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.JUKEBOX.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
	}
}
