package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.objects.base.ItemBase;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBadge extends ItemBase
{
	public static UUID EntityUUID;
	
	public ItemBadge(String name) 
	{
		super(name);
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
}