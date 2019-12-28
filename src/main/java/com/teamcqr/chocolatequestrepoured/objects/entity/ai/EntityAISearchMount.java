package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class EntityAISearchMount extends AbstractCQREntityAI {

	protected static final double MOUNT_SEARCH_RADIUS = 16;
	protected static final double DISTANCE_TO_MOUNT = 2.0D;
	protected static final boolean FORCE_MOUNTING = true;
	protected static final double WALK_SPEED_TO_MOUNT = 1.0D;

	protected EntityAnimal entityToMount = null;

	public EntityAISearchMount(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.canRide()) {
			return false;
		}
		if (this.entity.isRiding()) {
			return false;
		}
		if (this.entity.ticksExisted % 4 == 0) {
			Vec3d vec1 = this.entity.getPositionVector().addVector(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			Vec3d vec2 = this.entity.getPositionVector().subtract(MOUNT_SEARCH_RADIUS, MOUNT_SEARCH_RADIUS * 0.5D, MOUNT_SEARCH_RADIUS);
			AxisAlignedBB aabb = new AxisAlignedBB(vec1, vec2);
			List<EntityAnimal> possibleMounts = this.entity.world.getEntitiesWithinAABB(EntityAnimal.class, aabb, TargetUtil.PREDICATE_MOUNTS);
			if (!possibleMounts.isEmpty()) {
				this.entityToMount = TargetUtil.getNearestEntity(this.entity, possibleMounts);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.entity.canRide()) {
			return false;
		}
		if (this.entity.isRiding()) {
			return false;
		}
		if (this.entityToMount == null) {
			return false;
		}
		if (!this.entityToMount.isEntityAlive()) {
			return false;
		}
		if (this.entityToMount.isBeingRidden()) {
			return false;
		}
		if (this.entity.getDistance(this.entityToMount) > 16.0D) {
			return false;
		}
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		if (this.entity.getDistance(this.entityToMount) > DISTANCE_TO_MOUNT) {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entityToMount, WALK_SPEED_TO_MOUNT);
		}
	}

	@Override
	public void updateTask() {
		if (this.entity.getDistance(this.entityToMount) > DISTANCE_TO_MOUNT) {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entityToMount, WALK_SPEED_TO_MOUNT);
		} else {
			if (this.entityToMount instanceof AbstractHorse) {
				AbstractHorse horse = (AbstractHorse) this.entityToMount;
				horse.setOwnerUniqueId(this.entity.getPersistentID());
				horse.setHorseTamed(true);
				horse.setHorseSaddled(true);
				// Should that stay? -> Arlo says yes.
				if (horse instanceof EntityHorse) {
					((EntityHorse) horse).setHorseArmorStack(new ItemStack(Items.IRON_HORSE_ARMOR, 1));
				}
			} else if (this.entityToMount instanceof EntityPig) {
				((EntityPig) this.entityToMount).setSaddled(true);
			}
			this.entity.startRiding(this.entityToMount, FORCE_MOUNTING);
		}
	}

	@Override
	public void resetTask() {
		this.entityToMount = null;
		this.entity.getNavigator().clearPath();
	}

}
