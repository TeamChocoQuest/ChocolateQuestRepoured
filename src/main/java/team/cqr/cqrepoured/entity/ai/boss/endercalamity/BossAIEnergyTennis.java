package team.cqr.cqrepoured.entity.ai.boss.endercalamity;

import javax.annotation.Nullable;

import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.phases.EEnderCalamityPhase;
import team.cqr.cqrepoured.entity.projectiles.ProjectileEnergyOrb;
import team.cqr.cqrepoured.init.CQRSounds;

public class BossAIEnergyTennis extends AbstractBossAIEnderCalamity {
	/*
	 * Animation lengths:
	 * - Shooting:
	 * - Spawning (single, it is looped): 1,56s => 32 ticks (sound: 5s => 100 ticks, "bang" sound: 0.8s => 16 ticks )
	 * Transition length: 10 ticks (it is 0 for the shooting
	 */

	@Nullable
	protected ProjectileEnergyOrb tennisball = null;
	private int ballTicks = 0;
	private int remainingAttempts = 0;
	private static final int WARMUP_DURATION = 100;
	private int warmupTime = WARMUP_DURATION;

	public void calculateRemainingAttempts() {
		switch (this.world.getDifficulty()) {
		case HARD:
			this.remainingAttempts = 3;
			break;
		case NORMAL:
			this.remainingAttempts = 6;
			break;
		default:
			this.remainingAttempts = 9;
			break;
		}
	}

	// TODO: Play sound

	public BossAIEnergyTennis(EntityCQREnderCalamity entity) {
		super(entity);
		this.calculateRemainingAttempts();
	}

	@Override
	public boolean canUse() {
		return this.remainingAttempts > 0 && super.canUse() && this.entity.hasAttackTarget();
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse() && (this.tennisball != null || this.warmupTime >= 0) && this.ballTicks < 200;
	}

	@Override
	public void start() {
		super.start();
		this.entity.setCantUpdatePhase(true);
		this.entity.forceTeleport();

		// IMessage message =
		// SPacketUpdateAnimationOfEntity.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_CHARGE_ENERGY_BALL).build();
		// CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		this.entity.sendAnimationUpdate(EntityCQREnderCalamity.ANIM_NAME_CHARGE_ENERGY_BALL);

		this.entity.playSound(CQRSounds.ENDER_CALAMITY_CHARGE_ENERGY_BALL, 12.0F, 1.0F);
	}

	@Override
	public void tick() {
		if (this.entity.hasAttackTarget()) {
			this.entity.getLookControl().setLookAt(this.entity.getTarget(), 90, 90);
		}
		if (this.warmupTime > 0) {
			if (this.warmupTime == (16/* bang sound duration */)) {
				this.entity.playSound(CQRSounds.ENDER_CALAMITY_READY_ENERGY_BALL, 24.0F, 1.0F);
			}
			if (this.warmupTime == 20) {
				// Sound duration of summoning sound: 3s => 60 ticks
				this.entity.playSound(CQRSounds.ENDER_CALAMITY_FIRE_ENERGY_BALL, 24.0F, 2.0F);
			}
			this.warmupTime--;
			return;
		}
		if (this.tennisball == null) {
			// DONE: Play throw sound
			// IMessage message =
			// SPacketUpdateAnimationOfEntity.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_SHOOT_BALL).build();
			// CQRMain.NETWORK.sendToAllTracking(message, this.entity);

			this.entity.sendAnimationUpdate(EntityCQREnderCalamity.ANIM_NAME_SHOOT_BALL);

			this.tennisball = ProjectileEnergyOrb.shootAt(this.entity.getTarget(), this.entity, this.world);
		} else {
			this.ballTicks++;
		}
	}

	@Override
	public void stop() {
		super.stop();
		if (this.tennisball != null) {
			this.tennisball.discard();
		}
		this.tennisball = null;
		this.warmupTime = WARMUP_DURATION;
		this.ballTicks = 0;
		this.entity.setCantUpdatePhase(false);
		// IMessage message =
		// SPacketCalamityUpdateMainAnimation.builder(this.entity).animate(EntityCQREnderCalamity.ANIM_NAME_IDLE_BODY).build();
		// CQRMain.NETWORK.sendToAllTracking(message, this.entity);
		this.remainingAttempts--;
		// TODO: Patch not switching to stunned phase
		if (this.remainingAttempts <= 0) {
			this.entity.forcePhaseChangeToNextOf(EEnderCalamityPhase.PHASE_IDLE.getPhaseObject());
		}
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase == EEnderCalamityPhase.PHASE_ENERGY_TENNIS;
	}

}
