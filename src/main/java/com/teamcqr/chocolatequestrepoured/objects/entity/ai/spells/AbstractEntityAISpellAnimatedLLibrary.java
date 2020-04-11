package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public abstract class AbstractEntityAISpellAnimatedLLibrary<T extends AbstractEntityCQR & IAnimatedEntity> extends AnimationAI<T> implements IEntityAISpell {

	protected final boolean isCombatSpell;
	protected final int cooldown;
	protected final int chargeUpTicks;
	protected final int castTicks;
	protected int prevTimeCasted;
	protected int tick;

	public AbstractEntityAISpellAnimatedLLibrary(T entity, boolean isCombatSpell, int cooldown, int chargeUpTicks, int castTicks) {
		super(entity);
		this.isCombatSpell = isCombatSpell;
		this.cooldown = cooldown;
		this.chargeUpTicks = chargeUpTicks;
		this.castTicks = castTicks;
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.isEntityAlive()) {
			return false;
		}
		if (this.isCombatSpell && this.entity.getAttackTarget() == null) {
			return false;
		}
		return this.entity.ticksExisted > this.prevTimeCasted + this.cooldown;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!this.entity.isEntityAlive()) {
			return false;
		}
		return this.tick < this.chargeUpTicks + this.castTicks;
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

	@Override
	public void resetTask() {
		this.prevTimeCasted = this.entity.ticksExisted;
		this.tick = 0;
	}

	@Override
	public void updateTask() {
		if (this.tick++ < this.chargeUpTicks) {
			this.chargeUpSpell();
		} else {
			this.castSpell();
		}
	}

	protected abstract void chargeUpSpell();

	protected abstract void castSpell();

}
