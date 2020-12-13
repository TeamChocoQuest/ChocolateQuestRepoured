package team.cqr.cqrepoured.objects.entity.ai.boss.piratecaptain.parrot;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import team.cqr.cqrepoured.objects.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.objects.entity.boss.EntityCQRPirateParrot;

public class BossAIPirateParrotLandOnCaptainsShoulder extends EntityAIBase {

	private final EntityCQRPirateParrot entity;
	private EntityLivingBase owner;
	private boolean isSittingOnShoulder;

	public BossAIPirateParrotLandOnCaptainsShoulder(EntityCQRPirateParrot parrot) {
		this.entity = parrot;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = (EntityLivingBase) this.entity.getOwner();
		boolean flag = entitylivingbase != null && !entitylivingbase.isInWater();
		return !this.entity.isSitting() && flag && this.entity.canSitOnShoulder();
	}

	/**
	 * Determine if this AI Task is interruptible by a higher (= lower value) priority task. All vanilla AITask have this value set to true.
	 */
	@Override
	public boolean isInterruptible() {
		return !this.isSittingOnShoulder;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.owner = (EntityLivingBase) this.entity.getOwner();
		this.isSittingOnShoulder = false;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void updateTask() {
		if (!this.isSittingOnShoulder && !this.entity.isSitting() && !this.entity.getLeashed()) {
			if (this.entity.getEntityBoundingBox().intersects(this.owner.getEntityBoundingBox())) {
				if (this.owner instanceof AbstractEntityCQR) {
					this.isSittingOnShoulder = this.entity.setCQREntityOnShoulder((AbstractEntityCQR) this.owner);
				} else if (this.owner instanceof EntityPlayer) {
					this.isSittingOnShoulder = this.entity.setEntityOnShoulder((EntityPlayer) this.owner);
				}
			}
		}
	}

}
