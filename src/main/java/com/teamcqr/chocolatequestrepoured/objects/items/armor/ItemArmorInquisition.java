package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import com.teamcqr.chocolatequestrepoured.client.init.ModArmorModels;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorInquisition extends ArmorCQRBase {

	public ItemArmorInquisition(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getBipedArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot) {
		return armorSlot == EntityEquipmentSlot.LEGS ? ModArmorModels.inquisitionArmorLegs : ModArmorModels.inquisitionArmor;
	}

}
