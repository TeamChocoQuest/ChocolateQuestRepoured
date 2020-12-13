package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.init.Blocks;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorPlanks extends RoomDecorBlocksBase {
	public RoomDecorPlanks() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.PLANKS.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
	}
}
