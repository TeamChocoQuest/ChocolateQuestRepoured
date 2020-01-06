package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise.ETortoiseAnimState;

public class BossAIHealingTurtle extends AbstractCQREntityAI {

	private boolean healingActive = false;

	private final int healingDuration = 160;
	private int currHealTicks = 0;

	private long ticksAnimStart = 0;

	public BossAIHealingTurtle(EntityCQRGiantTortoise entity) {
		super(entity);
	}

	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public boolean shouldExecute() {
		return (this.entity.getHealth() / this.entity.getMaxHealth() <= 0.1F) && !this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.SPIN) && this.currHealTicks < this.healingDuration;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.entity.isDead && !this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.SPIN) && this.currHealTicks < this.healingDuration;
	}

	@Override
	public void updateTask() {
		if (!this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.HEALING)) {
			if (this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.NONE)) {
				this.getBoss().setCurrentAnimation(ETortoiseAnimState.MOVE_PARTS_IN);
				this.ticksAnimStart = this.entity.ticksExisted;
			}
			if (this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.NONE) || (this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.MOVE_PARTS_IN) && (this.entity.ticksExisted - this.ticksAnimStart) >= 115)) {
				this.healingActive = true;
			}
		}
		if (this.healingActive) {
			if (!this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.HEALING)) {
				this.getBoss().setCurrentAnimation(ETortoiseAnimState.HEALING);
			} else {
				if (this.currHealTicks >= this.healingDuration) {
					// Cancel
					this.healingActive = false;
					this.getBoss().setCurrentAnimation(ETortoiseAnimState.NONE);
				} else {
					if (this.currHealTicks % 2 == 0) {
						this.getBoss().heal(1F);
					}
					this.currHealTicks++;
				}
			}
		}
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.getBoss().setCurrentAnimation(ETortoiseAnimState.NONE);
		this.healingActive = false;
	}

}
