package team.cqr.cqrepoured.util.math;

import net.minecraft.world.phys.Vec3;

public class Square {

	public final Vec3 vec1;
	public final Vec3 vec2;
	public final Vec3 vec3;
	public final Vec3 vec4;
	private final Vec3 normal;

	public Square(Vec3 vec1, Vec3 vec2, Vec3 vec3, Vec3 vec4) {
		this.vec1 = vec1;
		this.vec2 = vec2;
		this.vec3 = vec3;
		this.vec4 = vec4;
		this.normal = vec2.subtract(vec1).cross(vec3.subtract(vec1));
	}

	public boolean isVecBehindPlane(Vec3 vec) {
		return vec.subtract(this.vec1).dot(this.normal) >= 0.0D;
	}

	public Vec3 getVec1() {
		return this.vec1;
	}

	public Vec3 getVec2() {
		return this.vec2;
	}

	public Vec3 getVec3() {
		return this.vec3;
	}

	public Vec3 getVec4() {
		return this.vec4;
	}

	public Vec3 getNormal() {
		return this.normal;
	}

}
