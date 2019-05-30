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
		rand = random;//new Random();
	}

	public double getNoiseAt(int x, int y, int z) {
		int ymin = (int) Math.floor((double) y / (double) this.frequency);
		int ymax = ymin + 1;
		return (double) this.cosineInterpolate((float) this.getNoiseLevelAtPosition(x, ymin, z),
				(float) this.getNoiseLevelAtPosition(x, ymax, z),
				((float) y - (float) ymin * (float) this.frequency) / (float) this.frequency);
	}

	private double getNoiseLevelAtPosition(int x, int y, int z) {
		int xmin = (int) Math.floor((double) x / (double) this.frequency);
		int xmax = xmin + 1;
		int zmin = (int) Math.floor((double) z / (double) this.frequency);
		int zmax = zmin + 1;
		return (double)cosineInterpolate(
				cosineInterpolate((float) getRandomAtPosition(xmin, y, zmin),
						(float) getRandomAtPosition(xmax, y, zmin), 
						(float) (x - xmin * frequency) / (float)frequency),
				cosineInterpolate((float) getRandomAtPosition(xmin, y, zmax),
						(float) getRandomAtPosition(xmax, y, zmax), 
						(float) (x - xmin * frequency) / (float) frequency),
				((float) z - (float) zmin * (float) this.frequency) / (float) this.frequency);
	}

	private float cosineInterpolate(float a, float b, float x) {
		float f = (float) ((1.0D - Math.cos((double)x * Math.PI)) * 0.5D);
		return a * (1.0F - f) + b * f;
	}

	@SuppressWarnings("unused")
	private float linearInterpolate(float a, float b, float x) {
		return a * (1.0F - x) + b * x;
	}

	private double getRandomAtPosition(int x, int y, int z) {
		Double seedD = new Double((10000.0D * (Math.sin(x) + Math.cos(z) + Math.cos(y) + Math.tan((double)this.seed))));
		//System.out.println("Double value: " + seedD);
		//System.out.println("double value: " + seedD.doubleValue());
		rand.setSeed(seedD.longValue());
		//System.out.println("long value: " + seedD.longValue());
		//System.out.println("long value with cast: " + ((long) seedD.doubleValue()));
		return rand.nextDouble();
	}

}
