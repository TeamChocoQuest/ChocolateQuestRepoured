package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

public class BossAISpinTowardsTarget extends AbstractCQREntityAI {

	private int INTERNAL_COOLDOWN = 0;

	static final int SPIN_TIME_MAX = 500;
	static final int SPIN_UP_COOLDWON = 100;
	static final int SPIN_DOWN_COOLDOWN = 100;
	static final int MOVE_PARTS_COOLDOWN = 50;

	public BossAISpinTowardsTarget(EntityCQRGiantTortoise entity) {
		super(entity);
	}

	public EntityCQRGiantTortoise getEntity() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public void updateTask() {
		if (this.shouldContinueExecuting()) {

			switch (this.getEntity().getCurrentAnimation()) {
			case MOVE_PARTS_IN:
				break;
			case MOVE_PARTS_OUT:
				break;
			case NONE:
				break;
			case SPIN:
				break;
			case SPIN_DOWN:
				break;
			case SPIN_UP:
				break;
			default:
				break;

			}

			this.INTERNAL_COOLDOWN++;
		}

	}

	@Override
	public boolean shouldExecute() {
		// TODO Auto-generated method stub
		return false;
	}

}
