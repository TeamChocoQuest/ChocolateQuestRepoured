package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class AIHumanPanic extends EntityAIBase {
      private EntityCQRHumanBase theEntityCreature;
      private double speed;
      private double randPosX;
      private double randPosY;
      private double randPosZ;

      public AIHumanPanic(EntityCQRHumanBase human, double speed) {
            this.theEntityCreature = human;
            this.speed = speed;
            this.setMutexBits(1);
      }

      public boolean shouldExecute() {
            if (this.theEntityCreature.getAttackTarget() == null && !this.theEntityCreature.isBurning()) {
                  return false;
            } else {
                  Vec3d vec3 = RandomPositionGenerator.findRandomTarget(this.theEntityCreature, 5, 4);
                  if (vec3 == null) {
                        return false;
                  } else {
                        this.randPosX = vec3.x;
                        this.randPosY = vec3.y;
                        this.randPosZ = vec3.z;
                        return true;
                  }
            }
      }

      public void startExecuting() {
            this.theEntityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
      }

      public boolean continueExecuting() {
            return !this.theEntityCreature.getNavigator().noPath();
      }
}
