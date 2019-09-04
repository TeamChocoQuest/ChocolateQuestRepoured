package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIInteractBase;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AIHumanAttackEvasive extends AIInteractBase {
      World worldObj;

      public AIHumanAttackEvasive(EntityCQRHumanBase par1EntityLiving, float par2) {
            super(par1EntityLiving);
            this.owner = par1EntityLiving;
            this.worldObj = par1EntityLiving.world;
      }

      public boolean shouldExecute() {
            if (super.shouldExecute()) {
                  EntityLivingBase entityliving = this.owner.getAttackTarget();
                  if (this.owner.party != null && this.owner.party.getLeader() != this.owner) {
                        double distToleader = this.owner.getDistanceSqToEntity(this.owner.party.getLeader());
                        if (distToleader > (double)(this.owner.partyDistanceToLeader * this.owner.partyDistanceToLeader * Math.max(1, 16 - this.owner.partyDistanceToLeader))) {
                              return false;
                        }
                  }

                  this.entityTarget = entityliving;
                  return true;
            } else {
                  return false;
            }
      }

      public boolean shouldContinueExecuting() {
            return this.shouldExecute() && super.shouldContinueExecuting();
      }

      public void resetTask() {
            super.resetTask();
            this.owner.getNavigator().clearPath();
            this.owner.moveForward = 0.0F;
      }

      public void updateTask() {
            double distance = this.owner.getDistanceSq(this.entityTarget.posX, this.entityTarget.getEntityBoundingBox().minY, this.entityTarget.posZ);
            boolean canSee = this.owner.getEntitySenses().canSee(this.entityTarget);
            this.owner.getLookHelper().setLookPositionWithEntity(this.entityTarget, 0.0F, 0.0F);
            this.owner.rotationYaw = this.owner.rotationYawHead;
            this.owner.moveForward = -1.0E-4F;
            boolean stayInFormation = false;
            if (canSee && distance < 64.0D) {
                  double ry = Math.toRadians((double)(this.owner.rotationYaw - 180.0F));
                  int x = MathHelper.floor(this.owner.posX - Math.sin(ry) * 6.0D);
                  int z = MathHelper.floor(this.owner.posZ + Math.cos(ry) * 6.0D);
                  Material mat = this.owner.world.getBlockState(new BlockPos(x, MathHelper.floor(this.owner.posY) - 1, z)).getMaterial();
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
                        this.owner.moveForward = -0.1F;
                  }
            } else if (this.owner.party != null) {
                  this.stayInFormation();
                  stayInFormation = true;
            }

            if (!this.attackTarget(distance)) {
                  if (!stayInFormation) {
                        this.tryMoveToXYZ(this.entityTarget.posX, this.entityTarget.posY, this.entityTarget.posZ, 1.0F);
                  } else if (!stayInFormation) {
                        this.getNavigator().clearPath();
                  }
            }

      }
}
