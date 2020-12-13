package team.cqr.cqrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class DecoBlockBase {
	public Vec3i offset;
	public IBlockState blockState;
	public BlockStateGenArray.GenerationPhase genPhase;

	protected DecoBlockBase(int x, int y, int z, IBlockState block, BlockStateGenArray.GenerationPhase generationPhase) {
		this.offset = new Vec3i(x, y, z);
		this.blockState = block;
		this.genPhase = generationPhase;
	}

	protected DecoBlockBase(Vec3i offset, IBlockState block, BlockStateGenArray.GenerationPhase generationPhase) {
		this.offset = offset;
		this.blockState = block;
		this.genPhase = generationPhase;
	}

	protected IBlockState getState(EnumFacing side) {
		return this.blockState;
	}

	public BlockStateGenArray.GenerationPhase getGenPhase() {
		return this.genPhase;
	}
}
