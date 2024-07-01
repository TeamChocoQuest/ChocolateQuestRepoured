package team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import team.cqr.cqrepoured.common.primitive.IntUtil;

public class NoiseContributionCache {

	public static record NoiseConfiguration(int radiusXZ, int radiusDown, int radiusUp, double factorHorizontal, double factorDown, double factorUp) {

		public BoundingBox extend(BoundingBox boundingBox) {
			return new BoundingBox(boundingBox.minX() - this.radiusXZ, boundingBox.minY() - this.radiusDown - 1, boundingBox.minZ() - this.radiusXZ,
					boundingBox.maxX() + this.radiusXZ, boundingBox.maxY() + this.radiusUp, boundingBox.maxZ() + this.radiusXZ);
		}

		private int getHorizontalSpan() {
			return this.radiusXZ * 2 + 1;
		}

		private int getVerticalSpan() {
			return this.radiusDown + this.radiusUp + 1;
		}

	}

	private static final ConcurrentMap<NoiseConfiguration, NoiseContributionCache> INSTANCES = new ConcurrentHashMap<>();
	private final NoiseConfiguration config;
	private final double[] beardKernel;

	public NoiseContributionCache(NoiseConfiguration noiseConfiguration) {
		this.config = noiseConfiguration;
		this.beardKernel = IntUtil
				.streamXYZClosed(-this.config.radiusXZ, -this.config.radiusDown, -this.config.radiusXZ, this.config.radiusXZ, this.config.radiusUp,
						this.config.radiusXZ, this::computeBeardContribution)
				.toArray();
	}

	public static NoiseContributionCache get(NoiseConfiguration noiseConfiguration) {
		return INSTANCES.computeIfAbsent(
				noiseConfiguration,
				NoiseContributionCache::new);
	}

	private int index(int x, int y, int z) {
		return ((x + this.config.radiusXZ) * this.config.getVerticalSpan() + (y + this.config.radiusDown)) * this.config.getHorizontalSpan() + (z + this.config.radiusXZ);
	}

	private double computeBeardContribution(int x, int y, int z) {
		return Math.exp(-Mth.lengthSquared(this.config.factorHorizontal * x, (y > 0 ? this.config.factorUp : this.config.factorDown) * y, this.config.factorHorizontal * z) / 16.0D);
	}

	public double getBeardContribution(int x, int y, int z, int height) {
		return NoiseContributionCache.getBeardMultiplier(x, y, z, height) * this.beardKernel[this.index(x, y, z)];
	}

	private static double getBeardMultiplier(int x, int y, int z, int height) {
		double x0 = Math.max(Math.abs(x) - 1.0D, 0.0D);
		if (x0 == 0 && (height >= 0 && y < 0 || height < 0 && y > 0)) {
			x0++;
		}
		double y0 = height + 0.5D;
		return -y0 / Mth.length(x0, y0, z);
	}

}
