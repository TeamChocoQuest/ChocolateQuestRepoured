package com.teamcqr.chocolatequestrepoured.capability.structureselector;

import net.minecraft.util.math.BlockPos;

public class CapabilityStructureSelector {

	private BlockPos pos1;
	private BlockPos pos2;

	public void setPos1(BlockPos pos) {
		this.pos1 = pos;
	}

	public BlockPos getPos1() {
		return this.pos1;
	}

	public void setPos2(BlockPos pos) {
		this.pos2 = pos;
	}

	public BlockPos getPos2() {
		return this.pos2;
	}

	public boolean hasPos1() {
		return this.pos1 != null;
	}

	public boolean hasPos2() {
		return this.pos2 != null;
	}

	public boolean hasPos1AndPos2() {
		return this.hasPos1() && this.hasPos2();
	}

}
