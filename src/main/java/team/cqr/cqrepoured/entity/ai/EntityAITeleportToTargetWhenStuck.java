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
			if (this.entity.getDistanceSq(this.entity.getAttackTarget()) >= 256) {
				// TODO: Maybe check if we are not ranged...
				return true;
			}
			return (this.entity.hasPath() && this.entity.collidedHorizontally) || !this.entity.hasPath();
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

		this.entity.teleport(this.entity.getAttackTarget().posX, this.entity.getAttackTarget().posY, this.entity.getAttackTarget().posZ);
	}

	@Override
	public void stop() {
		super.stop();

		this.cooldown = 15;
	}

}
