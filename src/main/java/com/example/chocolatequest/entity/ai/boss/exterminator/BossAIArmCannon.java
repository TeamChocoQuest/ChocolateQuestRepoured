package com.example.chocolatequest.entity.ai.boss.exterminator;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;
import com.example.chocolatequest.entity.ai.goal.RangedAttackGoalCQR;
import com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator;
import com.example.chocolatequest.entity.projectile.ProjectileCannonBall;
import com.example.chocolatequest.registry.ModSounds;
import net.minecraft.util.Mth;

public class BossAIArmCannon extends net.minecraft.world.entity.ai.goal.Goal {
	protected final EntityCQRExterminator mob;
	protected final EntityCQRExterminator entity;

	private boolean isCurrentSequenceFast = false;
	private boolean isSequenceRunning = false;
	private int remainingShotsInSequence = -1;

	private static final int SHOT_COUNT_FAST = 10;
	private static final int SHOT_COUNT_NORMAL = 1;

	private static final double MIN_DISTANCE_SQ = 64;

	private int cooldown = 100;

	private static final int MIN_COOLDOWN = 200;
	private static final int MAX_COOLDOWN = 400;

	@Override
	public boolean canUse() {
		if (this.mob.getTarget() != null) {
			if (this.mob.isStunned()) {
				return false;
			}

			if (this.cooldown > 0) {
				this.cooldown--;
			}
			if (this.isFarAwayEnough()) {
				if ((this.mob.getTarget() != null)) {
					this.cooldown -= this.mob.distanceTo(this.mob.getTarget());
				}

				return this.cooldown <= 0;
			}
		}
		return false;
	}

	
	public boolean canStrafe() {
		return !this.isSequenceRunning && !this.mob.isStunned();
	}

	@Override
	public boolean canContinueToUse() {
		if (this.mob.isStunned()) {
			return false;
		}
		return this.mob.getTarget() != null && this.mob.getTarget().isAlive() && (this.isFarAwayEnough() || this.isSequenceRunning);
	}

	private boolean isFarAwayEnough() {
		return this.mob.distanceToSqr(this.mob.getTarget()) >= MIN_DISTANCE_SQ;
	}
	private int prevTimeAttacked;
	public BossAIArmCannon(EntityCQRExterminator entity) {
		this.mob = entity;
		this.entity = entity;

		this.setFlags(java.util.EnumSet.of(net.minecraft.world.entity.ai.goal.Goal.Flag.MOVE, net.minecraft.world.entity.ai.goal.Goal.Flag.LOOK));
	}

	public void tick() {
		LivingEntity attackTarget = this.mob.getTarget();
		if(attackTarget == null) return;
		if (!this.canStrafe()) this.mob.getNavigation().stop();
		else this.mob.getNavigation().moveTo(attackTarget, this.getStrafingSpeed());
		
		if (this.mob.tickCount > this.prevTimeAttacked + 60) {
			if (this.mob.isCannonRaised()) {
				if (this.mob.isCannonArmReadyToShoot()) {
					if (!this.isSequenceRunning) {
						this.isSequenceRunning = true;
						this.isCurrentSequenceFast = this.mob.getRandom().nextBoolean();
						this.remainingShotsInSequence = this.isCurrentSequenceFast ? SHOT_COUNT_FAST : SHOT_COUNT_NORMAL;
					}

					this.mob.startShootingAnimation(this.isCurrentSequenceFast);

					this.mob.getLookControl().setLookAt(attackTarget, 180, 180);

					ProjectileCannonBall cannonBall = new ProjectileCannonBall(this.mob, this.mob.level(), this.isCurrentSequenceFast);
					final Vec3 armPos = this.mob.getCannonFiringLocation();

					this.spawnParticles(armPos);

					cannonBall.setPos(armPos.x, armPos.y, armPos.z);

					double vx = attackTarget.getX() - this.mob.getX() + this.mob.getDeltaMovement().x;
					double vy = attackTarget.getY() + attackTarget.getBbHeight() * 0.5D - armPos.y + this.mob.getDeltaMovement().y;
					double vz = attackTarget.getZ() - this.mob.getZ() + this.mob.getDeltaMovement().z;

					float inaccuracy = 1.0f;
					if (this.isCurrentSequenceFast) {
						inaccuracy *= 2;
					}

					cannonBall.shoot(vx, vy, vz, 1.2F, inaccuracy);
					this.mob.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE.value(), 5.0F, 0.75F + this.mob.getRandom().nextFloat() * 0.5F);

					this.mob.level().addFreshEntity(cannonBall);

					this.remainingShotsInSequence--;
					if (this.remainingShotsInSequence <= 0) {
						this.prevTimeAttacked = this.mob.tickCount;
						this.stop();
					}
				}
			} else {
				this.mob.switchCannonArmState(true);
			}

		}
	}

	public float getStrafingSpeed() {
		if (this.isCurrentSequenceFast) {
			return 1.25F;
		}
		return 1.0F;
	}
	
	final ParticleOptions[] SHOOT_PARTICLES = new ParticleOptions[] {
		ParticleTypes.LARGE_SMOKE,
		ParticleTypes.CLOUD,
		ParticleTypes.FLAME
	};

	private void spawnParticles(Vec3 armPos) {
		if (this.mob.level() instanceof ServerLevel) {
			ServerLevel sw = (ServerLevel)this.mob.level();
			
			for(int i = 0; i < SHOOT_PARTICLES.length; i++) {
				int count = i > 0 ? 5 : 10;
				sw.sendParticles(SHOOT_PARTICLES[i], armPos.x, armPos.y, armPos.z, count, 0, 0, 0, 0.05D);
			}
		} else {
			for(int i = 0; i < SHOOT_PARTICLES.length; i++) {
				int count = i > 0 ? 5 : 10;
				for(int j = 0; j < count; j++) {
					this.mob.level().addParticle(SHOOT_PARTICLES[i], armPos.x, armPos.y, armPos.z, 0, 0, 0);
				}
			}
		}
	}

	@Override
	public boolean isInterruptable() {
		return this.mob.getRandom().nextBoolean();
	}

	
	protected int getAttackCooldown() {
		// Same values as for the bow => 1, 1.5 and 2 seconds for hard, normal, any other
		switch (this.mob.level().getDifficulty()) {
		case HARD:
			return 20;
		case NORMAL:
			return 30;
		default:
			return 40;
		}
	}

	@Override
	public void stop() {
		

		this.mob.setCannonArmAutoTimeoutForLowering(40);

		this.isSequenceRunning = false;

		this.cooldown = net.minecraft.util.Mth.nextInt(this.mob.getRandom(), MIN_COOLDOWN, MAX_COOLDOWN);
	}

	// Since the weapon belongs to the entity we always have a weapon, but only if the cannon is ready and not in use
	
	protected boolean isRangedWeapon(Item item) {
		return true;
	}

}

