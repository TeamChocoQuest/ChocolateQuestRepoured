package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.util.math.Vec3d;

public class BossAITortoiseSwimming extends EntityAISwimming {
	
	private EntityCQRGiantTortoise boss;

	public BossAITortoiseSwimming(EntityCQRGiantTortoise entityIn) {
		super(entityIn);
		this.boss = entityIn;
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
		if(boss.getAttackTarget() != null) {
			Vec3d v = boss.getAttackTarget().getPositionVector().subtract(boss.getPositionVector());
			v = v.normalize().scale(0.2);
			boss.motionX = v.x;
			boss.motionY = v.y / 2;
			boss.motionZ = v.z;
			boss.velocityChanged = true;
		}
	}
	

}
