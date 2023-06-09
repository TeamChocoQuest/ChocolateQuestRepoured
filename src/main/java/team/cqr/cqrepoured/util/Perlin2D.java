package team.cqr.cqrepoured.util;

import java.util.Random;

import meldexun.randomutil.FastRandom;
import net.minecraft.util.Mth;
import team.cqr.cqrepoured.util.math.InterpolationUtil;

public class Perlin2D {

	private final Random rand = new FastRandom();
	private float tanSeed;
	private float frequency;

	public Perlin2D(long seed, int octave) {
		this.setup(seed, octave);
	}

	public void setup(long seed, int octave) {
		this.tanSeed = (float) Math.tan((double) seed);
		this.frequency = 1.0F / (float) octave;
	}

	public float getNoiseAt(float x, float y) {
		x *= this.frequency;
		int minX = Mth.floor(x);
		int maxX = minX + 1;
		float fracX = x - minX;

		y *= this.frequency;
		int minY = Mth.floor(y);
		int maxY = minY + 1;
		float fracY = y - minY;

		float f00 = this.getRandomAtPosition(minX, minY);
		float f01 = this.getRandomAtPosition(minX, maxY);
		float f10 = this.getRandomAtPosition(maxX, minY);
		float f11 = this.getRandomAtPosition(maxX, maxY);

		return InterpolationUtil.SMOOTHSTEP.interpolate(f00, f01, f10, f11, fracX, fracY);
	}

	private float getRandomAtPosition(float x, float y) {
		long newSeed = (long) ((Mth.sin(x) + Mth.cos(y) + this.tanSeed) * 10000.0F);
		this.rand.setSeed(newSeed);
		return this.rand.nextFloat();
	}

}
