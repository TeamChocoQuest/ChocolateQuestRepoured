package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIControlledBase;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AIControlledFormation extends AIControlledBase {
      protected World worldObj;
      protected int attackCooldown;
      double x = 0.0D;
      double y = 0.0D;
      double z = 0.0D;

      public AIControlledFormation(EntityCQRHumanBase par1EntityLiving) {
            super(par1EntityLiving);
            this.worldObj = par1EntityLiving.world;
            this.setMutexBits(3);
      }

      public boolean shouldExecute() {
            if (this.owner.getLeader() == null) {
                  return false;
            } else {
                  EntityLivingBase leader = this.owner.getLeader();
                  Vec3d absPosition = leader.getLookVec();
                  float angle = (float)this.owner.partyPositionAngle * 3.1416F / 180.0F;
                  double cos = (double)MathHelper.cos(angle);
                  double sin = (double)MathHelper.sin(angle);
                  int dist = this.owner.partyDistanceToLeader;
                  this.x = leader.posX + (absPosition.x * cos - absPosition.z * sin) * (double)dist;
                  this.y = leader.posY;
                  this.z = leader.posZ + (absPosition.z * cos + absPosition.x * sin) * (double)dist;
                  Entity distCheckEntity = this.owner.getRidingEntity() != null ? this.owner.getRidingEntity() : this.owner;
                  double distToPoint = ((Entity)distCheckEntity).getDistanceSq(this.x, this.y, this.z);
                  if (distToPoint > 9.0D) {
                        this.owner.setSprinting(true);
                  }

                  return distToPoint > (double)(((Entity)distCheckEntity).width * ((Entity)distCheckEntity).width);
            }
      }

      public void startExecuting() {
            this.getNavigator().clearPath();
      }

      public void resetTask() {
            if (this.owner.getLeader() != null) {
                  this.owner.rotationYaw = this.owner.getLeader().rotationYawHead;
                  this.owner.rotationYawHead = this.owner.getLeader().rotationYawHead;
            }

            this.getNavigator().clearPath();
      }

      public void updateTask() {
            this.tryMoveToXYZ(this.x, this.y, this.z, 1.15F);
      }

      public boolean stayInFormation() {
            return false;
      }
}
