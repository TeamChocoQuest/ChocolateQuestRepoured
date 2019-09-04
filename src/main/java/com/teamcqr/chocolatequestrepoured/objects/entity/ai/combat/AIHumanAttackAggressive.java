package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.AIInteractBase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.world.World;

public class AIHumanAttackAggressive extends AIInteractBase {
      protected World worldObj;
      protected int attackTick;
      protected float moveSpeed;
      protected boolean requireSight;
      protected boolean isPlayerTarget;
      Path entityPathEntity;
      @SuppressWarnings("rawtypes")
	Class classTarget;
      private int pathFindingCooldown;

      public AIHumanAttackAggressive(EntityCQRHumanBase par1EntityLiving, @SuppressWarnings("rawtypes") Class par2Class, float speed, boolean requiresSight) {
            this(par1EntityLiving, speed, requiresSight);
            this.classTarget = par2Class;
      }

      public AIHumanAttackAggressive(EntityCQRHumanBase par1EntityLiving, float speed, boolean requireSight) {
            super(par1EntityLiving);
            this.isPlayerTarget = false;
            this.owner = par1EntityLiving;
            this.worldObj = par1EntityLiving.world;
            this.moveSpeed = speed;
            this.requireSight = requireSight;
      }

      public boolean shouldExecute() {
            if (super.shouldExecute()) {
                  EntityLivingBase var1 = this.owner.getAttackTarget();
                  this.entityTarget = var1;
                  return true;
            } else {
                  return false;
            }
      }

      public boolean continueExecuting() {
            EntityLivingBase var1 = this.owner.getAttackTarget();
            return var1 == null ? false : (var1 != this.entityTarget ? false : this.entityTarget.isEntityAlive());
      }

      public void startExecuting() {
            if (this.entityTarget instanceof EntityPlayer) {
                  this.isPlayerTarget = true;
            }

            this.pathFindingCooldown = 0;
      }

      public void resetTask() {
            super.resetTask();
            this.owner.setDefending(false);
            this.entityPathEntity = null;
      }

      public void updateTask() {
            double dist = this.owner.getDistanceSq(this.entityTarget.posX, this.entityTarget.getEntityBoundingBox().minY, this.entityTarget.posZ);
            this.owner.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
            boolean canSeeTarget = this.owner.getEntitySenses().canSee(this.entityTarget);
            boolean shouldStayAway = false;
            @SuppressWarnings("unused")
			boolean havePath = this.owner.onGround;
            if (dist <= this.owner.getAttackDistance() + this.getMinDistanceToInteract() && canSeeTarget) {
                  this.getNavigator().clearPath();
            } else if (--this.pathFindingCooldown <= 0) {
                  int inteligenceLevel = this.owner.getInteligence();
                  this.pathFindingCooldown = inteligenceLevel + this.owner.getRNG().nextInt(inteligenceLevel + 2);
                  if (dist > 16.0D) {
                        this.owner.setSprinting(true);
                  }

                  if (!this.tryToMoveToEntity()) {
                        shouldStayAway = true;
                  }

                  if (shouldStayAway) {
                        ;
                  }
            }

            this.attackTarget(dist);
      }

      public boolean tryToMoveToEntity() {
            return this.tryMoveToXYZ(this.entityTarget.posX, this.entityTarget.posY, this.entityTarget.posZ, 1.0F);
      }
}
