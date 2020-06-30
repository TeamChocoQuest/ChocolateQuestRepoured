package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class EntityUtil {

	public static void move2D(Entity entity, double strafe, double forward, double speed, double yaw) {
		double d = strafe * strafe + forward * forward;
		if (d >= 1.0E-4D) {
			d = Math.sqrt(d);
			if (d < 1.0D) {
				d = 1.0D;
			}
			d = speed / d;

			strafe *= d;
			forward *= d;

			double d1 = Math.sin(Math.toRadians(yaw));
			double d2 = Math.cos(Math.toRadians(yaw));

			entity.motionX += strafe * d2 - forward * d1;
			entity.motionZ += forward * d2 + strafe * d1;
		}
	}

	public static void move3D(Entity entity, double strafe, double up, double forward, double speed, double yaw, double pitch) {
		double d = strafe * strafe + up * up + forward * forward;
		if (d >= 1.0E-4D) {
			d = Math.sqrt(d);
			if (d < 1.0D) {
				d = 1.0D;
			}
			d = speed / d;

			strafe *= d;
			up *= d;
			forward *= d;

			double d1 = Math.sin(Math.toRadians(yaw));
			double d2 = Math.cos(Math.toRadians(yaw));
			double d3 = Math.sin(Math.toRadians(pitch));
			double d4 = Math.cos(Math.toRadians(pitch));

			entity.motionX += strafe * d2 - forward * d1 * d4;
			entity.motionY += up - forward * d3;
			entity.motionZ += forward * d2 * d4 + strafe * d1;
		}
	}
	
	public static boolean isEntityFlying(Entity entity) {
		if (entity.onGround) {
			return false;
		}
		if (entity.collided) {
			return false;
		}
		if (entity.motionY < -0.1D) {
			return false;
		}
		BlockPos pos = new BlockPos(entity);
		int y = 0;
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX() + i, 255, pos.getZ() + j);
				if (!entity.getEntityWorld().isBlockLoaded(mutablePos)) {
					continue;
				}
				while (mutablePos.getY() > 0 && entity.getEntityWorld().getBlockState(mutablePos).getCollisionBoundingBox(entity.getEntityWorld(), mutablePos) == Block.NULL_AABB) {
					mutablePos.setY(mutablePos.getY() - 1);
				}
				y += mutablePos.getY();
				count++;
			}
		}
		y = count > 0 ? y / count : (int) entity.posY;
		if (entity.posY < y + 8) {
			return false;
		}
		return !entity.getEntityWorld().checkBlockCollision(entity.getEntityBoundingBox());
	}

}
