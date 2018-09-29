package com.tiviacz.chocolatequestrepoured.init.base;

import com.tiviacz.chocolatequestrepoured.ChocolateQuestRepouredMain;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class ArmorBase extends ItemArmor implements IHasModel
	{

		public ArmorBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		
		super(materialIn, renderIndexIn, equipmentSlotIn);
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