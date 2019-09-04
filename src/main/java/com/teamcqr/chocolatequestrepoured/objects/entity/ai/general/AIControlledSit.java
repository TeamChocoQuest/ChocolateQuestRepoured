package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIControlledBase;

public class AIControlledSit extends AIControlledBase {
      boolean shouldLie;

      public AIControlledSit(EntityCQRHumanBase owner) {
            super(owner);
            this.shouldLie = false;
      }

      public AIControlledSit(EntityCQRHumanBase owner, boolean lie) {
            this(owner);
            this.shouldLie = lie;
      }

      public boolean shouldExecute() {
            return true;
      }

      public void updateTask() {
            if (!this.owner.isSitting()) {
                  this.owner.setSitting(true);
            }

            this.owner.getNavigator().clearPath();
      }

      public void resetTask() {
            this.owner.setSitting(false);
            super.resetTask();
      }

      public void startExecuting() {
            this.owner.setSitting(true);
            super.startExecuting();
      }
}
