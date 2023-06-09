package team.cqr.cqrepoured.util.datafixer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import meldexun.reflectionutil.ReflectionField;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.EmptyProfiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.EmptyTickList;
import net.minecraft.world.ITickList;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.storage.MapData;

public class DataFixerWorld extends Level {

	public static class DataFixerChunkSource extends AbstractChunkProvider {

		private final Level world;
		private final Map<ChunkPos, Chunk> chunks = new HashMap<>();
		private final WorldLightManager lightEngine = new WorldLightManager(this, false, false);

		public DataFixerChunkSource(Level world) {
			this.world = world;
		}

		@Override
		public BlockGetter getLevel() {
			return world;
		}

		@Override
		public WorldLightManager getLightEngine() {
			return lightEngine;
		}

		@Override
		public IChunk getChunk(int pChunkX, int pChunkZ, ChunkStatus pRequiredStatus, boolean pLoad) {
			return chunks.computeIfAbsent(new ChunkPos(pChunkX, pChunkZ), k -> new EmptyChunk(world, k));
		}

		@Override
		public String gatherStats() {
			throw new UnsupportedOperationException();
		}

	}

	private static final ReflectionField<DimensionType> DEFAULT_OVERWORLD = new ReflectionField<>(DimensionType.class,
			"field_236004_h_", "DEFAULT_OVERWORLD");
	private final DynamicRegistries dynamicRegistries = new DynamicRegistries.Impl();
	private final DataFixerChunkSource chunkSource = new DataFixerChunkSource(this);

	public DataFixerWorld() {
		super(null, null, DEFAULT_OVERWORLD.get(null), () -> EmptyProfiler.INSTANCE, false, false, 0L);
	}

	@Override
	public ITickList<Block> getBlockTicks() {
		return EmptyTickList.empty();
	}

	@Override
	public ITickList<Fluid> getLiquidTicks() {
		return EmptyTickList.empty();
	}

	@Override
	public AbstractChunkProvider getChunkSource() {
		return chunkSource;
	}

	@Override
	public void levelEvent(Player pPlayer, int pType, BlockPos pPos, int pData) {
		// ignore
	}

	@Override
	public DynamicRegistries registryAccess() {
		return dynamicRegistries;
	}

	@Override
	public List<? extends Player> players() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Biome getUncachedNoiseBiome(int pX, int pY, int pZ) {
		throw new UnsupportedOperationException();
	}

	@Override
	public float getShade(Direction pDirection, boolean pIsShade) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void sendBlockUpdated(BlockPos pPos, BlockState pOldState, BlockState pNewState, int pFlags) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void playSound(Player pPlayer, double pX, double pY, double pZ, SoundEvent pSound,
                          SoundSource pCategory, float pVolume, float pPitch) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void playSound(Player pPlayer, Entity pEntity, SoundEvent pEvent, SoundSource pCategory,
                          float pVolume, float pPitch) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Entity getEntity(int pId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MapData getMapData(String pMapName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMapData(MapData p_217399_1_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFreeMapId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void destroyBlockProgress(int pBreakerId, BlockPos pPos, int pProgress) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Scoreboard getScoreboard() {
		throw new UnsupportedOperationException();
	}

	@Override
	public RecipeManager getRecipeManager() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ITagCollectionSupplier getTagManager() {
		throw new UnsupportedOperationException();
	}

	public void setChunk(ChunkPos chunkPos, Chunk chunk) {
		chunkSource.chunks.put(chunkPos, chunk);
	}

	public Collection<Chunk> chunks() {
		return chunkSource.chunks.values();
	}

}
