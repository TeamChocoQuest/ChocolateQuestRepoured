package team.cqr.cqrepoured.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkCacheCQR extends ChunkCache {

	/**
	 * @param worldIn    The world from which the chunks will get taken.
	 * @param pos1       The start position from which to cache chunks.
	 * @param pos2       The end position to which to cache chunks.
	 * @param pos3       The chunk that gets loaded despite having loadChunks=false. pos1 is clamped to be at most pos3. pos2 is clamped to be at least pos3.
	 * @param loadChunks Whether chunks should be loaded or not.
	 */
	public ChunkCacheCQR(World worldIn, BlockPos pos1, BlockPos pos2, BlockPos pos3, boolean loadChunks) {
		super(worldIn, pos3, pos3, 0);

		this.chunkX = Math.min(pos1.getX() >> 4, pos3.getX() >> 4);
		this.chunkZ = Math.min(pos1.getZ() >> 4, pos3.getZ() >> 4);
		int chunkX2 = Math.max(pos2.getX() >> 4, pos3.getX() >> 4);
		int chunkZ2 = Math.max(pos2.getZ() >> 4, pos3.getZ() >> 4);
		int y1 = Math.min(pos1.getY(), pos3.getY());
		int y2 = Math.max(pos2.getY(), pos3.getY());
		this.chunkArray = new Chunk[chunkX2 - this.chunkX + 1][chunkZ2 - this.chunkZ + 1];

		for (int x = this.chunkX; x <= chunkX2; x++) {
			for (int z = this.chunkZ; z <= chunkZ2; z++) {
				Chunk chunk = loadChunks ? worldIn.getChunk(x, z) : worldIn.getChunkProvider().getLoadedChunk(x, z);
				this.chunkArray[x - this.chunkX][z - this.chunkZ] = chunk;

				if (this.empty && chunk != null && !chunk.isEmptyBetween(y1, y2)) {
					this.empty = false;
				}
			}
		}
	}

	/**
	 * @param worldIn    The world from which the chunks will get taken.
	 * @param pos1       The start position from which to cache chunks.
	 * @param pos2       The end position to which to cache chunks.
	 * @param pos3       The chunk that gets loaded despite having loadChunks=false.
	 * @param blockRange If a chunk corner is less than this value away from the line between pos1 and pos2 this chunk will get cached.
	 * @param loadChunks Whether chunks should be loaded or not.
	 */
	public ChunkCacheCQR(World worldIn, BlockPos pos1, BlockPos pos2, BlockPos pos3, int blockRange, boolean loadChunks) {
		super(worldIn, pos3, pos3, 0);

		this.chunkX = (Math.min(pos1.getX(), pos2.getX()) - blockRange) >> 4;
		this.chunkZ = (Math.min(pos1.getZ(), pos2.getZ()) - blockRange) >> 4;
		int chunkX2 = (Math.max(pos1.getX(), pos2.getX()) + blockRange) >> 4;
		int chunkZ2 = (Math.max(pos1.getZ(), pos2.getZ()) + blockRange) >> 4;
		int y1 = Math.min(pos1.getY(), pos2.getY());
		int y2 = Math.max(pos1.getY(), pos2.getY());
		this.chunkArray = new Chunk[chunkX2 - this.chunkX + 1][chunkZ2 - this.chunkZ + 1];

		double lineStartX = pos1.getX() + 0.5D;
		double lineStartZ = pos1.getZ() + 0.5D;
		double lineDirectionX = pos2.getX() - pos1.getX();
		double lineDirectionZ = pos2.getZ() - pos1.getZ();
		for (int x = this.chunkX; x <= chunkX2; x++) {
			for (int z = this.chunkZ; z <= chunkZ2; z++) {
				if (!isChunkCornerInRangeToLine(lineStartX, lineStartZ, lineDirectionX, lineDirectionZ, x << 4, z << 4, blockRange)) {
					continue;
				}

				Chunk chunk = loadChunks ? worldIn.getChunk(x, z) : worldIn.getChunkProvider().getLoadedChunk(x, z);
				this.chunkArray[x - this.chunkX][z - this.chunkZ] = chunk;

				if (this.empty && chunk != null && !chunk.isEmptyBetween(y1, y2)) {
					this.empty = false;
				}
			}
		}
	}

	private static boolean isChunkCornerInRangeToLine(double lineStartX, double lineStartZ, double lineDirectionX, double lineDirectionZ, int x, int z, double range) {
		if (getDistanceFromPointToLine2D(lineStartX, lineStartZ, lineDirectionX, lineDirectionZ, x, z) <= range) {
			return true;
		}
		if (getDistanceFromPointToLine2D(lineStartX, lineStartZ, lineDirectionX, lineDirectionZ, x + 16.0D, z) <= range) {
			return true;
		}
		if (getDistanceFromPointToLine2D(lineStartX, lineStartZ, lineDirectionX, lineDirectionZ, x, z + 16.0D) <= range) {
			return true;
		}
		return getDistanceFromPointToLine2D(lineStartX, lineStartZ, lineDirectionX, lineDirectionZ, x + 16.0D, z + 16.0D) <= range;
	}

	private static double oldX = 0.0D;
	private static double oldZ = 0.0D;
	private static double oldLength = 0.0D;

	private static double getDistanceFromPointToLine2D(double lineStartX, double lineStartZ, double lineDirectionX, double lineDirectionZ, double x, double z) {
		if (lineDirectionX != oldX || lineDirectionZ != oldZ) {
			oldX = lineDirectionX;
			oldZ = lineDirectionZ;
			oldLength = Math.sqrt(lineDirectionX * lineDirectionX + lineDirectionZ * lineDirectionZ);
		}
		return ((lineStartX - x) * lineDirectionX - (lineStartZ - z) * lineDirectionZ) / oldLength;
	}

}
