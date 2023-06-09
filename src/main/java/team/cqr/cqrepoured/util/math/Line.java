package team.cqr.cqrepoured.util.math;

import net.minecraft.world.phys.Vec3;

public class Line {

	public final Vec3 vec1;
	public final Vec3 vec2;

	public Line(Vec3 vec1, Vec3 vec2) {
		this.vec1 = vec1;
		this.vec2 = vec2;
	}

	public Vec3 getVec1() {
		return this.vec1;
	}

	public Vec3 getVec2() {
		return this.vec2;
	}

}
