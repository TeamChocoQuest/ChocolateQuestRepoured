package com.tiviacz.chocolatequestrepoured.init.base;

import com.tiviacz.chocolatequestrepoured.CQRMain;
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
		setCreativeTab(CQRMain.CQRItemsTab);
		
		ModItems.ITEMS.add(this);
	}

	@Override
	public void registerModels() 	
	{ 
		CQRMain.proxy.registerItemRenderer(this, 0, "inventory");
	}
}