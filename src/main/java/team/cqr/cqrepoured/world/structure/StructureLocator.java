package team.cqr.cqrepoured.world.structure;

import java.util.Comparator;
import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
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

public class StructureLocator {

	public static Optional<Structure> getStructureAt(ServerLevel level, ChunkPos chunkPos) {
		ServerChunkCache chunkSource = level.getChunkSource();
		return StructureLocator.getStructureAt(chunkSource.getGenerator(), level.registryAccess(), chunkSource.getGeneratorState(), level.structureManager(),
				chunkPos, level, level.getStructureManager())
				.map(Pair::getFirst);
	}

	public static Optional<Pair<Structure, GenerationStub>> getStructureAt(ChunkGenerator chunkGenerator, RegistryAccess registryAccess,
			ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkPos chunkPos, LevelHeightAccessor levelHeightAccessor,
			StructureTemplateManager structureTemplateManager) {
		return structureState.possibleStructureSets()
				.stream()
				.map(Holder::value)
				.filter(StructureLocator::isCQRStructureSet)
				.sorted(StructureLocator.structureSetComparator())
				.map(structureSet -> StructureLocator.getStructureAt(structureSet, chunkGenerator, registryAccess, structureState, structureManager, chunkPos,
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
		return structureList.getRandomValue(random)
				.filter(structureInfo -> StructureLocator.testStructureGenerationChance(structureSet, structureInfo.getFirst(), structureList.totalWeight));
	}

	public static boolean isCQRStructureSet(StructureSet structureSet) {
		// TODO check if placement is CQR placement
		return false;
	}

	private static Comparator<StructureSet> structureSetComparator() {
		// TODO create comparator which sorts StructureSets according to the priority of their placement
		return Comparator.comparingInt(structureSet -> 0);
	}

	private static boolean testStructureGenerationChance(StructureSet structureSet, Structure structure, int totalWeight) {
		// TODO test structure specific generation chance
		return false;
	}

	private static WorldgenRandom makeRandom(long pSeed, ChunkPos pChunkPos) {
		WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenrandom.setLargeFeatureSeed(pSeed, pChunkPos.x, pChunkPos.z);
		return worldgenrandom;
	}

}
