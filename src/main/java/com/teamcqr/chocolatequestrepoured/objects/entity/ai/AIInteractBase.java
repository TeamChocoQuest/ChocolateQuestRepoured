package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AIInteractBase extends AIControlledBase {
      protected EntityLivingBase entityTarget;

      public AIInteractBase(EntityCQRHumanBase par1EntityLiving) {
            super(par1EntityLiving);
            this.setMutexBits(11);
      }

      public boolean shouldExecute() {
            EntityLivingBase entityliving = this.owner.getAttackTarget();
            if (entityliving == null) {
                  return false;
            } else if (!entityliving.isEntityAlive()) {
                  this.owner.setAttackTarget((EntityLivingBase)null);
                  return false;
            } else if (this.owner.isSuitableTargetAlly(entityliving)) {
                  return false;
            } else {
                  this.entityTarget = entityliving;
                  return true;
            }
      }

      public void resetTask() {
            this.entityTarget = null;
      }

      public void updateTask() {
            double x = 0.0D;
            double y = 0.0D;
            double z = 0.0D;
            EntityLivingBase leader = this.owner.getLeader();
            Vec3d absPosition = leader.getLookVec();
            float angle = (float)this.owner.partyPositionAngle * 3.1416F / 180.0F;
            double cos = (double)MathHelper.cos(angle);
            double sin = (double)MathHelper.sin(angle);
            int dist = this.owner.partyDistanceToLeader;
            x = leader.posX + (absPosition.x * cos - absPosition.z * sin) * (double)dist;
            y = leader.posY;
            z = leader.posZ + (absPosition.z * cos + absPosition.x * sin) * (double)dist;
            this.tryMoveToXYZ(x, y, z, 1.2F);
      }

      public boolean attackTarget(double distance) {
            this.owner.attackTime = Math.max(this.owner.attackTime - 1, 0);
            double sumLengthBB = this.getMinDistanceToInteract() + this.owner.getAttackRangeBonus();
            if (this.owner.haveShied()) {
                  double distToStopDefending;
                  if (this.entityTarget instanceof EntityPlayer) {
                        distToStopDefending = sumLengthBB * (double)(2.0F + (float)(3 - this.owner.worldObj.difficultySetting.ordinal()));
                  } else {
                        distToStopDefending = 0.0D;
                  }

                  if (this.owner.isDefending() && distance <= distToStopDefending && this.owner.attackTime <= 10 && this.owner.hurtTime == 0) {
                        this.owner.setDefending(false);
                  }
            }

            if (distance <= sumLengthBB) {
                  this.owner.attackEntityAsMob(this.entityTarget);
            } else if (this.owner.isRanged()) {
                  EntityLivingBase target = this.getFrontTarget();
                  if (target != null && this.owner.isSuitableTargetAlly(target) && target != this.owner.ridingEntity) {
                        float moveStrafing = -MathHelper.sin((float)this.owner.partyPositionAngle) / 4.0F;
                        double ry = -Math.toRadians(this.owner.rotationYaw + this.owner.moveStrafing > 0.0F ? 90.0D : -90.0D);
                        int x = MathHelper.floor(this.owner.posX - Math.sin(ry) * 6.0D);
                        int z = MathHelper.floor(this.owner.posZ + Math.cos(ry) * 6.0D);
                        Material mat = this.owner.world.getBlock(x, MathHelper.floor(this.owner.posY) - 1, z).getMaterial();
                        boolean move = false;
                        if (mat != Material.AIR && mat != Material.LAVA && mat.isSolid()) {
                              move = true;
                        } else {
                              mat = this.owner.world.getBlockState(new BlockPos(x, MathHelper.floor(this.owner.posY) - 2, z)).getMaterial();
                              if (mat.isSolid()) {
                                    move = true;
                              }
                        }

                        if (move) {
                              this.owner.moveStrafing = moveStrafing;
                        } else {
                              this.owner.moveStrafing = 0.0F;
                        }
                  } else {
                        this.owner.moveStrafing = 0.0F;
                        this.owner.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
                        if (!this.owner.attackEntityWithRangedAttack(this.entityTarget, (float)distance)) {
                              return false;
                        }
                  }
            }

            return true;
      }

      public double getMinDistanceToInteract() {
            double attackerBB = (double)(this.owner.width * 2.2F);
            if (this.owner.getRidingEntity() != null) {
                  attackerBB += (double)(this.owner.getRidingEntity().width * 2.0F);
            }

            double targetBB = (double)(this.entityTarget.width * 2.2F);
            if (this.entityTarget.getRidingEntity() != null) {
                  targetBB += (double)this.entityTarget.getRidingEntity().width;
            }

            return attackerBB * targetBB + this.owner.rightHand.getAttackRangeBonus();
      }

      public EntityLivingBase getFrontTarget() {
            EntityLivingBase target = null;
            double arrowMotionX = this.entityTarget.posX - this.owner.posX;
            double arrowMotionZ = this.entityTarget.posZ - this.owner.posZ;
            float tRot = this.owner.rotationYaw;
            this.owner.rotationYaw = (float)(Math.atan2(arrowMotionZ, arrowMotionX) * 180.0D / 3.141592653589793D) - 90.0F;
            MovingObjectPosition mop = null;
            double dist = 30.0D;
            float yOffset = 0.0F;
            Vec3d playerPos = new Vec3d(this.owner.posX, this.owner.posY - (double)yOffset, this.owner.posZ);
            Vec3d look = this.owner.getLookVec();
            Vec3d playerView = playerPos.addVector(look.x * dist, look.y * dist, look.z * dist);
            List<Entity> list = this.owner.world.getEntitiesWithinAABBExcludingEntity(this.owner, this.owner.getEntityBoundingBox().offset(look.x * dist, look.y * dist, look.z * dist).expand(1.0D, 1.0D, 1.0D));
            dist *= dist;

            for(int j = 0; j < list.size(); ++j) {
                  Entity entity1 = (Entity)list.get(j);
                  if (entity1 instanceof EntityLivingBase && entity1.canBeCollidedWith()) {
                        float f2 = 0.4F;
                        AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)entity1.width, (double)entity1.height, (double)entity1.width);
                        MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(playerPos, playerView);
                        if (movingobjectposition1 != null) {
                              double tDist = this.owner.getDistanceSq(entity1);
                              if (tDist < dist && target != this.owner.getRidingEntity() && target != this.owner.riddenByEntity) {
                                    dist = tDist;
                                    target = (EntityLivingBase)entity1;
                              }
                        }
                  }
            }

            return target;
      }
}
