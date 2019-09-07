package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemPotionHealing;
import com.teamcqr.chocolatequestrepoured.util.EntityUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIHealingPotion extends EntityAIBase {

	// First step: Walk away about 10-15 blocks from the attacker if that is
	// possible
	// Once the entity is far away enough or it hits an obstacle -> start drinking
	// your potion
	// While it is drinking the potion, it should play the "eating" animation of
	// vanilla whilst holding the potion
	// Also it should play the drinking sound
	// Once it is done drinking, it plays the "burp" sound of the player and starts
	// attacking again
	// While it is running away, it faces the last attacker and only blocks with its
	// shield (if it has one)
	// If the entity is in a party, the party should attack the last attacker of
	// this mob

	protected final AbstractEntityCQR entity;
	protected int ticksNotHealing;
	protected boolean isHealing;
	protected ItemStack stack = ItemStack.EMPTY;

	public EntityAIHealingPotion(AbstractEntityCQR entity) {
		this.entity = entity;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.getHealingPotions() > 0 && this.entity.getHealth() <= Math.max(this.entity.getMaxHealth() * 0.1D, 5.0D);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute() && (!this.isHealing || this.entity.getActiveItemStack().getItem() instanceof ItemPotionHealing);
	}

	@Override
	public void startExecuting() {
		this.ticksNotHealing = 0;
		this.isHealing = false;
		this.stack = ItemStack.EMPTY;
	}

	@Override
	public void updateTask() {
		Entity attackTarget = this.entity.getAttackTarget();

		if (attackTarget == null) {
			this.startHealing();
		} else {
			double x = attackTarget.posX - this.entity.posX;
			double z = attackTarget.posZ - this.entity.posZ;

			if (!this.isHealing) {
				ItemStack offhand = this.entity.getHeldItem(EnumHand.OFF_HAND);

				if (offhand.getItem().isShield(offhand, this.entity)) {
					this.entity.setActiveHand(EnumHand.OFF_HAND);
				}

				if (this.entity.collidedHorizontally || ++this.ticksNotHealing > 100 || Math.sqrt(x * x + z * z) > 10.0D) {
					this.startHealing();
				}
			}

			this.entity.rotationYaw = (float) (Math.atan2(-x, z) * (180.0D / Math.PI));
			this.entity.rotationYawHead = this.entity.rotationYaw;

			// TODO Only move backwards if there are blocks
			double speed = this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
			EntityUtil.move2D(this.entity, 0.0D, -0.2D, speed, this.entity.rotationYaw);
		}
	}

	@Override
	public void resetTask() {
		if (this.isHealing) {
			this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
			this.entity.resetActiveHand();
		}
		this.ticksNotHealing = 0;
		this.isHealing = false;
		this.stack = ItemStack.EMPTY;
	}

	public void startHealing() {
		if (!this.isHealing) {
			this.isHealing = true;
			this.stack = this.entity.getHeldItem(EnumHand.MAIN_HAND);
			this.entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ModItems.POTION_HEALING));
			this.entity.resetActiveHand();
			this.entity.setActiveHand(EnumHand.MAIN_HAND);
		}
	}

}
