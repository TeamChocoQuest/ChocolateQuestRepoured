package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIControlledBase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AIControlledFollowOwner extends AIControlledBase {
      protected EntityLivingBase target;
      World theWorld;
      private PathNavigate ownerPathfinder;
      float maxDist;
      float minDist;
      private int pathFindingCooldown;

      public AIControlledFollowOwner(EntityCQRHumanBase par1EntityTameable, float minDist, float maxDist) {
            super(par1EntityTameable);
            this.theWorld = par1EntityTameable.world;
            this.ownerPathfinder = par1EntityTameable.getNavigator();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.setMutexBits(4);
      }

      public boolean shouldExecute() {
            EntityLivingBase entityliving = this.getOwner();
            if (entityliving == null) {
                  return false;
            } else {
                  double dist = this.owner.getDistanceSq(entityliving);
                  double minDist = (double)(this.minDist * this.minDist);
                  if (this.owner.getAttackTarget() == null) {
                        if (dist < minDist) {
                              return false;
                        }
                  } else if (dist < minDist * 3.0D) {
                        return false;
                  }

                  this.target = entityliving;
                  return true;
            }
      }

      public EntityLivingBase getOwner() {
            return (EntityLivingBase)this.owner.getOwner();
      }

      public boolean continueExecuting() {
            return !this.getNavigator().noPath() && this.owner.getDistanceSq(this.target) > (double)(this.minDist * this.minDist);
      }

      public void startExecuting() {
            this.pathFindingCooldown = 0;
      }

      public void resetTask() {
            this.target = null;
            this.getNavigator().clearPath();
      }

      public void updateTask() {
            this.owner.getLookHelper().setLookPositionWithEntity(this.target, 10.0F, (float)this.owner.getVerticalFaceSpeed());
            if (--this.pathFindingCooldown <= 0) {
                  this.pathFindingCooldown = 10;
                  double dist = this.owner.getDistanceSq(this.target);
                  if (dist > 100.0D) {
                        this.owner.setSprinting(true);
                  }

                  if (!this.tryMoveToXYZ(this.target.posX, this.target.posY, this.target.posZ, 1.15F) && dist >= (double)(this.maxDist * this.maxDist)) {
                        int i = MathHelper.floor(this.target.posX) - 2;
                        int j = MathHelper.floor(this.target.posZ) - 2;
                        int k = MathHelper.floor(this.target.getEntityBoundingBox().minY);

                        for(int l = 0; l <= 4; ++l) {
                              for(int i1 = 0; i1 <= 4; ++i1) {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && theWorld.isSideSolid(new BlockPos(i + l, k - 1, j + i1), EnumFacing.UP) && !this.theWorld.isBlockNormalCube(new BlockPos(i + l, k, j + i1), true) && !this.theWorld.isBlockNormalCube(new BlockPos(i + l, k + 1, j + i1), true)) {
                                          if (this.owner.getRidingEntity() != null) {
                                                this.owner.getRidingEntity().setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.owner.rotationYaw, this.owner.rotationPitch);
                                          }

                                          this.owner.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.owner.rotationYaw, this.owner.rotationPitch);
                                          this.ownerPathfinder.clearPath();
                                          return;
                                    }
                              }
                        }
                  }
            }

      }
}
