package com.tiviacz.chocolatequestrepoured.util.handlers;

import com.tiviacz.chocolatequestrepoured.init.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.Console;

@EventBusSubscriber
public class FallEventHandler
{ 	
	@SubscribeEvent
	public static void cancelPlayerFallDamage(LivingFallEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.getEntity();
			
			if (player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ModItems.CLOUD_BOOTS)
			{
				event.setDistance(0.0F);
			}
			
			if(player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ModItems.GOLDEN_FEATHER)
			{
				event.setDistance(0.0F);
			}
			
			if(player.getHeldItem(EnumHand.OFF_HAND).getItem() == ModItems.GOLDEN_FEATHER)
			{
				event.setDistance(0.0F);
			}
		}
	}
}