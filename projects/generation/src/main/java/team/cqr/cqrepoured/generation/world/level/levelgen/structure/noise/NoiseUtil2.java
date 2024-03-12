package team.cqr.cqrepoured.generation.world.level.levelgen.structure.noise;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import team.cqr.cqrepoured.common.primitive.IntUtil;

public class NoiseUtil2 {

	public static record NoiseConfiguration(int radiusXZ, int radiusY, double factorXZ, double factorY) {

		public BoundingBox extend(BoundingBox boundingBox) {
			return new BoundingBox(boundingBox.minX() - this.radiusXZ, boundingBox.minY() - this.radiusY, boundingBox.minZ() - this.radiusXZ,
					boundingBox.maxX() + this.radiusXZ, boundingBox.maxY() + this.radiusY, boundingBox.maxZ() + this.radiusXZ);
		}

		public BoundingBox unextend(BoundingBox boundingBox) {
			return new BoundingBox(boundingBox.minX() + this.radiusXZ, boundingBox.minY() + this.radiusY, boundingBox.minZ() + this.radiusXZ,
					boundingBox.maxX() - this.radiusXZ, boundingBox.maxY() - this.radiusY, boundingBox.maxZ() - this.radiusXZ);
		}

	}

	private static final ConcurrentMap<NoiseConfiguration, NoiseUtil2> INSTANCES = new ConcurrentHashMap<>();
	private final NoiseConfiguration config;
	private final int diameterXZ;
	private final int diameterY;
	private final double[] beardKernel;
	private final Vec3i[] nearestPositions;

	public NoiseUtil2(NoiseConfiguration noiseConfiguration) {
		this.config = noiseConfiguration;
		this.diameterXZ = this.config.radiusXZ * 2 + 1;
		this.diameterY = this.config.radiusY * 2 + 1;
		this.beardKernel = IntUtil
				.streamXYZClosed(-this.config.radiusXZ, -this.config.radiusY, -this.config.radiusXZ, this.config.radiusXZ, this.config.radiusY,
						this.config.radiusXZ, this::computeBeardContribution)
				.toArray();
		this.nearestPositions = IntUtil
				.streamXYZClosed(-this.config.radiusXZ, -this.config.radiusY, -this.config.radiusXZ, this.config.radiusXZ, this.config.radiusY,
						this.config.radiusXZ, Vec3i::new)
				.sorted(Comparator.comparingDouble(v -> v.distSqr(Vec3i.ZERO)))
				.toArray(Vec3i[]::new);
	}

	public static NoiseUtil2 get(NoiseConfiguration noiseConfiguration) {
		return INSTANCES.computeIfAbsent(noiseConfiguration, NoiseUtil2::new);
	}

	private int index(int x, int y, int z) {
		return ((x + this.config.radiusXZ) * this.diameterY + (y + this.config.radiusY)) * this.diameterXZ + (z + this.config.radiusXZ);
	}

	private double computeBeardContribution(int x, int y, int z) {
		return Math.exp(-Mth.lengthSquared(this.config.factorXZ * x, this.config.factorY * (y + 0.5D), this.config.factorXZ * z) / 16.0D);
	}

	public boolean isInKernelRange(int x, int y, int z) {
		if (x < -this.config.radiusXZ || x > this.config.radiusXZ)
			return false;
		if (y < -this.config.radiusY || y > this.config.radiusY)
			return false;
		if (z < -this.config.radiusXZ || z > this.config.radiusXZ)
			return false;
		return true;
	}

	@SuppressWarnings("deprecation")
	public double getBeardContribution(int x, int y, int z, int height) {
		double y0 = height + 0.5D;
		double d1 = Mth.lengthSquared(x, y0, z);
		double d2 = -y0 * Mth.fastInvSqrt(d1 / 2.0D) / 2.0D;
		return d2 * this.beardKernel[index(x, y, z)];
	}

	public Vec3i[] getNearestPositions() {
		return this.nearestPositions;
	}

}
