package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

public class AIApproachTarget extends AbstractCQREntityAI {

	public AIApproachTarget(EntityCQRGiantTortoise entity) {
		super(entity);
		this.setMutexBits(1);
	}
	
	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public boolean shouldExecute() {
		if(!getBoss().isStunned() && getBoss().getAttackTarget() != null && !getBoss().getAttackTarget().isDead && !getBoss().isSpinning()) {
			if(getBoss().getDistance(getBoss().getAttackTarget()) < AISpinAttackTurtle.MIN_DISTANCE_TO_BEGIN_SPIN) {
				return false;
			}
		} else {
			return false;
		}
		if(!getBoss().isHealing()) {
			if(!getBoss().isInShell()) {
				getBoss().setCanBeStunned(true);
				getBoss().setWantsToSpin(false);
				return true;
			} else {
				getBoss().targetNewState(EntityCQRGiantTortoise.TARGET_MOVE_OUT);
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}
	
	@Override
	public void updateTask() {
		if(getBoss().getAttackTarget() != null) {
			if(!getBoss().isInShell()) {
				getBoss().getNavigator().tryMoveToEntityLiving(getBoss().getAttackTarget(), 0.9D);
			} else {
				getBoss().targetNewState(EntityCQRGiantTortoise.TARGET_MOVE_OUT);
			}
		}
	}

}
