package team.cqr.cqrepoured.entity.ai.boss.netherdragon;

import net.minecraft.util.math.vector.Vector3d;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;

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
	public boolean canUse() {
		this.aiCooldown--;
		return super.canUse() && this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead && this.aiCooldown <= 0 && !this.entity.isFlyingUp();
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead;
	}

	@Override
	public void tick() {
		if (this.entity.getPositionVector().distanceTo(this.getTargetLocation()) <= 4) {
			this.entity.attackEntityAsMob(this.entity.getAttackTarget());
			this.stop();
		}
		super.tick();
		if (!this.breathFire) {
			this.attackCooldown--;
			if (this.attackCooldown <= 0) {
				this.attackCooldown = 20 + this.entity.getRNG().nextInt(41);
				this.entity.attackEntityWithRangedAttack(this.entity.getAttackTarget(), this.entity.getDistance(this.entity.getAttackTarget()));
			}
		} else {
			this.entity.breatheFire();
			this.entity.setBreathingFireFlag(true);
		}
	}

	@Override
	public void start() {
		super.start();

		this.breathFire = this.entity.getRNG().nextDouble() >= 0.75;
	}

	@Override
	public void stop() {
		super.stop();
		this.aiCooldown = 40;
		if (this.breathFire) {
			this.entity.setBreathingFireFlag(false);
		}
		this.breathFire = false;
		this.entity.setTargetLocation(new Vector3d(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ()));
	}

	@Override
	protected Vector3d getTargetLocation() {
		return (this.entity.getAttackTarget() != null && !this.entity.getAttackTarget().isDead) ? this.entity.getAttackTarget().getPositionVector() : null;
	}

}
