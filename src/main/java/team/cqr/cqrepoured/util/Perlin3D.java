package team.cqr.cqrepoured.util;

import java.util.Random;

import meldexun.randomutil.FastRandom;
import net.minecraft.util.Mth;
import team.cqr.cqrepoured.util.math.InterpolationUtil;

public class Perlin3D {

	// DIRECTLY COPIED OUT OF OLD MOD, IS LEGAL BECAUSE THIS IS AN OPEN SOURCE PUBLIC LIBRARY YOU CAN DOWNLOAD

	private final Random rand = new FastRandom();
	private float tanSeed;
	private float frequency;

	public Perlin3D(long seed, int octave) {
		this.setup(seed, octave);
	}

	public void setup(long seed, int octave) {
		this.tanSeed = (float) Math.tan((double) seed);
		this.frequency = 1.0F / (float) octave;
	}

	public float getNoiseAt(float x, float y, float z) {
		x *= this.frequency;
		int minX = Mth.floor(x);
		int maxX = minX + 1;
		float fracX = x - minX;

		y *= this.frequency;
		int minY = Mth.floor(y);
		int maxY = minY + 1;
		float fracY = y - minY;

		z *= this.frequency;
		int minZ = Mth.floor(z);
		int maxZ = minZ + 1;
		float fracZ = z - minZ;

		float f000 = this.getRandomAtPosition(minX, minY, minZ);
		float f001 = this.getRandomAtPosition(minX, minY, maxZ);
		float f010 = this.getRandomAtPosition(minX, maxY, minZ);
		float f011 = this.getRandomAtPosition(minX, maxY, maxZ);
		float f100 = this.getRandomAtPosition(maxX, minY, minZ);
		float f101 = this.getRandomAtPosition(maxX, minY, maxZ);
		float f110 = this.getRandomAtPosition(maxX, maxY, minZ);
		float f111 = this.getRandomAtPosition(maxX, maxY, maxZ);

		return InterpolationUtil.SMOOTHSTEP.interpolate(f000, f001, f010, f011, f100, f101, f110, f111, fracX, fracY, fracZ);
	}

	private float getRandomAtPosition(float x, float y, float z) {
		long newSeed = (long) ((Mth.sin(x) + Mth.cos(z) + Mth.cos(y) + this.tanSeed) * 10000.0F);
		this.rand.setSeed(newSeed);
		return this.rand.nextFloat();
	}

}
