package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemUtil {

	public static boolean isFullSet(EntityPlayer playerIn, ItemStack stack) {
		Class itemClass = stack.getItem().getClass();
		ItemStack helm = playerIn.inventory.armorInventory.get(3);
		ItemStack chest = playerIn.inventory.armorInventory.get(2);
		ItemStack legs = playerIn.inventory.armorInventory.get(1);
		ItemStack feet = playerIn.inventory.armorInventory.get(0);

		return helm.getClass() == itemClass && chest.getClass() == itemClass && legs.getClass() == itemClass
				&& feet.getClass() == itemClass;
	}

}
