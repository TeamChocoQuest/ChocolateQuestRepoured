package com.teamcqr.chocolatequestrepoured.proxy;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.CQSaveStructureRequestPacket;
import com.teamcqr.chocolatequestrepoured.network.ExporterUpdatePacket;
import com.teamcqr.chocolatequestrepoured.network.ExporterUpdatePacketHandler;
import com.teamcqr.chocolatequestrepoured.network.SaveStructureRequestPacketHandler;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.handlers.EntityHandler;
import com.teamcqr.chocolatequestrepoured.util.handlers.SoundsHandler;
import com.teamcqr.chocolatequestrepoured.util.handlers.TileEntityHandler;

import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy 
{
	public void preInit(FMLPreInitializationEvent event)
	{
		EntityHandler.registerEntity();
		loadDungeons();
		
		CQRMain.NETWORK.registerMessage(SaveStructureRequestPacketHandler.class, CQSaveStructureRequestPacket.class, Reference.SAVE_STRUCUTRE_REQUEST_MESSAGE_ID, Side.SERVER);
		CQRMain.NETWORK.registerMessage(ExporterUpdatePacketHandler.class, ExporterUpdatePacket.class,0, Side.SERVER);
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
	
	private void loadDungeons() {
		//Fills the biomes of the biome-dungeonlist map
		for(Biome b : ForgeRegistries.BIOMES.getValuesCollection()) {
			if(b != null) {
				CQRMain.dungeonRegistry.addBiomeEntryToMap(b);
			}
		}
		CQRMain.dungeonRegistry.loadDungeonFiles();
	}
}