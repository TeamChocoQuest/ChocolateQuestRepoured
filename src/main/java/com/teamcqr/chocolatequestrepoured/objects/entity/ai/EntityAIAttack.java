package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;

public class EntityAIAttack extends AbstractCQREntityAI {

	protected Path path;
	protected int attackTick;
	protected int shieldTick;

	public EntityAIAttack(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (!TargetUtil.PREDICATE.apply(attackTarget)) {
			return false;
		}
		/*
		if (this.entity == attackTarget) {
			return false;
		}
		if (!this.entity.getFaction().isEntityEnemy(attackTarget)) {
			return false;
		}
		*/
		if (!this.entity.getEntitySenses().canSee(attackTarget)) {
			return false;
		}
		return this.canMoveToEntity(attackTarget) || this.inAttackRange(attackTarget);
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (attackTarget == null) {
			return false;
		}
		if (!attackTarget.isEntityAlive()) {
			return false;
		}
		if (!EntitySelectors.CAN_AI_TARGET.apply(attackTarget)) {
			return false;
		}
		/*
		if (this.entity == attackTarget) {
			return false;
		}
		if (!this.entity.getFaction().isEntityEnemy(attackTarget)) {
			return false;
		}
		*/
		if (!this.entity.getEntitySenses().canSee(attackTarget)) {
			return this.entity.hasPath();
		}
		return this.canMoveToEntity(attackTarget) || this.entity.hasPath() || this.inAttackRange(attackTarget);
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().setPath(this.path, 1.0D);
		if (!this.entity.isActiveItemStackBlocking()) {
			ItemStack offhand = this.entity.getHeldItemOffhand();
			if (offhand.getItem().isShield(offhand, this.entity)) {
				this.entity.setActiveHand(EnumHand.OFF_HAND);
			}
		}
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (this.attackTick > 0) {
			this.attackTick--;
		}
		if (this.shieldTick > 0) {
			this.shieldTick--;
		}

		this.checkAndPerformBlock();

		if(attackTarget != null && !attackTarget.isDead) {
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
		}

		if (this.path != null) {
			this.entity.getNavigator().setPath(this.path, 1.0D);
		}

		this.checkAndPerformAttack(this.entity.getAttackTarget());
	}

	@Override
	public void resetTask() {
		this.entity.setAttackTarget(null);
		this.entity.getNavigator().clearPath();
	}

	protected boolean canMoveToEntity(EntityLivingBase target) {
		this.path = this.entity.getNavigator().getPathToEntityLiving(target);
		return this.path != null;
	}

	protected void checkAndPerformBlock() {
		if (this.shieldTick <= 0 && !this.entity.isActiveItemStackBlocking()) {
			ItemStack offhand = this.entity.getHeldItemOffhand();
			if (offhand.getItem().isShield(offhand, this.entity)) {
				this.entity.setActiveHand(EnumHand.OFF_HAND);
			}
		}
	}

	protected void checkAndPerformAttack(EntityLivingBase attackTarget) {
		if (this.attackTick <= 0 && this.inAttackRange(attackTarget)) {
			if (this.entity.isActiveItemStackBlocking()) {
				this.entity.resetActiveHand();
				this.attackTick = 40;
				this.shieldTick = 20;
			} else {
				this.attackTick = 20;
			}
			this.entity.swingArm(EnumHand.MAIN_HAND);
			this.entity.attackEntityAsMob(attackTarget);
		}
	}

	protected boolean inAttackRange(EntityLivingBase attackTarget) {
		double attackReach = (double) (this.entity.width * 2.0F * this.entity.width * 2.0F + attackTarget.width);
		double distance = this.entity.getDistanceSq(attackTarget);
		return distance <= attackReach;
	}

}
