package team.cqr.cqrepoured.generation.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationStub;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import team.cqr.cqrepoured.world.structure.CQRStructureLocator;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;

@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator {

	@Inject(method = "lambda$createStructures$1", cancellable = true, at = @At("HEAD"))
	private void skipCQRStructureSets(Holder<StructureSet> structureSetHolder, CallbackInfo info) {
		if (CQRStructureLocator.isCQRStructureSet(structureSetHolder.value())) {
			info.cancel();
		}
	}

	@Inject(method = "createStructures", at = @At("RETURN"))
	private void createCQRStructures(RegistryAccess pRegistryAccess, ChunkGeneratorStructureState pStructureState, StructureManager pStructureManager,
			ChunkAccess pChunk, StructureTemplateManager pStructureTemplateManager, CallbackInfo info) {
		ChunkGenerator chunkGenerator = (ChunkGenerator) (Object) this;
		ChunkPos chunkPos = pChunk.getPos();

		CQRStructureLocator.getStructureAt(chunkGenerator, pRegistryAccess, pStructureState, pStructureManager, chunkPos, pChunk, pStructureTemplateManager)
				.ifPresent(structureInfo -> {
					Structure structure = structureInfo.getFirst();
					GenerationStub generationStub = structureInfo.getSecond();
					StructurePiecesBuilder structurePiecesBuilder = generationStub.getPiecesBuilder();
					PiecesContainer piecesContainer = structurePiecesBuilder.build();
					SectionPos sectionPos = SectionPos.bottomOf(pChunk);
					int references = fetchReferences(pStructureManager, pChunk, sectionPos, structure);
					StructureStart structureStart = new StructureStart(structure, chunkPos, references, piecesContainer);

					if (structureStart.isValid()) {
						pStructureManager.setStartForStructure(sectionPos, structure, structureStart, pChunk);
						ResourceLocation structureName = pRegistryAccess.registryOrThrow(Registries.STRUCTURE).getKey(structure);
						DungeonDataManager.addDungeonEntry(WorldDungeonGenerator.getLevel(chunkGenerator), structureName, generationStub.position(),
								DungeonSpawnType.DUNGEON_GENERATION);
					}
				});
	}

	@Shadow
	private static int fetchReferences(StructureManager pStructureManager, ChunkAccess pChunk, SectionPos pSectionPos, Structure pStructure) {
		return 0;
	}

}
