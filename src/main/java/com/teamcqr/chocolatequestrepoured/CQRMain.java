package com.teamcqr.chocolatequestrepoured;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.factions.FactionRegistry;
import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModCapabilities;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.init.ModMaterials;
import com.teamcqr.chocolatequestrepoured.init.ModMessages;
import com.teamcqr.chocolatequestrepoured.objects.banners.BannerHelper;
import com.teamcqr.chocolatequestrepoured.objects.banners.EBannerPatternsCQ;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesBoss;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesNormal;
import com.teamcqr.chocolatequestrepoured.proxy.IProxy;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonRegistry;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;
import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.LootTableLoader;
import com.teamcqr.chocolatequestrepoured.structuregen.thewall.WorldWallGenerator;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectionHandler;
import com.teamcqr.chocolatequestrepoured.util.CopyHelper;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.handlers.GuiHandler;
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
public class CQRMain {

	// public static boolean generateInFlat = false;

	public static File CQ_CONFIG_FOLDER = null;
	public static File CQ_DUNGEON_FOLDER = null;
	public static File CQ_STRUCTURE_FILES_FOLDER = null;
	public static File CQ_EXPORT_FILES_FOLDER = null;
	public static File CQ_CHEST_FOLDER = null;
	public static File CQ_FACTION_FOLDER = null;

	public static List<ResourceLocation> CQ_LOOT_TABLES = new ArrayList<ResourceLocation>();
	public static List<ResourceLocation> CQ_DUNGEON_LOOT = new ArrayList<ResourceLocation>();

	public static CreativeTabs CQRItemsTab = new CreativeTabs("ChocolateQuestRepouredItemsTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.BOOTS_CLOUD);
		}
	};
	// Tab with all the blocks
	public static CreativeTabs CQRBlocksTab = new CreativeTabs("ChocolateQuestRepouredBlocksTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModBlocks.TABLE_OAK);
		}
	};
	// Tab that holds all banner designs loaded
	public static CreativeTabs CQRBannersTab = new CreativeTabs("ChocolateQuestRepouredBannerTab") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.BANNER);
		}

		@Override
		public void displayAllRelevantItems(net.minecraft.util.NonNullList<ItemStack> itemList) {
			List<ItemStack> banners = new ArrayList<ItemStack>();
			// banners = BannerHandler.addBannersToTabs();
			banners = BannerHelper.addBannersToTabs();
			if (banners != null && !banners.isEmpty()) {
				for (ItemStack stack : banners) {
					itemList.add(stack);
				}
			}
		};
	};
	// Tab that holds placers for all dungeons
	public static CreativeTabs CQRDungeonPlacerTab = new CreativeTabs("ChocolateQuestRepouredDungeonPlacers") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.STONEBRICK);
		}
	};
	// Tab that holds all dungeon building things (chests + exporter)
	public static CreativeTabs CQRExporterChestTab = new CreativeTabs("ChocolateQuestRepouredExporterChests") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.CHEST);
		}
	};
	// Tab that holds all cqr spawn eggs
	public static CreativeTabs CQRSpawnEggTab = new CreativeTabs("CQR Spawn Eggs") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.SPAWN_EGG);
		}
	};
	// Tab that has some dummy presets
	/*
	 * public static CreativeTabs CQRDummyPresetTab = new CreativeTabs("ChocolateQuestRepouredDummyPresets") {
	 * 
	 * @Override
	 * public ItemStack getTabIconItem() {
	 * return new ItemStack(ModItems.SOUL_BOTTLE);
	 * }
	 * 
	 * public void displayAllRelevantItems(net.minecraft.util.NonNullList<ItemStack> items) {
	 * items.addAll(PresetTabItems.getLeatherPresets());
	 * items.addAll(PresetTabItems.getGoldPresets());
	 * items.addAll(PresetTabItems.getChainmailPresets());
	 * items.addAll(PresetTabItems.getIronPresets());
	 * items.addAll(PresetTabItems.getDiamondPresets());
	 * };
	 * };
	 */

	@Instance
	public static CQRMain INSTANCE;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("cqrepoured");

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static IProxy proxy;

	// Dungeon Registry instance, responsible for everything regarding dungeons
	public static DungeonRegistry dungeonRegistry = new DungeonRegistry();

	@EventHandler
	public void PreInit(FMLPreInitializationEvent event) {
		// Important: This has to be the F I R S T statement
		this.initConfigFolder(event);

		proxy.preInit();

		// Faction system
		FactionRegistry.instance().loadFactions();

		// Enables Dungeon generation in worlds, do not change the number (!) and do NOT
		// remove this line, moving it somewhere else is fine, but it must be called in
		// pre initialization (!)
		GameRegistry.registerWorldGenerator(new WorldDungeonGenerator(), 100);
		if (Reference.CONFIG_HELPER_INSTANCE.buildWall()) {
			GameRegistry.registerWorldGenerator(new WorldWallGenerator(), 101);
		}

		// Instantiating the banners
		try {
			// BannerHandler.initPatterns();
			for (EBannerPatternsCQ cqPattern : EBannerPatternsCQ.values()) {
				cqPattern.getPattern();
			}
		} catch (Exception ex) {
			System.err.println("WARNING: Failed to instantiate the banners!!");
			ex.printStackTrace();
		}
		// Instantiating loot tables for entities
		try {
			for (ELootTablesNormal eltn : ELootTablesNormal.values()) {
				eltn.getLootTable();
			}
			for (ELootTablesBoss eltn : ELootTablesBoss.values()) {
				eltn.getLootTable();
			}
		} catch (Exception ex) {
			System.err.println("WARNING: Failed to instantiate entity loot tables!!");
			ex.printStackTrace();
		}

		// Register event handling for dungeon protection system
		MinecraftForge.EVENT_BUS.register(ProtectionHandler.getInstance());

		ModMessages.registerMessages();
		ModCapabilities.registerCapabilities();
	}

	private void initConfigFolder(FMLPreInitializationEvent event) {
		Configuration configFile = new Configuration(event.getSuggestedConfigurationFile());

		Reference.CONFIG_HELPER_INSTANCE.loadValues(configFile);
		Reference.BLOCK_PLACING_THREADS_INSTANCE.resetThreads(Reference.CONFIG_HELPER_INSTANCE.getBlockPlacerThreadCount());

		CQRMain.CQ_CONFIG_FOLDER = configFile.getConfigFile().getParentFile();

		boolean installCQ = false;
		File CQFolder = new File(CQ_CONFIG_FOLDER.getAbsolutePath() + "/CQR/");
		if (!CQFolder.exists() || (CQFolder.exists() && !CQFolder.isDirectory())) {
			CQFolder.mkdirs();
			// Install default files
			installCQ = true;
		}
		if (Reference.CONFIG_HELPER_INSTANCE.reInstallDefaultFiles()) {
			installCQ = true;
		}

		File dungeonFolder = new File(CQFolder.getAbsolutePath() + "/dungeons//");
		if (!dungeonFolder.exists()) {
			dungeonFolder.mkdirs();
		}
		System.out.println("Dungeon Folder Path: " + dungeonFolder.getAbsolutePath());
		CQRMain.CQ_DUNGEON_FOLDER = dungeonFolder;

		File chestFolder = new File(CQFolder.getAbsolutePath() + "/lootconfigs//");
		if (!chestFolder.exists()) {
			chestFolder.mkdirs();
		}
		System.out.println("LootConfig Folder Path: " + chestFolder.getAbsolutePath());
		CQRMain.CQ_CHEST_FOLDER = chestFolder;

		File structureFolder = new File(CQFolder.getAbsolutePath() + "/structures//");
		if (!structureFolder.exists()) {
			structureFolder.mkdirs();
		}
		System.out.println("Structure Folder Path: " + structureFolder.getAbsolutePath());
		CQRMain.CQ_STRUCTURE_FILES_FOLDER = structureFolder;

		File exportFolder = new File(CQFolder.getAbsolutePath() + "/exporter_output//");
		if (!exportFolder.exists()) {
			exportFolder.mkdirs();
		}
		System.out.println("Export Folder Path: " + exportFolder.getAbsolutePath());
		CQRMain.CQ_EXPORT_FILES_FOLDER = exportFolder;

		File factionFolder = new File(CQFolder.getAbsolutePath() + "/factions//");
		if (!factionFolder.exists()) {
			factionFolder.mkdirs();
		}
		System.out.println("Faction Folder Path: " + factionFolder.getAbsolutePath());
		CQRMain.CQ_FACTION_FOLDER = factionFolder;

		if (installCQ) {
			try {
				Path target = new File(event.getModConfigurationDirectory(), "CQR").toPath();
				CopyHelper.copyFromJar("/assets/cqrepoured/defaultConfigs", target);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();

		// Instantiating the ELootTable class
		try {
			ResourceLocation resLoc = ELootTable.CQ_VANILLA_WOODLAND_MANSION.getResourceLocation();
			if (resLoc != null) {
				System.out.println("Loot tables instantiated successfully!");
				LootTableLoader ltl = new LootTableLoader();
				System.out.println("Loading the loot configs...");
				ltl.loadConfigs();
			}
		} catch (Exception e) {
			System.err.println("WARNING: Failed to instantiate the loot tables or to exchange the files!!");
			e.printStackTrace();
		}

		TileEntityHandler.registerTileEntity();
		NetworkRegistry.INSTANCE.registerGuiHandler(CQRMain.INSTANCE, new GuiHandler());
		// DungeonRegistry.loadDungeons();
		ModMaterials.setRepairItemsForMaterials();
		// SmeltingHandler.init();
	}

	@EventHandler
	public void PostInit(FMLPostInitializationEvent event) {
		DungeonRegistry.loadDungeons();
		proxy.postInit();
	}
}
