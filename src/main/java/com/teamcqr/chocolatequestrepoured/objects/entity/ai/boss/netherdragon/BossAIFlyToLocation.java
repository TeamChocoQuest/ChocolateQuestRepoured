package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.entity.ai.EntityAIBase;

public class BossAIFlyToLocation extends EntityAIBase {

	protected EntityCQRNetherDragon dragon;
	
	public BossAIFlyToLocation(EntityCQRNetherDragon dragon) {
		this.dragon = dragon;
	}

	@Override
	public boolean shouldExecute() {
		return this.dragon.getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING);
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
	}

}
