package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.util.math.MathHelper;

public class AIHumanAttackAggressiveBackstab extends AIHumanAttackAggressive {
      public AIHumanAttackAggressiveBackstab(EntityCQRHumanBase par1EntityLiving, float speed, boolean requireSight) {
            super(par1EntityLiving, speed, requireSight);
      }

      public boolean tryToMoveToEntity() {
            float targetAngle = (this.entityTarget.rotationYawHead - 180.0F) * 3.1416F / 180.0F;
            double cos = (double)MathHelper.cos(targetAngle);
            double sin = (double)MathHelper.sin(targetAngle);

            float angle;
            for(angle = this.owner.rotationYawHead - this.entityTarget.rotationYawHead; angle > 360.0F; angle -= 360.0F) {
                  ;
            }

            while(angle < 0.0F) {
                  angle += 360.0F;
            }

            angle = 180.0F - Math.abs(angle - 180.0F);
            double dist = Math.min(2.5D, (double)(Math.abs(angle) / 60.0F));
            double x = this.entityTarget.posX + -sin * dist;
            double y = this.entityTarget.posY;
            double z = this.entityTarget.posZ + cos * dist;
            return this.tryMoveToXYZ(x, y, z, 1.0F);
      }
}
