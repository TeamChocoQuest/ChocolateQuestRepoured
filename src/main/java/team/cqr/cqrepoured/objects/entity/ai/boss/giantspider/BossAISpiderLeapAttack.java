package team.cqr.cqrepoured.objects.entity.ai.boss.giantspider;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;

public class BossAISpiderLeapAttack extends EntityAIBase {
	/** The entity that is leaping. */
	EntityLiving leaper;
	/** The entity that the leaper is leaping towards. */
	EntityLivingBase leapTarget;
	/** The entity's motionY after leaping. */
	float leapMotionY = 0.8F;

	private int cooldown;

	private final double MIN_VERTICAL_DISTANCE_TO_LEAP = 2;
	private final double MIN_DISTANCE_TO_LEAP = 9;
	private final double MAX_LEAP_DISTANCE = 256;
	private final int MAX_COOLDOWN = 40;

	public BossAISpiderLeapAttack(EntityLiving leapingEntity, float leapMotionYIn) {
		this.leaper = leapingEntity;
		this.leapMotionY = leapMotionYIn;
		this.setMutexBits(5);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (this.cooldown > 0) {
			this.cooldown--;
			return false;
		}
		this.leapTarget = this.leaper.getAttackTarget();

		if (this.leapTarget == null) {
			return false;
		} else {
			double d0 = this.leaper.getDistanceSq(this.leapTarget);

			double distVert = this.leapTarget.posY - this.leaper.posY;

			if ((d0 >= this.MIN_DISTANCE_TO_LEAP || distVert >= this.MIN_VERTICAL_DISTANCE_TO_LEAP) && d0 <= this.MAX_LEAP_DISTANCE) {
				if (!this.leaper.onGround) {
					return false;
				} else {
					return this.leaper.getRNG().nextInt(3) == 0;
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
	public boolean shouldContinueExecuting() {
		return !this.leaper.onGround;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		double d0 = this.leapTarget.posX - this.leaper.posX;
		double d1 = this.leapTarget.posZ - this.leaper.posZ;
		float f = MathHelper.sqrt(d0 * d0 + d1 * d1);

		this.leaper.faceEntity(this.leapTarget, 100F, 100F);

		if ((double) f >= 1.0E-4D) {
			this.leaper.motionX += d0 / (double) f * 0.5D * 8.4F + this.leaper.motionX * 8.4F;
			this.leaper.motionZ += d1 / (double) f * 0.5D * 8.4F + this.leaper.motionZ * 8.4F;
		}

		this.leaper.motionY = (this.leapTarget.posY - this.leaper.posY) * 0.5;

		this.leaper.motionY = this.leaper.motionY < this.leapMotionY ? this.leapMotionY : this.leaper.motionY;

		this.leaper.velocityChanged = true;
	}

	@Override
	public void resetTask() {
		this.cooldown = this.MAX_COOLDOWN;

		super.resetTask();
	}

}
