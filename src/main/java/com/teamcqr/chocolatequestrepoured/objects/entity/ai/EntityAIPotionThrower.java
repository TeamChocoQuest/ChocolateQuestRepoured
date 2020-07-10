package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.HashSet;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemAlchemyBag;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EntityAIPotionThrower extends EntityAIAttack {

	public EntityAIPotionThrower(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		return (this.entity.getHeldItemOffhand().getItem() instanceof ItemAlchemyBag || 
				this.entity.getHeldItemOffhand().getItem() instanceof ItemSplashPotion ||
				this.entity.getHeldItemOffhand().getItem() instanceof ItemLingeringPotion) && super.shouldExecute();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return (this.entity.getHeldItemOffhand().getItem() instanceof ItemAlchemyBag || 
				this.entity.getHeldItemOffhand().getItem() instanceof ItemSplashPotion ||
				this.entity.getHeldItemOffhand().getItem() instanceof ItemLingeringPotion) && super.shouldContinueExecuting();
	}

	@Override
	public void startExecuting() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (this.entity.getDistance(attackTarget) < 14.0D) {
			this.entity.setActiveHand(EnumHand.OFF_HAND);
			this.entity.isSwingInProgress = true;
		} else {
			this.updatePath(attackTarget);
		}
	}

	@Override
	public void resetTask() {
		super.resetTask();
		this.entity.isSwingInProgress = false;
	}

	@Override
	public void updateTask() {
		EntityLivingBase attackTarget = this.entity.getAttackTarget();

		if (attackTarget != null) {
			this.entity.getLookHelper().setLookPositionWithEntity(attackTarget, 12.0F, 12.0F);

			double distance = this.entity.getDistance(attackTarget);
			if (distance < 14.0D || (!this.entity.hasPath() && distance < 16.0D)) {
				this.checkAndPerformAttack(this.entity.getAttackTarget());
				this.entity.getNavigator().clearPath();
				this.entity.setActiveHand(EnumHand.OFF_HAND);
				this.entity.isSwingInProgress = true;
			} else {
				this.updatePath(attackTarget);
				this.entity.resetActiveHand();
				this.entity.isSwingInProgress = false;
			}
		}
	}

	@Override
	protected void checkAndPerformAttack(EntityLivingBase attackTarget) {
		if (this.attackTick <= 0 && this.entity.getDistance(attackTarget) <= 16.0D) {
			ItemStack stack = this.entity.getHeldItemOffhand();
			if(stack.getItem() instanceof ItemSplashPotion || stack.getItem() instanceof ItemLingeringPotion) {
				this.attackTick = 60;
				EntityPotion proj = new EntityPotion(world, entity, stack);
				double x = attackTarget.posX - this.entity.posX;
				double y = attackTarget.posY + (double) attackTarget.height * 0.5D - proj.posY;
				double z = attackTarget.posZ - this.entity.posZ;
				double distance = Math.sqrt(x * x + z * z);
				proj.shoot(x,y + distance * 0.06D, z, 1.F,entity.getRNG().nextFloat() * 0.25F);
				proj.motionX += this.entity.motionX;
				proj.motionZ += this.entity.motionZ;
				if (!this.entity.onGround) {
					proj.motionY += this.entity.motionY;
				}
				this.entity.world.spawnEntity(proj);
				this.entity.playSound(SoundEvents.ENTITY_SPLASH_POTION_THROW, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
				
				if(CQRConfig.mobs.offhandPotionsAreSingleUse) {
					stack.shrink(1);
				}
				
			}
			else if (stack.getItem() instanceof ItemAlchemyBag) {
				this.attackTick = 60;
				IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				int indx = entity.getRNG().nextInt(inventory.getSlots());
				ItemStack st = inventory.getStackInSlot(indx);
				Set<Integer> usedIDs = new HashSet<Integer>(); 
				while((st == null || st.isEmpty()) && !usedIDs.contains(indx)) {
					indx = entity.getRNG().nextInt(inventory.getSlots());
					usedIDs.add(indx);
					st = inventory.getStackInSlot(indx);
				}
				boolean removeBag = false;
				if(st != null && !st.isEmpty()) {
					ItemStack potion = st.copy();
					if(CQRConfig.mobs.potionsInBagAreSingleUse) {
						st.shrink(1);
					}
					
					//Now throw it
					if(potion.getItem() instanceof ItemSplashPotion || potion.getItem() instanceof ItemLingeringPotion) {
						EntityPotion proj = new EntityPotion(world, entity, potion);
						double x = attackTarget.posX - this.entity.posX;
						double y = attackTarget.posY + (double) attackTarget.height * 0.5D - proj.posY;
						double z = attackTarget.posZ - this.entity.posZ;
						double distance = Math.sqrt(x * x + z * z);
						proj.shoot(x,y + distance * 0.06D, z, 1.F,entity.getRNG().nextFloat() * 0.25F);
						proj.motionX += this.entity.motionX;
						proj.motionZ += this.entity.motionZ;
						if (!this.entity.onGround) {
							proj.motionY += this.entity.motionY;
						}
						this.entity.world.spawnEntity(proj);
						this.entity.playSound(SoundEvents.ENTITY_SPLASH_POTION_THROW, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
					}
					int itemsInBag = 0;
					for(int s = 0; s < inventory.getSlots(); s++) {
						if(inventory.getStackInSlot(s) != null && !inventory.getStackInSlot(s).isEmpty()) {
							itemsInBag++;
						}
					}
					removeBag = itemsInBag <= 0;
				} else {
					removeBag = true;
				}
				
				if(removeBag) {
					//Remove the bag
					this.entity.entityDropItem(stack, 1);
					this.entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
				}
			} 
		}
	}

}
