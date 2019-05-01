package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.base.ItemBase;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAlchemyBag extends ItemBase
{
	public ItemAlchemyBag(String name) 
	{
		super(name);
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if(!worldIn.isRemote)
		{
			if(playerIn.getHeldItemMainhand().getItem() == this)
			{
				playerIn.openGui(CQRMain.INSTANCE, Reference.ALCHEMY_BAG_GUI_ID, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItemMainhand());
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItemMainhand());
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.alchemy_bag.name"));
		}		
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
}