package com.teamcqr.chocolatequestrepoured.util.math;

import net.minecraft.util.math.Vec3d;

public class Line {

	public final Vec3d vec1;
	public final Vec3d vec2;

	public Line(Vec3d vec1, Vec3d vec2) {
		this.vec1 = vec1;
		this.vec2 = vec2;
	}

	public Vec3d getVec1() {
		return this.vec1;
	}

	public Vec3d getVec2() {
		return this.vec2;
	}

}
