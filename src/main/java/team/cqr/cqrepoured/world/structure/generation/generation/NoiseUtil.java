package team.cqr.cqrepoured.world.structure.generation.generation;

import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import team.cqr.cqrepoured.util.IntUtil;

public class NoiseUtil {

	public static final int RADIUS = 16;
	private static final int DIAMETER = RADIUS * 2 + 1;
	private static final double[] BEARD_KERNEL = Util.make(new double[DIAMETER * DIAMETER * DIAMETER], array -> {
		IntUtil.forEachXYZClosed(-RADIUS, -RADIUS, -RADIUS, RADIUS, RADIUS, RADIUS, (x, y, z) -> {
			array[index(x, y, z)] = computeContribution(x, y, z);
		});
	});

	private static double computeContribution(int x, int y, int z) {
		double d0 = x * x + z * z;
		double d1 = y - 0.5D;
		double d2 = d1 * d1;
		double d3 = Math.pow(Math.E, -(d2 / 16.0D + d0 / 16.0D));
		double d4 = -d1 * Mth.fastInvSqrt(d2 / 2.0D + d0 / 2.0D) / 2.0D;
		return d4 * d3;
	}

	private static int index(int x, int y, int z) {
		return ((x + RADIUS) * DIAMETER + (y + RADIUS)) * DIAMETER + (z + RADIUS);
	}

	public static double getContribution(int x, int y, int z) {
		return BEARD_KERNEL[index(x, y, z)];
	}

}
