package com.teamcqr.chocolatequestrepoured;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

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
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.CopyHelper;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.handlers.GuiHandler;
import com.teamcqr.chocolatequestrepoured.util.handlers.TileEntityHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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

	@Instance
	public static CQRMain INSTANCE;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static IProxy proxy;

	public static DungeonRegistry dungeonRegistry = new DungeonRegistry();

	public static Logger logger = null;

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
	public static CreativeTabs CQRBlocksTab = new CreativeTabs("ChocolateQuestRepouredBlocksTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModBlocks.TABLE_OAK);
		}
	};
	public static CreativeTabs CQRBannersTab = new CreativeTabs("ChocolateQuestRepouredBannerTab") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.BANNER);
		}

		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {
			super.displayAllRelevantItems(itemList);
			List<ItemStack> banners = BannerHelper.addBannersToTabs();
			for (ItemStack stack : banners) {
				itemList.add(stack);
			}
		};
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
	public static CreativeTabs CQRSpawnEggTab = new CreativeTabs("CQR Spawn Eggs") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.SPAWN_EGG);
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		// Important: This has to be the F I R S T statement
		this.initConfigFolder(event);

		proxy.preInit();

		// Faction system
		FactionRegistry.instance().loadFactions();

		// Enables Dungeon generation in worlds, do not change the number (!) and do NOT
		// remove this line, moving it somewhere else is fine, but it must be called in
		// pre initialization (!)
		GameRegistry.registerWorldGenerator(new WorldDungeonGenerator(), 100);
		if (CQRConfig.wall.enabled) {
			GameRegistry.registerWorldGenerator(new WorldWallGenerator(), 101);
		}

		// Instantiating banners
		try {
			logger.info("Loading banner configuration...");
			for (EBannerPatternsCQ cqPattern : EBannerPatternsCQ.values()) {
				cqPattern.getPattern();
			}
		} catch (Exception e) {
			logger.error("Failed to instantiate banners!");
			logger.error(e);
		}
		// Instantiating loot tables for entities
		try {
			logger.info("Loading entity loot table configuration...");
			for (ELootTablesNormal eltn : ELootTablesNormal.values()) {
				eltn.getLootTable();
			}
			for (ELootTablesBoss eltn : ELootTablesBoss.values()) {
				eltn.getLootTable();
			}
		} catch (Exception e) {
			logger.error("Failed to instantiate entity loot tables!");
			logger.error(e);
		}

		// Register event handling for dungeon protection system
		//MinecraftForge.EVENT_BUS.register(ProtectionHandler.getInstance());

		ModMessages.registerMessages();
		ModCapabilities.registerCapabilities();
	}

	private void initConfigFolder(FMLPreInitializationEvent event) {
		Configuration configFile = new Configuration(event.getSuggestedConfigurationFile());

		Reference.BLOCK_PLACING_THREADS_INSTANCE.resetThreads(CQRConfig.advanced.threadCount);

		boolean installCQ = false;

		CQ_CONFIG_FOLDER = new File(event.getModConfigurationDirectory(), "CQR");
		CQ_DUNGEON_FOLDER = new File(CQ_CONFIG_FOLDER, "dungeons");
		CQ_CHEST_FOLDER = new File(CQ_CONFIG_FOLDER, "lootconfigs");
		CQ_STRUCTURE_FILES_FOLDER = new File(CQ_CONFIG_FOLDER, "structures");
		CQ_EXPORT_FILES_FOLDER = new File(CQ_CONFIG_FOLDER, "exports");
		CQ_FACTION_FOLDER = new File(CQ_CONFIG_FOLDER, "factions");

		if (!CQ_CONFIG_FOLDER.exists()) {
			CQ_CONFIG_FOLDER.mkdir();

			installCQ = true;
		} else if (CQRConfig.general.reinstallDefaultConfigs) {
			installCQ = true;
		}
		if (!CQ_DUNGEON_FOLDER.exists()) {
			CQ_DUNGEON_FOLDER.mkdir();
		}
		if (!CQ_CHEST_FOLDER.exists()) {
			CQ_CHEST_FOLDER.mkdir();
		}
		if (!CQ_STRUCTURE_FILES_FOLDER.exists()) {
			CQ_STRUCTURE_FILES_FOLDER.mkdir();
		}
		if (!CQ_EXPORT_FILES_FOLDER.exists()) {
			CQ_EXPORT_FILES_FOLDER.mkdir();
		}
		if (!CQ_FACTION_FOLDER.exists()) {
			CQ_FACTION_FOLDER.mkdir();
		}

		if (installCQ) {
			try {
				CopyHelper.copyFromJar("/assets/cqrepoured/defaultConfigs", CQ_CONFIG_FOLDER.toPath());
			} catch (URISyntaxException | IOException e) {
				logger.error(e);
			}
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();

		// Instantiating the ELootTable class
		try {
			if (ELootTable.CQ_VANILLA_WOODLAND_MANSION.getResourceLocation() != null) {
				logger.info("Loading chest loot table configuration...");
				LootTableLoader lootTableLoader = new LootTableLoader();
				lootTableLoader.loadConfigs();
			} else {
				throw new Exception("Couldn't load loot table configs!!!");
			}
		} catch (Exception e) {
			logger.error("Failed to instantiate the loot tables or to exchange the files!");
			logger.error(e);
		}

		TileEntityHandler.registerTileEntity();
		NetworkRegistry.INSTANCE.registerGuiHandler(CQRMain.INSTANCE, new GuiHandler());
		ModMaterials.setRepairItemsForMaterials();
		// SmeltingHandler.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();

		DungeonRegistry.loadDungeons();
	}

}
