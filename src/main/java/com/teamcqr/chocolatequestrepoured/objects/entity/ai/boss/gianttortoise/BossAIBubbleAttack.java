package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBubble;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class BossAIBubbleAttack extends AnimationAI<EntityCQRGiantTortoise> {

	private final double moveSpeedAmp = 1D;
	private int attackCooldown = 40;
	private final float maxAttackDistance = 20 * 20;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private boolean attackInProgress = false;
	private int attackTicks = 0;
	private final int MAX_ATTACK_TICKS = 40;
	private int strafingTime = -1;
	private int attackCharge = 0;

	public BossAIBubbleAttack(EntityCQRGiantTortoise entity) {
		super(entity);
	}
	
	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public Animation getAnimation() {
		return attackInProgress ? EntityCQRGiantTortoise.ANIMATION_SHOOT_BUBBLES : EntityCQRGiantTortoise.NO_ANIMATION;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if(this.entity.getAttackTarget() != null && !(getBoss().isStunned() || getBoss().isHealing() || getBoss().isSpinning() || getBoss().wantsToSpin())) {
			getBoss().setWantsToSpin(false);
			if(getBoss().isInShell()) {
				getBoss().targetNewState(EntityCQRGiantTortoise.TARGET_MOVE_OUT);
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting() {
		return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && getBoss().isInShell();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
		this.entity.setSwingingArms(true);
		entity.setAnimation(getAnimation());
		entity.currentAnim = this;
		entity.setAnimationTick(0);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask() {
		super.resetTask();
		this.entity.setSwingingArms(false);
		entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
		entity.currentAnim = null;
		entity.setAnimationTick(0);
		this.seeTime = 0;
		this.attackTime = -1;
		this.entity.resetActiveHand();
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask() {
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

		if (entitylivingbase != null) {
			double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
			boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);
			boolean flag1 = this.seeTime > 0;

			// Attack
			if (attackInProgress && flag) {
				doAttack();
				attackTicks++;
				if (attackTicks >= MAX_ATTACK_TICKS) {
					attackTicks = 0;
					attackInProgress = false;
					this.attackTime = this.attackCooldown;
					// TODO: Reset animation
				}
				return;
			}

			if (flag != flag1) {
				this.seeTime = 0;
			}

			if (flag) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}

			if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
				this.entity.getNavigator().clearPath();
				++this.strafingTime;
			} else {
				this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1) {
				if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
					this.strafingBackwards = false;
				} else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
					this.strafingBackwards = true;
				}

				this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
				this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
			} else {
				this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
			}

			if (--this.attackTime <= 0 && this.seeTime >= -60) {
				if (!flag && this.seeTime < -60) {
					this.entity.resetActiveHand();
				} else if (flag) {
					attackCharge++;
					if (attackCharge >= 20) {
						attackCharge = 0;
						this.entity.resetActiveHand();
						this.attackInProgress = true;
						this.attackTicks = 0;
					}
				}
			}
			entity.setAnimation(getAnimation());
		}
	}

	private void doAttack() {
		System.out.println("ATTACK IN PROGRESS");
		Vec3d v = entity.getAttackTarget().getPositionVector().subtract(entity.getPositionVector());
		v = v.addVector(entity.getRNG().nextDouble() -0.5D, 0D, entity.getRNG().nextDouble() -0.5D);
		v = v.normalize();
		v = v.scale(1.4);
		entity.faceEntity(entity.getAttackTarget(), 30, 30);
		ProjectileBubble bubble = new ProjectileBubble(entity.world, entity);
		//Vec3d p = entity.getPositionEyes(1F);
		//bubble.setPosition(p.x, p.y, p.z);
		bubble.motionX = v.x;
		bubble.motionY = v.y;
		bubble.motionZ = v.z;
		bubble.velocityChanged = true;
		entity.world.spawnEntity(bubble);
	}

}
