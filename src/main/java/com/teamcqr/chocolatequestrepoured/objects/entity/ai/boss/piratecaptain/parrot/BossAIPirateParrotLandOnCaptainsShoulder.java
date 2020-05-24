package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.piratecaptain.parrot;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateParrot;

import net.minecraft.entity.ai.EntityAIBase;

public class BossAIPirateParrotLandOnCaptainsShoulder extends EntityAIBase {

	private final EntityCQRPirateParrot entity;
    private AbstractEntityCQR owner;
    private boolean isSittingOnShoulder;

    public BossAIPirateParrotLandOnCaptainsShoulder(EntityCQRPirateParrot parrot)
    {
        this.entity = parrot;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	AbstractEntityCQR entitylivingbase = (AbstractEntityCQR) this.entity.getOwner();
        boolean flag = entitylivingbase != null &&  !entitylivingbase.isInWater();
        return !this.entity.isSitting() && flag && this.entity.canSitOnShoulder();
    }

    /**
     * Determine if this AI Task is interruptible by a higher (= lower value) priority task. All vanilla AITask have
     * this value set to true.
     */
    public boolean isInterruptible()
    {
        return !this.isSittingOnShoulder;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.owner = (AbstractEntityCQR) this.entity.getOwner();
        this.isSittingOnShoulder = false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        if (!this.isSittingOnShoulder && !this.entity.isSitting() && !this.entity.getLeashed())
        {
            if (this.entity.getEntityBoundingBox().intersects(this.owner.getEntityBoundingBox()))
            {
                this.isSittingOnShoulder = this.entity.setCQREntityOnShoulder(this.owner);
            }
        }
    }

}
