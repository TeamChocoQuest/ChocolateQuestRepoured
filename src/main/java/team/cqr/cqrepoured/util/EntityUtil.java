package team.cqr.cqrepoured.util;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				mutablePos.setPos(pos.getX() + i, pos.getY(), pos.getZ() + j);
				if (!entity.getEntityWorld().isBlockLoaded(mutablePos)) {
					continue;
				}
				while (mutablePos.getY() > 0 && entity.getEntityWorld().getBlockState(mutablePos).getCollisionBoundingBox(entity.getEntityWorld(), mutablePos) == Block.NULL_AABB) {
					mutablePos.setY(mutablePos.getY() - 1);
				}
				y += mutablePos.getY() + 1;
				count++;
			}
		}
		y = count > 0 ? y / count : (int) entity.posY;
		if (entity.posY < y + 8) {
			return false;
		}
		return !entity.getEntityWorld().checkBlockCollision(entity.getEntityBoundingBox());
	}

	@Nullable
	public static Entity getEntityByUUID(World world, UUID uuid) {
		if (!world.isRemote) {
			return ((WorldServer) world).getEntityFromUuid(uuid);
		}

		for (Entity entity : world.loadedEntityList) {
			if (entity.getPersistentID().equals(uuid)) {
				return entity;
			}
		}

		return null;
	}

	public static void applyMaxHealthModifier(EntityLivingBase entity, UUID uuid, String name, double amount) {
		IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		if (attribute == null) {
			return;
		}
		AttributeModifier oldModifier = attribute.getModifier(uuid);
		float oldHealth = entity.getHealth();
		double oldAmount = 0.0D;
		if (oldModifier != null) {
			oldAmount = oldModifier.getAmount();
			if (Math.abs(amount - oldAmount) < 0.01D) {
				return;
			}
			attribute.removeModifier(oldModifier);
		}
		attribute.applyModifier(new AttributeModifier(uuid, name, amount, 2));
		entity.setHealth((float) ((double) oldHealth / (1.0D + oldAmount) * (1.0D + amount) + 1e-7D));
	}

}
