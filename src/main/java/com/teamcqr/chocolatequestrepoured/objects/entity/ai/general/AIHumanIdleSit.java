package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.entity.ai.EntityAIBase;

public class AIHumanIdleSit extends EntityAIBase {
      int iddleTime = 0;
      EntityCQRHumanBase owner;

      public AIHumanIdleSit(EntityCQRHumanBase owner) {
            this.owner = owner;
            this.setMutexBits(1);
      }

      public boolean shouldExecute() {
            if (this.owner.getAttackTarget() == null && this.owner.getRidingEntity() == null) {
                  if (this.owner.getOwner() != null && (this.owner.getDistance(this.owner.getOwner()) > 15.0F || this.owner.getAttackTarget() != null)) {
                        return false;
                  } else {
                        return this.owner.getNavigator().getPath() == null && this.owner.getAttackTarget() == null;
                  }
            } else {
                  return false;
            }
      }

      public boolean continueExecuting() {
            return super.shouldContinueExecuting() && this.iddleTime > 0;
      }

      public void startExecuting() {
            boolean flag = false;
            if (this.owner.getOwner() instanceof EntityCQRHumanBase && ((EntityCQRHumanBase)this.owner.getOwner()).isSitting()) {
                  flag = true;
            }

            if (this.owner.getRNG().nextInt(50) == 0 || flag) {
                  this.iddleTime = 500 + this.owner.getRNG().nextInt(1000);
                  this.owner.setSitting(true);
            }

      }

      public void resetTask() {
            this.owner.setSitting(false);
      }

      public void updateTask() {
            if (this.iddleTime > 0) {
                  --this.iddleTime;
            }

      }
}
