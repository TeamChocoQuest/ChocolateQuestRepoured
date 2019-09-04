package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.chocolate.chocolateQuest.utils.Vec4I;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AIControlledBase extends EntityAIBase {
      protected EntityCQRHumanBase owner;
      private Vec4I position;
      private int pathFindingCooldown;
      public int pathBlockedTime = 0;

      public AIControlledBase(EntityCQRHumanBase owner) {
            this.owner = owner;
            this.setMutexBits(3);
      }

      public boolean shouldExecute() {
            return true;
      }

      public boolean stayInFormation() {
            if (this.owner.getLeader() != null) {
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
                  this.tryMoveToXYZ(x, y, z, 1.15F);
                  return true;
            } else {
                  return false;
            }
      }

      public boolean tryMoveToXYZ(double x, double y, double z, float moveSpeed) {
            if (this.owner.getRidingEntity() != null) {
                  ++moveSpeed;
            }

            --this.pathFindingCooldown;
            if (this.pathFindingCooldown > 0 && !this.getNavigator().noPath()) {
                  return true;
            } else {
                  this.pathFindingCooldown = 5 + this.owner.getRNG().nextInt(10);
                  if (this.getNavigator().tryMoveToXYZ(x, y, z, (double)moveSpeed) && !this.getNavigator().noPath()) {
                        this.position = null;
                        return true;
                  } else if (this.position == null) {
                        Vec3d vec3 = new Vec3d(x - this.owner.posX, y - this.owner.posY, z - this.owner.posZ);
                        vec3 = vec3.normalize();
                        vec3 = new Vec3d(vec3.x * 10.0D + this.owner.posX, vec3.y * 10.0D + this.owner.posY, vec3.z * 10.0D + this.owner.posZ);
                        if (this.owner.world.isAirBlock(MathHelper.floor(vec3.x), MathHelper.floor(vec3.y - 1.0D), MathHelper.floor(vec3.z))) {
                              Vec3d direction = new Vec3d(x, y, z);
                              vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.owner, 10, 3, direction);
                              if (vec3 == null) {
                                    return false;
                              }
                        }

                        this.position = new Vec4I(MathHelper.floor(vec3.x), MathHelper.floor(vec3.y), MathHelper.floor(vec3.z), 0);
                        return true;
                  } else if (this.getNavigator().tryMoveToXYZ((double)this.position.xCoord, (double)this.position.yCoord, (double)this.position.zCoord, (double)moveSpeed)) {
                        PathPoint path = this.getNavigator().getPath().getFinalPathPoint();
                        if (this.owner.getDistanceSq((double)path.x, (double)path.y, (double)path.z) <= 1.0D) {
                              this.pathFindingCooldown = 10;
                              this.position = null;
                        }

                        return true;
                  } else {
                        if (this.owner.canTeleport() && (this.owner.onGround || this.owner.isInWater())) {
                              this.owner.setPosition((double)this.position.xCoord, (double)(this.position.yCoord + 1), (double)this.position.zCoord);
                        }

                        this.position = null;
                        return false;
                  }
            }
      }

      public PathNavigate getNavigator() {
            return this.owner.getRidingEntity() != null && this.owner.getRidingEntity() instanceof EntityLiving && ((EntityLiving)this.owner.getRidingEntity()).getNavigator() != null ? ((EntityLiving)this.owner.getRidingEntity()).getNavigator() : this.owner.getNavigator();
      }
}
