package com.teamcqr.chocolatequestrepoured.capability.structureselector;

import net.minecraft.util.math.BlockPos;

public class StructureSelector implements IStructureSelector {

	private BlockPos pos1;
	private BlockPos pos2;

	@Override
	public void setPos1(BlockPos pos) {
		this.pos1 = pos;
	}

	@Override
	public BlockPos getPos1() {
		return pos1;
	}

	@Override
	public void setPos2(BlockPos pos) {
		this.pos2 = pos;
	}

	@Override
	public BlockPos getPos2() {
		return pos2;
	}

}
