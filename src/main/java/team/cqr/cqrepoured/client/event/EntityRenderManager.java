package team.cqr.cqrepoured.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.util.CachedBlockAccess;
import team.cqr.cqrepoured.util.PartialTicksUtil;

@OnlyIn(Dist.CLIENT)
//@EventBusSubscriber(modid = CQRMain.MODID, value = Dist.CLIENT)
public class EntityRenderManager {

	@FunctionalInterface
	private interface BiDoublePredicate {

		boolean test(double x, double y);

	}

	private static final CachedBlockAccess CACHED_BLOCK_ACCESS = new CachedBlockAccess();
	private static double x;
	private static double y;
	private static double z;
	private static double camX;
	private static double camY;
	private static double camZ;

	@SubscribeEvent
	public static void onRenderTickEvent(RenderTickEvent event) {
		Minecraft mc = Minecraft.getInstance();

		if (event.phase == Phase.START) {
			CACHED_BLOCK_ACCESS.setupCached(mc.level);

			Entity entity = mc.getCameraEntity();
			if (entity != null) {
				double partialTick = PartialTicksUtil.getCurrentPartialTicks();
				x = entity.xOld + (entity.xo - entity.xOld) * partialTick;
				y = entity.yOld + (entity.yo - entity.yOld) * partialTick;
				z = entity.zOld + (entity.zo - entity.zOld) * partialTick;
				//Previously used ActiveRenderInfo
				Vector3d cam = mc.getCameraEntity().position();
				camX = x + cam.x;
				camY = y + cam.y;
				camZ = z + cam.z;
			}
		} else {
			CACHED_BLOCK_ACCESS.clearCache();
		}
	}

	public static boolean shouldEntityBeRendered(AbstractEntityCQR entity) {
		if (CQRMain.isEntityCullingInstalled) {
			return true;
		}
		if (!entity.canChangeDimensions()) {
			return true;
		}
		if (!CQRConfig.SERVER_CONFIG.advanced.skipHiddenEntityRendering.get()) {
			return true;
		}

		AxisAlignedBB aabb = entity.getBoundingBoxForCulling();
		double minX = aabb.minX - 0.5D;
		double minY = aabb.minY - 0.5D;
		double minZ = aabb.minZ - 0.5D;
		double maxX = aabb.maxX + 0.5D;
		double maxY = aabb.maxY + 0.5D;
		double maxZ = aabb.maxZ + 0.5D;

		return isAABBVisible(minX, minY, minZ, maxX, maxY, maxZ);
	}

	private static boolean isAABBVisible(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		if (camX >= minX && camX <= maxX && camY >= minY && camY <= maxY && camZ >= minZ && camZ <= maxZ) {
			return true;
		}

		if (camX < minX) {
			if (isPointOnPlaneVisible(minY, maxY, minZ, maxZ, (y, z) -> rayTraceBlocks(minX, y, z))) {
				return true;
			}
		} else if (camX > maxX) {
			if (isPointOnPlaneVisible(minY, maxY, minZ, maxZ, (y, z) -> rayTraceBlocks(maxX, y, z))) {
				return true;
			}
		}
		if (camY < minY) {
			if (isPointOnPlaneVisible(minX, maxX, minZ, maxZ, (x, z) -> rayTraceBlocks(x, minY, z))) {
				return true;
			}
		} else if (camY > maxY) {
			if (isPointOnPlaneVisible(minX, maxX, minZ, maxZ, (x, z) -> rayTraceBlocks(x, maxY, z))) {
				return true;
			}
		}
		if (camZ < minZ) {
			if (isPointOnPlaneVisible(minX, maxX, minY, maxY, (x, y) -> rayTraceBlocks(x, y, minZ))) {
				return true;
			}
		} else if (camZ > maxZ) {
			if (isPointOnPlaneVisible(minX, maxX, minY, maxY, (x, y) -> rayTraceBlocks(x, y, maxZ))) {
				return true;
			}
		}

		return false;
	}

	private static boolean isPointOnPlaneVisible(double min1, double max1, double min2, double max2, BiDoublePredicate predicate) {
		if (predicate.test((min1 + max1) * 0.5D, (min2 + max2) * 0.5D)) {
			return true;
		}

		if (predicate.test(min1, min2)) {
			return true;
		}
		if (predicate.test(max1, max2)) {
			return true;
		}
		if (predicate.test(min1, max2)) {
			return true;
		}
		if (predicate.test(max1, min2)) {
			return true;
		}

		return false;
	}

	private static boolean rayTraceBlocks(final double endX, final double endY, final double endZ) {
		double threshold = CQRConfig.SERVER_CONFIG.advanced.skipHiddenEntityRenderingDiff.get();
		final double dirX = endX - camX;
		final double dirY = endY - camY;
		final double dirZ = endZ - camZ;
		final int incX = signum(dirX);
		final int incY = signum(dirY);
		final int incZ = signum(dirZ);
		final double dx = (incX == 0) ? Double.MAX_VALUE : (incX / dirX);
		final double dy = (incY == 0) ? Double.MAX_VALUE : (incY / dirY);
		final double dz = (incZ == 0) ? Double.MAX_VALUE : (incZ / dirZ);
		double percentX = dx * ((incX > 0) ? (1.0 - frac(camX)) : frac(camX));
		double percentY = dy * ((incY > 0) ? (1.0 - frac(camY)) : frac(camY));
		double percentZ = dz * ((incZ > 0) ? (1.0 - frac(camZ)) : frac(camZ));
		final Direction facingX = (incX > 0) ? Direction.WEST : Direction.EAST;
		final Direction facingY = (incY > 0) ? Direction.DOWN : Direction.UP;
		final Direction facingZ = (incZ > 0) ? Direction.NORTH : Direction.SOUTH;
		int x = floor(camX);
		int y = floor(camY);
		int z = floor(camZ);

		if (isOpaque(x, y, z)) {
			double d2;
			if (percentX < percentY) {
				if (percentX < percentZ) {
					d2 = percentX;
				} else {
					d2 = percentZ;
				}
			} else if (percentY < percentZ) {
				d2 = percentY;
			} else {
				d2 = percentZ;
			}
			d2 = Math.min(d2, 1.0);
			final double nextHitX = camX + dirX * d2;
			final double nextHitY = camY + dirY * d2;
			final double nextHitZ = camZ + dirZ * d2;
			threshold -= Math.sqrt(squareDist(camX, camY, camZ, nextHitX, nextHitY, nextHitZ));
			if (threshold < 0.0) {
				return false;
			}
		}

		while (percentX <= 1.0 || percentY <= 1.0 || percentZ <= 1.0) {
			Direction facing;
			if (percentX < percentY) {
				if (percentX < percentZ) {
					x += incX;
					percentX += dx;
					facing = facingX;
				} else {
					z += incZ;
					percentZ += dz;
					facing = facingZ;
				}
			} else if (percentY < percentZ) {
				y += incY;
				percentY += dy;
				facing = facingY;
			} else {
				z += incZ;
				percentZ += dz;
				facing = facingZ;
			}

			if (!isOpaque(x, y, z)) {
				continue;
			}

			double d;
			if (facing == facingX) {
				d = percentX - dx;
			} else if (facing == facingY) {
				d = percentY - dy;
			} else {
				d = percentZ - dz;
			}
			d = Math.min(d, 1.0);
			final double hitX = camX + dirX * d;
			final double hitY = camY + dirY * d;
			final double hitZ = camZ + dirZ * d;
			double d2;
			if (percentX < percentY) {
				if (percentX < percentZ) {
					d2 = percentX;
				} else {
					d2 = percentZ;
				}
			} else if (percentY < percentZ) {
				d2 = percentY;
			} else {
				d2 = percentZ;
			}
			d2 = Math.min(d2, 1.0);
			final double nextHitX = camX + dirX * d2;
			final double nextHitY = camY + dirY * d2;
			final double nextHitZ = camZ + dirZ * d2;
			threshold -= Math.sqrt(squareDist(hitX, hitY, hitZ, nextHitX, nextHitY, nextHitZ));
			if (threshold < 0.0) {
				return false;
			}
		}
		return true;
	}

	private static boolean isOpaque(final int x, final int y, final int z) {
		ChunkSection section = CACHED_BLOCK_ACCESS.getChunkSection(x >> 4, y >> 4, z >> 4);
		if (section == null || section.isEmpty()) {
			return false;
		}
		return section.getBlockState(x & 15, y & 15, z & 15).canOcclude();//Correct replacement for isOpaqueCube()?
	}

	private static int signum(final double x) {
		if (x == 0.0) {
			return 0;
		}
		return (x > 0.0) ? 1 : -1;
	}

	private static double frac(final double number) {
		return number - floor(number);
	}

	private static int floor(final double value) {
		final int i = (int) value;
		return (value < i) ? (i - 1) : i;
	}

	private static double squareDist(final double x1, final double y1, final double z1, double x2, double y2, double z2) {
		x2 -= x1;
		y2 -= y1;
		z2 -= z1;
		return x2 * x2 + y2 * y2 + z2 * z2;
	}

}
