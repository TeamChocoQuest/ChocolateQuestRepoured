package team.cqr.cqrepoured.client.occlusion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.PartialTicksUtil;

@SideOnly(Side.CLIENT)
public class EntityOcclusionTester {

	private static final CachedBlockAccess CACHED_BLOCK_ACCESS = new CachedBlockAccess();
	private static double camX;
	private static double camY;
	private static double camZ;
	private static double minX;
	private static double minY;
	private static double minZ;
	private static double maxX;
	private static double maxY;
	private static double maxZ;
	private static long resultCache;

	public static void onPreRenderTickEvent() {
		Minecraft mc = Minecraft.getMinecraft();
		CACHED_BLOCK_ACCESS.init(mc.world);

		Entity entity = mc.getRenderViewEntity();
		if (entity != null) {
			double partialTick = PartialTicksUtil.getCurrentPartialTicks();
			double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTick;
			double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTick;
			double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTick;
			Vec3d cam = ActiveRenderInfo.getCameraPosition();
			camX = x + cam.x;
			camY = y + cam.y;
			camZ = z + cam.z;
		}
	}

	public static void onPostRenderTickEvent() {
		CACHED_BLOCK_ACCESS.clear();
	}

	public static boolean isNotOccluded(AbstractEntityCQR entity) {
		if (CQRMain.isEntityCullingInstalled) {
			return true;
		}
		if (!CQRConfig.advanced.skipHiddenEntityRendering) {
			return true;
		}
		if (!entity.isNonBoss()) {
			return true;
		}

		AxisAlignedBB aabb = entity.getRenderBoundingBox();
		minX = aabb.minX - 0.5D;
		minY = aabb.minY - 0.5D;
		minZ = aabb.minZ - 0.5D;
		maxX = aabb.maxX + 0.5D;
		maxY = aabb.maxY + 0.5D;
		maxZ = aabb.maxZ + 0.5D;

		return isAABBVisible();
	}

	private static boolean isAABBVisible() {
		if (camX >= minX && camX <= maxX && camY >= minY && camY <= maxY && camZ >= minZ && camZ <= maxZ) {
			return true;
		}

		resultCache = 0L;

		if (camX < minX) {
			if (isPointOnPlaneVisible(0, EntityOcclusionTester::rayTraceBlocksYZX)) {
				return true;
			}
		} else if (camX > maxX) {
			if (isPointOnPlaneVisible(2, EntityOcclusionTester::rayTraceBlocksYZX)) {
				return true;
			}
		}
		if (camY < minY) {
			if (isPointOnPlaneVisible(0, EntityOcclusionTester::rayTraceBlocksXZY)) {
				return true;
			}
		} else if (camY > maxY) {
			if (isPointOnPlaneVisible(2, EntityOcclusionTester::rayTraceBlocksXZY)) {
				return true;
			}
		}
		if (camZ < minZ) {
			if (isPointOnPlaneVisible(0, EntityOcclusionTester::rayTraceBlocksXYZ)) {
				return true;
			}
		} else if (camZ > maxZ) {
			if (isPointOnPlaneVisible(2, EntityOcclusionTester::rayTraceBlocksXYZ)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isPointOnPlaneVisible(int z, IntIntIntPredicate predicate) {
		if (predicate.test(1, 1, z)) {
			return true;
		}

		if (predicate.test(0, 0, z)) {
			return true;
		}
		if (predicate.test(0, 2, z)) {
			return true;
		}
		if (predicate.test(2, 0, z)) {
			return true;
		}
		if (predicate.test(2, 2, z)) {
			return true;
		}

		return false;
	}

	private static boolean rayTraceBlocksYZX(int y, int z, int x) {
		return rayTraceBlocksCached(x, y, z);
	}

	private static boolean rayTraceBlocksXZY(int x, int z, int y) {
		return rayTraceBlocksCached(x, y, z);
	}

	private static boolean rayTraceBlocksXYZ(int x, int y, int z) {
		return rayTraceBlocksCached(x, y, z);
	}

	private static boolean rayTraceBlocksCached(int x, int y, int z) {
		int offset = ((x * 3 + y) * 3 + z) * 2;
		long value = (resultCache >>> offset) & 3;
		if (value == 0) {
			double endX = minX + (maxX - minX) * x * 0.5D;
			double endY = minY + (maxY - minY) * y * 0.5D;
			double endZ = minZ + (maxZ - minZ) * z * 0.5D;
			double threshold = CQRConfig.advanced.skipHiddenEntityRenderingDiff;
			value = raytraceThreshold(camX, camY, camZ, endX, endY, endZ, threshold) ? 2 : 1;
			resultCache |= value << offset;
		}
		return value == 2;
	}

	private static boolean raytraceThreshold(double startX, double startY, double startZ, double endX, double endY, double endZ, double threshold) {
		if (threshold <= 0.0D) {
			return raytrace(startX, startY, startZ, endX, endY, endZ);
		}

		double dirX = endX - startX;
		double dirY = endY - startY;
		double dirZ = endZ - startZ;

		if (dirX * dirX + dirY * dirY + dirZ * dirZ <= threshold * threshold) {
			return true;
		}

		int x = floor(startX);
		int y = floor(startY);
		int z = floor(startZ);
		int incX = signum(dirX);
		int incY = signum(dirY);
		int incZ = signum(dirZ);
		double dx = incX == 0 ? Double.MAX_VALUE : incX / dirX;
		double dy = incY == 0 ? Double.MAX_VALUE : incY / dirY;
		double dz = incZ == 0 ? Double.MAX_VALUE : incZ / dirZ;
		double percentX = dx * (incX > 0 ? 1.0D - frac(startX) : frac(startX));
		double percentY = dy * (incY > 0 ? 1.0D - frac(startY) : frac(startY));
		double percentZ = dz * (incZ > 0 ? 1.0D - frac(startZ) : frac(startZ));
		Axis axis;

		if (isOpaque(x, y, z)) {
			double d1 = Math.min(Math.min(Math.min(percentX, percentY), percentZ), 1.0D);
			double nextHitX = startX + dirX * d1;
			double nextHitY = startY + dirY * d1;
			double nextHitZ = startZ + dirZ * d1;

			threshold -= dist(startX, startY, startZ, nextHitX, nextHitY, nextHitZ);
			if (threshold <= 0.0D) {
				return false;
			}
		}

		while (percentX <= 1.0D || percentY <= 1.0D || percentZ <= 1.0D) {
			if (percentX < percentY) {
				if (percentX < percentZ) {
					x += incX;
					percentX += dx;
					axis = Axis.X;
				} else {
					z += incZ;
					percentZ += dz;
					axis = Axis.Z;
				}
			} else if (percentY < percentZ) {
				y += incY;
				percentY += dy;
				axis = Axis.Y;
			} else {
				z += incZ;
				percentZ += dz;
				axis = Axis.Z;
			}

			if (isOpaque(x, y, z)) {
				double d = Math.min(axis != Axis.X ? (axis != Axis.Y ? percentZ - dz : percentY - dy) : percentX - dx, 1.0D);
				double hitX = startX + dirX * d;
				double hitY = startY + dirY * d;
				double hitZ = startZ + dirZ * d;

				double d1 = Math.min(Math.min(Math.min(percentX, percentY), percentZ), 1.0D);
				double nextHitX = startX + dirX * d1;
				double nextHitY = startY + dirY * d1;
				double nextHitZ = startZ + dirZ * d1;

				threshold -= dist(hitX, hitY, hitZ, nextHitX, nextHitY, nextHitZ);
				if (threshold <= 0.0D) {
					return false;
				}
			}
		}

		return true;
	}

	private static boolean raytrace(double startX, double startY, double startZ, double endX, double endY, double endZ) {
		int x = floor(startX);
		int y = floor(startY);
		int z = floor(startZ);

		if (isOpaque(x, y, z)) {
			return false;
		}

		double dirX = endX - startX;
		double dirY = endY - startY;
		double dirZ = endZ - startZ;
		int incX = signum(dirX);
		int incY = signum(dirY);
		int incZ = signum(dirZ);
		double dx = incX == 0 ? Double.MAX_VALUE : incX / dirX;
		double dy = incY == 0 ? Double.MAX_VALUE : incY / dirY;
		double dz = incZ == 0 ? Double.MAX_VALUE : incZ / dirZ;
		double percentX = dx * (incX > 0 ? 1.0D - frac(startX) : frac(startX));
		double percentY = dy * (incY > 0 ? 1.0D - frac(startY) : frac(startY));
		double percentZ = dz * (incZ > 0 ? 1.0D - frac(startZ) : frac(startZ));

		while (percentX <= 1.0D || percentY <= 1.0D || percentZ <= 1.0D) {
			if (percentX < percentY) {
				if (percentX < percentZ) {
					x += incX;
					percentX += dx;
				} else {
					z += incZ;
					percentZ += dz;
				}
			} else if (percentY < percentZ) {
				y += incY;
				percentY += dy;
			} else {
				z += incZ;
				percentZ += dz;
			}

			if (isOpaque(x, y, z)) {
				return false;
			}
		}

		return true;
	}

	private static boolean isOpaque(int x, int y, int z) {
		return CACHED_BLOCK_ACCESS.getBlockState(x, y, z).isOpaqueCube();
	}

	public static int signum(double x) {
		if (x > 0.0D) {
			return 1;
		}
		if (x < 0.0D) {
			return -1;
		}
		return 0;
	}

	public static double frac(double number) {
		return number - floor(number);
	}

	public static int floor(double value) {
		int i = (int) value;
		return value < i ? i - 1 : i;
	}

	public static double dist(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt(distSqr(x1, y1, z1, x2, y2, z2));
	}

	public static double distSqr(double x1, double y1, double z1, double x2, double y2, double z2) {
		x2 -= x1;
		y2 -= y1;
		z2 -= z1;
		return x2 * x2 + y2 * y2 + z2 * z2;
	}

}
