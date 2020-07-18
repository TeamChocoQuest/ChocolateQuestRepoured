package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.util.math.Vec3d;

public class BossAIFlyToTarget extends BossAIFlyToLocation {

	private int attackCooldown = 10;
	private int aiCooldown = 60;

	private boolean breathFire = false;

	public BossAIFlyToTarget(EntityCQRNetherDragon entity) {
		super(entity);
	}

	@Override
	protected double getMovementSpeed() {
		return 0.3;
	}

	@Override
	public boolean shouldExecute() {
		this.aiCooldown--;
		return super.shouldExecute() && this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead && this.aiCooldown <= 0 && !this.entity.isFlyingUp();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead;
	}

	@Override
	public void updateTask() {
		if (this.entity.getPositionVector().distanceTo(this.getTargetLocation()) <= 4) {
			this.entity.attackEntityAsMob(this.entity.getAttackTarget());
			this.resetTask();
		}
		super.updateTask();
		if (!this.breathFire) {
			this.attackCooldown--;
			if (this.attackCooldown <= 0) {
				this.attackCooldown = 20 + this.entity.getRNG().nextInt(41);
				this.entity.attackEntityWithRangedAttack(this.entity.getAttackTarget(), this.entity.getDistance(this.entity.getAttackTarget()));
			}
		} else {
			((EntityCQRNetherDragon) this.entity).breatheFire();
			((EntityCQRNetherDragon) this.entity).setBreathingFireFlag(true);
		}
	}

	@Override
	public void startExecuting() {
		super.startExecuting();

		this.breathFire = this.entity.getRNG().nextDouble() >= 0.75;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.aiCooldown = 40;
		if (this.breathFire) {
			((EntityCQRNetherDragon) this.entity).setBreathingFireFlag(false);
		}
		this.breathFire = false;
		this.entity.setTargetLocation(new Vec3d(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ()));
	}

	@Override
	protected Vec3d getTargetLocation() {
		return (this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead) ? this.entity.getAttackTarget().getPositionVector() : null;
	}

}
