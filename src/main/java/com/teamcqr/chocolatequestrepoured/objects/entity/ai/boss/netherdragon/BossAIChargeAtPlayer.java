package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.entity.ai.EntityAIBase;

public class BossAIChargeAtPlayer extends EntityAIBase {

	protected EntityCQRNetherDragon dragon;
	
	protected static final int maxTargetDistance = 128;
	protected static final double chargingSpeed = 2.5D;
	protected static final int minDistanceToFireBall = 60; 
	protected static final int attackCooldownBorder = 40;
	
	private int attackCooldown = 40;
	
	public BossAIChargeAtPlayer(EntityCQRNetherDragon dragon) {
		this.dragon = dragon;
	}

	//TODO: Add checks if the target has been hit
	//TODO: Fly a bit away and up after hitting your target and return to circling
	
	@Override
	public boolean shouldExecute() {
		if(this.dragon.isDead) {
			return false;
		}
		if(this.dragon.getAttackTarget() != null && !(this.dragon.getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS) || this.dragon.getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS))) {
			if(this.dragon.getDistance(this.dragon.getAttackTarget()) <= maxTargetDistance) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void updateTask() {
		this.dragon.updateMovementState(EntityCQRNetherDragon.EDragonMovementState.CHARGING);
		attackCooldown++;
		if(attackCooldown >= attackCooldownBorder && this.dragon.getDistance(this.dragon.getAttackTarget()) >= minDistanceToFireBall) {
			//TODO: Fire ball
			
			attackCooldown = 0;
		} else if(attackCooldown >= attackCooldownBorder){
			//TODO: SPITFIRE (Eurobeat intensifies)
			
			attackCooldown = 0;
		}
		
		this.dragon.getNavigator().tryMoveToEntityLiving(dragon.getAttackTarget(), chargingSpeed);
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
	}

}
