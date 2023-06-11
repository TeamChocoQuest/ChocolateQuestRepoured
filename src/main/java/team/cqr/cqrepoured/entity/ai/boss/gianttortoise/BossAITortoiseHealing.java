package team.cqr.cqrepoured.entity.ai.boss.gianttortoise;

import java.util.EnumSet;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.ai.goal.Goal.Flag;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.entity.boss.gianttortoise.EntityCQRGiantTortoise;

public class BossAITortoiseHealing extends AbstractCQREntityAI<EntityCQRGiantTortoise> {

	private boolean healingActive = false;

	private final int healingDuration = 160;
	private final int MIN_HEALING_AMOUNT = 80;
	private int currHealTicks = 0;

	public BossAITortoiseHealing(EntityCQRGiantTortoise entity) {
		super(entity);
		//this.setMutexBits(8);
		this.setFlags(EnumSet.allOf(Flag.class));
	}

	private EntityCQRGiantTortoise getBoss() {
		return this.entity;
	}

	@Override
	public boolean canUse() {
		this.healingActive = false;
		if (!this.getBoss().isSpinning() && !this.getBoss().isStunned() && (this.entity.getHealth() / this.entity.getMaxHealth() <= 0.2F) && this.currHealTicks < this.getHealingAmount() && this.getHealingAmount() >= this.MIN_HEALING_AMOUNT) {
			this.entity.setHealing(true);
			if (this.entity.isInShell() || this.entity.getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_IN_SHELL) {
				this.entity.setInShell(true);
				this.healingActive = true;
				return true;
			}
			if (this.entity.getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_WALK) {
				this.entity.setInShell(true);
				this.entity.setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_ENTER_SHELL);
			}
		}
		return false;
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

	@Override
	public void start() {
		super.start();
		this.getBoss().setCanBeStunned(false);
		this.entity.setReadyToSpin(false);
		this.getBoss().setStunned(false);
		this.currHealTicks = 0;
		this.tick();
	}

	@Override
	public boolean canContinueToUse() {
		this.healingActive = false;
		if (!this.entity.isDeadOrDying() && this.currHealTicks <= this.getHealingAmount()) {
			if (this.entity.isInShell()) {
				this.healingActive = true;
			}
			return this.entity.getCurrentAnimationId() == EntityCQRGiantTortoise.ANIMATION_ID_IN_SHELL;
		}
		return false;
	}

	@Override
	public void tick() {
		this.entity.setHealing(true);
		if (this.entity.getCurrentAnimationId() != EntityCQRGiantTortoise.ANIMATION_ID_IN_SHELL) {
			this.getBoss().setNextAnimation(EntityCQRGiantTortoise.ANIMATION_ID_ENTER_SHELL);
			return;
		}
		if (this.healingActive) {
			this.entity.setInShell(true);
			if (this.currHealTicks >= this.getHealingAmount() || (this.entity.getHealth() / this.entity.getMaxHealth() >= 0.8F)) {
				// Cancel
				this.healingActive = false;
				this.entity.setTimesHealed(this.entity.getTimesHealed() + 1);
				this.getBoss().setCanBeStunned(true);
			} else {
					((ServerWorld) this.entity.getWorld()).sendParticles(ParticleTypes.HEART, this.entity.getX(), this.entity.getY(), this.entity.getZ(), 5, 0.5D, 1.0D, 0.5D, 0);
				this.getBoss().heal(1F);
				this.getBoss().setCanBeStunned(false);
				this.getBoss().setStunned(false);
			}
			this.currHealTicks++;
		}
	}

	public int getHealingAmount() {
		if (this.entity.getTimesHealed() <= 0) {
			return 0;
		}
		return this.healingDuration / this.entity.getTimesHealed();
	}

	@Override
	public void stop() {
		super.stop();
		this.getBoss().setCanBeStunned(true);
		this.currHealTicks = 0;
		this.entity.setHealing(false);
		this.entity.setInShell(true);
		this.entity.setReadyToSpin(true);
		this.healingActive = false;
	}

}
