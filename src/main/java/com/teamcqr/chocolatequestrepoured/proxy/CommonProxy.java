package com.teamcqr.chocolatequestrepoured.proxy;

import com.teamcqr.chocolatequestrepoured.util.handlers.EntityHandler;
import com.teamcqr.chocolatequestrepoured.util.handlers.SoundsHandler;
import com.teamcqr.chocolatequestrepoured.util.handlers.TileEntityHandler;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy 
{
	public void preInit(FMLPreInitializationEvent event)
	{
		EntityHandler.registerEntity();
	}
	
	public void init(FMLInitializationEvent event)
	{
		TileEntityHandler.registerTileEntity();
		SoundsHandler.registerSounds();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	public void registerItemRenderer(Item item, int meta, String id) 
	{
		
	}
	
	public void registerRenderers()
	{
		
	}
}