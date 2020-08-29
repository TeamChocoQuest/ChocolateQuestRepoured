package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIMoveToHome;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

public class BossAITortoiseMoveToHome extends EntityAIMoveToHome {

	public BossAITortoiseMoveToHome(EntityCQRGiantTortoise entity) {
		super(entity);
	}
	
	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}
	
	@Override
	public boolean shouldExecute() {
		if(super.shouldExecute()) {
			return checkTurtleSpecific();
		}
		return false;
	}
	
	private boolean checkTurtleSpecific() {
		if (!(this.getBoss().hasHomePositionCQR() || this.getBoss().hasHome())) {
			return false;
		}
		if (!this.getBoss().hasAttackTarget() && !(this.getBoss().isStunned() || this.getBoss().isSpinning() || this.getBoss().isHealing())) {
			if (this.getBoss().isInShell()) {
				this.getBoss().targetNewState(EntityCQRGiantTortoise.TARGET_MOVE_OUT);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if(super.shouldContinueExecuting()) {
			return checkTurtleSpecific();
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		getBoss().setInShell(false);
		super.startExecuting();
	}

	@Override
	public void updateTask() {
		if(!getBoss().isInShell()) {
			super.updateTask();
		}
	}

}
