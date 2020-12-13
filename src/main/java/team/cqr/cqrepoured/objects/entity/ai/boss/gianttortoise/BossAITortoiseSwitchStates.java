package team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise;

public class BossAITortoiseSwitchStates extends AnimationAI<EntityCQRGiantTortoise> {

	protected EntityCQRGiantTortoise turtle;
	protected Animation animationIn;
	protected Animation animationOut;

	public BossAITortoiseSwitchStates(EntityCQRGiantTortoise entity, Animation animIn, Animation animOut) {
		super(entity);
		this.setMutexBits(8);
		this.turtle = entity;
		this.animationIn = animIn;
		this.animationOut = animOut;
	}

	@Override
	public Animation getAnimation() {
		if (this.turtle.getTargetedState() != 0) {
			return this.turtle.getTargetedState() < 0 ? this.animationIn : this.animationOut;
		}
		return null;
	}

	@Override
	public boolean shouldExecute() {
		if (this.turtle.wantsToChangeState() && !this.turtle.isStunned() && !this.turtle.isSpinning()) {
			return true;
		}
		return false;
	}

	@Override
	public void updateTask() {
		super.updateTask();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.turtle.currentAnim = this;
		this.turtle.setReadyToSpin(false);
		this.turtle.setAnimationTick(0);
		this.turtle.setInShell(false);
		if (this.turtle.getTargetedState() < 0) {
			this.turtle.setAnimation(this.animationIn);
		} else {
			this.turtle.setAnimation(this.animationOut);
		}
		this.turtle.setBypassInShell(true);
	}

	@Override
	public boolean isAutomatic() {
		return false;
	}

	@Override
	public boolean isInterruptible() {
		return false;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.turtle.setAnimationTick(0);
		this.turtle.currentAnim = null;
		this.turtle.setAnimation(IAnimatedEntity.NO_ANIMATION);
		this.turtle.setInShell(this.turtle.getTargetedState() < 0);
		this.turtle.changedState();
		this.turtle.targetNewState(0);
		this.turtle.setBypassInShell(false);
	}

}
