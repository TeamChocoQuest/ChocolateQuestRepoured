package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AIHumanPotion extends EntityAIBase {
      protected World worldObj;
      protected EntityCQRHumanBase owner;
      protected int attackCooldown;

      public AIHumanPotion(EntityCQRHumanBase par1EntityLiving) {
            this.owner = par1EntityLiving;
            this.worldObj = par1EntityLiving.world;
            this.setMutexBits(3);
      }

      public boolean shouldExecute() {
            return this.owner.getRemainingHealingPotions() > 0 && (double)this.owner.getHealth() <= Math.max((double)this.owner.getMaxHealth() * 0.1D, 6.0D);
      }

      public void startExecuting() {
            this.owner.getNavigator().clearPath();
            this.attackCooldown = 0;
            this.owner.setDefending(true);
      }

      public void resetTask() {
            this.owner.getNavigator().clearPath();
            this.owner.setDefending(false);
            this.owner.moveForward = 0.0F;
            this.owner.setEating(false);
      }

      public void updateTask() {
            boolean flag = true;
            int timeTillPotion = 90;
            if (this.owner.getAttackTarget() != null && this.attackCooldown < timeTillPotion) {
                  this.owner.getLookHelper().setLookPositionWithEntity(this.owner.getAttackTarget(), 30.0F, 30.0F);
                  this.owner.rotationYaw = this.owner.rotationYawHead;
                  double ry = Math.toRadians((double)(this.owner.rotationYaw - 180.0F));
                  int x = MathHelper.floor(this.owner.posX - Math.sin(ry) * 3.0D);
                  int z = MathHelper.floor(this.owner.posZ + Math.cos(ry) * 3.0D);
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
                        this.owner.moveForward = -0.25F;
                  } else {
                        this.owner.moveForward = 0.0F;
                  }

                  if (this.owner.getDistanceSq(this.owner.getAttackTarget()) > 100.0D || !this.owner.getEntitySenses().canSee(this.owner.getAttackTarget())) {
                        this.attackCooldown = timeTillPotion;
                        this.owner.moveForward = 0.0F;
                  }

                  if (this.owner.collidedHorizontally) {
                        this.attackCooldown += 5;
                  }

                  double var9 = this.owner.getDistanceSq(this.owner.getAttackTarget());
            }

            ++this.attackCooldown;
            if (this.attackCooldown > timeTillPotion) {
                  if (this.owner.onGround) {
                        this.owner.motionX = 0.0D;
                        this.owner.motionZ = 0.0D;
                  }

                  if (!this.owner.isEating()) {
                        this.owner.setEating(true);
                        if (this.owner.getIsDefending()) {
                              this.owner.toogleBlocking();
                        }
                  }

                  this.owner.swingItem();
                  if (this.attackCooldown > timeTillPotion + 50) {
                        this.owner.world.playSoundAtEntity(this.owner, "random.burp", 0.5F, this.owner.world.rand.nextFloat() * 0.1F + 0.9F);
                        this.owner.heal(20.0F);
                        this.owner.removePotion(1);
                        this.owner.setEating(false);
                  } else {
                        this.owner.world.playSoundAtEntity(this.owner, "random.drink", 0.5F, this.owner.world.rand.nextFloat() * 0.1F + 0.9F);
                  }
            }

      }
}
