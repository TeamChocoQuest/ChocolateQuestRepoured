package team.cqr.cqrepoured.world.structure;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class StructureCQR extends Structure<NoFeatureConfig> {

	public StructureCQR() {
		super(NoFeatureConfig.CODEC);
	}

	@Override
	public Decoration step() {
		return Decoration.SURFACE_STRUCTURES;
	}

	@Override
	public ChunkPos getPotentialFeatureChunk(StructureSeparationSettings separationSettings, long seed, SharedSeedRandom random, int chunkX, int chunkZ) {
		return new ChunkPos(chunkX, chunkZ);
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long seed, SharedSeedRandom random, int chunkX, int chunkZ,
			Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
		ServerWorld level = WorldDungeonGenerator.getLevel(chunkGenerator);
		return WorldDungeonGenerator.getDungeonAt(level, chunkPos) != null;
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return Start::new;
	}

	public static class Start extends StructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int references, long seed) {
			super(structure, chunkX, chunkZ, boundingBox, references, seed);
		}

		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator chunkGenerator, TemplateManager templateManager, int chunkX, int chunkZ,
				Biome biome, NoFeatureConfig config) {
			ServerWorld level = WorldDungeonGenerator.getLevel(chunkGenerator);
			DungeonBase dungeon = WorldDungeonGenerator.getDungeonAt(level, new ChunkPos(chunkX, chunkZ));
			BlockPos pos = new BlockPos((chunkX << 4) + 8, 0, (chunkZ << 4) + 8);
			Random random = WorldDungeonGenerator.getRandomForCoords(level.getSeed(), pos.getX(), pos.getZ());

			this.pieces.addAll(dungeon.generate(registries, chunkGenerator, templateManager, pos, random, DungeonSpawnType.DUNGEON_GENERATION));

			this.calculateBoundingBox();
		}

	}

}
