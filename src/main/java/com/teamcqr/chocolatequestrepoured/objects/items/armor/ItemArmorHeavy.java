package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import com.google.common.collect.Multimap;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemArmorHeavy extends ItemArmor {

	private AttributeModifier movementSpeed;
	private AttributeModifier knockbackResistance;

	public ItemArmorHeavy(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		this.movementSpeed = new AttributeModifier("HeavySpeedModifier", -0.06D, 2);
		this.knockbackResistance = new AttributeModifier("HeavyKnockbackModifier", 0.25D, 0);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityLiving.getSlotForItemStack(stack)) {
			multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), this.knockbackResistance);
			multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.movementSpeed);
		}

		return multimap;
	}

	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			if (!player.onGround) {
				player.jumpMovementFactor = ((float) Math.max(0.015D, player.jumpMovementFactor - 0.01F));
			}
		}
	}

}