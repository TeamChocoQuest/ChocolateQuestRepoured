package team.cqr.cqrepoured.entity.ai.boss.exterminator;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.entity.ai.attack.EntityAIAttackRanged;
import team.cqr.cqrepoured.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.entity.projectiles.ProjectileCannonBall;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class BossAIArmCannon extends EntityAIAttackRanged<EntityCQRExterminator> {

	private boolean isCurrentSequenceFast = false;
	private boolean isSequenceRunning = false;
	private int remainingShotsInSequence = -1;

	private static final int SHOT_COUNT_FAST = 10;
	private static final int SHOT_COUNT_NORMAL = 1;

	private static final double MIN_DISTANCE_SQ = 9;

	private int cooldown = 100;

	private static final int MIN_COOLDOWN = 200;
	private static final int MAX_COOLDOWN = 400;

	@Override
	public boolean canUse() {
		if (super.canUse()) {
			if (this.entity.isStunned()) {
				return false;
			}

			if (this.cooldown > 0) {
				this.cooldown--;
			}
			if (this.isFarAwayEnough()) {
				if (this.entity.hasAttackTarget()) {
					this.cooldown -= this.entity.distanceTo(this.entity.getTarget());
				}

				return this.cooldown <= 0;
			}
		}
		return false;
	}

	@Override
	protected boolean canStrafe() {
		return super.canStrafe() && !this.isSequenceRunning && !this.entity.isStunned();
	}

	@Override
	public boolean canContinueToUse() {
		if (this.entity.isStunned()) {
			return false;
		}
		return super.canContinueToUse() && (this.isFarAwayEnough() || this.isSequenceRunning);
	}

	private boolean isFarAwayEnough() {
		return this.entity.distanceToSqr(this.entity.getTarget()) >= MIN_DISTANCE_SQ;
	}

	public BossAIArmCannon(EntityCQRExterminator entity) {
		super(entity);
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity attackTarget) {
		if (this.entity.tickCount > this.prevTimeAttacked + this.getAttackCooldown()) {
			if (this.entity.isCannonRaised()) {
				if (this.entity.isCannonArmReadyToShoot()) {
					if (!this.isSequenceRunning) {
						this.isSequenceRunning = true;
						this.isCurrentSequenceFast = this.entity.getRandom().nextBoolean();
						this.remainingShotsInSequence = this.isCurrentSequenceFast ? SHOT_COUNT_FAST : SHOT_COUNT_NORMAL;
					}

					this.entity.startShootingAnimation(this.isCurrentSequenceFast);

					this.entity.getLookControl().setLookAt(attackTarget, 180, 180);

					ProjectileCannonBall cannonBall = new ProjectileCannonBall(this.world, this.entity, this.isCurrentSequenceFast);
					final Vector3d armPos = this.entity.getCannonFiringLocation();

					this.spawnParticles(armPos);

					cannonBall.setPos(armPos.x, armPos.y, armPos.z);

					double vx = attackTarget.getX() - this.entity.getX() + this.entity.getDeltaMovement().x;
					double vy = attackTarget.getY() + attackTarget.getBbHeight() * 0.5D - armPos.y + this.entity.getDeltaMovement().y;
					double vz = attackTarget.getZ() - this.entity.getZ() + this.entity.getDeltaMovement().z;

					float inaccuracy = this.getInaccuracy();
					if (this.isCurrentSequenceFast) {
						inaccuracy *= 2;
					}

					cannonBall.shoot(vx, vy, vz, 1.2F, inaccuracy);
					this.entity.playSound(CQRSounds.EXTERMINATOR_CANNON_SHOOT, 5.0F, 0.75F + this.entity.getRandom().nextFloat() * 0.5F);

					this.world.addFreshEntity(cannonBall);

					this.remainingShotsInSequence--;
					if (this.remainingShotsInSequence <= 0) {
						this.prevTimeAttacked = this.entity.tickCount;
						this.stop();
					}
				}
			} else {
				this.entity.switchCannonArmState(true);
			}

		}
	}

	@Override
	protected float getStrafingSpeed() {
		if (this.isCurrentSequenceFast) {
			return 1.25F * super.getStrafingSpeed();
		}
		return super.getStrafingSpeed();
	}

	private void spawnParticles(Vector3d armPos) {
		if (this.world instanceof ServerWorld) {
			for(int i = 0; i < 5; i++) {
				((ServerWorld) this.world).addParticle(ParticleTypes.LARGE_SMOKE, true, armPos.x, armPos.y, armPos.z, 0, 0, 0);
				((ServerWorld) this.world).addParticle(ParticleTypes.LARGE_SMOKE, true, armPos.x, armPos.y, armPos.z, 0, 0, 0);
				
				((ServerWorld) this.world).addParticle(ParticleTypes.CLOUD, true, armPos.x, armPos.y, armPos.z, 0, 0, 0);
				((ServerWorld) this.world).addParticle(ParticleTypes.FLAME, true, armPos.x, armPos.y, armPos.z, 0, 0, 0);
			}
		}
	}

	@Override
	public boolean isInterruptable() {
		return this.entity.getRandom().nextBoolean();
	}

	@Override
	protected int getAttackCooldown() {
		// Same values as for the bow => 1, 1.5 and 2 seconds for hard, normal, any other
		switch (this.world.getDifficulty()) {
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
		super.stop();

		this.entity.setCannonArmAutoTimeoutForLowering(40);

		this.isSequenceRunning = false;

		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRandom());
	}

	// Since the weapon belongs to the entity we always have a weapon, but only if the cannon is ready and not in use
	@Override
	protected boolean isRangedWeapon(Item item) {
		return true;
	}

}
