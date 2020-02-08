package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise.ETortoiseAnimState;

public class BossAIHealingTurtle extends AbstractCQREntityAI {

	private boolean healingActive = false;

	private final int healingDuration = 160;
	private int currHealTicks = 0;

	public BossAIHealingTurtle(EntityCQRGiantTortoise entity) {
		super(entity);
		setMutexBits(8);
	}

	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public boolean shouldExecute() {
		this.healingActive = false;
		if((this.entity.getHealth() / this.entity.getMaxHealth() <= 0.2F) && this.currHealTicks < this.getHealingAmount()) {
			((EntityCQRGiantTortoise) this.entity).setHealing(true);
			if(((EntityCQRGiantTortoise) this.entity).isInShell()) {
				this.healingActive = true;
				return true;
			} else {
				((EntityCQRGiantTortoise) this.entity).targetNewState(EntityCQRGiantTortoise.TARGET_MOVE_IN);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.currHealTicks = 0;
		updateTask();
	}

	@Override
	public boolean shouldContinueExecuting() {
		this.healingActive = false;
		if(!this.entity.isDead && this.currHealTicks < this.getHealingAmount()) {
			if(((EntityCQRGiantTortoise) this.entity).isInShell()) {
				this.healingActive = true;
			}
			return true;
		} 
		return false;
	}

	@Override
	public void updateTask() {
		((EntityCQRGiantTortoise) this.entity).setHealing(healingActive);
		/*if (!this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.HEALING)) {
			if (this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.WALKING)) {
				this.getBoss().setCurrentAnimation(ETortoiseAnimState.MOVE_PARTS_IN);
				this.ticksAnimStart = this.entity.ticksExisted;
			}
			if (this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.NONE) || (this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.MOVE_PARTS_IN) && (this.entity.ticksExisted - this.ticksAnimStart) >= 115)) {
				this.healingActive = true;
			}
		}*/
		if (this.healingActive) {
			this.entity.getNavigator().clearPath();
			this.entity.setAttackTarget(null);
			((EntityCQRGiantTortoise) this.entity).setInShell(true);
			if (!this.getBoss().getCurrentAnimation().equals(ETortoiseAnimState.HEALING)) {
				this.getBoss().setCurrentAnimation(ETortoiseAnimState.HEALING);
			} else {
				if (this.currHealTicks >= this.getHealingAmount()) {
					// Cancel
					this.healingActive = false;
					this.currHealTicks = 0;
					((EntityCQRGiantTortoise) this.entity).setTimesHealed(((EntityCQRGiantTortoise) this.entity).getTimesHealed() +1);
					this.getBoss().setCurrentAnimation(ETortoiseAnimState.NONE);
				} else {
					this.getBoss().heal(1F);
					this.currHealTicks++;
				}
			}
		}
	}
	
	public int getHealingAmount() {
		return this.healingDuration / ((EntityCQRGiantTortoise) this.entity).getTimesHealed();
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.getBoss().setCurrentAnimation(ETortoiseAnimState.NONE);
		((EntityCQRGiantTortoise) this.entity).setTimesHealed(((EntityCQRGiantTortoise) this.entity).getTimesHealed() +1);
		this.currHealTicks = 0;
		((EntityCQRGiantTortoise) this.entity).setHealing(false);
		this.healingActive = false;
	}

}
