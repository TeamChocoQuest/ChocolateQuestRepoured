package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class AIFollowOwner extends EntityAIBase {
      protected EntityLiving theEntity;
      protected EntityLivingBase target;
      World theWorld;
      private float moveSpeed;
      private PathNavigate ownerPathfinder;
      private int remainingSteps;
      float maxDist;
      float minDist;
      private boolean avoidsWater;
      private boolean teleports = false;

      public AIFollowOwner(EntityLiving par1EntityTameable, float speed, float minDist, float maxDist, boolean teleport) {
            this.theEntity = par1EntityTameable;
            this.theWorld = par1EntityTameable.world;
            this.moveSpeed = speed;
            this.ownerPathfinder = par1EntityTameable.getNavigator();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.setMutexBits(4);
            this.teleports = teleport;
      }

      public boolean shouldExecute() {
            EntityLivingBase entityliving = this.getOwner();
            if (entityliving == null) {
                  return false;
            } else {
                  double dist = this.theEntity.getDistanceSq(entityliving);
                  double minDist = (double)(this.minDist * this.minDist);
                  if (this.theEntity.getAttackTarget() == null) {
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
            return (EntityLivingBase)((IEntityOwnable)this.theEntity).getOwner();
      }

      public boolean continueExecuting() {
            return !this.ownerPathfinder.noPath() && this.theEntity.getDistanceSq(this.target) > (double)(this.maxDist * this.maxDist);
      }

      public void startExecuting() {
            this.remainingSteps = 0;
            this.avoidsWater = this.theEntity.getNavigator().getAvoidsWater();
            this.theEntity.getNavigator().setAvoidsWater(false);
      }

      public void resetTask() {
            this.target = null;
            this.ownerPathfinder.clearPath();
            this.theEntity.getNavigator().setAvoidsWater(this.avoidsWater);
      }

      public void updateTask() {
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.target, 10.0F, (float)this.theEntity.getVerticalFaceSpeed());
            if (--this.remainingSteps <= 0) {
                  this.remainingSteps = 10;
                  if (this.theEntity.getRidingEntity() instanceof EntityLiving) {
                        ((EntityLiving)this.theEntity.getRidingEntity()).getNavigator().tryMoveToEntityLiving(this.target, (double)this.moveSpeed);
                  } else if (!this.ownerPathfinder.tryMoveToEntityLiving(this.target, 2.0D) && this.theEntity.getDistanceSq(this.target) >= 288.0D && this.teleports) {
                        int i = MathHelper.floor(this.target.posX) - 2;
                        int j = MathHelper.floor(this.target.posZ) - 2;
                        int k = MathHelper.floor(this.target.getEntityBoundingBox().minY);

                        for(int l = 0; l <= 4; ++l) {
                              for(int i1 = 0; i1 <= 4; ++i1) {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && theWorld.isSideSolid(new BlockPos(i + l, k - 1, j + i1), EnumFacing.UP) && this.theWorld.isAirBlock(new BlockPos(i + l, k, j + i1)) && this.theWorld.isAirBlock(new BlockPos(i + l, k + 1, j + i1))) {
                                          if (this.theEntity.getRidingEntity() != null) {
                                                this.theEntity.getRidingEntity().setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theEntity.rotationYaw, this.theEntity.rotationPitch);
                                          }

                                          this.theEntity.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theEntity.rotationYaw, this.theEntity.rotationPitch);
                                          this.ownerPathfinder.clearPath();
                                          return;
                                    }
                              }
                        }
                  }
            }

      }
}
