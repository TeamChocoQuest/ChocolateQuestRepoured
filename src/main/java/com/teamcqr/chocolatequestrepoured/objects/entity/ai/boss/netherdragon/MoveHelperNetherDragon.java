package com.teamcqr.chocolatequestrepoured.objects.entity.ai.boss.netherdragon;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRNetherDragon;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class MoveHelperNetherDragon extends EntityMoveHelper {

	public MoveHelperNetherDragon(EntityCQRNetherDragon entitylivingIn) {
		super(entitylivingIn);
		this.parentEntity = entitylivingIn;
	}
	
	private final EntityCQRNetherDragon parentEntity;
    private int courseChangeCooldown;

    public void onUpdateMoveHelper()
    {
        if (this.action == EntityMoveHelper.Action.MOVE_TO)
        {
            double d0 = this.posX - this.parentEntity.posX;
            double d1 = this.posY - this.parentEntity.posY;
            double d2 = this.posZ - this.parentEntity.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (this.courseChangeCooldown-- <= 0)
            {
                this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                d3 = (double)MathHelper.sqrt(d3);

                if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
                {
                    this.parentEntity.motionX += d0 / d3 * 0.1D;
                    this.parentEntity.motionY += d1 / d3 * 0.1D;
                    this.parentEntity.motionZ += d2 / d3 * 0.1D;
                }
                else
                {
                    this.action = EntityMoveHelper.Action.WAIT;
                }
            }
        }
    }

    /**
     * Checks if entity bounding box is not colliding with terrain
     */
    private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
    {
        return true;
    }

}
