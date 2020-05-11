package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.util.math.Vec3d;

public class BossAIFlyToTarget extends BossAIFlyToLocation {
	
	private int attackCooldown = 50;
	private int aiCooldown = 100;
	private boolean attackedMelee = false;

	public BossAIFlyToTarget(EntityCQRNetherDragon entity) {
		super(entity);
	}
	
	@Override
	protected double getMovementSpeed() {
		return 4;
	}
	
	@Override
	public boolean shouldExecute() {
		aiCooldown--;
		return super.shouldExecute() && entity.getAttackTarget() != null && !entity.getAttackTarget().isDead && aiCooldown <= 0 && !this.entity.isFlyingUp();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && entity.getAttackTarget() != null && !entity.getAttackTarget().isDead;
	}
	
	@Override
	public void updateTask() {
		if(entity.getPositionVector().distanceTo(getTargetLocation()) <= 4 && !attackedMelee) {
			entity.attackEntityAsMob(entity.getAttackTarget());
			attackCooldown = 20;
			attackedMelee = true;
		}
		super.updateTask();
		attackCooldown--;
		if(attackCooldown <= 0) {
			attackCooldown = 30 + entity.getRNG().nextInt(41);
			attackedMelee = false;
			entity.attackEntityWithRangedAttack(entity.getAttackTarget(), entity.getDistance(entity.getAttackTarget()));
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		attackedMelee = false;
		this.aiCooldown = 300;
		this.entity.setTargetLocation(new Vec3d(entity.getCirclingCenter().getX(), entity.getCirclingCenter().getY(), entity.getCirclingCenter().getZ()));
	}
	
	@Override
	protected Vec3d getTargetLocation() {
		return (entity.getAttackTarget() != null && !entity.getAttackTarget().isDead) ? entity.getAttackTarget().getPositionVector() : null;
	}
	
}
