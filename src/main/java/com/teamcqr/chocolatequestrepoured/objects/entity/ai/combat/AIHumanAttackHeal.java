package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.chocolate.chocolateQuest.ChocolateQuest;
import com.chocolate.chocolateQuest.packets.PacketSpawnParticlesAround;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.items.staves.ItemStaffHealing;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AIHumanAttackHeal extends AIHumanAttackAggressive {
      private int pathFindingCooldown;

      public AIHumanAttackHeal(EntityCQRHumanBase par1EntityLiving, @SuppressWarnings("rawtypes") Class par2Class, float par3, boolean par4, World world) {
            this(par1EntityLiving, par3, par4);
            this.classTarget = par2Class;
            this.worldObj = world;
      }

      public AIHumanAttackHeal(EntityCQRHumanBase par1EntityLiving, float par2, boolean par3) {
            super(par1EntityLiving, par2, par3);
      }

      public boolean shouldExecute() {
            EntityLivingBase var1 = this.owner.getAttackTarget();
            if (var1 == null) {
                  return false;
            } else if (!this.owner.isSuitableTargetAlly(var1)) {
                  return false;
            } else if (var1.getHealth() < var1.getMaxHealth() && var1 != this.owner) {
                  this.entityTarget = var1;
                  this.entityPathEntity = this.owner.getNavigator().getPathToEntityLiving(this.entityTarget);
                  return this.entityPathEntity != null || this.owner.getRidingEntity() != null;
            } else {
                  this.owner.setAttackTarget((EntityLivingBase)null);
                  return false;
            }
      }

      public boolean continueExecuting() {
            EntityLivingBase target = this.owner.getAttackTarget();
            if (target == null) {
                  return false;
            } else if (!this.owner.isSuitableTargetAlly(target)) {
                  return false;
            } else {
                  return target == null ? false : (!this.entityTarget.isEntityAlive() ? false : (!this.requireSight ? !this.owner.getNavigator().noPath() : this.owner.isWithinHomeDistance(MathHelper.floor(this.entityTarget.posX), MathHelper.floor(this.entityTarget.posY), MathHelper.floor(this.entityTarget.posZ))));
            }
      }

      public void startExecuting() {
            this.owner.getNavigator().setPath(this.entityPathEntity, (double)this.moveSpeed);
            this.pathFindingCooldown = 0;
      }

      public void resetTask() {
            this.entityTarget = null;
            this.owner.getNavigator().clearPath();
            this.owner.setDefending(false);
      }

      public void updateTask() {
            this.owner.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
            if ((this.requireSight || this.owner.getEntitySenses().canSee(this.entityTarget)) && --this.pathFindingCooldown <= 0) {
                  this.pathFindingCooldown = 4 + this.owner.getRNG().nextInt(7);
                  if (this.owner.getRidingEntity() instanceof EntityLiving) {
                        ((EntityLiving)this.owner.getRidingEntity()).getNavigator().tryMoveToEntityLiving(this.entityTarget, 1.2000000476837158D);
                  }

                  this.owner.getNavigator().tryMoveToEntityLiving(this.entityTarget, (double)this.moveSpeed);
            }

            this.owner.attackTime = Math.max(this.owner.attackTime - 1, 0);
            double bounds = this.getMinDistanceToInteract();
            double dist = this.owner.getDistanceSq(this.entityTarget.posX, this.entityTarget.boundingBox.minY, this.entityTarget.posZ);
            if (this.owner.getIsDefending() && dist <= bounds * 2.0D) {
                  this.owner.setDefending(false);
            }

            if (dist <= bounds && this.owner.attackTime <= 0) {
                  this.owner.attackTime = this.owner.getAttackSpeed();
                  ItemStack staff = null;
                  if (this.owner.getHeldItemMainhand() != null && this.owner.getHeldItemMainhand().getItem() == ModItems.STAFF_HEALING) {
                        staff = this.owner.getHeldItemMainhand();
                        this.owner.swingItem();
                  } else if (this.owner.getHeldItemOffhand() != null && this.owner.getHeldItemOffhand().getItem() == ModItems.STAFF_HEALING) {
                        staff = this.owner.getHeldItemOffhand();
                        this.owner.swingLeftHand();
                  }

                  this.entityTarget.heal(2.0F);
                  ItemStaffHealing.applyPotionEffects(staff, this.entityTarget);
                  if (!this.owner.world.isRemote) {
                        this.entityTarget.world.playSoundEffect((double)((int)this.entityTarget.posX), (double)((int)this.entityTarget.posY), (double)((int)this.entityTarget.posZ), "chocolateQuest:magic", 1.0F, (1.0F + (this.entityTarget.world.rand.nextFloat() - this.entityTarget.world.rand.nextFloat()) * 0.2F) * 0.7F);
                       // PacketSpawnParticlesAround packet = new PacketSpawnParticlesAround((byte)0, this.entityTarget.posX, this.entityTarget.posY + 1.6D, this.entityTarget.posZ);
                       // ChocolateQuest.channel.sendToAllAround(this.entityTarget, packet, 64);
                  }
            }

            if (this.entityTarget.getHealth() >= this.entityTarget.getMaxHealth()) {
                  this.owner.setAttackTarget((EntityLivingBase)null);
            }

      }
}
