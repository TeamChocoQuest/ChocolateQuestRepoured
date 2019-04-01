package com.teamcqr.chocolatequestrepoured.objects.items;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.util.IHasModel;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class ItemShieldBase extends ItemShield implements IHasModel
{
	int maxDamage;
	
	public ItemShieldBase(String name, int maxDamage) 
	{
		super();
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CQRMain.CQRItemsTab);
		
		ModItems.ITEMS.add(this);
		
		this.maxDamage = maxDamage;
		
		setMaxStackSize(1);
        setMaxDamage(maxDamage);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
    {
		return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
    }
	
	@Override
	public void registerModels()
	{
		CQRMain.proxy.registerItemRenderer(this, 0,"inventory");
	}
	
	@Override
	public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity)
    {
        return stack.getItem() == this;
    }
}