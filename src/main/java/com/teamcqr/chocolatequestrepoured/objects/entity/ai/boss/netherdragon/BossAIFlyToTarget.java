package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.util.math.Vec3d;

public class BossAIFlyToTarget extends BossAIFlyToLocation {
	
	private int attackCooldown = 100;
	private int aiCooldown = 200;

	public BossAIFlyToTarget(EntityCQRNetherDragon entity) {
		super(entity);
	}
	
	@Override
	protected double getMovementSpeed() {
		return 3;
	}
	
	@Override
	public boolean shouldExecute() {
		aiCooldown--;
		return super.shouldExecute() && entity.getAttackTarget() != null && !entity.getAttackTarget().isDead && aiCooldown <= 0;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && entity.getAttackTarget() != null && !entity.getAttackTarget().isDead;
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		attackCooldown--;
		if(attackCooldown <= 0) {
			attackCooldown = 80 + entity.getRNG().nextInt(61);
			entity.attackEntityWithRangedAttack(entity.getAttackTarget(), entity.getDistance(entity.getAttackTarget()));
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		this.aiCooldown = 600;
		this.entity.setTargetLocation(new Vec3d(entity.getCirclingCenter().getX(), entity.getCirclingCenter().getY(), entity.getCirclingCenter().getZ()));
	}
	
	@Override
	protected Vec3d getTargetLocation() {
		return (entity.getAttackTarget() != null && !entity.getAttackTarget().isDead) ? entity.getAttackTarget().getPositionVector() : null;
	}

}
