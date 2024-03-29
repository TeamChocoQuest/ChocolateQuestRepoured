package team.cqr.cqrepoured.world.structure.generation.generators;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public interface IDungeonGenerator<T extends DungeonBase> {

	default StructurePiece prepare(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random, T config) {
		ServerLevel level = WorldDungeonGenerator.getLevel(chunkGenerator);
		CQRStructurePiece.Builder dungeonBuilder = new CQRStructurePiece.Builder(level, pos, config);
		prepare(dynamicRegistries, chunkGenerator, templateManager, pos, random, config, dungeonBuilder);
		return dungeonBuilder.build();
	}

	void prepare(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random, T config, CQRStructurePiece.Builder dungeonBuilder);

	T getDungeon();
	
}
