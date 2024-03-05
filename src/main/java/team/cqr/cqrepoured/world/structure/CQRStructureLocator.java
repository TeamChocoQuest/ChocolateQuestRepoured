package team.cqr.cqrepoured.world.structure;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry.Wrapper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructure;
import team.cqr.cqrepoured.world.structure.generation.dungeons.PlacementSettings;

public class CQRStructureLocator {

	public static Optional<Structure> getStructureAt(ServerLevel level, ChunkPos chunkPos) {
		ServerChunkCache chunkSource = level.getChunkSource();
		return CQRStructureLocator.getStructureAt(chunkSource.getGenerator(), level.registryAccess(), chunkSource.getGeneratorState(), level.structureManager(),
				chunkPos, level, level.getStructureManager())
				.map(Pair::getFirst);
	}

	public static Optional<Pair<Structure, GenerationStub>> getStructureAt(ChunkGenerator chunkGenerator, RegistryAccess registryAccess,
			ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkPos chunkPos, LevelHeightAccessor levelHeightAccessor,
			StructureTemplateManager structureTemplateManager) {
		return CQRStructureLocator.getStructureAt(chunkGenerator, registryAccess, structureState, structureManager, chunkPos, levelHeightAccessor, structureTemplateManager,
				Integer.MAX_VALUE);
	}

	public static Optional<Pair<Structure, GenerationStub>> getStructureAt(ChunkGenerator chunkGenerator, RegistryAccess registryAccess,
			ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkPos chunkPos, LevelHeightAccessor levelHeightAccessor,
			StructureTemplateManager structureTemplateManager, int maximumPriority) {
		return structureState.possibleStructureSets()
				.stream()
				.map(Holder::value)
				.filter(CQRStructureLocator::isCQRStructureSet)
				.filter(structureSet -> ((CQRStructurePlacement) structureSet.placement()).priority() < maximumPriority)
				.sorted(CQRStructureLocator.structureSetComparator())
				.map(structureSet -> CQRStructureLocator.getStructureAt(structureSet, chunkGenerator, registryAccess, structureState, structureManager, chunkPos,
						levelHeightAccessor, structureTemplateManager))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.findFirst();
	}

	private static Optional<Pair<Structure, GenerationStub>> getStructureAt(StructureSet structureSet, ChunkGenerator chunkGenerator,
			RegistryAccess registryAccess, ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkPos chunkPos,
			LevelHeightAccessor levelHeightAccessor, StructureTemplateManager structureTemplateManager) {
		if (!structureSet.placement().isStructureChunk(structureState, chunkPos.x, chunkPos.z)) {
			return Optional.empty();
		}

		WorldgenRandom random = makeRandom(structureState.getLevelSeed(), chunkPos);
		SimpleWeightedRandomList.Builder<Pair<Structure, GenerationStub>> structureListBuilder = SimpleWeightedRandomList.builder();

		for (StructureSelectionEntry entry : structureSet.structures()) {
			Structure structure = entry.structure().value();
			GenerationContext generationContext = new GenerationContext(registryAccess, chunkGenerator, chunkGenerator.getBiomeSource(),
					structureState.randomState(), structureTemplateManager, random, structureState.getLevelSeed(), chunkPos, levelHeightAccessor,
					structure.biomes()::contains);

			structure.findValidGenerationPoint(generationContext)
					.ifPresent(generationStub -> {
						structureListBuilder.add(Pair.of(structure, generationStub), entry.weight());
					});
		}

		SimpleWeightedRandomList<Pair<Structure, GenerationStub>> structureList = structureListBuilder.build();
		return structureList.getRandom(random)
				.filter(wrapper -> CQRStructureLocator.testStructureGenerationChance(wrapper, random, structureList.totalWeight))
				.map(Wrapper::getData);
	}

	public static boolean isCQRStructureSet(StructureSet structureSet) {
		return structureSet.placement() instanceof CQRStructurePlacement;
	}

	private static Comparator<StructureSet> structureSetComparator() {
		return Comparator.comparingInt(structureSet -> structureSet.placement() instanceof CQRStructurePlacement cqrPlacement ? cqrPlacement.priority() : 0);
	}

	private static boolean testStructureGenerationChance(Wrapper<Pair<Structure, GenerationStub>> structureEntry, RandomSource random, int totalWeight) {
		if (structureEntry.getData().getFirst() instanceof CQRStructure cqrStructure) {
			PlacementSettings placementSettings = cqrStructure.placementSettings();
			double chanceModifier = 1.0D / Math.pow((double) structureEntry.getWeight().asInt() / totalWeight, placementSettings.rarityFactor());
			return random.nextFloat() < placementSettings.chance() * chanceModifier;
		}
		return true;
	}

	private static WorldgenRandom makeRandom(long seed, ChunkPos chunkPos) {
		WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenrandom.setLargeFeatureSeed(seed, chunkPos.x, chunkPos.z);
		return worldgenrandom;
	}

}
