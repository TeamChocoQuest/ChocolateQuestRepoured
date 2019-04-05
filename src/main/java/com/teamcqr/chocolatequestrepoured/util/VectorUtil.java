package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.util.math.Vec3i;

public class VectorUtil {
	
	public static enum EAxis {
		AXIS_Y,
		AXIS_X,
		AXIS_Z;
	}
	public static Vec3i rotateVectorAroundY(Vec3i vector, double degrees) {
	    double rad = Math.toRadians(degrees);

	    double currentX = vector.getX();
	    double currentZ = vector.getZ();

	    double cosine = Math.cos(rad);
	    double sine = Math.sin(rad);

	    return new Vec3i((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
	}
	public static Vec3i rotateVector(EAxis axis, Vec3i vector, Double degrees) {
		double rad = Math.toRadians(degrees);

	    double currentX = vector.getX();
	    double currentZ = vector.getZ();
	    double currentY = vector.getY();
	    
	    double cosine = Math.cos(rad);
		double sine = Math.sin(rad);
		
		switch(axis) {
		case AXIS_X:
			return new Vec3i(vector.getX(), (currentY * cosine - currentZ * sine), (currentY * sine - currentZ * cosine));
		case AXIS_Y:
			return new Vec3i((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
		case AXIS_Z:
			return new Vec3i((currentX * cosine - currentY * sine), (currentX * sine - currentY * cosine), vector.getZ());
		default:
			break;
		
		}
		return null;
	}

}
