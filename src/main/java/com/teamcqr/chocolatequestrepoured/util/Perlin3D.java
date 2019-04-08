package com.teamcqr.chocolatequestrepoured.util;

import java.util.Random;

public class Perlin3D {
	// DIRECTLY COPIED OUT OF OLD MOD, IS LEGAL BECAUSE THIS IS AN OPEN SOURCE
	// PUBLIC LIBRARY YOU CAN DOWNLOAD
	private long seed;
	private Random rand;
	private int frequency;

	public Perlin3D(long seed, int octave, Random random) {
		this.seed = seed;
		frequency = octave;
		rand = new Random();
	}

	public double getNoiseAt(int x, int y, int z) {
		int ymin = (int) Math.floor(y / frequency);
		int ymax = ymin + 1;
		return cosineInterpolate((float) getNoiseLevelAtPosition(x, ymin, z),
				(float) getNoiseLevelAtPosition(x, ymax, z), (y - ymin * frequency) / frequency);
	}

	private double getNoiseLevelAtPosition(int x, int y, int z) {
		int xmin = (int) Math.floor(x / frequency);
		int xmax = xmin + 1;
		int zmin = (int) Math.floor(z / frequency);
		int zmax = zmin + 1;
		return cosineInterpolate(
				cosineInterpolate((float) getRandomAtPosition(xmin, y, zmin),
						(float) getRandomAtPosition(xmax, y, zmin), (x - xmin * frequency) / frequency),
				cosineInterpolate((float) getRandomAtPosition(xmin, y, zmax),
						(float) getRandomAtPosition(xmax, y, zmax), (x - xmin * frequency) / frequency),
				(z - zmin * frequency) / frequency);
	}

	private float cosineInterpolate(float a, float b, float x) {
		float f = (float) ((1.0D - Math.cos(x * 3.141592653589793D)) * 0.5D);
		return a * (1.0F - f) + b * f;
	}

	@SuppressWarnings("unused")
	private float linearInterpolate(float a, float b, float x) {
		return a * (1.0F - x) + b * x;
	}

	private double getRandomAtPosition(int x, int y, int z) {
		rand.setSeed((long) (10000.0D * (Math.sin(x) + Math.cos(z) + Math.cos(y) + Math.tan(seed))));
		return rand.nextDouble();
	}

}
