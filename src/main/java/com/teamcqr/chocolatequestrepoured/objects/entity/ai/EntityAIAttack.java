package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;

public class EntityAIAttack extends AbstractCQREntityAI {

	protected int visionTick;
	protected int attackTick;
	protected int shieldTick;

	public EntityAIAttack(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		this.shieldTick = Math.max(this.shieldTick - 3, 0);
		this.attackTick = Math.max(this.attackTick - 3, 0);
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		return attackTarget != null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		this.shieldTick = Math.max(this.shieldTick - 1, 0);
		this.attackTick = Math.max(this.attackTick - 1, 0);
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (!TargetUtil.PREDICATE_ATTACK_TARGET.apply(attackTarget)) {
			return false;
		}
		if (this.entity.getDistanced(attackTarget) > 64.0D) {
			return false;
		}
		if (this.entity.getEntitySenses().canSee(attackTarget) && (this.entity.isEntityInFieldOfView(attackTarget) || this.visionTick > 0)) {
			return EntitySelectors.IS_ALIVE.apply(attackTarget);
		}
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		this.updatePath(attackTarget);
		this.checkAndPerformBlock();
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (this.entity.getEntitySenses().canSee(attackTarget)) {
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 12.0F, 12.0F);

			if (this.entity.isEntityInFieldOfView(attackTarget)) {
				this.updatePath(attackTarget);
				this.visionTick = 20;
			} else if (this.visionTick > 0) {
				this.updatePath(attackTarget);
				this.visionTick--;
			}

			this.checkAndPerformAttack(this.entity.getAttackTarget());
		}

		this.checkAndPerformBlock();
	}

	@Override
	public void resetTask() {
		this.visionTick = 0;
		this.entity.setAttackTarget(null);
		this.entity.getNavigator().clearPath();
		if (this.entity.isActiveItemStackBlocking()) {
			this.entity.resetActiveHand();
		}
	}

	protected void updatePath(EntityLivingBase target) {
		this.entity.getNavigator().tryMoveToEntityLiving(target, 1.0D);
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
		if (this.attackTick <= 0 && this.entity.isInAttackReach(attackTarget)) {
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

}
