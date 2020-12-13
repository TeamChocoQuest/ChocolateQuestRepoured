package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorTorch extends RoomDecorBlocksBase {
	public RoomDecorTorch() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockRotating(0, 0, 0, Blocks.TORCH.getDefaultState(), BlockTorch.FACING, EnumFacing.SOUTH, BlockStateGenArray.GenerationPhase.POST));
	}
}
