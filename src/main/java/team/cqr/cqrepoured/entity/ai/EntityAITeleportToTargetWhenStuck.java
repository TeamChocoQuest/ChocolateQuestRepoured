package team.cqr.cqrepoured.entity.ai;

import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class EntityAITeleportToTargetWhenStuck<T extends AbstractEntityCQR> extends AbstractCQREntityAI<T> {

	protected int cooldown = 0;

	public EntityAITeleportToTargetWhenStuck(T entity) {
		super(entity);
	}

	@Override
	public boolean canUse() {
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		if (this.entity.hasAttackTarget()) {
			if (this.entity.distanceToSqr(this.entity.getTarget()) >= 256) {
				// TODO: Maybe check if we are not ranged...
				return true;
			}
			return (this.entity.isPathFinding() && this.entity.horizontalCollision) || !this.entity.isPathFinding();
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		super.start();

		this.entity.teleport(this.entity.getTarget().getX(), this.entity.getTarget().getY(), this.entity.getTarget().getZ());
	}

	@Override
	public void stop() {
		super.stop();

		this.cooldown = 15;
	}

}
