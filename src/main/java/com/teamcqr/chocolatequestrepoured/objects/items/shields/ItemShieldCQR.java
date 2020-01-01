package com.teamcqr.chocolatequestrepoured.objects.items.shields;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemShieldCQR extends ItemShield {

	public static final String[] SHIELD_NAMES = {
			"bull",
			"carl",
			"dragonslayer",
			"fire",
			"goblin",
			"monking",
			"moon",
			"mummy",
			"pigman",
			"pirate",
			"pirate2",
			"rainbow",
			"reflective",
			"rusted",
			"skeleton_friends",
			"specter",
			"spider",
			"sun",
			"tomb",
			"triton",
			"turtle",
			"walker",
			"warped",
			"zombie"
	};

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
		return I18n.translateToLocal(this.getUnlocalizedName() + ".name");
	}

	@Override
	public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
		return stack.getItem() instanceof ItemShield;
	}

}
