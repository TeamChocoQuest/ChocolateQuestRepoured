package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.client.init.ModArmorModels;
import com.teamcqr.chocolatequestrepoured.init.ModMaterials;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemArmorHeavy extends ArmorCQRBase {

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

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			if (!player.onGround) {
				player.jumpMovementFactor = ((float) Math.max(0.015D, player.jumpMovementFactor - 0.01F));
			}
		}
	}

	@Override
	public ModelBiped getBipedArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot) {
		ModelBiped armor = armorSlot == EntityEquipmentSlot.LEGS ? ModArmorModels.heavyIronArmorLegs : ModArmorModels.heavyIronArmor;
		if (itemStack.getItem() instanceof ItemArmor) {
			//This is supposed! Reason: If future version have a separate model for the heavy diamond armor, adaption isnt really needed
			if (((ItemArmor) itemStack.getItem()).getArmorMaterial().equals(ModMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND)) {
				armor = armorSlot == EntityEquipmentSlot.LEGS ? ModArmorModels.heavyDiamondArmorLegs : ModArmorModels.heavyDiamondArmor;
			} else {
				armor = armorSlot == EntityEquipmentSlot.LEGS ? ModArmorModels.heavyIronArmorLegs : ModArmorModels.heavyIronArmor;
			}
		}
		return armor;
	}

}