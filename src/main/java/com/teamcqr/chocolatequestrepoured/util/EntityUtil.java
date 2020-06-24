package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.entity.Entity;

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

}
