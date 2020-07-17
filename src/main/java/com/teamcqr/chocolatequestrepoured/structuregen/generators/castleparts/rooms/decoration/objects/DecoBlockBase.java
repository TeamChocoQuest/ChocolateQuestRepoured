package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

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
		return blockState;
	}

	public BlockStateGenArray.GenerationPhase getGenPhase() {
		return genPhase;
	}
}
