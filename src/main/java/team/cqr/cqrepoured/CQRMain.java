package team.cqr.cqrepoured;

import java.io.File;
import java.util.List;

import net.minecraft.item.ItemGroup;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.FireBlock;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import software.bernie.geckolib3.GeckoLib;
import team.cqr.cqrepoured.block.banner.BannerHelper;
import team.cqr.cqrepoured.block.banner.EBannerPatternsCQ;
import team.cqr.cqrepoured.block.banner.EBanners;
import team.cqr.cqrepoured.command.CommandChangeReputation;
import team.cqr.cqrepoured.command.CommandDeleteProtectedRegion;
import team.cqr.cqrepoured.command.CommandExport;
import team.cqr.cqrepoured.command.CommandGetProtectedRegion;
import team.cqr.cqrepoured.command.CommandImport;
import team.cqr.cqrepoured.command.CommandLocateDungeon;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.TextureSetManager;
import team.cqr.cqrepoured.entity.boss.netherdragon.EntityCQRNetherDragon;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;
import team.cqr.cqrepoured.init.CQRDispenseBehaviors;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.init.CQRMaterials;
import team.cqr.cqrepoured.init.CQRMessages;
import team.cqr.cqrepoured.item.crafting.smelting.SmeltingHandler;
import team.cqr.cqrepoured.proxy.IProxy;
import team.cqr.cqrepoured.util.ConfigBackupHandler;
import team.cqr.cqrepoured.util.CopyHelper;
import team.cqr.cqrepoured.util.GuiHandler;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.thewall.WorldWallGenerator;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionHelper;

@Mod(modid = CQRMain.MODID, version = CQRMain.VERSION, acceptedMinecraftVersions = CQRMain.ACCEPTED_MINECRAFT_VERSIONS)
public class CQRMain {

	public static final String MODID = "cqrepoured";
	public static final String VERSION = "2.6.3B";
	public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.12,1.12.2]";

	@Instance
	public static CQRMain INSTANCE;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(CQRMain.MODID);

	@SidedProxy(clientSide = "team.cqr.cqrepoured.proxy.ClientProxy", serverSide = "team.cqr.cqrepoured.proxy.ServerProxy")
	public static IProxy proxy;

	public static Logger logger = null;

	public static File CQ_CONFIG_FOLDER = null;
	public static File CQ_DUNGEON_GRID_FOLDER = null;
	public static File CQ_DUNGEON_FOLDER = null;
	public static File CQ_STRUCTURE_FILES_FOLDER = null;
	public static File CQ_EXPORT_FILES_FOLDER = null;
	public static File CQ_CHEST_FOLDER = null;
	public static File CQ_FACTION_FOLDER = null;
	public static File CQ_INHABITANT_FOLDER = null;
	public static File CQ_ITEM_FOLDER = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_SETS = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_ROOT = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES_SYNC = null;
	public static File CQ_GLOWING_TEXTURES_FOLDER = null;

	public static boolean isWorkspaceEnvironment = true;
	public static boolean isPhosphorInstalled;
	public static boolean isEntityCullingInstalled;
	public static boolean isCubicChunksInstalled;
	public static boolean isAW2Installed;

	public static final ItemGroup CQR_ITEMS_TAB = new ItemGroup(CQRMain.MODID + "_items") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(CQRItems.BOOTS_CLOUD);
		}
	};
	public static final ItemGroup CQR_BLOCKS_TAB = new ItemGroup(CQRMain.MODID + "_blocks") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(CQRBlocks.TABLE_OAK);
		}
	};
	public static final ItemGroup CQR_CREATIVE_TOOL_TAB = new ItemGroup(CQRMain.MODID + "_creative_tools") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(CQRBlocks.EXPORTER);
		}
	};
	public static final ItemGroup CQR_BANNERS_TAB = new ItemGroup(CQRMain.MODID + "_banners") {
		@Override
		public ItemStack createIcon() {
			return EBanners.WALKER_ORDO.getBanner();
		}

		@Override
		public void displayAllRelevantItems(NonNullList<ItemStack> itemList) {
			super.displayAllRelevantItems(itemList);
			List<ItemStack> banners = BannerHelper.addBannersToTabs();
			for (ItemStack stack : banners) {
				itemList.add(stack);
			}
		}
	};
	public static final ItemGroup CQR_DUNGEON_PLACER_TAB = new ItemGroup(CQRMain.MODID + "_dungeon_placers") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(CQRMain.MODID, "dungeon_placer_d5")));
		}
	};
	public static final ItemGroup CQR_EXPORTER_CHEST_TAB = new ItemGroup(CQRMain.MODID + "_exporter_chests") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(CQRBlocks.EXPORTER_CHEST_VALUABLE);
		}
	};
	public static final ItemGroup CQR_SPAWN_EGG_TAB = new ItemGroup(CQRMain.MODID + "_spawn_eggs") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Items.SPAWN_EGG);
		}
	};

	public static final WorldDungeonGenerator DUNGEON_GENERATOR = new WorldDungeonGenerator();
	public static final WorldWallGenerator WALL_GENERATOR = new WorldWallGenerator();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// The geckolib comment says this should be in the constructor but that only applies to MC 1.16+
		GeckoLib.initialize();

		logger = event.getModLog();
		isWorkspaceEnvironment = !CQRMain.class.getResource("").getProtocol().equals("jar");
		// Important: This has to be the F I R S T statement
		this.initConfigFolder(event);

		proxy.preInit();

		// !!Custom texture system has to load BEFORE the faction system!!
		// Custom Textures System => Moved to server start
		// TextureSetManager.loadTextureSetsFromFolder(CQ_CUSTOM_TEXTURES_FOLDER_SETS);

		// Faction system => Moved to EventHandler (FMLServerStartingEvent)
		// FactionRegistry.instance().loadFactions();

		// Enables Dungeon generation in worlds, do not change the number (!) and do NOT
		// remove this line, moving it somewhere else is fine, but it must be called in
		// pre initialization (!)
		GameRegistry.registerWorldGenerator(DUNGEON_GENERATOR, 100);
		GameRegistry.registerWorldGenerator(WALL_GENERATOR, 101);

		// Instantiating enums
		EBannerPatternsCQ.values();
		EBanners.values();
		CreatureAttribute.values();
		/* DO NOT REMOVE, this fixes the neat issue */
		CQRCreatureAttributes.VOID.name();

		CQRMessages.registerMessages();
		CQRCapabilities.registerCapabilities();
		CQRLoottables.registerLootTables();
	}

	private void initConfigFolder(FMLPreInitializationEvent event) {
		CQ_CONFIG_FOLDER = new File(event.getModConfigurationDirectory(), "CQR");
		File[] subfolders = new File[] {
				CQ_DUNGEON_FOLDER = new File(CQ_CONFIG_FOLDER, "dungeons"),
				CQ_CHEST_FOLDER = new File(CQ_CONFIG_FOLDER, "lootconfigs"),
				CQ_STRUCTURE_FILES_FOLDER = new File(CQ_CONFIG_FOLDER, "structures"),
				CQ_EXPORT_FILES_FOLDER = new File(CQ_CONFIG_FOLDER, "exporter_output"),
				CQ_FACTION_FOLDER = new File(CQ_CONFIG_FOLDER, "factions"),
				CQ_INHABITANT_FOLDER = new File(CQ_CONFIG_FOLDER, "dungeon_inhabitants"),
				CQ_ITEM_FOLDER = new File(CQ_CONFIG_FOLDER, "items"),
				CQ_CUSTOM_TEXTURES_FOLDER_ROOT = new File(CQ_CONFIG_FOLDER, "textures"),
				CQ_CUSTOM_TEXTURES_FOLDER_SETS = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "texture_sets"),
				CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "textures"),
				CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES_SYNC = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "sync"),
				CQ_GLOWING_TEXTURES_FOLDER = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "eyes"),
				CQ_DUNGEON_GRID_FOLDER = new File(CQ_CONFIG_FOLDER, "grids") };

		ConfigBackupHandler.registerConfig(CQ_DUNGEON_FOLDER.getName(), "1.1.0");
		ConfigBackupHandler.registerConfig(CQ_DUNGEON_GRID_FOLDER.getName(), "1.0.0");
		ConfigBackupHandler.registerConfig(CQ_CHEST_FOLDER.getName(), "1.0.0");
		ConfigBackupHandler.registerConfig(CQ_STRUCTURE_FILES_FOLDER.getName(), "1.0.1");
		ConfigBackupHandler.registerConfig(CQ_FACTION_FOLDER.getName(), "1.0.0");
		ConfigBackupHandler.registerConfig(CQ_INHABITANT_FOLDER.getName(), "1.0.0");
		ConfigBackupHandler.registerConfig(CQ_ITEM_FOLDER.getName(), "1.0.0");
		ConfigBackupHandler.registerConfig(CQ_CUSTOM_TEXTURES_FOLDER_ROOT.getName(), "1.0.0");

		if (!CQ_CONFIG_FOLDER.exists() || CQRConfig.general.reinstallDefaultConfigs) {
			CopyHelper.copyFromJarOrWorkspace("/assets/cqrepoured/defaultConfigs", CQ_CONFIG_FOLDER, true);
		} else {
			ConfigBackupHandler.checkAndBackupConfigs();
		}

		int i = CQ_CONFIG_FOLDER.getAbsolutePath().length();
		for (File folder : subfolders) {
			CopyHelper.copyFromJarOrWorkspace("/assets/cqrepoured/defaultConfigs" + folder.getAbsolutePath().substring(i), folder, false);
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(CQRMain.INSTANCE, new GuiHandler());
		CQRMaterials.setRepairItemsForMaterials();
		SmeltingHandler.init();
		FireBlock.init();

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();

		isPhosphorInstalled = CQRMain.class.getResource("").getProtocol().equals("jar") && Loader.isModLoaded("phosphor-lighting");
		isEntityCullingInstalled = Loader.isModLoaded("entity_culling");
		isCubicChunksInstalled = Loader.isModLoaded("cubicchunks");
		isAW2Installed = Loader.isModLoaded("ancientwarfare");

		DungeonRegistry.getInstance().loadDungeonFiles();
		CQStructure.checkAndUpdateStructureFiles();
		CQStructure.updateSpecialEntities();
		ProtectedRegionHelper.updateWhitelists();
		CQRDispenseBehaviors.registerDispenseBehaviors();
		EntityCQRNetherDragon.reloadBreakableBlocks();
		DungeonInhabitantManager.instance().loadDungeonInhabitants();
	}

	@EventHandler
	public static void onFMLServerStartingEvent(FMLServerAboutToStartEvent event) {
		// Since the CTS manager could also be corrupted, let's make him reload his data...
		TextureSetManager.loadTextureSetsFromFolder(CQ_CUSTOM_TEXTURES_FOLDER_SETS);

		FactionRegistry.getServerInstance().loadFactions();

		CQStructure.cacheFiles();
	}

	@EventHandler
	public static void onFMLServerStartingEvent(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandExport());
		event.registerServerCommand(new CommandChangeReputation());
		event.registerServerCommand(new CommandGetProtectedRegion());
		event.registerServerCommand(new CommandDeleteProtectedRegion());
		event.registerServerCommand(new CommandLocateDungeon());
		event.registerServerCommand(new CommandImport());
	}

	@EventHandler
	public static void onFMLServerStoppingEvent(FMLServerStoppingEvent event) {
		FactionRegistry.getServerInstance().saveAllReputationData(true);
		CQStructure.clearCache();
	}

}
