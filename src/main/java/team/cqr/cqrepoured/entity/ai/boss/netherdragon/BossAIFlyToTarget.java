package team.cqr.cqrepoured.entity.ai.boss.netherdragon;

import net.minecraft.world.phys.Vec3;
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
		return super.canUse() && this.entity.getTarget() != null && !this.entity.getTarget().isDeadOrDying() && this.aiCooldown <= 0 && !this.entity.isFlyingUp();
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.entity.getTarget() != null && !this.entity.getTarget().isDeadOrDying();
	}

	@Override
	public void tick() {
		if (this.entity.position().distanceTo(this.getTargetLocation()) <= 4) {
			this.entity.canAttack(this.entity.getTarget());
			this.stop();
		}
		super.tick();
		if (!this.breathFire) {
			this.attackCooldown--;
			if (this.attackCooldown <= 0) {
				this.attackCooldown = 20 + this.entity.getRandom().nextInt(41);
				this.entity.performRangedAttack(this.entity.getTarget(), this.entity.distanceTo(this.entity.getTarget()));
			}
		} else {
			this.entity.breatheFire();
			this.entity.setBreathingFireFlag(true);
		}
	}

	@Override
	public void start() {
		super.start();

		this.breathFire = this.entity.getRandom().nextDouble() >= 0.75;
	}

	@Override
	public void stop() {
		super.stop();
		this.aiCooldown = 40;
		if (this.breathFire) {
			this.entity.setBreathingFireFlag(false);
		}
		this.breathFire = false;
		this.entity.setTargetLocation(new Vec3(this.entity.getCirclingCenter().getX(), this.entity.getCirclingCenter().getY(), this.entity.getCirclingCenter().getZ()));
	}

	@Override
	protected Vec3 getTargetLocation() {
		return (this.entity.getTarget() != null && !this.entity.getTarget().isDeadOrDying()) ? this.entity.getTarget().position() : null;
	}

}
