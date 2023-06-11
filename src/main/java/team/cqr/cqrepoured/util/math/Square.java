package team.cqr.cqrepoured.util.math;

import org.joml.Vector3d;

public class Square {

	public final Vector3d vec1;
	public final Vector3d vec2;
	public final Vector3d vec3;
	public final Vector3d vec4;
	private final Vector3d normal;

	public Square(Vector3d vec1, Vector3d vec2, Vector3d vec3, Vector3d vec4) {
		this.vec1 = vec1;
		this.vec2 = vec2;
		this.vec3 = vec3;
		this.vec4 = vec4;
		this.normal = vec2.subtract(vec1).cross(vec3.subtract(vec1));
	}

	public boolean isVecBehindPlane(Vector3d vec) {
		return vec.subtract(this.vec1).dot(this.normal) >= 0.0D;
	}

	public Vector3d getVec1() {
		return this.vec1;
	}

	public Vector3d getVec2() {
		return this.vec2;
	}

	public Vector3d getVec3() {
		return this.vec3;
	}

	public Vector3d getVec4() {
		return this.vec4;
	}

	public Vector3d getNormal() {
		return this.normal;
	}

}
