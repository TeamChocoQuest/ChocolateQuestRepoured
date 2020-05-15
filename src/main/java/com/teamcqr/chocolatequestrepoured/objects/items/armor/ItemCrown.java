package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import com.teamcqr.chocolatequestrepoured.client.init.ModArmorModels;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemCrown extends ItemArmor {

	public ItemCrown(ArmorMaterial materialIn, int renderIndexIn) {
		super(materialIn, renderIndexIn, EntityEquipmentSlot.HEAD);
	}
	
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		return ModArmorModels.crown;
	}

	
	

}
