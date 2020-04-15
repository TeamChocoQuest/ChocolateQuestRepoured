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
		if(getBoss() != null && !getBoss().isDead && getBoss().isStunned()) {
			return true;
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		getBoss().setReadyToSpin(false);
		getBoss().setSpinning(false);
		getBoss().setStunned(true);
		getBoss().setCanBeStunned(false);
		getBoss().setAnimation(getAnimation());
		getBoss().currentAnim = this;
		getBoss().setAnimationTick(0);
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		getBoss().setStunned(true);
		getBoss().setCanBeStunned(false);
		
		if(getBoss().getAnimationTick() >= 10 && getBoss().getAnimationTick() <= getAnimation().getDuration() -10) {
			getBoss().setInShell(false);
		} else {
			getBoss().setInShell(true);
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		getBoss().setAnimationTick(0);
		getBoss().currentAnim = null;
		getBoss().setAnimation(IAnimatedEntity.NO_ANIMATION);
		getBoss().setCanBeStunned(true);
		getBoss().setStunned(false);
	}

}
