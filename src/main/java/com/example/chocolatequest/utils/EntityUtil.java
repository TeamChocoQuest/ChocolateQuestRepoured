package com.example.chocolatequest.utils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.server.level.ServerLevel;

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

			entity.setDeltaMovement(entity.getDeltaMovement().add(strafe * d2 - forward * d1, 0, forward * d2 + strafe * d1));

			/*
			 * entity.motionX += strafe * d2 - forward * d1; entity.motionZ += forward * d2 + strafe * d1;
			 */
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

			entity.setDeltaMovement(entity.getDeltaMovement().add(strafe * d2 - forward * d1 * d4, up - forward * d3, forward * d2 * d4 + strafe * d1));
			/*
			 * entity.motionX += strafe * d2 - forward * d1 * d4; entity.motionY += up - forward * d3; entity.motionZ += forward * d2 * d4 + strafe * d1;
			 */
		}
	}

	public static boolean isEntityFlying(Entity entity) {
		if (entity == null) {
			return false;
		}
		if (entity.onGround()) {
			return false;
		}
		if (entity.horizontalCollision || entity.verticalCollision) {
			return false;
		}
		if (entity.getDeltaMovement().y() < -0.1D) {
			return false;
		}
		BlockPos pos = entity.blockPosition();
		int y = 0;
		int count = 0;
		MutableBlockPos mutablePos = new MutableBlockPos();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				mutablePos.set(pos.getX() + i, pos.getY(), pos.getZ() + j);
				if (!entity.level().isLoaded(mutablePos)) {
					continue;
				}
				while (mutablePos.getY() > 0 && entity.level().getBlockState(mutablePos).getCollisionShape(entity.level(), mutablePos) == Shapes.INFINITY) {
					mutablePos.setY(mutablePos.getY() - 1);
				}
				y += mutablePos.getY() + 1;
				count++;
			}
		}
		y = count > 0 ? y / count : (int) entity.getY();
		if (entity.getY() < y + 8) {
			return false;
		}
		return entity.level().noCollision(entity.getBoundingBox());
	}

	@Nullable
	public static Entity getEntityByUUID(Level world, UUID uuid) {
		if (!false) {
			return ((ServerLevel) world).getEntity(uuid);
		}

		/*Entity resClient = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
			return ClientWorldUtil.getEntityByUUID(world, uuid);
		});

		return resClient;*/ return null;
	}

	public static void applyMaxHealthModifier(LivingEntity entity, UUID uuid, String name, double amount) {
		AttributeInstance attribute = entity.getAttribute(Attributes.MAX_HEALTH);
		if (attribute == null) {
			return;
		}
		/*AttributeModifier oldModifier = attribute.getModifier(uuid);
		float oldHealth = entity.getHealth();
		double oldAmount = 0.0D;
		if (oldModifier != null) {
			oldAmount = oldModifier.getAmount();
			if (Math.abs(amount - oldAmount) < 0.01D) {
				return;
			}
			attribute.removeModifier(oldModifier);
		}
		attribute.addPermanentModifier(new AttributeModifier(uuid, name, amount, Operation.MULTIPLY_TOTAL));*/
		//entity.setHealth
	}

	
	@Deprecated
	public static boolean addEntityToAllRegionsAt(BlockPos position, Entity entity) {
		if (entity == null || position == null) {
			return false;
		}
		return false;
	}
}

