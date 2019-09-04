package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIInteractBase;

import net.minecraft.entity.EntityLivingBase;

public class AIHumanMount extends AIInteractBase {
      boolean requireSight;

      public AIHumanMount(EntityCQRHumanBase par1EntityLiving, float moveSpeed, boolean par3) {
            super(par1EntityLiving);
            this.setMutexBits(3);
            this.requireSight = par3;
      }

      public boolean shouldExecute() {
            if (this.owner.entityToMount != null) {
                  this.entityTarget = this.owner.entityToMount;
                  return true;
            } else {
                  EntityLivingBase var1 = this.owner.getAttackTarget();
                  if (var1 == null) {
                        return false;
                  } else {
                        boolean suitableMount = this.owner.isSuitableMount(var1);
                        if (this.owner.isSuitableMount(var1) && this.owner.getRidingEntity() == null && var1.riddenByEntity == null) {
                              this.entityTarget = var1;
                              return true;
                        } else {
                              if (suitableMount) {
                                    this.owner.setAttackTarget((EntityLivingBase)null);
                              }

                              return false;
                        }
                  }
            }
      }

      public boolean continueExecuting() {
            if (this.owner.getRidingEntity() == null && this.entityTarget.riddenByEntity == null) {
                  return this.entityTarget == null ? false : (!this.entityTarget.isEntityAlive() ? false : (!this.requireSight ? !this.owner.getNavigator().noPath() : true));
            } else {
                  return false;
            }
      }

      public void resetTask() {
            this.owner.setAttackTarget((EntityLivingBase)null);
            this.entityTarget = null;
            this.owner.getNavigator().clearPath();
            this.owner.entityToMount = null;
      }

      public void updateTask() {
            this.owner.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
            if (this.requireSight || this.owner.getEntitySenses().canSee(this.entityTarget)) {
                  this.owner.getNavigator().tryMoveToEntityLiving(this.entityTarget, 1.0D);
            }

            this.owner.attackTime = Math.max(this.owner.attackTime - 1, 0);
            double bounds = (double)(this.owner.width * 2.0F * this.entityTarget.width * 2.0F);
            double dist = this.owner.getDistanceSq(this.entityTarget.posX, this.entityTarget.getEntityBoundingBox().minY, this.entityTarget.posZ);
            if (dist <= bounds) {
                  this.owner.startRiding(this.entityTarget);
                  this.owner.setMountAI();
                  this.owner.setAttackTarget((EntityLivingBase)null);
            }

      }
}
