package com.teamcqr.chocolatequestrepoured.objects.entity.ai.target;

import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;

public class EntityAINetherDragonNearestAttackTarget extends AbstractCQREntityAI<EntityCQRNetherDragon> {

	protected final Predicate<EntityLivingBase> predicate = input -> {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(input)) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		return EntityAINetherDragonNearestAttackTarget.this.isSuitableTarget(input);
	};

	public EntityAINetherDragonNearestAttackTarget(EntityCQRNetherDragon entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			this.entity.setAttackTarget(null);
			return false;
		}
		if (this.isStillSuitableTarget(this.entity.getAttackTarget())) {
			return false;
		}
		this.entity.setAttackTarget(null);
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

	@Override
	public void startExecuting() {
		AxisAlignedBB aabb = this.entity.getEntityBoundingBox().grow(32.0D);
		List<EntityLivingBase> possibleTargets = this.entity.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, this.predicate);
		if (!possibleTargets.isEmpty()) {
			this.entity.setAttackTarget(TargetUtil.getNearestEntity(this.entity, possibleTargets));
		}
	}

	private boolean isSuitableTarget(EntityLivingBase possibleTarget) {
		if (possibleTarget == this.entity) {
			return false;
		}
		CQRFaction faction = this.entity.getFaction();
		if (this.entity.getHeldItemMainhand().getItem() == CQRItems.STAFF_HEALING) {
			if (faction == null || (!faction.isAlly(possibleTarget) && this.entity.getLeader() != possibleTarget)) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
			/*if (!this.entity.isInSightRange(possibleTarget)) {
				return false;
			}*/
			//return this.entity.getEntitySenses().canSee(possibleTarget);
			return isInHomeZone(possibleTarget);
		}
		if (faction == null || !this.entity.getFaction().isEnemy(possibleTarget) || this.entity.getLeader() == possibleTarget) {
			return false;
		}
		/*if (!this.entity.getEntitySenses().canSee(possibleTarget)) {
			return false;
		}*/
		if (this.entity.isInAttackReach(possibleTarget)) {
			return true;
		}
		/*if (this.entity.isEntityInFieldOfView(possibleTarget)) {
			return this.entity.isInSightRange(possibleTarget);
		}*/
		return !possibleTarget.isSneaking() && this.entity.getDistance(possibleTarget) < 32.0D;
	}

	private boolean isStillSuitableTarget(EntityLivingBase possibleTarget) {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(possibleTarget)) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(possibleTarget)) {
			return false;
		}
		if (possibleTarget == this.entity) {
			return false;
		}
		CQRFaction faction = this.entity.getFaction();
		if (this.entity.getHeldItemMainhand().getItem() == CQRItems.STAFF_HEALING) {
			if (faction == null || (!faction.isAlly(possibleTarget) && this.entity.getLeader() != possibleTarget)) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
			/*if (!this.entity.isInSightRange(possibleTarget)) {
				return false;
			}*/
			//return this.entity.getEntitySenses().canSee(possibleTarget);
			return isInHomeZone(possibleTarget);
		}
		if (faction == null || !this.entity.getFaction().isEnemy(possibleTarget) || this.entity.getLeader() == possibleTarget) {
			return false;
		}
		/*if (!this.entity.isInSightRange(possibleTarget)) {
			return false;
		}
		return this.entity.getEntitySenses().canSee(possibleTarget);*/
		return isInHomeZone(possibleTarget);
	}

	private boolean isInHomeZone(EntityLivingBase possibleTarget) {
		double distance = possibleTarget.getPosition().getDistance(entity.getCirclingCenter().getX(), entity.getCirclingCenter().getY(), entity.getCirclingCenter().getZ());
		return distance <= 24 + 8 * (world.getDifficulty().ordinal());
	}

}
