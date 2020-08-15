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
		float f1 = y / this.frequency;
		float ymin = MathHelper.floor(f1);
		float ymax = ymin + 1.0F;
		float f2 = f1 - ymin;

		return this.cosineInterpolate(this.getNoiseLevelAtPosition(x, ymin, z), this.getNoiseLevelAtPosition(x, ymax, z), f2);
	}

	private float getNoiseLevelAtPosition(float x, float y, float z) {
		float f1 = x / this.frequency;
		float xmin = MathHelper.floor(f1);
		float xmax = xmin + 1.0F;
		float f2 = f1 - xmin;

		float f3 = z / this.frequency;
		float zmin = MathHelper.floor(f3);
		float zmax = zmin + 1.0F;
		float f4 = f3 - zmin;

		float f5 = this.cosineInterpolate(this.getRandomAtPosition(xmin, y, zmin), this.getRandomAtPosition(xmax, y, zmin), f2);
		float f6 = this.cosineInterpolate(this.getRandomAtPosition(xmin, y, zmax), this.getRandomAtPosition(xmax, y, zmax), f2);
		return this.cosineInterpolate(f5, f6, f4);
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
