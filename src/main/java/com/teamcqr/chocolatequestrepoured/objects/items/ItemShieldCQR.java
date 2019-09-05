package com.teamcqr.chocolatequestrepoured.objects.items;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemShieldCQR extends ItemShield {

	private Item repairItem;

	public ItemShieldCQR(int durability, @Nullable Item repairItem) {
		setMaxDamage(durability);
		this.repairItem = repairItem;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == repairItem;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
	}

	@Override
	public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
		return stack.getItem() instanceof ItemShield;
	}

}