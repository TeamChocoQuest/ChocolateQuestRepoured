package team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise;

import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise.AnimationGecko;

public class BossAITortoiseSwitchStates extends AbstractCQREntityAI<EntityCQRGiantTortoise> {

	protected EntityCQRGiantTortoise turtle;
	protected AnimationGecko animationIn;
	protected AnimationGecko animationOut;

	public BossAITortoiseSwitchStates(EntityCQRGiantTortoise entity, AnimationGecko animIn, AnimationGecko animOut) {
		super(entity);
		this.setMutexBits(8);
		this.turtle = entity;
		this.animationIn = animIn;
		this.animationOut = animOut;
	}

	public AnimationGecko getAnimation() {
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
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && (this.turtle.getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_ENTER_SHELL || this.turtle.getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_EXIT_SHELL)&& this.turtle.shouldCurrentAnimationBePlaying();
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.turtle.setReadyToSpin(false);
		this.turtle.setInShell(false);
		if (this.turtle.getTargetedState() < 0) {
			//this.turtle.setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_ENTER_SHELL);
		} else {
			//this.turtle.setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_EXIT_SHELL);
		}
		this.turtle.setBypassInShell(true);
	}


	@Override
	public boolean isInterruptible() {
		return false;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.turtle.setInShell(this.turtle.getTargetedState() < 0);
		this.turtle.changedState();
		this.turtle.setBypassInShell(false);
	}

}
