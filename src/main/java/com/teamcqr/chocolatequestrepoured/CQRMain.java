package com.teamcqr.chocolatequestrepoured;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModCapabilities;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.ModMaterials;
import com.teamcqr.chocolatequestrepoured.init.ModMessages;
import com.teamcqr.chocolatequestrepoured.objects.banners.BannerHelper;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBannerPatternsCQ;
import com.teamcqr.chocolatequestrepoured.proxy.IProxy;
import com.teamcqr.chocolatequestrepoured.smelting.SmeltingHandler;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonRegistry;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.LootTableLoader;
import com.teamcqr.chocolatequestrepoured.structuregen.thewall.WorldWallGenerator;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectionHandler;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.handlers.GuiHandler;
import com.teamcqr.chocolatequestrepoured.util.handlers.SoundsHandler;
import com.teamcqr.chocolatequestrepoured.util.handlers.TileEntityHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
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
	
	//public static boolean generateInFlat = false;
	
	public static File CQ_CONFIG_FOLDER = null;
	public static File CQ_DUNGEON_FOLDER = null;
	public static File CQ_STRUCTURE_FILES_FOLDER = null;
	public static File CQ_EXPORT_FILES_FOLDER = null;
	public static File CQ_CHEST_FOLDER = null;
	
	public static List<ResourceLocation> CQ_LOOT_TABLES = new ArrayList<ResourceLocation>();
	public static List<ResourceLocation> CQ_DUNGEON_LOOT = new ArrayList<ResourceLocation>();
	
	public static CreativeTabs CQRItemsTab = new CreativeTabs("ChocolateQuestRepouredItemsTab")
	{
		@Override
		public ItemStack getTabIconItem() 
		{
			return new ItemStack(ModItems.BOOTS_CLOUD);
		}
	};
	//Tab with all the blocks
	public static CreativeTabs CQRBlocksTab = new CreativeTabs("ChocolateQuestRepouredBlocksTab")
	{
		@Override
		public ItemStack getTabIconItem() 
		{
			return new ItemStack(ModBlocks.TABLE_OAK);
		}
	};
	//Tab that holds all banner designs loaded
	public static CreativeTabs CQRBannersTab = new CreativeTabs("ChocolateQuestRepouredBannerTab") {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.BANNER);
		}
		@Override
		public void displayAllRelevantItems(net.minecraft.util.NonNullList<ItemStack> itemList) {
			List<ItemStack> banners = new ArrayList<ItemStack>();
			//banners = BannerHandler.addBannersToTabs();
			banners = BannerHelper.addBannersToTabs();
			if(banners != null && !banners.isEmpty()) {
				for(ItemStack stack : banners) itemList.add(stack);
			}
		};
	};
	//Tab that holds placers for all dungeons
	public static CreativeTabs CQRDungeonPlacerTab = new CreativeTabs("ChocolateQuestRepouredDungeonPlacers") {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.STONEBRICK);
		}
	};
	//Tab that holds all dungeon building things (chests + exporter)
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
	public static IProxy proxy;
	
	//Dungeon Registry instance, responsible for everything regarding dungeons
	public static DungeonRegistry dungeonRegistry = new DungeonRegistry();
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		initConfigFolder(event);
		
		proxy.preInit();
		
		//Enables Dungeon generation in worlds, do not change the number (!) and do NOT remove this line, moving it somewhere else is fine, but it must be called in pre initialization (!) 
		GameRegistry.registerWorldGenerator(new WorldDungeonGenerator(), 100);
		if(Reference.CONFIG_HELPER_INSTANCE.buildWall()) {
			GameRegistry.registerWorldGenerator(new WorldWallGenerator(), 101);
		}

		//Instantiating the ELootTable class
		try {
			ResourceLocation resLoc = ELootTable.CQ_VANILLA_WOODLAND_MANSION.getResourceLocation();
			if(resLoc != null) {
				System.out.println("Loot tables instantiated successfully!");
				LootTableLoader ltl = new LootTableLoader();
				System.out.println("Loading the loot configs...");
				ltl.loadConfigs();
			}
		} catch (Exception e) {
			System.err.println("WARNING: Failed to instantiate the loot tables or to exchange the files!!");
			e.printStackTrace();
		}
		//Instantiating the banners
		try {
			//BannerHandler.initPatterns();
			for(EBannerPatternsCQ cqPattern : EBannerPatternsCQ.values()) {
				cqPattern.getPattern();
			}
		} catch(Exception ex) {
			System.err.println("WARNING: Failed to instantiate the banners!!");
			ex.printStackTrace();
		}

		//Register event handling for dungeon protection system
		MinecraftForge.EVENT_BUS.register(ProtectionHandler.getInstance());

		ModMessages.registerMessages();
		SoundsHandler.registerSounds();
		ModCapabilities.registerCapabilities();
	}
	
	private void initConfigFolder(FMLPreInitializationEvent event) {
		Configuration configFile = new Configuration(event.getSuggestedConfigurationFile());
		
		Reference.CONFIG_HELPER_INSTANCE.loadValues(configFile);
		Reference.BLOCK_PLACING_THREADS_INSTANCE.resetThreads(Reference.CONFIG_HELPER_INSTANCE.getBlockPlacerThreadCount());
		
		CQRMain.CQ_CONFIG_FOLDER = configFile.getConfigFile().getParentFile();
		
		boolean installCQ = false;
		File CQFolder = new File(CQ_CONFIG_FOLDER.getAbsolutePath() + "/CQR/");
		if(!CQFolder.exists() || (CQFolder.exists() && !CQFolder.isDirectory())) {
			CQFolder.mkdirs();
			//Install default files
			installCQ = true;
		}
		
		File dungeonFolder = new File(CQFolder.getAbsolutePath() + "/dungeons//");
		if(!dungeonFolder.exists()) {
			dungeonFolder.mkdirs();
		}
		System.out.println("Dungeon Folder Path: " + dungeonFolder.getAbsolutePath());
		CQRMain.CQ_DUNGEON_FOLDER = dungeonFolder;
		
		File chestFolder = new File(CQFolder.getAbsolutePath() + "/lootconfigs//");
		if(!chestFolder.exists()) {
			chestFolder.mkdirs();
		}
		System.out.println("LootConfig Folder Path: " + chestFolder.getAbsolutePath());
		CQRMain.CQ_CHEST_FOLDER = chestFolder;
		
		File structureFolder = new File(CQFolder.getAbsolutePath() + "/structures//");
		if(!structureFolder.exists()) {
			structureFolder.mkdirs();
		}
		System.out.println("Structure Folder Path: " + structureFolder.getAbsolutePath());
		CQRMain.CQ_STRUCTURE_FILES_FOLDER = structureFolder;
		
		File exportFolder = new File(CQFolder.getAbsolutePath() + "/exports//");
		if(!exportFolder.exists()) {
			exportFolder.mkdirs();
		}
		System.out.println("Export Folder Path: " + exportFolder.getAbsolutePath());
		CQRMain.CQ_EXPORT_FILES_FOLDER = exportFolder;
		
		if(installCQ) {
			installDefaultFiles(CQFolder);
		}
	}
	
	private void installDefaultFiles(File folder) {
		//TODO: Install the default stuff
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{		
		proxy.init();

		TileEntityHandler.registerTileEntity();
		NetworkRegistry.INSTANCE.registerGuiHandler(CQRMain.INSTANCE, new GuiHandler());
		DungeonRegistry.loadDungeons();
		ModMaterials.setRepairItemsForMaterials();
		SmeltingHandler.init();
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event)
	{
		/*for(EFaction efac : EFaction.values()) {
			System.out.println(efac.toString());
		}*/
		proxy.postInit();
	}
}
