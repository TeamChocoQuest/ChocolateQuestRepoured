package team.cqr.cqrepoured.gentest.util;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.ForgeEventFactory;

public class NeighborNotifyUtil {

	private static final MutableBlockPos MUTABLE = new MutableBlockPos();
	private static final MutableBlockPos MUTABLE1 = new MutableBlockPos();

	public static void notifyNeighbors(World world, int chunkX, int chunkY, int chunkZ) {
		if (world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			return;
		}

		Chunk chunk = world.getChunk(chunkX, chunkZ);
		Chunk chunkWest = world.getChunkProvider().getLoadedChunk(chunkX - 1, chunkZ);
		Chunk chunkEast = world.getChunkProvider().getLoadedChunk(chunkX + 1, chunkZ);
		Chunk chunkNorth = world.getChunkProvider().getLoadedChunk(chunkX, chunkZ - 1);
		Chunk chunkSouth = world.getChunkProvider().getLoadedChunk(chunkX, chunkZ + 1);
		ExtendedBlockStorage blockStorage = chunk.getBlockStorageArray()[chunkY];
		ExtendedBlockStorage blockStorageWest = chunkWest != null ? chunkWest.getBlockStorageArray()[chunkY] : null;
		ExtendedBlockStorage blockStorageEast = chunkEast != null ? chunkEast.getBlockStorageArray()[chunkY] : null;
		ExtendedBlockStorage blockStorageDown = chunkY - 1 >= 0 ? chunk.getBlockStorageArray()[chunkY - 1] : null;
		ExtendedBlockStorage blockStorageUp = chunkY + 1 <= 15 ? chunk.getBlockStorageArray()[chunkY + 1] : null;
		ExtendedBlockStorage blockStorageNorth = chunkNorth != null ? chunkNorth.getBlockStorageArray()[chunkY] : null;
		ExtendedBlockStorage blockStorageSouth = chunkSouth != null ? chunkSouth.getBlockStorageArray()[chunkY] : null;

		int cx = chunkX << 4;
		int cy = chunkY << 4;
		int cz = chunkZ << 4;
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				for (int z = 0; z < 16; z++) {
					MUTABLE.setPos(cx + x, cy + y, cz + z);
					IBlockState state = get(blockStorage, x, y, z);

					if (true) {
						if (state.getMaterial().isLiquid()) {
							state.getBlock().onBlockAdded(world, MUTABLE, state);
						}
						continue;
					}

					if (ForgeEventFactory.onNeighborNotify(world, MUTABLE, state, EnumSet.allOf(EnumFacing.class), false).isCanceled()) {
						continue;
					}

					for (EnumFacing facing : EnumFacing.VALUES) {
						MUTABLE1.setPos(cx + x, cy + y, cz + z).move(facing);
						IBlockState neighborState;
						switch (facing) {
						case WEST:
							if (x == 0) {
								neighborState = get(blockStorageWest, 15, y, z);
							} else {
								neighborState = get(blockStorage, x - 1, y, z);
							}
							break;
						case EAST:
							if (x == 15) {
								neighborState = get(blockStorageEast, 0, y, z);
							} else {
								neighborState = get(blockStorage, x + 1, y, z);
							}
							break;
						case DOWN:
							if (y == 0) {
								neighborState = get(blockStorageDown, x, 15, z);
							} else {
								neighborState = get(blockStorage, x, y - 1, z);
							}
							break;
						case UP:
							if (y == 15) {
								neighborState = get(blockStorageUp, x, 0, z);
							} else {
								neighborState = get(blockStorage, x, y + 1, z);
							}
							break;
						case NORTH:
							if (z == 0) {
								neighborState = get(blockStorageNorth, x, y, 15);
							} else {
								neighborState = get(blockStorage, x, y, z - 1);
							}
							break;
						case SOUTH:
							if (z == 15) {
								neighborState = get(blockStorageSouth, x, y, 0);
							} else {
								neighborState = get(blockStorage, x, y, z + 1);
							}
							break;
						default:
							throw new NullPointerException();
						}
						neighborState.neighborChanged(world, MUTABLE1, state.getBlock(), MUTABLE);
					}
				}
			}
		}
	}

	private static IBlockState get(@Nullable ExtendedBlockStorage blockStorage, int x, int y, int z) {
		return blockStorage != null ? blockStorage.get(x, y, z) : Blocks.AIR.getDefaultState();
	}
}
