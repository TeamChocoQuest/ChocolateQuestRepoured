package team.cqr.cqrepoured.util;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

import javax.annotation.Nullable;

public class CachedBlockAccess implements BlockGetter {

	private final BlockPos.Mutable mutable = new BlockPos.Mutable();
	private Level level;
	private Chunk cachedChunk;
	private ChunkSection cachedSection;

	public void setupCached(Level level) {
		this.level = level;
		this.cachedChunk = null;
		this.cachedSection = null;
	}

	public void clearCache() {
		this.level = null;
		this.cachedChunk = null;
		this.cachedSection = null;
	}

	public Chunk getChunk(int chunkX, int chunkZ) {
		if (this.cachedChunk == null || this.cachedChunk.getPos().x != chunkX || this.cachedChunk.getPos().z != chunkZ) {
			this.cachedChunk = this.level.getChunk(chunkX, chunkZ);
			this.cachedSection = null;
		}
		return this.cachedChunk;
	}

	@Nullable
	public ChunkSection getChunkSection(int chunkX, int chunkY, int chunkZ) {
		return this.getChunkSection(this.mutable.set(chunkX << 4, chunkY << 4, chunkZ << 4));
	}

	@Nullable
	public ChunkSection getChunkSection(BlockPos pos) {
		if (Level.isOutsideBuildHeight(pos)) {
			return null;
		}
//		if (CQRMain.isCubicChunksInstalled && CubicChunks.isCubicWorld(this.level)) {
//			this.cachedSection = CubicChunks.getBlockStorage(this.level, pos.getX() >> 4, pos.getY() >> 4, pos.getZ() >> 4);
//		} else {
			Chunk chunk = this.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
			if (this.cachedSection == null || this.cachedSection.bottomBlockY() >> 4 != pos.getY() >> 4) {
				this.cachedSection = chunk.getSections()[pos.getY() >> 4];
			}
//		}
		return this.cachedSection;
	}

	/*@Override
	public int getCombinedLight(BlockPos pos, int lightValue) {
		throw new UnsupportedOperationException();
	}*/

	public BlockState getBlockState(int x, int y, int z) {
		return this.getBlockState(this.mutable.set(x, y, z));
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		ChunkSection section = this.getChunkSection(pos);
		if (section == null) {
			return Blocks.AIR.defaultBlockState();
		}
		return section.getBlockState(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
	}

	
	
	/*@Override
	public boolean isAirBlock(BlockPos pos) {
		throw new UnsupportedOperationException();
	}*/

	/*@Override
	public Biome getBiome(BlockPos pos) {
		throw new UnsupportedOperationException();
	}*/

	/*@Override
	public int getStrongPower(BlockPos pos, Direction direction) {
		throw new UnsupportedOperationException();
	}*/

	/*@Override
	public WorldType getWorldType() {
		throw new UnsupportedOperationException();
	}*/

	/*@Override
	public boolean isSideSolid(BlockPos pos, Direction side, boolean _default) {
		throw new UnsupportedOperationException();
	}*/

	@Override
	public BlockEntity getBlockEntity(BlockPos pPos) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FluidState getFluidState(BlockPos pos) {
		ChunkSection section = this.getChunkSection(pos);
		if (section == null) {
			return null;
		}
		return section.getFluidState(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15);
	}

}
