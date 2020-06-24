package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;

public abstract class AbstractEntityAISpellAnimatedLLibrary<T extends AbstractEntityCQR & IAnimatedEntity> extends AbstractEntityAISpell<T> {

	public AbstractEntityAISpellAnimatedLLibrary(T entity, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, cooldown, chargingTicks, castingTicks);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!super.shouldExecute()) {
			return false;
		}
		return this.entity.getAnimationTick() < this.getAnimation().getDuration();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, this.getAnimation());
		this.entity.setAnimationTick(0);
	}

	@Override
	public void resetTask() {
		super.resetTask();
		AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, IAnimatedEntity.NO_ANIMATION);
	}

	public abstract Animation getAnimation();

}
