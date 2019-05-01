package com.teamcqr.chocolatequestrepoured.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

public class InventoryBackpack extends InventoryItem
{
	public InventoryBackpack(ItemStack stack) 
	{
		super(stack, new TextComponentString(I18n.format("gui.backpack.name")), 27);
	}
}