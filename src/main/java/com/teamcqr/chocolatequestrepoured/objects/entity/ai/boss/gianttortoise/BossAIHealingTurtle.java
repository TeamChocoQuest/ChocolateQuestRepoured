package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise.ETortoiseAnimState;

public class BossAIHealingTurtle extends AbstractCQREntityAI {
	
	private boolean healingActive = false;
	
	private final int healingDuration = 160;
	private int currHealTicks = 0;

	private long ticksAnimStart = 0;
	
	public BossAIHealingTurtle(EntityCQRGiantTortoise entity) {
		super(entity);
	}
	
	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) entity;
	}

	@Override
	public boolean shouldExecute() {
		return (entity.getHealth() / entity.getMaxHealth() <= 0.1F) && !getBoss().getCurrentAnimation().equals(ETortoiseAnimState.SPIN) && currHealTicks < healingDuration;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return !entity.isDead && !getBoss().getCurrentAnimation().equals(ETortoiseAnimState.SPIN) && currHealTicks < healingDuration;
	}
	
	@Override
	public void updateTask() {
		if(!getBoss().getCurrentAnimation().equals(ETortoiseAnimState.HEALING)) {
			if(getBoss().getCurrentAnimation().equals(ETortoiseAnimState.NONE)) {
				getBoss().setCurrentAnimation(ETortoiseAnimState.MOVE_PARTS_IN);
				ticksAnimStart  = entity.ticksExisted;
			}
			if(getBoss().getCurrentAnimation().equals(ETortoiseAnimState.NONE) || (getBoss().getCurrentAnimation().equals(ETortoiseAnimState.MOVE_PARTS_IN) && (entity.ticksExisted - ticksAnimStart) >= 115)) {
				healingActive = true;
			}
		} 
		if(healingActive) {
			if(!getBoss().getCurrentAnimation().equals(ETortoiseAnimState.HEALING)) {
				getBoss().setCurrentAnimation(ETortoiseAnimState.HEALING);
			} else {
				if(currHealTicks >= healingDuration) {
					//Cancel
					healingActive = false;
					getBoss().setCurrentAnimation(ETortoiseAnimState.NONE);
				} else {
					if(currHealTicks % 2 == 0) {
						getBoss().heal(1F);
					}
					currHealTicks++;
				}
			}
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		getBoss().setCurrentAnimation(ETortoiseAnimState.NONE);
		healingActive = false;
	}

}
