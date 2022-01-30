package team.cqr.cqrepoured.entity.ai.boss.gianttortoise;

import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise.AnimationGecko;

public class BossAITortoiseStun extends AbstractCQREntityAI<EntityCQRGiantTortoise> {

	public BossAITortoiseStun(EntityCQRGiantTortoise entity) {
		super(entity);
	}

	private EntityCQRGiantTortoise getBoss() {
		return this.entity;
	}

	public AnimationGecko getAnimation() {
		return EntityCQRGiantTortoise.ANIMATIONS[EntityCQRGiantTortoise.ANIMATION_ID_STUNNED];
	}

	@Override
	public boolean canUse() {
		if (this.getBoss() != null && !this.getBoss().isDeadOrDying() && this.getBoss().isStunned()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.getBoss().shouldCurrentAnimationBePlaying() && this.getBoss().isStunned();
	}

	@Override
	public void start() {
		super.start();
		this.getBoss().setReadyToSpin(false);
		this.getBoss().setSpinning(false);
		this.getBoss().setStunned(true);
		this.getBoss().setCanBeStunned(false);
		this.getBoss().setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_STUNNED);
	}

	@Override
	public void tick() {
		super.tick();
		this.getBoss().setStunned(true);
		this.getBoss().setCanBeStunned(false);

		if (this.getBoss().getCurrentAnimationTick() >= 10 && this.getBoss().getCurrentAnimationTick() <= this.getAnimation().getAnimationDuration() - 10) {
			this.getBoss().setInShell(false);
		} else {
			this.getBoss().setInShell(true);
		}
	}

	@Override
	public void stop() {
		super.stop();
		this.getBoss().setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_IN_SHELL);
		this.getBoss().setCanBeStunned(true);
		this.getBoss().setStunned(false);
		this.getBoss().setReadyToSpin(true);
	}

}
