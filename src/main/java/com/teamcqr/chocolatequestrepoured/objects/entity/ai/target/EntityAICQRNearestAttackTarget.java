package com.teamcqr.chocolatequestrepoured.objects.entity.ai.target;

import java.util.List;

import com.google.common.base.Predicate;
import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;

public class EntityAICQRNearestAttackTarget extends AbstractCQREntityAI {

	protected final Predicate<EntityLivingBase> predicate = input -> {
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(input)) {
			return false;
		}
		if (!EntitySelectors.IS_ALIVE.apply(input)) {
			return false;
		}
		return EntityAICQRNearestAttackTarget.this.isSuitableTarget(input);
	};

	public EntityAICQRNearestAttackTarget(AbstractEntityCQR entity) {
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
		if (this.entity.getHeldItemMainhand().getItem() == ModItems.STAFF_HEALING) {
			if (faction == null || (!faction.isAlly(possibleTarget) && this.entity.getLeader() != possibleTarget)) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
			if (!this.entity.isInSightRange(possibleTarget)) {
				return false;
			}
			return this.entity.getEntitySenses().canSee(possibleTarget);
		}
		if (faction == null || !this.entity.getFaction().isEnemy(possibleTarget) || this.entity.getLeader() == possibleTarget) {
			return false;
		}
		if (!this.entity.getEntitySenses().canSee(possibleTarget)) {
			return false;
		}
		if (this.entity.isInAttackReach(possibleTarget)) {
			return true;
		}
		if (this.entity.isEntityInFieldOfView(possibleTarget)) {
			return this.entity.isInSightRange(possibleTarget);
		}
		return !possibleTarget.isSneaking() && this.entity.getDistance(possibleTarget) < 12.0D;
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
		if (this.entity.getHeldItemMainhand().getItem() == ModItems.STAFF_HEALING) {
			if (faction == null || (!faction.isAlly(possibleTarget) && this.entity.getLeader() != possibleTarget)) {
				return false;
			}
			if (possibleTarget.getHealth() >= possibleTarget.getMaxHealth()) {
				return false;
			}
			if (!this.entity.isInSightRange(possibleTarget)) {
				return false;
			}
			return this.entity.getEntitySenses().canSee(possibleTarget);
		}
		if (faction == null || !this.entity.getFaction().isEnemy(possibleTarget) || this.entity.getLeader() == possibleTarget) {
			return false;
		}
		if (!this.entity.isInSightRange(possibleTarget)) {
			return false;
		}
		return this.entity.getEntitySenses().canSee(possibleTarget);
	}

}
