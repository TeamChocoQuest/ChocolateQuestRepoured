package team.cqr.cqrepoured.client.occlusion;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.integration.cubicchunks.CubicChunks;

class CachedBlockAccess {

	private World world;
	private ObjIntIntInt2ObjFunction<World, ExtendedBlockStorage> sectionGetter;
	private ExtendedBlockStorage cachedSection;
	private int cachedSectionX = Integer.MAX_VALUE;
	private int cachedSectionY = Integer.MAX_VALUE;
	private int cachedSectionZ = Integer.MAX_VALUE;

	public void init(World world) {
		this.world = world;
		if (world == null) {
			this.sectionGetter = (world1, sectionX, sectionY, setionZ) -> null;
		} else if (CQRMain.isCubicChunksInstalled && CubicChunks.isCubicWorld(world)) {
			this.sectionGetter = (world1, sectionX, sectionY, sectionZ) -> {
				if (CubicChunks.isOutsideBuildHeight(world1, sectionY << 4)) {
					return null;
				}
				return CubicChunks.getBlockStorage(world1, sectionX, sectionY, sectionZ);
			};
		} else {
			this.sectionGetter = (world1, sectionX, sectionY, sectionZ) -> {
				if (sectionY < 0 || sectionY >= 16) {
					return null;
				}
				Chunk chunk = world1.getChunkProvider().getLoadedChunk(sectionX, sectionZ);
				if (chunk == null) {
					return null;
				}
				return chunk.getBlockStorageArray()[sectionY];
			};
		}
		this.cachedSection = null;
		this.cachedSectionX = Integer.MAX_VALUE;
		this.cachedSectionY = Integer.MAX_VALUE;
		this.cachedSectionZ = Integer.MAX_VALUE;
	}

	public void clear() {
		this.world = null;
		this.sectionGetter = null;
		this.cachedSection = null;
		this.cachedSectionX = Integer.MAX_VALUE;
		this.cachedSectionY = Integer.MAX_VALUE;
		this.cachedSectionZ = Integer.MAX_VALUE;
	}

	public IBlockState getBlockState(int x, int y, int z) {
		ExtendedBlockStorage section = this.getChunkSection(x >> 4, y >> 4, z >> 4);
		if (section == null) {
			return Blocks.AIR.getDefaultState();
		}
		return section.get(x & 15, y & 15, z & 15);
	}

	@Nullable
	private ExtendedBlockStorage getChunkSection(int sectionX, int sectionY, int sectionZ) {
		if (this.cachedSectionX != sectionX || this.cachedSectionY != sectionY || this.cachedSectionZ != sectionZ) {
			this.cachedSection = this.sectionGetter.apply(this.world, sectionX, sectionY, sectionZ);
			this.cachedSectionX = sectionX;
			this.cachedSectionY = sectionY;
			this.cachedSectionZ = sectionZ;
		}
		return this.cachedSection;
	}

}
