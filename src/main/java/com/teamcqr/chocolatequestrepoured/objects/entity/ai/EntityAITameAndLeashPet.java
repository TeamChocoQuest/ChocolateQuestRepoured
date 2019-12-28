package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.item.ItemLead;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class EntityAITameAndLeashPet extends AbstractCQREntityAI {

	// TODO: Save pet information on entity!!!

	protected static final double PET_SEARCH_RADIUS = 16.0D;
	protected static final double DISTANCE_TO_PET = 2.0D;
	protected static final double WALK_SPEED_TO_PET = 1.0D;

	protected EntityTameable entityToTame = null;

	public EntityAITameAndLeashPet(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (!(this.entity.getHeldItemMainhand().getItem() instanceof ItemLead || this.entity.getHeldItemOffhand().getItem() instanceof ItemLead)) {
			return false;
		}
		if (this.entity.ticksExisted % 4 == 0) {
			Vec3d vec1 = this.entity.getPositionVector().addVector(PET_SEARCH_RADIUS, PET_SEARCH_RADIUS * 0.5D, PET_SEARCH_RADIUS);
			Vec3d vec2 = this.entity.getPositionVector().subtract(PET_SEARCH_RADIUS, PET_SEARCH_RADIUS * 0.5D, PET_SEARCH_RADIUS);
			AxisAlignedBB aabb = new AxisAlignedBB(vec1, vec2);
			List<EntityTameable> possiblePets = this.entity.world.getEntitiesWithinAABB(EntityTameable.class, aabb, TargetUtil.PREDICATE_PETS);
			if (!possiblePets.isEmpty()) {
				this.entityToTame = TargetUtil.getNearestEntity(this.entity, possiblePets);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!(this.entity.getHeldItemMainhand().getItem() instanceof ItemLead || this.entity.getHeldItemOffhand().getItem() instanceof ItemLead)) {
			return false;
		}
		if (this.entityToTame == null) {
			return false;
		}
		if (!this.entityToTame.isEntityAlive()) {
			return false;
		}
		if (this.entityToTame.getOwner() != null) {
			return false;
		}
		if (this.entity.getDistance(this.entityToTame) > 16.0D) {
			return false;
		}
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		if (this.entity.getDistance(this.entityToTame) > DISTANCE_TO_PET) {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entityToTame, WALK_SPEED_TO_PET);
		}
	}

	@Override
	public void updateTask() {
		if (this.entity.getDistance(this.entityToTame) > DISTANCE_TO_PET) {
			this.entity.getNavigator().tryMoveToEntityLiving(this.entityToTame, WALK_SPEED_TO_PET);
		} else {
			this.entityToTame.setOwnerId(this.entity.getPersistentID());
			this.entityToTame.setTamed(true);
			this.entityToTame.setLeashHolder(this.entity, true);
		}
	}

	@Override
	public void resetTask() {
		this.entityToTame = null;
		this.entity.getNavigator().clearPath();
	}

}
