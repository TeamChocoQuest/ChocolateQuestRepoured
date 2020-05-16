package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.capability.armor.kingarmor.CapabilityDynamicCrownProvider;
import com.teamcqr.chocolatequestrepoured.client.init.ModArmorModels;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemCrown extends ItemArmor {

	public ItemCrown(ArmorMaterial materialIn, int renderIndexIn) {
		super(materialIn, renderIndexIn, EntityEquipmentSlot.HEAD);
	}
	
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		return ModArmorModels.crown;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return CapabilityDynamicCrownProvider.createProvider();
	}
	
	@Nullable
	public Item getAttachedItem(ItemStack stack) {
		if(stack.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			return stack.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).getAttachedItem();
		}
		return null;
	}
	
	public void attachItem(ItemStack crown, Item toAttach) {
		if(crown.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).attachItem(toAttach);
		}
	}
	
	public void attachItem(ItemStack crown, ItemStack toAttach) {
		if(crown.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).attachItem(toAttach);
		}
	}
	
	public void attachItem(ItemStack crown, ResourceLocation toAttach) {
		if(crown.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).attachItem(toAttach);
		}
	}

}
