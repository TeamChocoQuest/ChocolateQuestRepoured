package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise.ETortoiseAnimState;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.minecraft.util.math.Vec3d;

public class AISpinAttackTurtle extends AnimationAI<EntityCQRGiantTortoise> {
	
	private Vec3d movementVector;
	
	private static final int COOLDOWN = 100;
	private int cooldown = 0;

	public AISpinAttackTurtle(EntityCQRGiantTortoise entity) {
		super(entity);
		//setMutexBits(8);
	}

	private EntityCQRGiantTortoise getBoss() {
		return (EntityCQRGiantTortoise) this.entity;
	}
	
	@Override
	public Animation getAnimation() {
		return EntityCQRGiantTortoise.ANIMATION_SPIN;
	}

	@Override
	public boolean shouldExecute() {
		cooldown--;
		if(!getBoss().isStunned() && getBoss().getAttackTarget() != null && !getBoss().getAttackTarget().isDead && cooldown <= 0) {
			getBoss().setWantsToSpin(true);
			if(getBoss().isInShell() && getBoss().isReadyToSpin()) {
				getBoss().setCanBeStunned(false);
				getBoss().setSpinning(true);
				getBoss().setWantsToSpin(false);
				return true;
			} else {
				getBoss().targetNewState(EntityCQRGiantTortoise.TARGET_MOVE_IN);
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting() && getBoss() != null && !getBoss().isDead && getBoss().getAttackTarget() != null && !getBoss().getAttackTarget().isDead;
	}
	
	private void calculateVelocity() {
		this.movementVector = getBoss().getAttackTarget().getPositionVector().subtract(getBoss().getPositionVector());
		this.movementVector = this.movementVector.normalize();
		this.movementVector = this.movementVector.scale(0.75D);
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.getBoss().setSpinning(true);
		this.getBoss().setCanBeStunned(false);
		this.getBoss().setInShell(true);
		this.getBoss().setReadyToSpin(false);
		getBoss().setAnimation(getAnimation());
		getBoss().currentAnim = this;
		getBoss().setAnimationTick(0);
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		//this.getBoss().setSpinning(false);
		if(getBoss().getAnimationTick() > 20 && getAnimation().getDuration() - getBoss().getAnimationTick() > 20) {
			if(getBoss().collidedHorizontally || movementVector == null || getBoss().getDistance(getBoss().getAttackTarget()) >= 20) {
				calculateVelocity();
			}
			this.getBoss().setSpinning(true);
			this.getBoss().setCanBeStunned(false);
			this.getBoss().setInShell(true);
			getBoss().motionX = movementVector.x;
			getBoss().motionZ = movementVector.z;
			getBoss().motionY = movementVector.y /2;
		} else if(getBoss().getAnimationTick() < 20) {
			this.getBoss().setSpinning(false);
			//TODO: Shoot bubbles
		} else {
			this.getBoss().setSpinning(false);
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		this.getBoss().setSpinning(false);
		this.getBoss().setReadyToSpin(true);
		this.getBoss().setCanBeStunned(true);
		this.getBoss().setCurrentAnimation(ETortoiseAnimState.NONE);
		cooldown = COOLDOWN;
		getBoss().setAnimationTick(0);
	}

}
