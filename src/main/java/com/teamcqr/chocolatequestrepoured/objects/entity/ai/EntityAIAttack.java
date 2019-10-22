package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;

public class EntityAIAttack extends AbstractCQREntityAI {

	protected Path path;
	protected int visionTick;
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
		return this.canMoveToEntity(attackTarget);
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (!TargetUtil.PREDICATE.apply(attackTarget)) {
			return false;
		}
		if (!this.entity.getEntitySenses().canSee(attackTarget)) {
			return this.entity.hasPath();
		}
		if (this.entity.canSeeEntity(attackTarget)) {
			if (!EntitySelectors.IS_ALIVE.apply(attackTarget)) {
				return false;
			}
			if (this.entity.inAttackReach(attackTarget)) {
				return true;
			}
			if (this.canMoveToEntity(attackTarget)) {
				return true;
			}
			if (this.entity.hasPath()) {
				return true;
			}
		}
		if (this.entity.ticksExisted - this.visionTick < 20) {
			if (this.canMoveToEntity(attackTarget)) {
				return true;
			}
		}
		return this.entity.hasPath();
	}

	@Override
	public void startExecuting() {
		this.entity.getNavigator().setPath(this.path, 1.0D);
		this.checkAndPerformBlock();
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
		if (attackTarget != null && this.entity.getEntitySenses().canSee(attackTarget)) {
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 10.0F, 10.0F);

			if (this.entity.canSeeEntity(attackTarget)) {
				this.visionTick = this.entity.ticksExisted;
			}
		}

		this.checkAndPerformBlock();

		if (this.path != null) {
			this.entity.getNavigator().setPath(this.path, 1.0D);
		}

		this.checkAndPerformAttack(this.entity.getAttackTarget());
	}

	@Override
	public void resetTask() {
		this.visionTick = 0;
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
		if (this.attackTick <= 0 && this.entity.inAttackReach(attackTarget)) {
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
