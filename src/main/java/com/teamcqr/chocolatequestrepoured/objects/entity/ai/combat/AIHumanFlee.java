package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIInteractBase;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class AIHumanFlee extends AIInteractBase {
      @SuppressWarnings("unused")
	private int attackCooldown;
      float moveSpeed;
      int x;
      int y;
      int z;
      int pathCD = 0;

      public AIHumanFlee(EntityCQRHumanBase par1EntityLiving, float speed) {
            super(par1EntityLiving);
            this.moveSpeed = speed;
      }

      public boolean shouldExecute() {
            return super.shouldExecute();
      }

      public void startExecuting() {
            super.startExecuting();
            this.attackCooldown = 0;
      }

      public void resetTask() {
            super.resetTask();
      }

      public void updateTask() {
            double dist = this.owner.getDistanceSq(this.entityTarget);
            if (this.pathCD > 0) {
                  --this.pathCD;
            }

            if (dist < 128.0D) {
                  if (this.pathCD == 0) {
                        Vec3d direction = new Vec3d(this.owner.posX + (this.owner.posX - this.entityTarget.posX), this.owner.posY + (this.owner.posY - this.entityTarget.posY), this.owner.posZ + (this.owner.posZ - this.entityTarget.posZ));
                        Vec3d vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.owner, 10, 10, direction);
                        if (vec3 != null) {
                              this.getNavigator().tryMoveToXYZ(vec3.x, vec3.y, vec3.z, 1.2D);
                              this.pathCD = 20;
                        }
                  }
            } else {
                  this.stayInFormation();
            }

      }
}
