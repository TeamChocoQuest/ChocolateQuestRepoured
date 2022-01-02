package team.cqr.cqrepoured.world.structure.generation.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3i;
import team.cqr.cqrepoured.util.BlockStateGenArray;

public class DecoBlockBase {
	public Vector3i offset;
	public BlockState blockState;
	public BlockStateGenArray.GenerationPhase genPhase;

	protected DecoBlockBase(int x, int y, int z, BlockState block, BlockStateGenArray.GenerationPhase generationPhase) {
		this.offset = new Vector3i(x, y, z);
		this.blockState = block;
		this.genPhase = generationPhase;
	}

	protected DecoBlockBase(Vector3i offset, BlockState block, BlockStateGenArray.GenerationPhase generationPhase) {
		this.offset = offset;
		this.blockState = block;
		this.genPhase = generationPhase;
	}

	protected BlockState getState(Direction side) {
		return this.blockState;
	}

	public BlockStateGenArray.GenerationPhase getGenPhase() {
		return this.genPhase;
	}
}
