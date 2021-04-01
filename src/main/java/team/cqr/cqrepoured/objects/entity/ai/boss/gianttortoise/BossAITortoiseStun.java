package team.cqr.cqrepoured.objects.entity.ai.boss.gianttortoise;

import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRGiantTortoise.AnimationGecko;

public class BossAITortoiseStun extends AbstractCQREntityAI<EntityCQRGiantTortoise> {

	public BossAITortoiseStun(EntityCQRGiantTortoise entity) {
		super(entity);
	}

	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	public AnimationGecko getAnimation() {
		return EntityCQRGiantTortoise.ANIMATIONS[EntityCQRGiantTortoise.ANIMATION_ID_STUNNED];
	}

	@Override
	public boolean shouldExecute() {
		if (this.getBoss() != null && !this.getBoss().isDead && this.getBoss().isStunned()) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && this.getBoss().shouldCurrentAnimationBePlaying() && this.getBoss().isStunned();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		this.getBoss().setReadyToSpin(false);
		this.getBoss().setSpinning(false);
		this.getBoss().setStunned(true);
		this.getBoss().setCanBeStunned(false);
		this.getBoss().setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_STUNNED);
	}

	@Override
	public void updateTask() {
		super.updateTask();
		this.getBoss().setStunned(true);
		this.getBoss().setCanBeStunned(false);

		if (this.getBoss().getCurrentAnimationTick() >= 10 && this.getBoss().getCurrentAnimationTick() <= this.getAnimation().getAnimationDuration() - 10) {
			this.getBoss().setInShell(false);
		} else {
			this.getBoss().setInShell(true);
		}
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.getBoss().setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_IN_SHELL);
		this.getBoss().setCanBeStunned(true);
		this.getBoss().setStunned(false);
		this.getBoss().setReadyToSpin(true);
	}

}
