package com.teamcqr.chocolatequestrepoured.objects.items;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.base.ItemBase;
import com.teamcqr.chocolatequestrepoured.util.IHasModel;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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