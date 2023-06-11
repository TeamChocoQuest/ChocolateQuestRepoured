package team.cqr.cqrepoured.entity.ai.boss.giantspider;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.util.Mth;

import java.util.EnumSet;

public class BossAISpiderLeapAttack extends Goal {
	/** The entity that is leaping. */
	MobEntity leaper;
	/** The entity that the leaper is leaping towards. */
	LivingEntity leapTarget;
	/** The entity's motionY after leaping. */
	float leapMotionY = 0.8F;

	private int cooldown;

	private final double MIN_VERTICAL_DISTANCE_TO_LEAP = 2;
	private final double MIN_DISTANCE_TO_LEAP = 9;
	private final double MAX_LEAP_DISTANCE = 256;
	private final int MAX_COOLDOWN = 40;

	public BossAISpiderLeapAttack(MobEntity leapingEntity, float leapMotionYIn) {
		this.leaper = leapingEntity;
		this.leapMotionY = leapMotionYIn;
		//this.setMutexBits(5);
		this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean canUse() {
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		this.leapTarget = this.leaper.getTarget();

		if (this.leapTarget == null) {
			return false;
		} else {
			double d0 = this.leaper.distanceToSqr(this.leapTarget);

			double distVert = this.leapTarget.getY() - this.leaper.getY();

			if ((d0 >= this.MIN_DISTANCE_TO_LEAP || distVert >= this.MIN_VERTICAL_DISTANCE_TO_LEAP) && d0 <= this.MAX_LEAP_DISTANCE) {
				if (!this.leaper.isOnGround()) {
					return false;
				} else {
					return this.leaper.getRandom().nextInt(3) == 0;
				}
			} else {
				return false;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean canContinueToUse() {
		return !this.leaper.isOnGround();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		double d0 = this.leapTarget.getX() - this.leaper.getX();
		double d1 = this.leapTarget.getZ() - this.leaper.getZ();
		float f = Mth.sqrt(d0 * d0 + d1 * d1);

		this.leaper.getLookControl().setLookAt(this.leapTarget, 100F, 100F);

		double vx = this.leaper.getDeltaMovement().x;
		double vy = this.leaper.getDeltaMovement().y;
		double vz = this.leaper.getDeltaMovement().z;
		
		if (f >= 1.0E-4D) {
			vx += d0 / f * 0.5D * 8.4F + vx * 8.4F;
			vz += d1 / f * 0.5D * 8.4F + vz * 8.4F;
		}

		vy = (this.leapTarget.getY() - this.leaper.getY()) * 0.5;

		vy = vy < this.leapMotionY ? this.leapMotionY : vy;

		this.leaper.setDeltaMovement(vx, vy, vz);
		this.leaper.hasImpulse = true;
	}

	@Override
	public void stop() {
		this.cooldown = this.MAX_COOLDOWN;

		super.stop();
	}

}
