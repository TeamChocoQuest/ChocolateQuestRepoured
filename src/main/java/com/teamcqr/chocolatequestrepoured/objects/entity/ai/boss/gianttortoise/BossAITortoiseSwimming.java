package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

import net.minecraft.entity.ai.EntityAISwimming;

public class BossAITortoiseSwimming extends EntityAISwimming {

	private EntityCQRGiantTortoise boss;

	public BossAITortoiseSwimming(EntityCQRGiantTortoise entityIn) {
		super(entityIn);
		this.boss = entityIn;
		setMutexBits(0);
	}

	@Override
	public boolean shouldExecute() {
		if (super.shouldExecute()) {
			return boss.getAttackTarget() != null;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (super.shouldContinueExecuting()) {
			return boss.getAttackTarget() != null;
		}
		return false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (boss.getAttackTarget() != null) {
			boss.getNavigator().tryMoveToEntityLiving(boss.getAttackTarget(), 3);
		}
	}

}
