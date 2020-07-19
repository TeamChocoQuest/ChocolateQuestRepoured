package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

import net.minecraft.entity.ai.EntityAISwimming;

public class BossAITortoiseSwimming extends EntityAISwimming {

	private EntityCQRGiantTortoise boss;

	public BossAITortoiseSwimming(EntityCQRGiantTortoise entityIn) {
		super(entityIn);
		this.boss = entityIn;
		this.setMutexBits(0);
	}

	@Override
	public boolean shouldExecute() {
		if (super.shouldExecute()) {
			return this.boss.getAttackTarget() != null;
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (super.shouldContinueExecuting()) {
			return this.boss.getAttackTarget() != null;
		}
		return false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (this.boss.getAttackTarget() != null) {
			this.boss.getNavigator().tryMoveToEntityLiving(this.boss.getAttackTarget(), 3);
		}
	}

}
