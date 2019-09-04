package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class AIFirefighter extends EntityAIBase {
      World worldObj;
      EntityCQRHumanBase owner;
      float moveSpeed;
      Path entityPathEntity;
      Vec3i nearestFire;
      protected int field_75445_i;

      public AIFirefighter(EntityCQRHumanBase par1EntityLiving, float par2, boolean par3) {
            this.owner = par1EntityLiving;
            this.worldObj = par1EntityLiving.world;
            this.moveSpeed = par2;
            this.setMutexBits(3);
      }

      public boolean shouldExecute() {
            int x = MathHelper.floor(this.owner.posX);
            int y = MathHelper.floor(this.owner.posY);
            int z = MathHelper.floor(this.owner.posZ);
            if (this.nearestFire == null) {
                  for(int i = -8; i < 14; ++i) {
                        for(int k = -8; k < 14; ++k) {
                              for(int j = -2; j < 4; ++j) {
                                    if (this.worldObj.getBlockState(new BlockPos(x + i, y + j, z + k)).getMaterial() == Material.FIRE) {
                                          if (this.nearestFire != null) {
                                                if (this.owner.getDistanceSq((double)(x + i), (double)(y + j), (double)(z + k)) < this.owner.getDistanceSq((double)this.nearestFire.getX(), (double)this.nearestFire.getY(), (double)this.nearestFire.getZ())) {
                                                      this.nearestFire = new Vec3i(x + i, y + j, k + z);
                                                }
                                          } else {
                                                this.nearestFire = new Vec3i(x + i, y + j, k + z);
                                          }
                                    }
                              }
                        }
                  }
            }

            return this.nearestFire != null;
      }

      public void startExecuting() {
      }

      public void resetTask() {
      }

      public void updateTask() {
            if (this.nearestFire != null) {
                  if (this.owner.getDistanceSq((double)this.nearestFire.getX(), (double)this.nearestFire.getY(), (double)this.nearestFire.getZ()) < 16.0D) {
                        this.worldObj.setBlockToAir((BlockPos) this.nearestFire);
                        this.owner.swingItem();
                        this.nearestFire = null;
                  } else if (this.owner.getNavigator().getPathToXYZ((double)this.nearestFire.getX(), (double)this.nearestFire.getY(), (double)this.nearestFire.getZ()) != null) {
                        this.owner.getNavigator().tryMoveToXYZ((double)this.nearestFire.getX(), (double)this.nearestFire.getY(), (double)this.nearestFire.getZ(), (double)this.moveSpeed);
                  } else {
                        this.nearestFire = null;
                  }
            }

      }
}
