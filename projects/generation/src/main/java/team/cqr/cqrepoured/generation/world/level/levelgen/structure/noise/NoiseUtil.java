package team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise;

import net.minecraft.Util;
import net.minecraft.util.Mth;
import team.cqr.cqrepoured.common.primitive.IntUtil;

public class NoiseUtil {

	public static final int RADIUS = 16;
	private static final int DIAMETER = RADIUS * 2 + 1;
	private static final double[] BEARD_KERNEL = Util.make(new double[DIAMETER * DIAMETER * DIAMETER], array -> {
		IntUtil.forEachXYZClosed(-RADIUS, -RADIUS, -RADIUS, RADIUS, RADIUS, RADIUS, (x, y, z) -> {
			array[index(x, y, z)] = computeContribution(x, y, z);
		});
	});

	private static double computeContribution(int x, int y, int z) {
		double y1 = y - 0.5D;
		double l = Mth.lengthSquared(x, y1, z);
		double d0 = -y1 / Math.sqrt(l / 2.0D) / 2.0D;
		double d1 = Math.pow(Math.E, -l / 16.0D);
		return d0 * d1;
	}

	private static int index(int x, int y, int z) {
		return ((x + RADIUS) * DIAMETER + (y + RADIUS)) * DIAMETER + (z + RADIUS);
	}

	public static double getContribution(int x, int y, int z) {
		return BEARD_KERNEL[index(x, y, z)];
	}

}
