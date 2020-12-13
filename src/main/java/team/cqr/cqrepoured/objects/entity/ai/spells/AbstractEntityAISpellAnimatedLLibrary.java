package team.cqr.cqrepoured.objects.entity.ai.spells;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;

public abstract class AbstractEntityAISpellAnimatedLLibrary<T extends AbstractEntityCQR & IAnimatedEntity> extends AbstractEntityAISpell<T> {

	public AbstractEntityAISpellAnimatedLLibrary(T entity, int cooldown, int chargingTicks, int castingTicks) {
		super(entity, cooldown, chargingTicks, castingTicks);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (!super.shouldContinueExecuting()) {
			return false;
		}
		return true;// this.entity.getAnimationTick() < this.getAnimation().getDuration();
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
