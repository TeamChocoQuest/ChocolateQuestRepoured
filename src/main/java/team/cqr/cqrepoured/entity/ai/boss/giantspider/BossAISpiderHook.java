package team.cqr.cqrepoured.entity.ai.boss.giantspider;

import team.cqr.cqrepoured.entity.ai.attack.special.EntityAIHooker;
import team.cqr.cqrepoured.entity.boss.EntityCQRGiantSpider;

public class BossAISpiderHook extends EntityAIHooker {

	public BossAISpiderHook(EntityCQRGiantSpider entity) {
		super(entity);
		this.MIN_RANGE = 81; // 9 blocks
		this.MAX_RANGE = 576; // 24 blocks
		this.MAX_COOLDOWN = 30;
	}

	@Override
	public boolean canUse() {
		if (this.hasHookShoot(this.entity)) {
			if (this.cooldown > 0) {
				this.cooldown--;
				return false;
			}
			return this.entity.hasAttackTarget() && this.entity.distanceToSqr(this.entity.getTarget()) >= this.MIN_RANGE && this.entity.getSensing().canSee(this.entity.getTarget());
		}

		return false;
	}

	@Override
	public void start() {
		super.start();
		this.state = STATE.PREPARING;
		if (this.entity.isPathFinding()) {
			this.entity.getNavigation().stop();
			double dist = this.entity.distanceToSqr(this.entity.getTarget());
			if (dist > this.MAX_RANGE) {
				this.entity.getNavigation().moveTo(this.entity.getTarget(), 1.1);
			} else if (dist >= this.MIN_RANGE) {
				this.entity.getNavigation().stop();
				this.state = STATE.PREPARING_LAUNCH;
			} else {
				this.stop();
			}
		}
	}

}
