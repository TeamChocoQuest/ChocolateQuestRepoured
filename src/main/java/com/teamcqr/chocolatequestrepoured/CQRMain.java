package com.teamcqr.chocolatequestrepoured;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonRegistry;
import com.teamcqr.chocolatequestrepoured.dungeongen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.proxy.CommonProxy;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class CQRMain
{
	
	public static File CQ_CONFIG_FOLDER = null;
	public static File CQ_DUNGEON_FOLDER = null;
	public static File CQ_STRUCTURE_FILES_FOLDER = null;
	public static File CQ_CHEST_FOLDER = null;
	
	public static CreativeTabs CQRItemsTab = new CreativeTabs("ChocolateQuestRepouredItemsTab")
	{
		@Override
		public ItemStack getTabIconItem() 
		{
			return new ItemStack(ModItems.BOOTS_CLOUD);
		}
	};
	
	public static CreativeTabs CQRBlocksTab = new CreativeTabs("ChocolateQuestRepouredBlocksTab")
	{
		@Override
		public ItemStack getTabIconItem() 
		{
			return new ItemStack(ModBlocks.TABLE_OAK);
		}
	};
	
	public static CreativeTabs CQRDungeonPlacerTab = new CreativeTabs("ChocolateQuestRepouredDungeonPlacers") {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.STONEBRICK);
		}
	};
	
	public static CreativeTabs CQRExporterChestTab = new CreativeTabs("ChocolateQuestRepouredExporterChests") {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.CHEST);
		}
	};
	
	@Instance
	public static CQRMain INSTANCE;
	
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("cqrepoured");
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	//Dungeon Registry instance, responsible for everything regarding dungeons
	public static DungeonRegistry dungeonRegistry = new DungeonRegistry();
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		Configuration configFile = new Configuration(event.getSuggestedConfigurationFile());
		CQRMain.CQ_CONFIG_FOLDER = configFile.getConfigFile().getParentFile();
		
		File dungeonFolder = new File(CQ_CONFIG_FOLDER.getAbsolutePath() + "/CQR/dungeons/");
		if(!dungeonFolder.exists()) {
			dungeonFolder.mkdirs();
		}
		CQRMain.CQ_DUNGEON_FOLDER = dungeonFolder;
		
		File chestFolder = new File(CQ_CONFIG_FOLDER.getAbsolutePath() + "/CQR/lootconfigs/");
		if(!chestFolder.exists()) {
			chestFolder.mkdirs();
		}
		CQRMain.CQ_DUNGEON_FOLDER = chestFolder;
		
		File structureFolder = new File(CQ_CONFIG_FOLDER.getAbsolutePath() + "/CQR/structures/");
		if(!structureFolder.exists()) {
			structureFolder.mkdirs();
		}
		CQRMain.CQ_STRUCTURE_FILES_FOLDER = structureFolder;
		
		proxy.preInit(event);
		
		//Enables Dungeon generation in worlds, do not change the number (!) and do NOT remove this line, moving it somewhere else is fine, but it must be called in pre initialization (!) 
		GameRegistry.registerWorldGenerator(new WorldDungeonGenerator(), 100);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{		
		proxy.init(event);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
}