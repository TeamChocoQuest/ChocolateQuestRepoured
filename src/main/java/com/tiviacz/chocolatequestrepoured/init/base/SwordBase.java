package com.tiviacz.chocolatequestrepoured.init.base;

import com.tiviacz.chocolatequestrepoured.CQRMain;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.util.IHasModel;

import net.minecraft.item.ItemSword;

public class SwordBase extends ItemSword implements IHasModel
{
	public SwordBase(String name, ToolMaterial material) 
	{
		super(material);
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CQRMain.CQRItemsTab);
		
		ModItems.ITEMS.add(this);
	}
		
	@Override
	public void registerModels()
	{
		CQRMain.proxy.registerItemRenderer(this, 0,"inventory");
	}
}