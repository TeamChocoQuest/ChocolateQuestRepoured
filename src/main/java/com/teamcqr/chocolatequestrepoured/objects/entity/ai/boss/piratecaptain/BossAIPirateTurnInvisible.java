package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;

public class BossAIPirateTurnInvisible extends AbstractCQREntityAI<EntityCQRPirateCaptain> {

	private int cooldown = 0;
	private int invisibleTime = 0;
	
	public BossAIPirateTurnInvisible(EntityCQRPirateCaptain entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if( entity.getHealth() / entity.getMaxHealth() <= 0.5) {
			cooldown --;
			return cooldown <= 0;
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		if(entity.getIsInvisible()) {
			entity.setIsInvisible(true);
			entity.setInvisible(false);
		}
		entity.setIsTurningInvisible(true);
		
		invisibleTime = 100;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return invisibleTime > 0;
	}
	
	@Override
	public void updateTask() {
		invisibleTime--;
		if(invisibleTime == 0) {
			entity.setIsTurningInvisible(false);
			
			this.cooldown = 50;
		}
	}

}
