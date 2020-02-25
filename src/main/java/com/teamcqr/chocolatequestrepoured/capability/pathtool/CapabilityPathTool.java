package com.teamcqr.chocolatequestrepoured.capability.pathtool;

import net.minecraft.util.math.BlockPos;

public class CapabilityPathTool {

	private BlockPos[] points = new BlockPos[] {};
	
	public BlockPos[] getPathPoints() {
		return points;
	}

	public void addPathPoint(BlockPos position) {
		BlockPos[] newPosArr = new BlockPos[this.points.length +1];
		for(int i = 0; i < this.points.length; i++) {
			newPosArr[i] = this.points[i];
		}
		newPosArr[this.points.length] = position;
		this.points = newPosArr;
	}
	
	public void clearPathPoints() {
		this.points = new BlockPos[] {};
	}
	
	public void setPathPoints(final BlockPos[] points) {
		this.points = points;
	}

}
