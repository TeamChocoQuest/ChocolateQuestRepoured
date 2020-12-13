package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.BlockAnvil;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorAnvil extends RoomDecorBlocksBase {
	public RoomDecorAnvil() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockRotating(0, 0, 0, Blocks.ANVIL.getDefaultState(), BlockAnvil.FACING, EnumFacing.WEST, BlockStateGenArray.GenerationPhase.MAIN));
	}
}
