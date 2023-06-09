package team.cqr.cqrepoured.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.math.vector.Vector3i;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class VectorUtil {

	public enum EAxis {
		AXIS_Y, AXIS_X, AXIS_Z;
	}

	/*
	 * Important: Mathematical positive => turn left around
	 */
	public static Vec3 rotateVectorAroundY(Vec3 vector, double degrees) {
		return vector.yRot((float) Math.toRadians(degrees));
	}

	public static double getAngleBetween(final Vec3 axis, final Vec3 vector) {
		double cosphi = axis.dot(vector);
		cosphi /= axis.length() * vector.length();
		double phi = Math.acos(cosphi);
		return Math.toDegrees(phi);
	}

	public static Vector3i rotateVector(EAxis axis, Vector3i vector, Double degrees) {
		double rad = Math.toRadians(degrees);

		double currentX = vector.getX();
		double currentZ = vector.getZ();
		double currentY = vector.getY();

		double cosine = Math.cos(rad);
		double sine = Math.sin(rad);

		switch (axis) {
		case AXIS_X:
			return new Vector3i(vector.getX(), (currentY * cosine - currentZ * sine), (currentY * sine - currentZ * cosine));
		case AXIS_Y:
			return new Vector3i((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
		case AXIS_Z:
			return new Vector3i((currentX * cosine - currentY * sine), (currentX * sine - currentY * cosine), vector.getZ());
		default:
			break;

		}
		return null;
	}

	public static Vector3i rotateVectorAroundY(Vector3i newPos, double degrees) {
		Vec3 res = rotateVectorAroundY(new Vec3(newPos.getX(), newPos.getY(), newPos.getZ()), degrees);
		return new Vector3i(Math.floor(res.x), Math.floor(res.y), Math.floor(res.z));
	}

	public static Vector3i vectorAdd(Vector3i start, int x, int y, int z) {
		return new Vector3i(start.getX() + x, start.getY() + y, start.getZ() + z);
	}

	public static CompoundTag createVectorNBTTag(Vec3 vector) {
		CompoundTag nbttagcompound = new CompoundTag();
		nbttagcompound.putDouble("X", vector.x);
		nbttagcompound.putDouble("Y", vector.y);
		nbttagcompound.putDouble("Z", vector.z);
		return nbttagcompound;
	}

	public static Vec3 getVectorFromTag(CompoundTag tag) {
		return new Vec3(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
	}

	/**
	 * @param axis Needs to be normalized!
	 */
	public static Vec3 rotateAroundAnyAxis(Vec3 axis, Vec3 toBeRotated, double rotationDegrees) {
		return rotate(axis, toBeRotated, Math.toRadians(rotationDegrees));
	}

	/**
	 * @param axis Needs to be normalized!
	 */
	public static Vec3 rotate(Vec3 axis, Vec3 vec, double radian) {
		// setup quaternion
		double d = Mth.sin((float) (radian * 0.5D));
		double i = d * axis.x;
		double j = d * axis.y;
		double k = d * axis.z;
		double r = Mth.cos((float) (radian * 0.5D));

		// setup rotation matrix
		double i2 = 2.0D * i * i;
		double j2 = 2.0D * j * j;
		double k2 = 2.0D * k * k;
		double ij = 2.0D * i * j;
		double jk = 2.0D * j * k;
		double ik = 2.0D * i * k;
		double ir = 2.0D * i * r;
		double jr = 2.0D * j * r;
		double kr = 2.0D * k * r;

		double d00 = 1 - (j2 + k2);
		double d01 = (ij - kr);
		double d02 = (ik + jr);

		double d10 = (ij + kr);
		double d11 = 1 - (i2 + k2);
		double d12 = (jk - ir);

		double d20 = (ik - jr);
		double d21 = (jk + ir);
		double d22 = 1 - (i2 + j2);

		// rotate vertex
		return new Vec3(vec.x * d00 + vec.y * d01 + vec.z * d02, vec.x * d10 + vec.y * d11 + vec.z * d12, vec.x * d20 + vec.y * d21 + vec.z * d22);
	}

}
