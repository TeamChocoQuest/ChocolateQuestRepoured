package team.cqr.cqrepoured.world.structure;

import java.util.Optional;
import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import team.cqr.cqrepoured.init.CQRStructureTypes;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;

public class StructureCQR extends Structure {

	public static final Codec<StructureCQR> CODEC = simpleCodec(StructureCQR::new);

	public StructureCQR(StructureSettings structureSettings) {
		super(structureSettings);
	}

	@Override
	public StructureType<?> type() {
		return CQRStructureTypes.CQR_STRUCTURE_TYPE;
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		ServerLevel level = WorldDungeonGenerator.getLevel(context.chunkGenerator());
		return Optional.ofNullable(WorldDungeonGenerator.getDungeonAt(level, context.chunkPos()))
				.map(dungeon -> {
					BlockPos pos = context.chunkPos().getMiddleBlockPosition(0);
					Random random = WorldDungeonGenerator.getRandomForCoords(level.getSeed(), pos.getX(), pos.getZ());
					return new GenerationStub(pos, dungeon.createGenerator(context, pos, random, DungeonSpawnType.DUNGEON_GENERATION));
				});
	}

//	public StructureCQR() {
//		super(NoFeatureConfig.CODEC);
//	}
//
//	@Override
//	public Decoration step() {
//		return Decoration.SURFACE_STRUCTURES;
//	}
//
//	@Override
//	public ChunkPos getPotentialFeatureChunk(StructureSeparationSettings separationSettings, long seed, SharedSeedRandom random, int chunkX, int chunkZ) {
//		return new ChunkPos(chunkX, chunkZ);
//	}
//
//	@Override
//	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long seed, SharedSeedRandom random, int chunkX, int chunkZ,
//			Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
//		ServerLevel level = WorldDungeonGenerator.getLevel(chunkGenerator);
//		return WorldDungeonGenerator.getDungeonAt(level, chunkPos) != null;
//	}
//
//	@Override
//	public IStartFactory<NoFeatureConfig> getStartFactory() {
//		return Start::new;
//	}
//
//	public static class Start extends StructureStart<NoFeatureConfig> {
//
//		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int references, long seed) {
//			super(structure, chunkX, chunkZ, boundingBox, references, seed);
//		}
//
//		@Override
//		public void generatePieces(DynamicRegistries registries, ChunkGenerator chunkGenerator, TemplateManager templateManager, int chunkX, int chunkZ,
//				Biome biome, NoFeatureConfig config) {
//			ServerLevel level = WorldDungeonGenerator.getLevel(chunkGenerator);
//			DungeonBase dungeon = WorldDungeonGenerator.getDungeonAt(level, new ChunkPos(chunkX, chunkZ));
//			BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
//			Random random = WorldDungeonGenerator.getRandomForCoords(level.getSeed(), pos.getX(), pos.getZ());
//
//			this.pieces.addAll(dungeon.generate(registries, chunkGenerator, templateManager, pos, random, DungeonSpawnType.DUNGEON_GENERATION));
//
//			this.calculateBoundingBox();
//		}
//
//	}

}
