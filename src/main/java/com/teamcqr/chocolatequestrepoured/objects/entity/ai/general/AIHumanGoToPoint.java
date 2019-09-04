package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import com.chocolate.chocolateQuest.utils.Vec4I;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIControlledBase;

public class AIHumanGoToPoint extends AIControlledBase {
      public Vec4I currentPos;

      public AIHumanGoToPoint(EntityCQRHumanBase owner) {
            super(owner);
      }

      public boolean shouldExecute() {
            if (this.owner.currentPos != null) {
                  ;
            }

            this.currentPos = this.owner.currentPos;
            return this.currentPos != null;
      }

      public boolean continueExecuting() {
            return this.owner.currentPos == this.currentPos;
      }

      public void resetTask() {
            this.currentPos = null;
            super.resetTask();
      }

      public void updateTask() {
            Vec4I p = this.currentPos;
            double dist = 0.0D;
            float width = 0.0F;
            if (this.owner.getRidingEntity() == null) {
                  dist = this.owner.getDistanceSq((double)p.xCoord, (double)p.yCoord, (double)p.zCoord);
                  width = this.owner.width + 1.0F;
            } else {
                  dist = this.owner.getRidingEntity().getDistanceSq((double)p.xCoord, (double)p.yCoord, (double)p.zCoord);
                  width = this.owner.getRidingEntity().width;
            }

            width *= width;
            if (dist > (double)(width + 1.0F)) {
                  this.tryMoveToXYZ((double)p.xCoord, (double)p.yCoord, (double)p.zCoord, 1.0F);
            } else if (this.owner.currentPos == this.currentPos) {
                  this.owner.currentPos = null;
            }

            super.updateTask();
      }
}
