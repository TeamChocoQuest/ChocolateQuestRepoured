package com.teamcqr.chocolatequestrepoured.util;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public class Perlin3D {

	// DIRECTLY COPIED OUT OF OLD MOD, IS LEGAL BECAUSE THIS IS AN OPEN SOURCE PUBLIC LIBRARY YOU CAN DOWNLOAD

	private final Random rand = new Random();
	private final long seed;
	private final float tanSeed;
	private final float frequency;

	public Perlin3D(long seed, int octave) {
		this.seed = seed;
		this.frequency = octave;
		this.tanSeed = (float) Math.tan(this.seed);
	}

	public float getNoiseAt(float x, float y, float z) {
		float fx1 = x / this.frequency;
		float xmin = MathHelper.floor(fx1);
		float xmax = xmin + 1.0F;
		float fx2 = fx1 - xmin;

		float fy1 = y / this.frequency;
		float ymin = MathHelper.floor(fy1);
		float ymax = ymin + 1.0F;
		float fy2 = fy1 - ymin;

		float fz1 = z / this.frequency;
		float zmin = MathHelper.floor(fz1);
		float zmax = zmin + 1.0F;
		float fz2 = fz1 - zmin;

		float f1 = this.getRandomAtPosition(xmin, ymin, zmin);
		float f2 = this.getRandomAtPosition(xmin, ymin, zmax);
		float f3 = this.getRandomAtPosition(xmin, ymax, zmin);
		float f4 = this.getRandomAtPosition(xmin, ymax, zmax);
		float f5 = this.getRandomAtPosition(xmax, ymin, zmin);
		float f6 = this.getRandomAtPosition(xmax, ymin, zmax);
		float f7 = this.getRandomAtPosition(xmax, ymax, zmin);
		float f8 = this.getRandomAtPosition(xmax, ymax, zmax);

		float f11 = this.cosineInterpolate(f1, f5, fx2);
		float f12 = this.cosineInterpolate(f2, f6, fx2);
		float f13 = this.cosineInterpolate(f3, f7, fx2);
		float f14 = this.cosineInterpolate(f4, f8, fx2);

		float f21 = this.cosineInterpolate(f11, f12, fz2);
		float f22 = this.cosineInterpolate(f13, f14, fz2);

		return this.cosineInterpolate(f21, f22, fy2);
	}

	private float cosineInterpolate(float a, float b, float x) {
		float d = (1.0F - MathHelper.cos(x * (float) Math.PI)) * 0.5F;
		return a * (1.0F - d) + b * d;
	}

	@SuppressWarnings("unused")
	private float linearInterpolate(float a, float b, float x) {
		return a * (1.0F - x) + b * x;
	}

	private float getRandomAtPosition(float x, float y, float z) {
		long newSeed = (long) ((MathHelper.sin(x) + MathHelper.cos(z) + MathHelper.cos(y) + this.tanSeed) * 10000.0F);
		this.rand.setSeed(newSeed);
		return this.rand.nextFloat();
	}

}
