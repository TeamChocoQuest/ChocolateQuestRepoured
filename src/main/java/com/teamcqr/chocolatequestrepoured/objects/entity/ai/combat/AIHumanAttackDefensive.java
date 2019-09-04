package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIInteractBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EnumAiState;

import net.minecraft.entity.EntityLivingBase;

public class AIHumanAttackDefensive extends AIInteractBase {
      private int attackCooldown;
      float moveSpeed;

      public AIHumanAttackDefensive(EntityCQRHumanBase par1EntityLiving, float speed) {
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
            boolean canSeeTarget = this.owner.getEntitySenses().canSee(this.entityTarget);
            this.owner.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
            double dist = this.owner.getDistanceSq(this.entityTarget.posX, this.entityTarget.getEntityBoundingBox().minY, this.entityTarget.posZ);
            boolean goToTarget = true;
            if (this.owner.getLeader() != null) {
                  if (this.owner.getLeader().getDistance(this.entityTarget) > (float)(this.owner.partyDistanceToLeader * this.owner.partyDistanceToLeader)) {
                        this.stayInFormation();
                        goToTarget = false;
                  }
            } else if ((this.owner.AIMode == EnumAiState.WARD.ordinal() || this.owner.AIMode == EnumAiState.PATH.ordinal()) && this.owner.hasHome() && !this.owner.isWithinHomeDistanceCurrentPosition()) {
                  goToTarget = false;
                  this.owner.setAttackTarget((EntityLivingBase)null);
            }

            if (goToTarget) {
                  boolean havePath = this.owner.onGround;
                  if (canSeeTarget && --this.attackCooldown <= 0) {
                        if (dist > 16.0D) {
                              this.owner.setSprinting(true);
                        }

                        this.getNavigator().tryMoveToEntityLiving(this.entityTarget, (double)this.moveSpeed);
                  }
            }

            if (canSeeTarget) {
                  this.attackTarget(dist);
            }

      }
}
