package team.cqr.cqrepoured.world.structure.debug;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.world.structure.generation.generation.INoiseAffectingStructurePiece;

@EventBusSubscriber(modid = CQRConstants.MODID)
public class TestStructures {

	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, CQRConstants.MODID);

	public static final RegistryObject<Structure<NoFeatureConfig>> TEST = STRUCTURES.register("test", TestStructure::new);

	public static void registerTestStructures() {
		STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SuppressWarnings("unchecked")
	public static void loadTestStructures() {
		STRUCTURES.getEntries().stream().map(RegistryObject::get).forEach(structure -> {
			// add to noise affecting structures
			INoiseAffectingStructurePiece.NOISE_AFFECTING_STRUCTURES.add(structure);

			// register structure
			Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

			// register structure separation settings
			StructureSeparationSettings structureSeparationSettings = new StructureSeparationSettings(1, 0, 1982365671);

			DimensionStructuresSettings.DEFAULTS = immutableMap(DimensionStructuresSettings.DEFAULTS, structure, structureSeparationSettings);

			WorldGenRegistries.NOISE_GENERATOR_SETTINGS.forEach(dimensionSettings -> {
				DimensionStructuresSettings dimensionStructureSettings = dimensionSettings.structureSettings();
				dimensionStructureSettings.structureConfig = hashMap(dimensionStructureSettings.structureConfig, structure, structureSeparationSettings);
			});

			// register configured structure
			StructureFeature<?, ?> structureFeature = ((Structure<NoFeatureConfig>) structure).configured(IFeatureConfig.NONE);

			Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, structure.getRegistryName(), structureFeature);

			FlatGenerationSettings.STRUCTURE_FEATURES.put(structure, structureFeature);
		});
	}

	@SubscribeEvent
	public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
		STRUCTURES.getEntries().stream().map(RegistryObject::get).forEach(structure -> {
			event.getGeneration().getStructures().add(() -> WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE.get(structure.getRegistryName()));
		});
	}

	@SubscribeEvent
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		Level iworld = event.getWorld();
		if (!(iworld instanceof ServerLevel)) {
			return;
		}
		STRUCTURES.getEntries().stream().map(RegistryObject::get).forEach(structure -> {
			DimensionStructuresSettings settings = ((ServerLevel) iworld).getChunkSource().getGenerator().getSettings();
			settings.structureConfig = hashMap(settings.structureConfig, structure, DimensionStructuresSettings.DEFAULTS.get(structure));
		});
	}

	private static <K, V> Map<K, V> hashMap(Map<K, V> map, K k, V v) {
		try {
			map.computeIfAbsent(k, key -> v);
			return map;
		} catch (Exception e) {
			map = new HashMap<>(map);
			map.computeIfAbsent(k, key -> v);
			return map;
		}
	}

	private static <K, V> ImmutableMap<K, V> immutableMap(ImmutableMap<K, V> map, K k, V v) {
		return ImmutableMap.<K, V>builder().putAll(map).put(k, v).build();
	}

}
