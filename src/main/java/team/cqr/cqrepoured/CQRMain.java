package team.cqr.cqrepoured;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import software.bernie.geckolib3.GeckoLib;
import team.cqr.cqrepoured.block.banner.BannerHelper;
import team.cqr.cqrepoured.client.CQRepouredClient;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.customtextures.TextureSetManager;
import team.cqr.cqrepoured.faction.FactionRegistry;
import team.cqr.cqrepoured.init.CQRBlockEntities;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRCapabilities;
import team.cqr.cqrepoured.init.CQRConfiguredStructures;
import team.cqr.cqrepoured.init.CQRContainerTypes;
import team.cqr.cqrepoured.init.CQREnchantments;
import team.cqr.cqrepoured.init.CQREntityTypes;
import team.cqr.cqrepoured.init.CQRItems;
import team.cqr.cqrepoured.init.CQRMessages;
import team.cqr.cqrepoured.init.CQRRecipeTypes;
import team.cqr.cqrepoured.init.CQRStructureProcessors;
import team.cqr.cqrepoured.init.CQRStructures;
import team.cqr.cqrepoured.mixinutil.PartEntityCache;
import team.cqr.cqrepoured.proxy.ClientProxy;
import team.cqr.cqrepoured.proxy.IProxy;
import team.cqr.cqrepoured.proxy.ServerProxy;
import team.cqr.cqrepoured.util.ConfigBackupHandler;
import team.cqr.cqrepoured.util.CopyHelper;

@Mod(CQRMain.MODID)
@EventBusSubscriber(modid = CQRMain.MODID, bus = Bus.MOD)
public class CQRMain {

	public static final String MODID = "cqrepoured";
	public static final String MODID_STRUCTURES = "cqrepoured_structures";
	
	public static final String VERSION = "2.6.3B";

	public static final IProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main-network-channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	// @SidedProxy(clientSide = "team.cqr.cqrepoured.proxy.ClientProxy", serverSide = "team.cqr.cqrepoured.proxy.ServerProxy")
	// public static IProxy proxy;

	public static Logger logger = LogManager.getLogger();

	public static File CQ_CONFIG_FOLDER = null;
	public static File CQ_DUNGEON_GRID_FOLDER = null;
	public static File CQ_DUNGEON_FOLDER = null;
	public static File CQ_STRUCTURE_FILES_FOLDER = null;
	public static File CQ_STRUCTURE_PROCESSOR_FOLDER = null;
	public static File CQ_EXPORT_FILES_FOLDER = null;
	public static File CQ_CHEST_FOLDER = null;
	public static File CQ_FACTION_FOLDER = null;
	public static File CQ_INHABITANT_FOLDER = null;
	public static File CQ_ITEM_FOLDER = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_SETS = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_ROOT = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES = null;
	public static File CQ_CUSTOM_TEXTURES_FOLDER_TEXTURES_SYNC = null;

	public static boolean isWorkspaceEnvironment = true;
	public static boolean isPhosphorInstalled;
	public static boolean isEntityCullingInstalled;
	public static boolean isCubicChunksInstalled;
	public static boolean isAW2Installed;

	public static final ItemGroup CQR_ITEMS_TAB = new ItemGroup(CQRMain.MODID + "_items") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(CQRItems.BOOTS_CLOUD.get());
		}

	};
	public static final ItemGroup CQR_BLOCKS_TAB = new ItemGroup(CQRMain.MODID + "_blocks") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(CQRBlocks.TABLE_OAK.get());
		}
	};
	public static final ItemGroup CQR_CREATIVE_TOOL_TAB = new ItemGroup(CQRMain.MODID + "_creative_tools") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(CQRBlocks.EXPORTER.get());
		}
	};
	public static final ItemGroup CQR_BANNERS_TAB = new ItemGroup(CQRMain.MODID + "_banners") {
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
	public static final ItemGroup CQR_DUNGEON_PLACER_TAB = new ItemGroup(CQRMain.MODID + "_dungeon_placers") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.APPLE);
			// return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(CQRMain.MODID, "dungeon_placer_d5")));
		}
	};
	public static final ItemGroup CQR_EXPORTER_CHEST_TAB = new ItemGroup(CQRMain.MODID + "_exporter_chests") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(CQRBlocks.EXPORTER_CHEST_VALUABLE.get());
		}
	};
	public static final ItemGroup CQR_SPAWN_EGG_TAB = new ItemGroup(CQRMain.MODID + "_spawn_eggs") {
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

		PartEntityCache.registerMultipartEvents(MinecraftForge.EVENT_BUS);
		
		CQRBlocks.registerBlocks();
		CQRItems.registerItems();
		CQRBlockEntities.registerBlockEntities();
		CQRContainerTypes.registerContainerTypes();
		CQREntityTypes.registerEntityTypes();
		CQRStructures.registerStructures();
		CQRStructureProcessors.registerStructureProcessors();
		CQREnchantments.registerEnchantments();
		CQRRecipeTypes.register(bus);
		
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
		CQ_CONFIG_FOLDER = new File(configFolderPath.toAbsolutePath().getFileName().toString(), "CQR");
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

		if (!CQ_CONFIG_FOLDER.exists() || CQRConfig.SERVER_CONFIG.general.reinstallDefaultConfigs.get()) {
			CopyHelper.copyFromJarOrWorkspace("/assets/cqrepoured/defaultConfigs", CQ_CONFIG_FOLDER, true);
		} else {
			ConfigBackupHandler.checkAndBackupConfigs();
		}

		int i = CQ_CONFIG_FOLDER.getAbsolutePath().length();
		for (File folder : subfolders) {
			CopyHelper.copyFromJarOrWorkspace("/assets/cqrepoured/defaultConfigs" + folder.getAbsolutePath().substring(i), folder, false);
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
		//DungeonInhabitantManager.instance().loadDungeonInhabitants();
		CQRCapabilities.registerCapabilities();
		CQRMessages.registerMessages();
		event.enqueueWork(() -> {
			CQRStructures.setupStructures();
			CQRConfiguredStructures.registerConfiguredStructures();
		});
	}

	public static final ResourceLocation prefix(final String path) {
		return new ResourceLocation(MODID, path);
	}
	
	public static final ResourceLocation prefixStructureTemplateId(final String path) {
		return new ResourceLocation(MODID_STRUCTURES, path);
	}

	public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T entry, String registryKey) {
		entry.setRegistryName(new ResourceLocation(CQRMain.MODID, registryKey));
		registry.register(entry);
		return entry;
	}

}
