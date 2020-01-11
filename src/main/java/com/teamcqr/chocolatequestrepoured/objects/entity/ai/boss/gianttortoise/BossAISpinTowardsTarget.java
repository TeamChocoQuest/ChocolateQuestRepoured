package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.gianttortoise;

import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AbstractCQREntityAI;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRGiantTortoise.ETortoiseAnimState;

import net.minecraft.util.math.Vec3d;

public class BossAISpinTowardsTarget extends AbstractCQREntityAI {

	private int INTERNAL_COOLDOWN = 0;
	private int ATTACK_COOLDOWN = 0;

	static final int ATTACK_DELAY = 100;
	static final int SPIN_TIME_MAX = 500;
	static final int SPIN_UP_COOLDWON = 100;
	static final int SPIN_DOWN_COOLDOWN = 100;
	static final int MOVE_PARTS_COOLDOWN = 50;
	
	private Vec3d velocity;

	public BossAISpinTowardsTarget(EntityCQRGiantTortoise entity) {
		super(entity);
	}

	public EntityCQRGiantTortoise getEntity() {
		return (EntityCQRGiantTortoise) this.entity;
	}

	@Override
	public void updateTask() {
		if (this.shouldContinueExecuting()) {

			switch (this.getEntity().getCurrentAnimation()) {
			case SPIN:
				if(this.INTERNAL_COOLDOWN >= SPIN_TIME_MAX) {
					getEntity().setCurrentAnimation(ETortoiseAnimState.SPIN_DOWN);
					getEntity().setInShell(true);
					//getEntity().setVelocity(0, 0, 0);
					entity.motionX = 0;
					entity.motionY = 0;
					entity.motionZ = 0;
					entity.velocityChanged = true;
					this.INTERNAL_COOLDOWN = 0;
					this.ATTACK_COOLDOWN = ATTACK_DELAY; 
				} else {
					if(getEntity().collidedHorizontally) {
						this.INTERNAL_COOLDOWN += 10;
						calcVelo();
					}
					//getEntity().setVelocity(velocity.x, 0, velocity.z);
					entity.motionX = velocity.x;
					entity.motionY = 0;
					entity.motionZ = velocity.z;
					entity.velocityChanged = true;
				}
				break;
			case SPIN_DOWN:
				getEntity().setVelocity(0, 0, 0);
				if(this.INTERNAL_COOLDOWN >= SPIN_DOWN_COOLDOWN) {
					this.INTERNAL_COOLDOWN = 0;
					getEntity().setCurrentAnimation(ETortoiseAnimState.NONE);
					getEntity().setInShell(true);
				}
				break;
			case SPIN_UP:
				getEntity().setVelocity(0, 0, 0);
				if(this.INTERNAL_COOLDOWN >= SPIN_UP_COOLDWON) {
					this.INTERNAL_COOLDOWN = 0;
					getEntity().setCurrentAnimation(ETortoiseAnimState.SPIN);
					getEntity().setInShell(true);
					calcVelo();
				}
				break;
			default:
				return;
			}

			this.INTERNAL_COOLDOWN++;
		}

	}

	private void calcVelo() {
		Vec3d v = getEntity().getAttackTarget().getPositionVector().subtract(getEntity().getPositionVector());
		v = v.normalize();
		v = v.scale(1.6D);
		velocity = v;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(entity.isDead || entity == null) {
			return false;
		}
		if(getEntity().getAttackTarget() != null && !getEntity().getAttackTarget().isDead) {
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldExecute() {
		if(this.getEntity().getAttackTarget() != null && this.ATTACK_COOLDOWN <= 0) {
			this.INTERNAL_COOLDOWN = 0;
			this.ATTACK_COOLDOWN = ATTACK_DELAY;
			return true;
		} else {
			this.ATTACK_COOLDOWN --;
		}
		return false;
	}

}
