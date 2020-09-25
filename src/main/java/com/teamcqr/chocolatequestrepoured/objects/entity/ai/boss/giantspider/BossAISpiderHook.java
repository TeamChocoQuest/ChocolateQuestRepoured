package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.giantspider;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAIHooker;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantSpider;

public class BossAISpiderHook extends EntityAIHooker {
	
	public BossAISpiderHook(EntityCQRGiantSpider entity) {
		super(entity);
		this.MIN_RANGE = 81; //9 blocks
		this.MAX_RANGE = 576; //24 blocks
		this.MAX_COOLDOWN = 30;
	}
	
	@Override
	public boolean shouldExecute() {
		if (this.hasHookShoot(this.entity)) {
			if (this.cooldown > 0) {
				this.cooldown--;
				return false;
			}
			return this.entity.hasAttackTarget() && this.entity.getDistanceSq(entity.getAttackTarget()) >= this.MIN_RANGE && this.entity.getEntitySenses().canSee(this.entity.getAttackTarget());
		}

		return false;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.state = STATE.PREPARING;
		if (this.entity.hasPath()) {
			this.entity.getNavigator().clearPath();
			double dist = this.entity.getDistanceSq(this.entity.getAttackTarget());
			if (dist > MAX_RANGE) {
				this.entity.getNavigator().tryMoveToEntityLiving(this.entity.getAttackTarget(), 1.1);
			} else if (dist >= MIN_RANGE) {
				this.entity.getNavigator().clearPath();
				this.state = STATE.PREPARING_LAUNCH;
			} else {
				resetTask();
			}
		}
	}

}
