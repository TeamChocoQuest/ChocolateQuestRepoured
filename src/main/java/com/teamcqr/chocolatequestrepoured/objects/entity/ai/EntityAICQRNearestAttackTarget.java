package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAICQRNearestAttackTarget extends EntityAIBase {

	protected final AbstractEntityCQR entity;
	protected final Predicate<EntityLivingBase> predicate;
	protected final TargetUtil.Sorter sorter;
	protected EntityLivingBase attackTarget;

	public EntityAICQRNearestAttackTarget(AbstractEntityCQR entity) {
		this.entity = entity;
		this.predicate = new Predicate<EntityLivingBase>() {
			@Override
			public boolean apply(EntityLivingBase input) {
				if (!TargetUtil.PREDICATE.apply(input)) {
					return false;
				}
				if (!EntitySelectors.IS_ALIVE.apply(input)) {
					return false;
				}
				if (!EntityAICQRNearestAttackTarget.this.isSuitableTarget(input)) {
					return false;
				}
				return true;
			}
		};
		this.sorter = new TargetUtil.Sorter(entity);
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.ticksExisted % 8 == 0 && !this.predicate.apply(this.entity.getAttackTarget())) {
			AxisAlignedBB aabb = this.entity.getEntityBoundingBox().grow(32.0D, 8.0D, 32.0D);
			List<EntityLivingBase> possibleTargets = this.entity.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, this.predicate);
			if (!possibleTargets.isEmpty()) {
				possibleTargets.sort(this.sorter);
				this.attackTarget = possibleTargets.get(0);
				return true;
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.entity.setAttackTarget(attackTarget);
	}

	private boolean isSuitableTarget(EntityLivingBase possibleTarget) {
		if (possibleTarget == this.entity) {
			return false;
		}
		if (!this.entity.getFaction().isEntityEnemy(possibleTarget)) {
			return false;
		}
		if (!this.entity.getEntitySenses().canSee(possibleTarget)) {
			return false;
		}
		double distance = this.entity.getDistanceSq(possibleTarget);
		if (distance <= this.entity.getAttackReach(possibleTarget)) {
			return true;
		}
		if (!this.entity.canSeeEntity(possibleTarget)) {
			if (distance > 12.0D) {
				return false;
			}
			if (possibleTarget.isSneaking()) {
				return false;
			}
		}
		return this.canMoveToEntity(possibleTarget);
	}

	protected boolean canMoveToEntity(EntityLivingBase possibleTarget) {
		Path path = this.entity.getNavigator().getPathToEntityLiving(possibleTarget);
		return path != null;
	}

}
