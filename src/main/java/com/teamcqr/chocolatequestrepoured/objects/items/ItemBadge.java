package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBadge extends Item
{
	public static UUID EntityUUID;
	
	public ItemBadge() 
	{
		setMaxStackSize(1);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		 if(player.capabilities.isCreativeMode && !player.world.isRemote) 
		 {
			 this.setEntityUUID(entity);
			 player.openGui(CQRMain.INSTANCE, Reference.BADGE_GUI_ID, player.world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
		 }
		 return true;
    }
	
	public void setEntityUUID(Entity entity)
	{
		if(entity != null)
		{
			UUID uuid = entity.getUniqueID();
			EntityUUID = uuid;
		}
	}
	
	public static Entity getEntityByUniqueUUID(World world)
	{
		for(Entity entity : world.loadedEntityList)
		{
			if(entity.getUniqueID() == EntityUUID)
			{
				return entity;
			}
		}
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.badge.name"));
		}		
		else
		{
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
    }
}