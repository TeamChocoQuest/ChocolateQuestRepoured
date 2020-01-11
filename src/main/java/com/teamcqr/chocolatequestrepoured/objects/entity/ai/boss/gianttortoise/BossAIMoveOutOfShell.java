package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise.ETortoiseAnimState;

public class BossAIMoveOutOfShell extends AbstractCQREntityAI {

	private EntityCQRGiantTortoise tortoise;
	
	public BossAIMoveOutOfShell(AbstractEntityCQR entity) {
		super(entity);
		if(entity instanceof EntityCQRGiantTortoise) {
			tortoise = (EntityCQRGiantTortoise)entity;
		}
	}

	@Override
	public boolean shouldExecute() {
		if(tortoise.getAttackTarget() != null && tortoise.canEntityBeSeen(tortoise.getAttackTarget())) {
			if(!tortoise.getCurrentAnimation().equals(ETortoiseAnimState.WALKING)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		
		switch(tortoise.getCurrentAnimation()) {
		case HEALING:
			tortoise.setInShell(true);
			return;
		case MOVE_PARTS_IN:
			tortoise.setInShell(false);
			return;
		case MOVE_PARTS_OUT:
			if(tortoise.getAnimationProgress() >= 100) {
				tortoise.setInShell(false);
				tortoise.setCurrentAnimation(ETortoiseAnimState.WALKING);
				return;
			}
			break;
		case NONE:
			tortoise.setInShell(true);
			tortoise.setCurrentAnimation(ETortoiseAnimState.MOVE_PARTS_OUT);
			break;
		case SPIN:
			tortoise.setInShell(true);
			return;
		case SPIN_DOWN:
			if(tortoise.getAnimationProgress() >= 250) {
				tortoise.setCurrentAnimation(ETortoiseAnimState.MOVE_PARTS_OUT);
			}
			break;
		case SPIN_UP:
			tortoise.setInShell(true);
			return;
		case WALKING:
			tortoise.setInShell(false);
			return;
		default:
			break;
		}
	}

}
