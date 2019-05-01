package com.teamcqr.chocolatequestrepoured.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class InventoryAlchemyBag extends InventoryItem
{
	public InventoryAlchemyBag(ItemStack stack) 
	{
		super(stack, new TextComponentString(I18n.format("gui.alchemy_bag.name")), 4);
	}
}