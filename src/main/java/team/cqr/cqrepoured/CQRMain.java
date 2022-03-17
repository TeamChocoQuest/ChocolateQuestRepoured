package team.cqr.cqrepoured;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import software.bernie.geckolib3.GeckoLib;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQRBlocks;

@Mod(CQRMain.MODID)
public class CQRMain {

	public static final String MODID = "cqrepoured";
	public static final String VERSION = "2.6.3B";

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main-network-channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	//@SidedProxy(clientSide = "team.cqr.cqrepoured.proxy.ClientProxy", serverSide = "team.cqr.cqrepoured.proxy.ServerProxy")
	//public static IProxy proxy;

	public static Logger logger = LogManager.getLogger();

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
		public ItemStack makeIcon() {
			return new ItemStack(Items.APPLE);
			//return new ItemStack(CQRItems.BOOTS_CLOUD);
		}

	};
	public static final ItemGroup CQR_BLOCKS_TAB = new ItemGroup(CQRMain.MODID + "_blocks") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.APPLE);
			//return new ItemStack(CQRBlocks.TABLE_OAK);
		}
	};
	public static final ItemGroup CQR_CREATIVE_TOOL_TAB = new ItemGroup(CQRMain.MODID + "_creative_tools") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.APPLE);
			//return new ItemStack(CQRBlocks.EXPORTER);
		}
	};
	public static final ItemGroup CQR_BANNERS_TAB = new ItemGroup(CQRMain.MODID + "_banners") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.APPLE);
			//return EBanners.WALKER_ORDO.getBanner();
		}
		
		@Override
		public void fillItemList(NonNullList<ItemStack> itemList) {
			super.fillItemList(itemList);
			/*
			List<ItemStack> banners = BannerHelper.addBannersToTabs();
			for (ItemStack stack : banners) {
				itemList.add(stack);
			}
			*/
		}
	};
	public static final ItemGroup CQR_DUNGEON_PLACER_TAB = new ItemGroup(CQRMain.MODID + "_dungeon_placers") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.APPLE);
			//return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(CQRMain.MODID, "dungeon_placer_d5")));
		}
	};
	public static final ItemGroup CQR_EXPORTER_CHEST_TAB = new ItemGroup(CQRMain.MODID + "_exporter_chests") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.APPLE);
			//return new ItemStack(CQRBlocks.EXPORTER_CHEST_VALUABLE);
		}
	};
	public static final ItemGroup CQR_SPAWN_EGG_TAB = new ItemGroup(CQRMain.MODID + "_spawn_eggs") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.PILLAGER_SPAWN_EGG);
		}
	};

	//public static final WorldDungeonGenerator DUNGEON_GENERATOR = new WorldDungeonGenerator();
	//public static final WorldWallGenerator WALL_GENERATOR = new WorldWallGenerator();

	public CQRMain() {
		isWorkspaceEnvironment = !CQRMain.class.getResource("").getProtocol().equals("jar");

		GeckoLib.initialize();

		CQRBlocks.registerBlocks();
		CQRBlockEntities.registerBlockEntities();
	}

	/*
	@SubscribeEvent
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
		// DO NOT REMOVE, this fixes the neat issue
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

	@SubscribeEvent
	public void init(FMLInitializationEvent event) {
		proxy.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(CQRMain.INSTANCE, new GuiHandler());
		CQRMaterials.setRepairItemsForMaterials();
		SmeltingHandler.init();
		FireBlock.init();

	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();

		isPhosphorInstalled = CQRMain.class.getResource("").getProtocol().equals("jar") && Loader.isModLoaded("phosphor-lighting");
		isEntityCullingInstalled = ModList.get().isLoaded("entity_culling");
		isCubicChunksInstalled = ModList.get().isLoaded("cubicchunks");
		isAW2Installed = ModList.get().isLoaded("ancientwarfare");

		DungeonRegistry.getInstance().loadDungeonFiles();
		CQStructure.checkAndUpdateStructureFiles();
		CQStructure.updateSpecialEntities();
		ProtectedRegionHelper.updateWhitelists();
		CQRDispenseBehaviors.registerDispenseBehaviors();
		EntityCQRNetherDragon.reloadBreakableBlocks();
		DungeonInhabitantManager.instance().loadDungeonInhabitants();
	}

	@SubscribeEvent
	public static void onFMLServerStartingEvent(FMLServerAboutToStartEvent event) {
		// Since the CTS manager could also be corrupted, let's make him reload his data...
		TextureSetManager.loadTextureSetsFromFolder(CQ_CUSTOM_TEXTURES_FOLDER_SETS);

		FactionRegistry.getServerInstance().loadFactions();

		CQStructure.cacheFiles();
	}

	@SubscribeEvent
	public static void onFMLServerStartingEvent(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandExport());
		event.registerServerCommand(new CommandChangeReputation());
		event.registerServerCommand(new CommandGetProtectedRegion());
		event.registerServerCommand(new CommandDeleteProtectedRegion());
		event.registerServerCommand(new CommandLocateDungeon());
		event.registerServerCommand(new CommandImport());
	}

	@SubscribeEvent
	public static void onFMLServerStoppingEvent(FMLServerStoppingEvent event) {
		FactionRegistry.getServerInstance().saveAllReputationData(true, event.getServer().overworld());
		CQStructure.clearCache();
	}
	*/
	
	public static final ResourceLocation prefix(final String path) {
		return new ResourceLocation(MODID, path);
	}

}
