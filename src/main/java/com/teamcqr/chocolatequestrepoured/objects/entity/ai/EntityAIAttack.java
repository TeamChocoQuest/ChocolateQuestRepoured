package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIAttack extends AbstractCQREntityAI<AbstractEntityCQR> {

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
		return this.entity.getAttackTarget() != null;
	}

	@Override
	public boolean shouldContinueExecuting() {
		this.shieldTick = Math.max(this.shieldTick - 1, 0);
		this.attackTick = Math.max(this.attackTick - 1, 0);
		return this.entity.getAttackTarget() != null;
	}

	@Override
	public void startExecuting() {
		this.updatePath(this.entity.getAttackTarget());
		this.checkAndPerformBlock();
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (attackTarget != null) {
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 12.0F, 12.0F);
			this.updatePath(attackTarget);
			this.checkAndPerformAttack(this.entity.getAttackTarget());
			this.checkAndPerformBlock();
		}
	}

	@Override
	public void resetTask() {
		this.entity.getNavigator().clearPath();
		this.entity.resetActiveHand();
	}

	protected void updatePath(EntityLivingBase target) {
		this.entity.getNavigator().tryMoveToEntityLiving(target, 1.0D);
	}

	protected void checkAndPerformBlock() {
		if (this.entity.getLastTimeHitByAxeWhileBlocking() + 60 > this.entity.ticksExisted) {
			if (this.entity.isActiveItemStackBlocking()) {
				this.entity.resetActiveHand();
			}
		} else if (this.shieldTick <= 0 && !this.entity.isActiveItemStackBlocking()) {
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
