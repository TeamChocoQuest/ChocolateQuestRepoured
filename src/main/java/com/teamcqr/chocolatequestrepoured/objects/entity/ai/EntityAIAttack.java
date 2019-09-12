package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
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
		if (this.isSuitableTarget(attackTarget) && this.entity.getEntitySenses().canSee(attackTarget)
				&& this.canMoveToEntity(attackTarget)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if ((this.isSuitableTarget(attackTarget) || (attackTarget != null && attackTarget != this.entity
				&& !this.entity.getEntitySenses().canSee(attackTarget))) && this.entity.hasPath()) {
			return true;
		}
		this.resetTask();
		return false;
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
		this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
		this.attackTick--;
		if (this.shieldTick > 0) {
			this.shieldTick--;
		}

		if (this.shieldTick <= 0) {
			if (!this.entity.isActiveItemStackBlocking()) {
				ItemStack offhand = this.entity.getHeldItemOffhand();
				if (offhand.getItem().isShield(offhand, this.entity)) {
					this.entity.setActiveHand(EnumHand.OFF_HAND);
				}
			}
		}

		if (this.entity.getEntitySenses().canSee(attackTarget)) {
			this.canMoveToEntity(attackTarget);
			this.entity.getNavigator().setPath(this.path, 1.0D);
		}

		double distance = this.entity.getDistanceSq(attackTarget);
		this.checkAndPerformAttack(this.entity.getAttackTarget(), distance);
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
		this.attackTick = 0;
		this.shieldTick = 0;
	}

	protected boolean isSuitableTarget(EntityLivingBase possibleTarget) {
		if (!TargetUtil.PREDICATE.apply(possibleTarget)) {
			return false;
		}
		if (possibleTarget == this.entity) {
			return false;
		}
		return this.entity.getFaction() != null && this.entity.getFaction().isEntityEnemy(possibleTarget);
	}

	protected boolean canMoveToEntity(EntityLivingBase target) {
		this.path = this.entity.getNavigator().getPathToEntityLiving(target);
		return this.path != null;
	}

	protected void checkAndPerformAttack(EntityLivingBase attackTarget, double distance) {
		double d0 = this.getAttackReachSqr(attackTarget);

		if (distance <= d0 && this.attackTick <= 0) {
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

	protected double getAttackReachSqr(EntityLivingBase attackTarget) {
		return (double) (this.entity.width * 2.0F * this.entity.width * 2.0F + attackTarget.width);
	}

}
