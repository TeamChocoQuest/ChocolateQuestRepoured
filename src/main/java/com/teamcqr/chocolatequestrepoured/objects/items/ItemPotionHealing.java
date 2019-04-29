package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.objects.base.ItemBase;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPotionHealing extends ItemBase
{
	public ItemPotionHealing(String name) 
	{
		super(name);
		setMaxStackSize(16);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 24;
    }

	@Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(playerIn.getHealth() < playerIn.getMaxHealth())
		{
			playerIn.setActiveHand(handIn);
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		}
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
		EntityPlayer player = (EntityPlayer)entityLiving;
		
		if(player instanceof EntityPlayer)
		{
			if(!worldIn.isRemote)
			{
				float h = player.getHealth();
				player.setHealth(h + 4.0F);
			}
			
			if(!player.capabilities.isCreativeMode)
			{
				stack.shrink(1);
				
				if(stack.isEmpty())
				{
					return new ItemStack(Items.GLASS_BOTTLE);
				}
				else
				{
					if(!player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE)))
	        		{
	        			if(!worldIn.isRemote)
	        			{
	        				worldIn.spawnEntity(new EntityItem(worldIn, entityLiving.posX, entityLiving.posY, entityLiving.posZ, new ItemStack(Items.GLASS_BOTTLE)));
	        			}
	        		}
				}
			}
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.healing_potion.name"));
		}		
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
}