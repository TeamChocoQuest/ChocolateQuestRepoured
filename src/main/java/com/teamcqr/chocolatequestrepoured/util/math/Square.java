package com.teamcqr.chocolatequestrepoured.util.math;

import net.minecraft.util.math.Vec3d;

public class Square {

	public final Vec3d vec1;
	public final Vec3d vec2;
	public final Vec3d vec3;
	public final Vec3d vec4;
	private final Vec3d normal;

	public Square(Vec3d vec1, Vec3d vec2, Vec3d vec3, Vec3d vec4) {
		this.vec1 = vec1;
		this.vec2 = vec2;
		this.vec3 = vec3;
		this.vec4 = vec4;
		this.normal = vec2.subtract(vec1).crossProduct(vec3.subtract(vec1));
	}

	public boolean isVecBehindPlane(Vec3d vec) {
		return vec.subtract(this.vec1).dotProduct(this.normal) >= 0.0D;
	}

	public Vec3d getVec1() {
		return this.vec1;
	}

	public Vec3d getVec2() {
		return this.vec2;
	}

	public Vec3d getVec3() {
		return this.vec3;
	}

	public Vec3d getVec4() {
		return this.vec4;
	}

	public Vec3d getNormal() {
		return this.normal;
	}

}
