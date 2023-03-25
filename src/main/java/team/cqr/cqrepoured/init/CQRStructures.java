package team.cqr.cqrepoured.init;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.StructureGlobalCQR;
import team.cqr.cqrepoured.world.structure.StructureGridCQR;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.generation.INoiseAffectingStructurePiece;
import team.cqr.cqrepoured.world.structure.generation.grid.DungeonGrid;
import team.cqr.cqrepoured.world.structure.generation.grid.GridRegistry;
import team.cqr.cqrepoured.world.structure.generation.thewall.WallStructure;

@EventBusSubscriber
public class CQRStructures {

	public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, CQRMain.MODID);

	public static RegistryObject<Structure<NoFeatureConfig>> WALL_IN_THE_NORTH = DEFERRED_REGISTRY_STRUCTURE.register("wall_in_the_north", () -> (new WallStructure(NoFeatureConfig.CODEC)));
	//public static RegistryObject<Structure<NoFeatureConfig>> CQR_ANY_DUNGEON = DEFERRED_REGISTRY_STRUCTURE.register("cqr_dungeon", () -> (new StructureGlobalCQR(NoFeatureConfig.CODEC)));

	// protected static final Map<DungeonBase, Structure<?>> DUNGEON_ENTRIES = new HashMap<>();
	// public static final Map<DungeonBase, StructureFeature<?, ?>> DUNGEON_CONFIGURED_ENTRIES = new HashMap<>();
	// protected static final ConcurrentLinkedDeque<Triple<DungeonBase, Structure<?>, StructureSeparationSettings>> SEP_SETTINGS_QUEUE = new ConcurrentLinkedDeque<Triple<DungeonBase, Structure<?>, StructureSeparationSettings>>();

	protected static final Map<DungeonGrid, Structure<?>> GRID_ENTRIES = new HashMap<>();
	public static final Map<DungeonGrid, StructureFeature<?, ?>> GRID_CONFIGURED_ENTRIES = new HashMap<>();
	protected static final ConcurrentLinkedDeque<Triple<DungeonGrid, Structure<?>, StructureSeparationSettings>> SEP_SETTINGS_QUEUE_GRID = new ConcurrentLinkedDeque<Triple<DungeonGrid, Structure<?>, StructureSeparationSettings>>();

	public static void setupStructures() {
		setupMapSpacingAndLand(WALL_IN_THE_NORTH.get(), new StructureSeparationSettings(1, 0, 1237654789), false);
		final int size = GridRegistry.getInstance().getGrids().size();
		int[] valsDist = new int[size];
		int[] valsSpread = new int[size];
		int i = 0;
		for (DungeonGrid grid : GridRegistry.getInstance().getGrids()) {
			if (i < size) {
				valsDist[i] = grid.getDistance();
				valsSpread[i] = grid.getSpread();

				i++;
			}
		}
		int dist = gcd(valsDist);
		int spread = gcd(valsSpread);
		int minDistInChunks = dist;
		int maxDistInChunks = dist + spread;

		//setupMapSpacingAndLand(CQR_ANY_DUNGEON.get(), new StructureSeparationSettings(Math.max(maxDistInChunks, minDistInChunks), Math.min(maxDistInChunks, minDistInChunks), 446854348), true);

		/*
		 * try { while (!SEP_SETTINGS_QUEUE.isEmpty()) { Triple<DungeonBase, Structure<?>, StructureSeparationSettings> entry = SEP_SETTINGS_QUEUE.poll(); if (entry != null) { setupMapSpacingAndLand(entry.getMiddle(), entry.getRight(),
		 * entry.getLeft().doBuildSupportPlatform()); } } } catch (Exception ex) { // Yes, this is necessary. Without it the error is suppressed! ex.printStackTrace(); }
		 */

		try {
			while (!SEP_SETTINGS_QUEUE_GRID.isEmpty()) {
				Triple<DungeonGrid, Structure<?>, StructureSeparationSettings> entry = SEP_SETTINGS_QUEUE_GRID.poll();
				if (entry != null) {
					setupMapSpacingAndLand(entry.getMiddle(), entry.getRight(), true);
				}
			}
		} catch (Exception ex) { // Yes, this is necessary. Without it the error is suppressed!
			ex.printStackTrace();
		}

		// Registry.STRUCTURE_FEATURE.forEach((s) -> System.out.println(s.getRegistryName().toString()));
	}

	static void registerStructurePiece(IStructurePieceType structurePiece, ResourceLocation rl) {
		Registry.register(Registry.STRUCTURE_PIECE, rl, structurePiece);
	}

	public static void registerStructures() {
		DEFERRED_REGISTRY_STRUCTURE.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings, boolean transformSurroundingLand) {
		Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

		if (transformSurroundingLand) {
			// Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure).build();
			INoiseAffectingStructurePiece.NOISE_AFFECTING_STRUCTURES.add(structure);
		}

		DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.DEFAULTS).put(structure, structureSeparationSettings).build();

		WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
			Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

			/*
			 * Pre-caution in case a mod makes the structure map immutable like datapacks do.
			 */
			if (structureMap instanceof ImmutableMap) {
				Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
				tempMap.put(structure, structureSeparationSettings);
				settings.getValue().structureSettings().structureConfig = tempMap;
			} else {
				structureMap.put(structure, structureSeparationSettings);
			}
		});
	}

	protected static IStructurePieceType register(IStructurePieceType type, String id) {
		return IStructurePieceType.setPieceId(type, CQRMain.MODID + ":" + id);
	}

	@SubscribeEvent
	public static void biomeModification(final BiomeLoadingEvent event) {
		RegistryKey<Biome> key = RegistryKey.create(Registry.BIOME_REGISTRY, event.getName());

		if (BiomeDictionary.hasType(key, BiomeDictionary.Type.NETHER) || BiomeDictionary.hasType(key, BiomeDictionary.Type.END)) {

		} else if (BiomeDictionary.hasType(key, BiomeDictionary.Type.OVERWORLD)) {
			event.getGeneration().getStructures().add(() -> CQRConfiguredStructures.CONFIGURED_WALL_IN_THE_NORTH);
		}
		//event.getGeneration().getStructures().add(() -> CQRConfiguredStructures.CONFIGURED_CQR_ANY_DUNGEON);

		// Now, parse all configured dungeon structures and add them too
		ResourceLocation biomeID = key.location(); // registryname is the registryname of the RegistryKey, here that is "minecraft/worldgen" or something like that
		/*
		 * for (Map.Entry<DungeonBase, StructureFeature<?, ?>> entry : DUNGEON_CONFIGURED_ENTRIES.entrySet()) { if(entry.getKey().isValidBiome(biomeID)) { event.getGeneration().getStructures().add(() -> entry.getValue());
		 * System.out.println("Added dungeon <" + entry.getKey().getName() + "> to biome " + biomeID.toString()); continue; } }
		 */

		for (Map.Entry<DungeonGrid, StructureFeature<?, ?>> entry : GRID_CONFIGURED_ENTRIES.entrySet()) {
			if (entry.getKey().isValidBiome(biomeID)) {
				event.getGeneration().getStructures().add(() -> entry.getValue());
				//System.out.println("Added dungeon <" + entry.getKey().getName() + "> to biome " + biomeID.toString());
				continue;
			}
		}

	}

	static int gcd(int a, int b) {
		while (b > 0) {
			int c = a % b;
			a = b;
			b = c;
		}
		return a;
	}

	static int gcd(int[] values) {
		int result = -1;
		if (values.length == 1) {
			result = values[0];
		} else if (values.length > 1) {
			result = values[0];
			for (int i = 1; i < values.length; i++) {
				result = gcd(result, values[i]);
			}
		}
		return result;
	}

	@SubscribeEvent
	public static void addDimensionalSpacing(final WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld) event.getWorld();

			// Prevent spawning our structure in Vanilla's superflat world as
			// people seem to want their superflat worlds free of modded structures.
			// Also that vanilla superflat is really tricky and buggy to work with in my experience.
			if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD)) {
				return;
			}

			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
			if (serverWorld.dimension().equals(World.OVERWORLD)) {
				tempMap.putIfAbsent(CQRStructures.WALL_IN_THE_NORTH.get(), DimensionStructuresSettings.DEFAULTS.get(CQRStructures.WALL_IN_THE_NORTH.get()));
			}
			//tempMap.putIfAbsent(CQRStructures.CQR_ANY_DUNGEON.get(), DimensionStructuresSettings.DEFAULTS.get(CQRStructures.CQR_ANY_DUNGEON.get()));
			boolean done = false;
			/*
			 * for (Map.Entry<DungeonBase, Structure<?>> entry : DUNGEON_ENTRIES.entrySet()) { for (ResourceLocation rs : entry.getKey().getAllowedDims()) { if (rs.equals(serverWorld.dimension().getRegistryName())) { if
			 * (entry.getKey().isAllowedDimsAsBlacklist()) { done = true; break; } else { tempMap.putIfAbsent(entry.getValue(), DimensionStructuresSettings.DEFAULTS.get(entry.getValue())); done = true; break; } } } if (done) { break; } }
			 */

			for (Map.Entry<DungeonGrid, Structure<?>> entry : GRID_ENTRIES.entrySet()) {
				for (ResourceLocation rs : entry.getKey().collectAllowedDims()) {
					if (rs.equals(serverWorld.dimension().getRegistryName())) {
						tempMap.putIfAbsent(entry.getValue(), DimensionStructuresSettings.DEFAULTS.get(entry.getValue()));
						done = true;
						break;
					}
				}
				if (done) {
					break;
				}
			}

			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CQRMain.MODID)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onStructureRegistration(final RegistryEvent.Register<Structure<?>> event) {
			DungeonRegistry.getInstance().loadDungeonFiles();
			// Now, load all dungeon configs and setup the spacing for them
			/*
			 * for (DungeonBase dunConf : DungeonRegistry.getInstance().getDungeons()) { try { // TODO: Create codec, it MUST NOT BE NULL!! Structure<?> structure = new StructureDungeonCQR(DungeonBase.CODEC, false); //
			 * event.getRegistry().register(structure); // RegistryObject<Structure<?>> regObj = DEFERRED_REGISTRY_STRUCTURE.register("dungeon_" + dunConf.getDungeonName(), () -> (structure));
			 * event.getRegistry().register(structure.setRegistryName(CQRMain.prefix("dungeon_" + dunConf.getDungeonName()))); DUNGEON_ENTRIES.put(dunConf, structure); StructureSeparationSettings sepSettings; if (dunConf.isUseVanillaSpreadSystem()) {
			 * sepSettings = new StructureSeparationSettings(dunConf.getVanillaSpreadSpacing(), dunConf.getVanillaSpreadSeparation(), dunConf.getVanillaSpreadSeed()); } else { sepSettings = new StructureSeparationSettings(1, 0, 123456789); }
			 * SEP_SETTINGS_QUEUE.add(Triple.of(dunConf, structure, sepSettings)); } catch (Exception ex) { ex.printStackTrace(); } }
			 */

			for (DungeonGrid grid : GridRegistry.getInstance().getGrids()) {
				try {
					Structure<?> structure = new StructureGridCQR<>(DungeonGrid.CODEC, false);
					event.getRegistry().register(structure.setRegistryName(CQRMain.prefix("dungeon_" + grid.getName())));
					GRID_ENTRIES.put(grid, structure);
					StructureSeparationSettings sepSettings;
					int maxDistInChunks = grid.getDistance() + grid.getSpread();
					int minDistInChunks = grid.getDistance();
					if(maxDistInChunks == minDistInChunks) {
						minDistInChunks--;
					}
					sepSettings = new StructureSeparationSettings(Math.max(maxDistInChunks, minDistInChunks), Math.min(maxDistInChunks, minDistInChunks), grid.getSeed());
					SEP_SETTINGS_QUEUE_GRID.add(Triple.of(grid, structure, sepSettings));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}
	}

}
