package com.example.chocolatequest.entity.ai.boss.exterminator;

import net.minecraft.world.entity.LivingEntity;
import com.example.chocolatequest.entity.ai.AbstractCQREntityAI;
import com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator;
import com.example.chocolatequest.entity.boss.exterminator.EntityExterminatorHandLaser;
import com.example.chocolatequest.entity.projectile.AbstractEntityLaser;

import java.util.EnumSet;

public class BossAIExterminatorHandLaser extends net.minecraft.world.entity.ai.goal.Goal {
	protected final EntityCQRExterminator mob;
	protected final EntityCQRExterminator entity;

	private static final int MAX_DISTANCE = 32;
	private static final int MIN_DISTANCE = 8;

	private AbstractEntityLaser activeLaser = null;
	private LivingEntity target = null;

	private int timer;
	private int timeOut = 0;
	private int prevTimeAttacked;
	public BossAIExterminatorHandLaser(EntityCQRExterminator entity) {
		this.mob = entity;
		this.entity = entity;

		this.setFlags(java.util.EnumSet.of(net.minecraft.world.entity.ai.goal.Goal.Flag.MOVE, net.minecraft.world.entity.ai.goal.Goal.Flag.LOOK));

		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.timeOut > 0) {
			this.timeOut--;
			return false;
		}
		if (this.entity != null && this.mob.isAlive() && (this.mob.getTarget() != null) && !this.mob.isCurrentlyPlayingAnimation() && this.mob.globalAttackCooldown <= 0) {
			if (this.mob.isStunned()) {
				return false;
			}

			final float distance = this.mob.distanceTo(this.mob.getTarget());
			this.target = this.mob.getTarget();
			return (this.mob.getTarget() != null) && distance <= MAX_DISTANCE && distance >= MIN_DISTANCE;
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.mob.isStunned()) {
			return false;
		}
		return this.mob.isAlive() && this.target != null && this.target.isAlive() && this.timer > 0;
	}

	@Override
	public void start() {
		this.timer = 150;
		super.start();
		this.mob.getNavigation().stop();
		this.mob.playSound(net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE, 1.25F, 0.62F);
		this.checkAndOrStartCannonLaser();
	}

	private boolean checkAndOrStartCannonLaser() {
		// If the laser already exists => no need to validate the rest!
		if (this.activeLaser != null) {
			return true;
		}
		if ((!this.mob.isCannonArmReadyToShoot() || !this.mob.isCannonRaised())) {
			if (!this.mob.isCannonRaised()) {
				if (this.mob.switchCannonArmState(true)) {
				} else {
				}
			}
		} else {
			this.activeLaser = new EntityExterminatorHandLaser(this.entity, this.target);
			this.activeLaser.setupPositionAndRotation();
			this.mob.level().addFreshEntity(this.activeLaser);
			this.mob.switchCannonArmState(true);
			return true;
		}
		return false;
	}

	public void tick() {
		super.tick();
		this.timer--;
		if (this.checkAndOrStartCannonLaser()) {
			
			if (this.target != null) {
				double d0 = this.target.getX() - this.mob.getX();
				double d1 = this.target.getZ() - this.mob.getZ();
				float f = (float)(net.minecraft.util.Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
				this.mob.setYRot(f);
				this.mob.yBodyRot = f;
				this.mob.yHeadRot = f;
				this.mob.yRotO = f;
				this.mob.yBodyRotO = f;
				this.mob.yHeadRotO = f;
			}
			this.mob.getLookControl().setLookAt(this.target, 180, 180);
		} else {
		}
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

	@Override
	public void stop() {
		
		this.target = null;
		this.timer = 0;
		if (this.activeLaser != null) {
			this.activeLaser.remove(net.minecraft.world.entity.Entity.RemovalReason.DISCARDED);
			this.activeLaser = null;
		}
		this.mob.setCannonArmAutoTimeoutForLowering(40);
		this.mob.globalAttackCooldown = 60; // 3 seconds
		this.timeOut = 200;
	}

}

