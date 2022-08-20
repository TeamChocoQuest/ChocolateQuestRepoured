package team.cqr.cqrepoured.world.structure.generation.generators;

import java.util.Random;
import java.util.stream.StreamSupport;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;

public interface IDungeonGenerator<T extends DungeonBase> {

	default StructurePiece prepare(ChunkGenerator chunkGenerator, BlockPos pos, Random random, T config) {
		ServerWorld level = StreamSupport.stream(ServerLifecycleHooks.getCurrentServer().getAllLevels().spliterator(), false)
				.filter(serverWorld -> serverWorld.getChunkSource().getGenerator() == chunkGenerator).findFirst().get();
		GeneratableDungeon.Builder dungeonBuilder = new GeneratableDungeon.Builder(level, pos, config);
		prepare(chunkGenerator, pos, random, config, dungeonBuilder);
		return dungeonBuilder.build();
	}

	void prepare(ChunkGenerator chunkGenerator, BlockPos pos, Random random, T config, GeneratableDungeon.Builder dungeonBuilder);

}
