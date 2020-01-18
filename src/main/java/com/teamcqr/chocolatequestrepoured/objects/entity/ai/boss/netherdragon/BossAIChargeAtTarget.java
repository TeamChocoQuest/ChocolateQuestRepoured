package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon.ENetherDragonAttacks;

public class BossAIChargeAtTarget extends AbstractCQREntityAI {

	protected static final int maxTargetDistance = 128;
	protected static final double chargingSpeed = 2.5D;
	protected static final int minDistanceToFireBall = 60;
	protected static final int attackCooldownBorder = 40;

	private int attackCooldown = 40;

	public BossAIChargeAtTarget(EntityCQRNetherDragon dragon) {
		super(dragon);
	}

	protected EntityCQRNetherDragon getDragon() {
		return (EntityCQRNetherDragon) this.entity;
	}

	// TODO: Add checks if the target has been hit
	// TODO: Fly a bit away and up after hitting your target and return to circling

	@Override
	public boolean shouldExecute() {
		if (this.getDragon().isDead) {
			return false;
		}
		if (this.getDragon().getAttackTarget() != null && !(this.getDragon().getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS)
				|| this.getDragon().getCurrentMovementState().equals(EntityCQRNetherDragon.EDragonMovementState.FLYING_DOWNWARDS))) {
			if (this.getDragon().getDistance(this.getDragon().getAttackTarget()) <= maxTargetDistance) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateTask() {
		this.getDragon().updateMovementState(EntityCQRNetherDragon.EDragonMovementState.CHARGING);
		this.attackCooldown++;
		if (this.attackCooldown >= attackCooldownBorder && this.getDragon().getDistance(this.getDragon().getAttackTarget()) >= minDistanceToFireBall) {
			// DONE: Fire ball
			this.getDragon().startAttack(ENetherDragonAttacks.FIREBALL);

			this.attackCooldown = 0;
		} else if (this.attackCooldown >= attackCooldownBorder) {
			// DONE: SPITFIRE (Eurobeat intensifies)
			this.getDragon().startAttack(ENetherDragonAttacks.SPIT_FIRE);

			this.attackCooldown = 0;
		}

		this.getDragon().getNavigator().tryMoveToEntityLiving(this.getDragon().getAttackTarget(), chargingSpeed);
	}

	@Override
	public void resetTask() {
		super.resetTask();
	}

}
