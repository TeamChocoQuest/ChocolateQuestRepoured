package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;

public class AIWakeup extends AnimationAI<EntityCQRGiantTortoise> {

	protected Animation animation;
	protected EntityCQRGiantTortoise turtle;
	
	public AIWakeup(EntityCQRGiantTortoise entity, Animation animation) {
		super(entity);
		setMutexBits(8);
		this.turtle = entity;
		this.animation = animation;
	}

	@Override
	public Animation getAnimation() {
		return animation;
	}

	@Override
	public boolean shouldExecute() {
		return false;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		turtle.currentAnim = this;
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
	}
	
	@Override
	public boolean isAutomatic() {
		return false;
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		turtle.currentAnim = null;
	}

}
