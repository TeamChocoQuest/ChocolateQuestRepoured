package com.tiviacz.chocolatequestrepoured.init.base;

import com.tiviacz.chocolatequestrepoured.CQRMain;
import com.tiviacz.chocolatequestrepoured.init.ModItems;
import com.tiviacz.chocolatequestrepoured.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ArmorBase extends ItemArmor implements IHasModel
{
	public ArmorBase(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);
		
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
	
	public boolean isFullSet(EntityPlayer playerIn, ItemStack stack) 
	{
		for(int i = 1; i < 4; i++) 
		{
			ItemStack is = playerIn.inventory.armorItemInSlot(i);
			{
				if(is == null)
				{
					return false;
				}
				if(is.getItem().getClass() != stack.getItem().getClass())
				{
					return false;
				}
			}
		}
		return true;
	}
}