package com.teamcqr.chocolatequestrepoured.proxy;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.handlers.EntityHandler;
import com.teamcqr.chocolatequestrepoured.util.handlers.SoundsHandler;
import com.teamcqr.chocolatequestrepoured.util.handlers.TileEntityHandler;

import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
	
	@SuppressWarnings("deprecation")
	public void postInit(FMLPostInitializationEvent event)
	{
		//Fills the biomes of the biome-dungeonlist map
		for(Biome b : ForgeRegistries.BIOMES.getValues()) {
			if(b != null) {
				CQRMain.dungeonRegistry.addBiomeEntryToMap(b);
			}
		}
	}
	
	public void registerItemRenderer(Item item, int meta, String id) 
	{
		
	}
	
	public void registerRenderers()
	{
		
	}
}