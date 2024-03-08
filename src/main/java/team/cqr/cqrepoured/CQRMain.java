package team.cqr.cqrepoured;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mod.azure.azurelib.GeckoLib;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import team.cqr.cqrepoured.block.banner.BannerHelper;
import team.cqr.cqrepoured.client.CQRepouredClient;
import team.cqr.cqrepoured.common.CQRConstants;
import team.cqr.cqrepoured.common.CQRepoured;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.TextureSetManager;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.init.CQREnchantments;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRMessages;
import team.cqr.cqrepoured.init.CQRParticleTypes;
import team.cqr.cqrepoured.init.CQRRecipeTypes;
import team.cqr.cqrepoured.init.CQRSensors;
import team.cqr.cqrepoured.init.CQRStructureProcessors;
import team.cqr.cqrepoured.init.CQRStructures;
import team.cqr.cqrepoured.proxy.ClientProxy;
import team.cqr.cqrepoured.proxy.IProxy;
import team.cqr.cqrepoured.proxy.ServerProxy;
import team.cqr.cqrepoured.util.ConfigBackupHandler;
import team.cqr.cqrepoured.util.CopyHelper;
import team.cqr.cqrepoured.world.structure.debug.TestStructures;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.inhabitants.DungeonInhabitantManager;

//@Mod(CQRConstants.MODID)
@EventBusSubscriber(modid = CQRConstants.MODID, bus = Bus.MOD)
public class CQRMain {

	public static final String MODID_STRUCTURES = "cqrepoured_structures";
	
	public static final String VERSION = "3.0.0B";

	public static final IProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

	// @SidedProxy(clientSide = "team.cqr.cqrepoured.proxy.ClientProxy", serverSide = "team.cqr.cqrepoured.proxy.ServerProxy")
	// public static IProxy proxy;

	/**
	 * Use {@link CQRepoured#LOGGER} instead.
	 */
	@Deprecated
	public static Logger logger = LogManager.getLogger();

	public static File CQ_CONFIG_FOLDER = null;
	public static File CQ_DUNGEON_GRID_FOLDER = null;
	public static File CQ_DUNGEON_FOLDER = null;
	public static File CQ_STRUCTURE_FILES_FOLDER = null;
	public static File CQ_STRUCTURE_PROCESSOR_FOLDER = null;
	public static File CQ_MIGRATED_STRUCTURE_FILES_FOLDER = null;
	public static File CQ_EXPORT_FILES_FOLDER = null;
	public static File CQ_CHEST_FOLDER = null;
	public static File CQ_FACTION_FOLDER = null;
	public static File CQ_INHABITANT_FOLDER = null;
	public static File CQ_ITEM_FOLDER = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_SETS = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_ROOT = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES_SYNC = null;

	/**
	 * Use {@link CQRepoured#isWorkspaceEnvironment} instead.
	 */
	@Deprecated
	public static boolean isWorkspaceEnvironment = true;
	public static boolean isPhosphorInstalled;
	public static boolean isEntityCullingInstalled;
	public static boolean isCubicChunksInstalled;
	public static boolean isAW2Installed;

	public static final CreativeModeTab CQR_ITEMS_TAB = new CreativeModeTab(CQRConstants.MODID + "_items") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(CQRItems.BOOTS_CLOUD.get());
		}

	};
	public static final CreativeModeTab CQR_BLOCKS_TAB = new CreativeModeTab(CQRConstants.MODID + "_blocks") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(CQRBlocks.TABLE_OAK.get());
		}
	};
	public static final CreativeModeTab CQR_CREATIVE_TOOL_TAB = new CreativeModeTab(CQRConstants.MODID + "_creative_tools") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(CQRBlocks.EXPORTER.get());
		}
	};
	public static final CreativeModeTab CQR_BANNERS_TAB = new CreativeModeTab(CQRConstants.MODID + "_banners") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.SKULL_BANNER_PATTERN);
		}

		@Override
		public void fillItemList(NonNullList<ItemStack> itemList) {
			super.fillItemList(itemList);

			List<ItemStack> banners = BannerHelper.addBannersToTabs();
			for (ItemStack stack : banners) {
				itemList.add(stack);
			}

		}
	};
	public static final CreativeModeTab CQR_DUNGEON_PLACER_TAB = new CreativeModeTab(CQRConstants.MODID + "_dungeon_placers") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.APPLE);
			// return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(CQRConstants.MODID, "dungeon_placer_d5")));
		}
	};
	public static final CreativeModeTab CQR_EXPORTER_CHEST_TAB = new CreativeModeTab(CQRConstants.MODID + "_exporter_chests") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(CQRBlocks.EXPORTER_CHEST_VALUABLE.get());
		}
	};
	public static final CreativeModeTab CQR_SPAWN_EGG_TAB = new CreativeModeTab(CQRConstants.MODID + "_spawn_eggs") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.PILLAGER_SPAWN_EGG);
		}
	};

	// public static final WorldDungeonGenerator DUNGEON_GENERATOR = new WorldDungeonGenerator();
	// public static final WorldWallGenerator WALL_GENERATOR = new WorldWallGenerator();

	public CQRMain() {
		isWorkspaceEnvironment = !CQRMain.class.getResource("").getProtocol().equals("jar");

		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CQRConfig.SERVER_SPEC);
		initConfigFolder(FMLPaths.CONFIGDIR.get());
		
		GeckoLib.initialize();
		
		CQRBlocks.registerBlocks();
		CQREntityTypes.registerEntityTypes();
		CQRItems.registerItems();
		CQRBlockEntities.registerBlockEntities();
		CQRContainerTypes.registerContainerTypes();
		CQRSensors.SENSORS.register(bus);
		CQRStructures.registerStructures();
		CQRStructureProcessors.registerStructureProcessors();
		CQREnchantments.registerEnchantments();
		CQRRecipeTypes.register(bus);
		CQRParticleTypes.PARTICLE_TYPES.register(bus);
		TestStructures.registerTestStructures();
		//CQRLoottables.registerLootTables();
		
		//TODO: Change so the actual values of the files get loaded LATER
		//DungeonRegistry.getInstance().loadDungeonFiles();

		MinecraftForge.EVENT_BUS.register(this);
		bus.<FMLCommonSetupEvent>addListener(this::init);
		bus.addListener(CQRepouredClient::setupClient);
		
	}

	/*
	 * @SubscribeEvent public void preInit(FMLPreInitializationEvent event) { // The geckolib comment says this should be in the constructor but that only applies to MC 1.16+ GeckoLib.initialize();
	 * 
	 * logger = event.getModLog(); isWorkspaceEnvironment = !CQRMain.class.getResource("").getProtocol().equals("jar"); // Important: This has to be the F I R S T statement this.initConfigFolder(event);
	 * 
	 * proxy.preInit();
	 * 
	 * // !!Custom texture system has to load BEFORE the faction system!! // Custom Textures System => Moved to server start // TextureSetManager.loadTextureSetsFromFolder(CQ_CUSTOM_TEXTURES_FOLDER_SETS);
	 * 
	 * // Faction system => Moved to EventHandler (FMLServerStartingEvent) // FactionRegistry.instance().loadFactions();
	 * 
	 * // Enables Dungeon generation in worlds, do not change the number (!) and do NOT // remove this line, moving it somewhere else is fine, but it must be called in // pre initialization (!) GameRegistry.registerWorldGenerator(DUNGEON_GENERATOR,
	 * 100); GameRegistry.registerWorldGenerator(WALL_GENERATOR, 101);
	 * 
	 * // Instantiating enums EBannerPatternsCQ.values(); EBanners.values(); CreatureAttribute.values(); // DO NOT REMOVE, this fixes the neat issue CQRCreatureAttributes.VOID.name();
	 * 
	 * CQRMessages.registerMessages(); CQRCapabilities.registerCapabilities(); CQRLoottables.registerLootTables(); }
	 */
	private void initConfigFolder(final Path configFolderPath) {
		CQ_CONFIG_FOLDER = new File(configFolderPath.toFile(), "CQR");
		File[] subfolders = new File[] {
				CQ_DUNGEON_FOLDER = new File(CQ_CONFIG_FOLDER, "dungeons"),
				CQ_CHEST_FOLDER = new File(CQ_CONFIG_FOLDER, "lootconfigs"),
				CQ_STRUCTURE_FILES_FOLDER = new File(CQ_CONFIG_FOLDER, "structures"),
				CQ_STRUCTURE_PROCESSOR_FOLDER = new File(CQ_CONFIG_FOLDER, "structure_processors"),
				CQ_EXPORT_FILES_FOLDER = new File(CQ_CONFIG_FOLDER, "exporter_output"),
				CQ_FACTION_FOLDER = new File(CQ_CONFIG_FOLDER, "factions"),
				CQ_INHABITANT_FOLDER = new File(CQ_CONFIG_FOLDER, "dungeon_inhabitants"),
				CQ_ITEM_FOLDER = new File(CQ_CONFIG_FOLDER, "items"),
				CQ_CUSTOM_TEXTURES_FOLDER_ROOT = new File(CQ_CONFIG_FOLDER, "textures"),
				CQ_CUSTOM_TEXTURES_FOLDER_SETS = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "texture_sets"),
				CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "textures"),
				CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES_SYNC = new File(CQ_CUSTOM_TEXTURES_FOLDER_ROOT, "sync"),
				CQ_DUNGEON_GRID_FOLDER = new File(CQ_CONFIG_FOLDER, "grids") 
		};

		ConfigBackupHandler.registerConfig(CQ_DUNGEON_FOLDER.getName(), "2.0.0");
		ConfigBackupHandler.registerConfig(CQ_DUNGEON_GRID_FOLDER.getName(), "2.0.0");
		ConfigBackupHandler.registerConfig(CQ_CHEST_FOLDER.getName(), "2.0.0");
		ConfigBackupHandler.registerConfig(CQ_STRUCTURE_FILES_FOLDER.getName(), "2.0.0");
		ConfigBackupHandler.registerConfig(CQ_STRUCTURE_PROCESSOR_FOLDER.getName(), "2.0.0");
		ConfigBackupHandler.registerConfig(CQ_FACTION_FOLDER.getName(), "2.0.0");
		ConfigBackupHandler.registerConfig(CQ_INHABITANT_FOLDER.getName(), "2.0.0");
		ConfigBackupHandler.registerConfig(CQ_ITEM_FOLDER.getName(), "2.0.0");
		ConfigBackupHandler.registerConfig(CQ_CUSTOM_TEXTURES_FOLDER_ROOT.getName(), "2.0.0");

		CQ_MIGRATED_STRUCTURE_FILES_FOLDER = new File(CQ_CONFIG_FOLDER, "_migrated_structures");

		if (!CQ_CONFIG_FOLDER.exists() || CQRConfig.SERVER_CONFIG.general.reinstallDefaultConfigs.get()) {
			CopyHelper.copyFromJarOrWorkspace("/data/cqrepoured/default_config", CQ_CONFIG_FOLDER.toPath(), true);
		} else {
			ConfigBackupHandler.checkAndBackupConfigs();
		}

		int i = CQ_CONFIG_FOLDER.getAbsolutePath().length();
		for (File folder : subfolders) {
			CopyHelper.copyFromJarOrWorkspace("/data/cqrepoured/default_config" + folder.getAbsolutePath().substring(i), folder.toPath(), false);
		}
	}
	/*
	 * @SubscribeEvent public void init(FMLInitializationEvent event) { proxy.init();
	 * 
	 * NetworkRegistry.INSTANCE.registerGuiHandler(CQRMain.INSTANCE, new GuiHandler()); CQRMaterials.setRepairItemsForMaterials(); SmeltingHandler.init(); FireBlock.init();
	 * 
	 * }
	 * 
	 * @SuppressWarnings("deprecation")
	 * 
	 * @SubscribeEvent public void postInit(FMLPostInitializationEvent event) { proxy.postInit();
	 * 
	 * isPhosphorInstalled = CQRMain.class.getResource("").getProtocol().equals("jar") && Loader.isModLoaded("phosphor-lighting"); isEntityCullingInstalled = ModList.get().isLoaded("entity_culling"); isCubicChunksInstalled =
	 * ModList.get().isLoaded("cubicchunks"); isAW2Installed = ModList.get().isLoaded("ancientwarfare");
	 * 
	 * DungeonRegistry.getInstance().loadDungeonFiles(); CQStructure.checkAndUpdateStructureFiles(); CQStructure.updateSpecialEntities(); ProtectedRegionHelper.updateWhitelists(); CQRDispenseBehaviors.registerDispenseBehaviors();
	 * EntityCQRNetherDragon.reloadBreakableBlocks(); DungeonInhabitantManager.instance().loadDungeonInhabitants(); }
	 *
	 */
	 @SubscribeEvent 
	 public void onFMLServerStartingEvent(FMLServerAboutToStartEvent event) { 
		 PROXY.postInit();
		 // Since the CTS manager could also be corrupted, let's make him reload his data...
		 TextureSetManager.loadTextureSetsFromFolder(CQ_CUSTOM_TEXTURES_FOLDER_SETS);
		 FactionRegistry.getServerInstance().loadFactions();
		 //CQStructure.cacheFiles(); 
	 }
	 /* 
	 * @SubscribeEvent public static void onFMLServerStartingEvent(FMLServerStartingEvent event) { event.registerServerCommand(new CommandExport()); event.registerServerCommand(new CommandChangeReputation()); event.registerServerCommand(new
	 * CommandGetProtectedRegion()); event.registerServerCommand(new CommandDeleteProtectedRegion()); event.registerServerCommand(new CommandLocateDungeon()); event.registerServerCommand(new CommandImport()); }
	 * 
	 * @SubscribeEvent public static void onFMLServerStoppingEvent(FMLServerStoppingEvent event) { FactionRegistry.getServerInstance().saveAllReputationData(true, event.getServer().overworld()); CQStructure.clearCache(); }
	 */

	public void init(final FMLCommonSetupEvent event) {
		DungeonRegistry.getInstance().loadDungeonFiles();
		DungeonInhabitantManager.instance().loadDungeonInhabitants();
		CQRCapabilities.registerCapabilities();
		CQRMessages.registerMessages();
		event.enqueueWork(() -> {
			CQRStructures.setupStructures();
			TestStructures.loadTestStructures();
		});
		
	}

	public static final ResourceLocation prefix(final String path) {
		return new ResourceLocation(CQRConstants.MODID, path);
	}
	
	public static final ResourceLocation prefixStructureTemplateId(final String path) {
		return new ResourceLocation(MODID_STRUCTURES, path);
	}
	
	static final String ANIMATION_SUFFIX = ".animation.json";
	public static final ResourceLocation prefixAnimation(final String path) {
		String pathToUse = path;
		if(!path.endsWith(ANIMATION_SUFFIX)) {
			pathToUse = path + ANIMATION_SUFFIX;
		}
		return new ResourceLocation(CQRConstants.MODID, "animations/" + pathToUse);
	}
	
	public static final ResourceLocation prefixEntityAnimation(final String path) {
		return prefixAnimation("entity/" + path);
	}
	
	public static final ResourceLocation prefixBlockAnimation(final String path) {
		return prefixAnimation("block/" + path);
	}
	
	public static ResourceLocation prefixArmorAnimation(final String path) {
		return prefixAnimation("armor/" + path);
	}

	public static ResourceLocation prefixAssesEnforcementManager(String string) {
		return prefix("asset_manager/" + string);
	}

	public static ResourceLocation prefixAssetFinder(String string) {
		return prefix("asset_finder/" + string);
	}

}
