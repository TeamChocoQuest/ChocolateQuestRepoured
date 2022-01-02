package team.cqr.cqrepoured.util.math;

import net.minecraft.util.math.vector.Vector3d;

public class Line {

	public final Vector3d vec1;
	public final Vector3d vec2;

	public Line(Vector3d vec1, Vector3d vec2) {
		this.vec1 = vec1;
		this.vec2 = vec2;
	}

	public Vector3d getVec1() {
		return this.vec1;
	}

	public Vector3d getVec2() {
		return this.vec2;
	}

}
