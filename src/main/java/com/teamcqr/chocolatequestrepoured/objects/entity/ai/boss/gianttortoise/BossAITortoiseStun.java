package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public class BossAITortoiseStun extends AnimationAI<EntityCQRGiantTortoise> {

	public BossAITortoiseStun(EntityCQRGiantTortoise entity) {
		super(entity);
	}

	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public Animation getAnimation() {
		return EntityCQRGiantTortoise.ANIMATION_STUNNED;
	}

	@Override
	public boolean isAutomatic() {
		return false;
	}

	@Override
	public boolean shouldExecute() {
		if (this.getBoss() != null && !this.getBoss().isDead && this.getBoss().isStunned()) {
			return true;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.getBoss().setReadyToSpin(false);
		this.getBoss().setSpinning(false);
		this.getBoss().setStunned(true);
		this.getBoss().setCanBeStunned(false);
		this.getBoss().setAnimation(this.getAnimation());
		this.getBoss().currentAnim = this;
		this.getBoss().setAnimationTick(0);
	}

	@Override
	public void updateTask() {
		super.updateTask();
		this.getBoss().setStunned(true);
		this.getBoss().setCanBeStunned(false);

		if (this.getBoss().getAnimationTick() >= 10 && this.getBoss().getAnimationTick() <= this.getAnimation().getDuration() - 10) {
			this.getBoss().setInShell(false);
		} else {
			this.getBoss().setInShell(true);
		}
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.getBoss().setAnimationTick(0);
		this.getBoss().currentAnim = null;
		this.getBoss().setAnimation(IAnimatedEntity.NO_ANIMATION);
		this.getBoss().setCanBeStunned(true);
		this.getBoss().setStunned(false);
	}

}
