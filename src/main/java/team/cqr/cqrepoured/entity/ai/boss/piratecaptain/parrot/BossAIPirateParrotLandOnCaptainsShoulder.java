package team.cqr.cqrepoured.entity.ai.boss.piratecaptain.parrot;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;

public class BossAIPirateParrotLandOnCaptainsShoulder extends Goal {

	private final EntityCQRPirateParrot entity;
	private LivingEntity owner;
	private boolean isSittingOnShoulder;

	public BossAIPirateParrotLandOnCaptainsShoulder(EntityCQRPirateParrot parrot) {
		this.entity = parrot;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean canUse() {
		LivingEntity entitylivingbase = this.entity.getOwner();
		boolean flag = entitylivingbase != null && !entitylivingbase.isInWater();
		return !this.entity.isInSittingPose() && flag && this.entity.canSitOnShoulder();
	}

	/**
	 * Determine if this AI Task is interruptible by a higher (= lower value) priority task. All vanilla AITask have this
	 * value set to true.
	 */
	@Override
	public boolean isInterruptable() {
		return !this.isSittingOnShoulder;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		this.owner = this.entity.getOwner();
		this.isSittingOnShoulder = false;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void tick() {
		if (!this.isSittingOnShoulder && !this.entity.isInSittingPose() && !this.entity.isLeashed()) {
			if (this.entity.getBoundingBox().intersects(this.owner.getBoundingBox())) {
				if (this.owner instanceof AbstractEntityCQR) {
					this.isSittingOnShoulder = this.entity.setCQREntityOnShoulder((AbstractEntityCQR) this.owner);
				} else if (this.owner instanceof ServerPlayerEntity) {
					this.isSittingOnShoulder = this.entity.setEntityOnShoulder((ServerPlayerEntity) this.owner);
				}
			}
		}
	}

}
