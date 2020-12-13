package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.BlockTorch;
import net.minecraft.util.EnumFacing;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class RoomDecorUnlitTorch extends RoomDecorBlocksBase {
	public RoomDecorUnlitTorch() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockRotating(0, 0, 0, CQRBlocks.UNLIT_TORCH.getDefaultState(), BlockTorch.FACING, EnumFacing.SOUTH, BlockStateGenArray.GenerationPhase.POST));

	}
}
