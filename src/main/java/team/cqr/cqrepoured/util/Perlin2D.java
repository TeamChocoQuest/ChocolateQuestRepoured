package team.cqr.cqrepoured.util;

import java.util.Random;

import net.minecraft.util.math.MathHelper;

public class Perlin2D {

	private final Random rand = new Random();
	private long seed;
	private float tanSeed;
	private float frequency;

	public Perlin2D(long seed, float octave) {
		this.seed = seed;
		this.frequency = octave;
		this.tanSeed = (float) Math.tan(this.seed);
	}

	public void setup(long seed, float octave) {
		this.seed = seed;
		this.frequency = octave;
		this.tanSeed = (float) Math.tan(this.seed);
	}

	public float getNoiseAt(float x, float y) {
		float fx1 = x / this.frequency;
		float xmin = MathHelper.floor(fx1);
		float xmax = xmin + 1.0F;
		float fx2 = fx1 - xmin;

		float fy1 = y / this.frequency;
		float ymin = MathHelper.floor(fy1);
		float ymax = ymin + 1.0F;
		float fy2 = fy1 - ymin;

		float f1 = this.getRandomAtPosition(xmin, ymin);
		float f2 = this.getRandomAtPosition(xmin, ymax);
		float f3 = this.getRandomAtPosition(xmax, ymin);
		float f4 = this.getRandomAtPosition(xmax, ymax);

		float f11 = cosineInterpolate(f1, f2, fy2);
		float f12 = cosineInterpolate(f3, f4, fy2);

		return cosineInterpolate(f11, f12, fx2);
	}

	private static float cosineInterpolate(float a, float b, float x) {
		float d = (1.0F - MathHelper.cos(x * (float) Math.PI)) * 0.5F;
		return a * (1.0F - d) + b * d;
	}

	@SuppressWarnings("unused")
	private static float linearInterpolate(float a, float b, float x) {
		return a * (1.0F - x) + b * x;
	}

	private float getRandomAtPosition(float x, float y) {
		long newSeed = (long) ((MathHelper.sin(x) + MathHelper.cos(y) + this.tanSeed) * 10000.0F);
		this.rand.setSeed(newSeed);
		return this.rand.nextFloat();
	}

}
