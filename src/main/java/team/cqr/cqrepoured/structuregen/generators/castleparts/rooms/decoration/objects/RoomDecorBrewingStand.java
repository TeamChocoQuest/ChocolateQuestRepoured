package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.init.Blocks;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorBrewingStand extends RoomDecorBlocksBase {
	public RoomDecorBrewingStand() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockBase(0, 0, 0, Blocks.BREWING_STAND.getDefaultState(), BlockStateGenArray.GenerationPhase.MAIN));
	}
}
