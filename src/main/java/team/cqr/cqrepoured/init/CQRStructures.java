package team.cqr.cqrepoured.init;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.StructureCQR;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generation.INoiseAffectingStructurePiece;
import team.cqr.cqrepoured.world.structure.generation.thewall.WallStructure;

@EventBusSubscriber(modid = CQRMain.MODID)
public class CQRStructures {

	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, CQRMain.MODID);

	public static final RegistryObject<Structure<NoFeatureConfig>> WALL_IN_THE_NORTH = STRUCTURES.register("wall_in_the_north", WallStructure::new);
	public static final RegistryObject<Structure<NoFeatureConfig>> CQR_ANY_DUNGEON = STRUCTURES.register("cqr_dungeon", StructureCQR::new);

	public static final IStructurePieceType GENERATABLE_DUNGEON = registerStructurePiece(GeneratableDungeon::new, "generatable_dungeon");

	public static void registerStructures() {
		STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static void setupStructures() {
		setupStructure(WALL_IN_THE_NORTH.get(), false, new StructureSeparationSettings(1, 0, 0), IFeatureConfig.NONE);
		setupStructure(CQR_ANY_DUNGEON.get(), true, new StructureSeparationSettings(1, 0, 0), IFeatureConfig.NONE);
	}

	private static <T extends IFeatureConfig> void setupStructure(Structure<T> structure, boolean noiseAffecting,
			StructureSeparationSettings structureSeparationSettings, T config) {
		// add to noise affecting structures
		if (noiseAffecting) {
			INoiseAffectingStructurePiece.NOISE_AFFECTING_STRUCTURES.add(structure);
		}

		// register structure
		Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

		// register structure separation settings
		DimensionStructuresSettings.DEFAULTS = immutableMap(DimensionStructuresSettings.DEFAULTS, structure, structureSeparationSettings);

		WorldGenRegistries.NOISE_GENERATOR_SETTINGS.forEach(dimensionSettings -> {
			DimensionStructuresSettings dimensionStructureSettings = dimensionSettings.structureSettings();
			dimensionStructureSettings.structureConfig = hashMap(dimensionStructureSettings.structureConfig, structure, structureSeparationSettings);
		});

		// register configured structure
		StructureFeature<?, ?> structureFeature = structure.configured(config);

		Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, structure.getRegistryName(), structureFeature);

		FlatGenerationSettings.STRUCTURE_FEATURES.put(structure, structureFeature);
	}

	@SubscribeEvent
	public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
		STRUCTURES.getEntries().stream().map(RegistryObject::get).forEach(structure -> {
			event.getGeneration().getStructures().add(() -> WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE.get(structure.getRegistryName()));
		});
	}

	@SubscribeEvent
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		IWorld iworld = event.getWorld();
		if (!(iworld instanceof ServerWorld)) {
			return;
		}
		STRUCTURES.getEntries().stream().map(RegistryObject::get).forEach(structure -> {
			DimensionStructuresSettings settings = ((ServerWorld) iworld).getChunkSource().getGenerator().getSettings();
			settings.structureConfig = hashMap(settings.structureConfig, structure, DimensionStructuresSettings.DEFAULTS.get(structure));
		});
	}

	private static <K, V> HashMap<K, V> hashMap(Map<K, V> map, K key, V value) {
		HashMap<K, V> result = map.getClass() != HashMap.class ? new HashMap<>(map) : (HashMap<K, V>) map;
		result.computeIfAbsent(key, k -> value);
		return result;
	}

	private static <K, V> ImmutableMap<K, V> immutableMap(Map<K, V> map, K k, V v) {
		return ImmutableMap.<K, V>builder().putAll(map).put(k, v).build();
	}

	private static IStructurePieceType registerStructurePiece(IStructurePieceType type, String id) {
		return IStructurePieceType.setPieceId(type, CQRMain.MODID + ":" + id);
	}

}
