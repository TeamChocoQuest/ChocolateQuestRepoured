package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.EntityLivingBase;

public class EntityAIAttackSpecial extends AbstractCQREntityAI<AbstractEntityCQR> {

	private static final List<AbstractEntityAIAttackSpecial> SPECIAL_ATTACKS = new ArrayList<>();
	private AbstractEntityAIAttackSpecial activeSpecialAttack;
	private int specialAttackTick;
	private int tick;

	public EntityAIAttackSpecial(AbstractEntityCQR entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if (attackTarget == null) {
			return false;
		}
		if (!this.entity.getEntitySenses().canSee(attackTarget)) {
			return false;
		}
		for (AbstractEntityAIAttackSpecial specialAttack : SPECIAL_ATTACKS) {
			if (this.specialAttackTick + specialAttack.getCooldown(this.entity) >= this.entity.ticksExisted) {
				continue;
			}
			if (specialAttack.shouldStartAttack(this.entity, attackTarget)) {
				this.activeSpecialAttack = specialAttack;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		if ((this.activeSpecialAttack.needsTargetToContinue || this.activeSpecialAttack.needsSightToContinue) && attackTarget == null) {
			return false;
		}
		if (this.activeSpecialAttack.needsSightToContinue && !this.entity.getEntitySenses().canSee(attackTarget)) {
			return false;
		}
		return this.activeSpecialAttack.shouldContinueAttack(this.entity, attackTarget);
	}

	@Override
	public boolean isInterruptible() {
		return this.activeSpecialAttack.isInterruptible(this.entity);
	}

	@Override
	public void startExecuting() {
		this.specialAttackTick = this.entity.ticksExisted;
		this.tick = 0;

		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		this.activeSpecialAttack.startAttack(this.entity, attackTarget);
	}

	@Override
	public void resetTask() {
		this.tick = -1;

		this.activeSpecialAttack.resetAttack(this.entity);
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();
		this.activeSpecialAttack.continueAttack(this.entity, attackTarget, this.tick++);
	}

}
