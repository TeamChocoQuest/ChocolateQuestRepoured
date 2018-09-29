package com.tiviacz.chocolatequestrepoured.tab;

import com.tiviacz.chocolatequestrepoured.init.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ChocolateQuestRepouredTab extends CreativeTabs 
{
	public ChocolateQuestRepouredTab() 
	{
			super("ChocolateQuestRepouredTab");
			setBackgroundImageName("items.png");
	}
	
	@Override
	public ItemStack getTabIconItem() 
	{
		return new ItemStack(ModItems.CLOUD_BOOTS); 
	}
	
}
		    
