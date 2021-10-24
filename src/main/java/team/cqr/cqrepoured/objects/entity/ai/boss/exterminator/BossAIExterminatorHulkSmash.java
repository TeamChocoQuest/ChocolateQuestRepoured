package team.cqr.cqrepoured.objects.entity.ai.boss.exterminator;

import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.objects.entity.ai.AbstractCQREntityAI;
import team.cqr.cqrepoured.objects.entity.boss.exterminator.EntityCQRExterminator;
import team.cqr.cqrepoured.objects.entity.projectiles.ProjectileEarthQuake;
import team.cqr.cqrepoured.util.DungeonGenUtils;

public class BossAIExterminatorHulkSmash extends AbstractCQREntityAI<EntityCQRExterminator> {

	protected static final int MIN_COOLDOWN = 100;
	protected static final int MAX_COOLDOWN = 200;

	protected int cooldown = MIN_COOLDOWN;
	private boolean shockwaveWasSpawnedInCurrentCycle = false;

	public BossAIExterminatorHulkSmash(EntityCQRExterminator entity) {
		super(entity);

		this.setMutexBits(2);
	}

	@Override
	public boolean shouldExecute() {
		if (this.entity != null && !this.entity.isDead && this.entity.hasAttackTarget()) {

			if(this.entity.isStunned()) {
				return false;
			}
			
			// Exterminator specific
			if (this.entity.isCannonRaised()) {
				if (this.entity.isCannonArmPlayingAnimation()) {
					return false;
				}
			}

			if (this.entity.isCurrentlyPlayingAnimation()) {
				return false;
			}

			if (this.entity.isSurroundedByGroupWithMinSize(10) != null) {
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
	public void startExecuting() {
		this.entity.sendAnimationUpdate(EntityCQRExterminator.ANIM_NAME_GROUND_SMASH);
	}

	@Override
	public boolean shouldContinueExecuting() {
		if(this.entity.isStunned()) {
			return false;
		}
		return this.entity.getCurrentAnimation() != null && this.entity.getCurrentAnimation().equalsIgnoreCase(EntityCQRExterminator.ANIM_NAME_GROUND_SMASH);
	}

	@Override
	public void updateTask() {
		super.updateTask();
		if (this.entity.isCannonRaised()) {
			this.entity.switchCannonArmState(false);
			return;
		}

		// Time when animation "hits" the ground => 1.6 seconds => 32 ticks
		if (this.entity.getCurrentAnimation().equalsIgnoreCase(EntityCQRExterminator.ANIM_NAME_GROUND_SMASH) && this.entity.getCurrentAnimationTicks() <= (EntityCQRExterminator.GROUND_SLAM_DURATION - 32) && !shockwaveWasSpawnedInCurrentCycle) {
			this.shockwaveWasSpawnedInCurrentCycle = true;

			// Now, spawn a explosion and create the shockwave entities
			final Vec3d hitLocation = this.entity.getPositionVector().add(this.entity.getLookVec().normalize().scale(1.5 * this.entity.getSizeVariation()));
			this.world.createExplosion(this.entity, hitLocation.x, hitLocation.y, hitLocation.z, 4.0F, false);

			// now, create the shockwaves
			final int quakeCount = 64;
			float angle = 360.0F / quakeCount;
			for (int i = 0; i < quakeCount; i++) {
				ProjectileEarthQuake peq = new ProjectileEarthQuake(this.world, this.entity);
				peq.shoot(this.entity, 0, angle * i, 0.0F, 0.5F, 0.0F);
				peq.setThrowHeight(0.6D);
				peq.setPosition(hitLocation.x, hitLocation.y, hitLocation.z);

				this.world.spawnEntity(peq);
			}
		}
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.cooldown = DungeonGenUtils.randomBetween(MIN_COOLDOWN, MAX_COOLDOWN, this.entity.getRNG());
		this.shockwaveWasSpawnedInCurrentCycle = false;
	}

}
