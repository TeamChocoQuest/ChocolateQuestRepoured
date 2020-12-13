package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorFurnace extends RoomDecorBlocksBase {
	public RoomDecorFurnace() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockRotating(0, 0, 0, Blocks.FURNACE.getDefaultState(), BlockFurnace.FACING, EnumFacing.SOUTH, BlockStateGenArray.GenerationPhase.MAIN));
	}
}
