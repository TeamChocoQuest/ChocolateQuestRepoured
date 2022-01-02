package team.cqr.cqrepoured.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
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
	public static Vector3d rotateVectorAroundY(Vector3d vector, double degrees) {
		return vector.rotateYaw((float) Math.toRadians(degrees));
	}

	public static double getAngleBetween(final Vector3d axis, final Vector3d vector) {
		double cosphi = axis.dotProduct(vector);
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
		Vector3d res = rotateVectorAroundY(new Vector3d(newPos.getX(), newPos.getY(), newPos.getZ()), degrees);
		return new Vector3i(Math.floor(res.x), Math.floor(res.y), Math.floor(res.z));
	}

	public static Vector3i vectorAdd(Vector3i start, int x, int y, int z) {
		return new Vector3i(start.getX() + x, start.getY() + y, start.getZ() + z);
	}

	public static CompoundNBT createVectorNBTTag(Vector3d vector) {
		CompoundNBT nbttagcompound = new CompoundNBT();
		nbttagcompound.setDouble("X", vector.x);
		nbttagcompound.setDouble("Y", vector.y);
		nbttagcompound.setDouble("Z", vector.z);
		return nbttagcompound;
	}

	public static Vector3d getVectorFromTag(CompoundNBT tag) {
		return new Vector3d(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
	}

	/**
	 * @param axis Needs to be normalized!
	 */
	public static Vector3d rotateAroundAnyAxis(Vector3d axis, Vector3d toBeRotated, double rotationDegrees) {
		return rotate(axis, toBeRotated, Math.toRadians(rotationDegrees));
	}

	/**
	 * @param axis Needs to be normalized!
	 */
	public static Vector3d rotate(Vector3d axis, Vector3d vec, double radian) {
		// setup quaternion
		double d = MathHelper.sin((float) (radian * 0.5D));
		double i = d * axis.x;
		double j = d * axis.y;
		double k = d * axis.z;
		double r = MathHelper.cos((float) (radian * 0.5D));

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
		return new Vector3d(vec.x * d00 + vec.y * d01 + vec.z * d02, vec.x * d10 + vec.y * d11 + vec.z * d12, vec.x * d20 + vec.y * d21 + vec.z * d22);
	}

}
