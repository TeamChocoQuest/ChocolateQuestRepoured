package team.cqr.cqrepoured.init;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
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
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.StructureDungeonCQR;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.thewall.WallStructure;

@EventBusSubscriber
public class CQRStructures {
	
	public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, CQRMain.MODID);
	
	public static RegistryObject<Structure<NoFeatureConfig>> WALL_IN_THE_NORTH = DEFERRED_REGISTRY_STRUCTURE.register("wall_in_the_north", () -> (new WallStructure(NoFeatureConfig.CODEC)));
	
	protected static final Map<DungeonBase, RegistryObject<Structure<?>>> DUNGEON_ENTRIES = new HashMap<>(); 
	public static final Map<DungeonBase, StructureFeature<?,?>> DUNGEON_CONFIGURED_ENTRIES = new HashMap<>();
	
	public static void setupStructures() {
		setupMapSpacingAndLand(WALL_IN_THE_NORTH.get(), new StructureSeparationSettings(1, 0, 1237654789), false);
		
		//Now, load all dungeon configs and setup the spacing for them
		for(DungeonBase dunConf : DungeonRegistry.getInstance().getDungeons()) {
			Structure<?> structure = new StructureDungeonCQR(null, false, dunConf);
			RegistryObject<Structure<?>> regObj = DEFERRED_REGISTRY_STRUCTURE.register("dungeon_" + dunConf.getDungeonName(), () -> (structure));
			DUNGEON_ENTRIES.put(dunConf, regObj);
			StructureSeparationSettings sepSettings;
			if(dunConf.isUseVanillaSpreadSystem()) {
				sepSettings = new StructureSeparationSettings(dunConf.getVanillaSpreadSpacing(), dunConf.getVanillaSpreadSeparation(), dunConf.getVanillaSpreadSeed());
			} else {
				sepSettings = new StructureSeparationSettings(1, 0, 123456789);
			}
			setupMapSpacingAndLand(regObj.get(), sepSettings, dunConf.doBuildSupportPlatform());
		}
    }

    static void registerStructurePiece(IStructurePieceType structurePiece, ResourceLocation rl) {
        Registry.register(Registry.STRUCTURE_PIECE, rl, structurePiece);
    }


	public static void registerStructures() {
		DEFERRED_REGISTRY_STRUCTURE.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand)
    {
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if(transformSurroundingLand){
            Structure.NOISE_AFFECTING_FEATURES =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.NOISE_AFFECTING_FEATURES)
                            .add(structure)
                            .build();
        }

        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();


        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

            /*
             * Pre-caution in case a mod makes the structure map immutable like datapacks do.
             */
            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().structureSettings().structureConfig = tempMap;
            }
            else{
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }
	
	protected static IStructurePieceType register(IStructurePieceType type, String id) {
		return IStructurePieceType.setPieceId(type, CQRMain.MODID + ":" + id );
	}
	
	@SubscribeEvent
	public static void biomeModification(final BiomeLoadingEvent event) {
		RegistryKey<Biome> key = RegistryKey.create(Registry.BIOME_REGISTRY, event.getName());

		if (BiomeDictionary.hasType(key, BiomeDictionary.Type.NETHER) || BiomeDictionary.hasType(key, BiomeDictionary.Type.END)) {

		} else if (BiomeDictionary.hasType(key, BiomeDictionary.Type.OVERWORLD)) {
			event.getGeneration().getStructures().add(() -> CQRConfiguredStructures.CONFIGURED_WALL_IN_THE_NORTH);
		}
		
		//Now, parse all configured dungeon structures and add them too
		boolean skip = false;
		for(Map.Entry<DungeonBase, StructureFeature<?,?>> entry : DUNGEON_CONFIGURED_ENTRIES.entrySet()) {
			for(ResourceLocation rs : entry.getKey().getDisallowedBiomes()) {
				if(rs.equals(key.getRegistryName())) {
					skip = true;
					break;
				}
			}
			if(skip) {
				skip = false;
				continue;
			}
			for(ResourceLocation rs : entry.getKey().getAllowedBiomes()) {
				if(rs.equals(key.getRegistryName())) {
					event.getGeneration().getStructures().add(entry::getValue);
				}
			}
		}
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
			boolean done = false;
			for(Map.Entry<DungeonBase, RegistryObject<Structure<?>>> entry : DUNGEON_ENTRIES.entrySet()) {
				for(ResourceLocation rs : entry.getKey().getAllowedDims()) {
					if(rs.equals(serverWorld.dimension().getRegistryName())) {
						if(entry.getKey().isAllowedDimsAsBlacklist()) {
							done = true;
							break;
						} else {
							tempMap.putIfAbsent(entry.getValue().get(), DimensionStructuresSettings.DEFAULTS.get(entry.getValue().get()));
							done = true;
							break;
						}
					}
				}
				if(done) {
					break;
				}
			}
			
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}
	
}
