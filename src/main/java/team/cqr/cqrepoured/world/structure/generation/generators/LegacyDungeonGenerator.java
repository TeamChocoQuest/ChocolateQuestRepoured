package team.cqr.cqrepoured.world.structure.generation.generators;

import java.util.Random;
import java.util.stream.StreamSupport;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraftforge.server.ServerLifecycleHooks;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece.Builder;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public abstract class LegacyDungeonGenerator<T extends DungeonBase> implements IDungeonGenerator<T> {

	protected BlockPos pos;
	protected T dungeon;
	protected Random random;
	protected final CQRStructurePiece.Builder dungeonBuilder;
	protected final ServerLevel level;
	protected final ChunkGenerator chunkGenerator;

	public LegacyDungeonGenerator(ChunkGenerator chunkGenerator, BlockPos pos, T dungeon, Random random) {
		this.pos = pos;
		this.dungeon = dungeon;
		this.random = random;
		ServerLevel level = StreamSupport.stream(ServerLifecycleHooks.getCurrentServer().getAllLevels().spliterator(), false).filter(serverWorld -> serverWorld.getChunkSource().getGenerator() == chunkGenerator).findFirst().get();
		this.level = level;
		this.chunkGenerator = chunkGenerator;
		this.dungeonBuilder = new CQRStructurePiece.Builder(level, pos, dungeon);
	}

	@Deprecated
	@Override
	public StructurePiece prepare(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random, T config) {
		return prepare();
	}

	@Deprecated
	@Override
	public void prepare(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random, T config, Builder dungeonBuilder) {

	}

	public StructurePiece prepare() {
		this.preProcess();
		this.buildStructure();
		this.postProcess();
		return this.dungeonBuilder.build();
	}

	protected abstract void preProcess();

	protected abstract void buildStructure();

	protected abstract void postProcess();
	
	public T getDungeon() {
		return this.dungeon;
	}

}
