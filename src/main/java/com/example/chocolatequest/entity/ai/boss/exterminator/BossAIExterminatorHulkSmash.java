package com.example.chocolatequest.entity.ai.boss.exterminator;

import net.minecraft.world.phys.Vec3;
import com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;

public class BossAIExterminatorHulkSmash extends net.minecraft.world.entity.ai.goal.Goal {
	protected final EntityCQRExterminator mob;
	protected final EntityCQRExterminator entity;

	protected static final int MIN_COOLDOWN = 100;
	protected static final int MAX_COOLDOWN = 200;

	protected int cooldown = MIN_COOLDOWN;
	private boolean shockwaveWasSpawnedInCurrentCycle = false;
	public BossAIExterminatorHulkSmash(EntityCQRExterminator entity) {
		this.mob = entity;
		this.entity = entity;

		this.setFlags(java.util.EnumSet.of(net.minecraft.world.entity.ai.goal.Goal.Flag.MOVE, net.minecraft.world.entity.ai.goal.Goal.Flag.LOOK));

		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.entity != null && !this.mob.isDeadOrDying() && (this.mob.getTarget() != null)) {

			if (this.mob.isStunned()) {
				return false;
			}

			// Exterminator specific
			if (this.mob.isCannonRaised()) {
				if (this.mob.isCannonArmPlayingAnimation()) {
					return false;
				}
			}

			if (this.mob.isCurrentlyPlayingAnimation()) {
				return false;
			}

			if (this.mob.isSurroundedByGroupWithMinSize(10) != null) {
				return true;
			}

			if (this.cooldown > 0) {
				this.cooldown--;
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void start() {
		this.mob.sendAnimationUpdate(EntityCQRExterminator.ANIM_NAME_GROUND_SMASH);
		this.mob.getNavigation().stop();
		this.mob.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.5F, 0.45F);
	}

	@Override
	public boolean canContinueToUse() {
		if (this.mob.isStunned()) {
			return false;
		}
		return this.mob.getCurrentAnimation() != null && this.mob.getCurrentAnimation().equalsIgnoreCase(EntityCQRExterminator.ANIM_NAME_GROUND_SMASH);
	}

	public void tick() {
		super.tick();
		if (this.mob.isCannonRaised()) {
			this.mob.switchCannonArmState(false);
			return;
		}

		if (this.mob.getCurrentAnimation().equalsIgnoreCase(EntityCQRExterminator.ANIM_NAME_GROUND_SMASH) && this.mob.getCurrentAnimationTicks() <= (EntityCQRExterminator.GROUND_SLAM_DURATION - 32) && !this.shockwaveWasSpawnedInCurrentCycle) {
			this.shockwaveWasSpawnedInCurrentCycle = true;

			// Position where the arms approximately hit the ground.
			final Vec3 hitLocation = this.mob.position().add(this.mob.getLookAngle().normalize().scale(1.5 * 1.0));
			this.mob.playSound(SoundEvents.GENERIC_EXPLODE.value(), 2.4F, 0.65F);
			if (this.mob.level() instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.EXPLOSION, hitLocation.x, hitLocation.y + 0.25D, hitLocation.z,
						4, 1.0D, 0.2D, 1.0D, 0.05D);
				serverLevel.sendParticles(ParticleTypes.CLOUD, hitLocation.x, hitLocation.y + 0.15D, hitLocation.z,
						70, 3.2D, 0.18D, 3.2D, 0.12D);
			}

			// A direct expanding impact replaces the old 64 empty projectile entities.
			for (LivingEntity target : this.mob.level().getEntitiesOfClass(LivingEntity.class,
					new AABB(hitLocation, hitLocation).inflate(7.0D, 3.0D, 7.0D),
					living -> living != this.mob && living.isAlive() && !this.mob.isAlliedTo(living))) {
				double distance = Math.max(0.5D, target.position().distanceTo(hitLocation));
				if (distance > 7.0D) continue;
				float damage = (float)(6.0D + (1.0D - distance / 7.0D) * 8.0D);
				target.hurt(this.mob.damageSources().mobAttack(this.mob), damage);
				Vec3 push = target.position().subtract(hitLocation).normalize()
						.scale(1.15D * (1.0D - distance / 10.0D));
				target.push(push.x, 0.55D, push.z);
			}
		}
	}

	@Override
	public void stop() {
		
		this.cooldown = net.minecraft.util.Mth.nextInt(this.mob.getRandom(), MIN_COOLDOWN, MAX_COOLDOWN);
		this.shockwaveWasSpawnedInCurrentCycle = false;
	}

}

