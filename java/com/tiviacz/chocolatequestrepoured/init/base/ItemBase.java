package com.tiviacz.chocolatequestrepoured.init.base;

import com.tiviacz.chocolatequestrepoured.ChocolateQuestRepouredMain;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel 
	{
	
		public ItemBase(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(ChocolateQuestRepouredMain.SpecialTab);
		
		ModItems.ITEMS.add(this);
		
	}

	@Override
	public void registerModels() 	
	
	{ 
		ChocolateQuestRepouredMain.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
