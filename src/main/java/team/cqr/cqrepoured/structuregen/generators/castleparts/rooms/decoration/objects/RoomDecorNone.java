package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.init.Blocks;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorNone extends RoomDecorBlocksBase {
	public RoomDecorNone() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.AIR.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
	}
}
