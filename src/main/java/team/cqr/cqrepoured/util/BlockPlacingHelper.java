//package team.cqr.cqrepoured.util;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldType;
//import net.minecraft.world.chunk.Chunk;
//import net.minecraft.world.chunk.ChunkSection;
//import net.minecraft.world.chunk.IChunk;
//import net.minecraftforge.common.property.IExtendedBlockState;
//import net.minecraftforge.common.util.BlockSnapshot;
//import team.cqr.cqrepoured.CQRMain;
//import team.cqr.cqrepoured.config.CQRConfig;
//import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
//
//import javax.annotation.Nullable;
//
//public class BlockPlacingHelper {
//
//	private static final BlockPos.Mutable MUTABLE = new BlockPos.Mutable();
//
//	public static boolean setBlockStates(World world, int chunkX, int chunkY, int chunkZ, GeneratableDungeon dungeon, IBlockInfo blockInfo) {
//		if (world.isOutsideBuildHeight(MUTABLE.set(chunkX << 4, chunkY << 4, chunkZ << 4))) {
//			return false;
//		}
//
//		if (!world.isRemote && world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
//			return false;
//		}
//
//		Chunk chunk = world.getChunk(chunkX, chunkZ);
//		ChunkSection blockStorage = chunk.getSections()[chunkY];
//
//		if (blockStorage == Chunk.EMPTY_SECTION) {
//			blockStorage = new ChunkSection(chunkY << 4/*, world.dimensionType().hasSkyLight()*/);
//			chunk.getSections()[chunkY] = blockStorage;
//			if (!blockInfo.place(world, chunk, blockStorage, dungeon)) {
//				chunk.getSections()[chunkY] = null;
//				return false;
//			}
//			return true;
//		}
//
//		return blockInfo.place(world, chunk, blockStorage, dungeon);
//	}
//
//	@FunctionalInterface
//	public interface IBlockInfo {
//
//		boolean place(World world, Chunk chunk, ChunkSection blockStorage, GeneratableDungeon dungeon);
//
//	}
//
//	public static boolean setBlockState(World world, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, int flags, boolean updateLight) {
//		if (CQRMain.isPhosphorInstalled || CQRConfig.advanced.instantLightUpdates || updateLight) {
//			if (!world.setBlock(pos, state, flags)) {
//				return false;
//			}
//			if (tileEntity != null) {
//				world.setBlockEntity(pos, tileEntity);
//				tileEntity.updateContainingBlockInfo();
//			}
//			return true;
//		}
//
//		if (world.isOutsideBuildHeight(pos)) {
//			return false;
//		}
//
//		if (!world.isRemote && world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
//			return false;
//		}
//
//		IChunk chunk = world.getChunk(pos);
//		ChunkSection blockStorage = chunk.getSections()[pos.getY() >> 4];
//		if (blockStorage == Chunk.EMPTY_SECTION) {
//			if (state == Blocks.AIR.defaultBlockState()) {
//				return false;
//			}
//
//			blockStorage = new ChunkSection(pos.getY() >> 4 << 4/*, world.provider.hasSkyLight()*/);
//			chunk.getSections()[pos.getY() >> 4] = blockStorage;
//		}
//
//		return setBlockState(world, chunk, blockStorage, pos, state, tileEntity, flags);
//	}
//
//	public static boolean setBlockState(World world, Chunk chunk, ChunkSection blockStorage, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, int flags) {
//		BlockState oldState = setBlockState(world, chunk, blockStorage, pos, state, tileEntity);
//
//		if (oldState == null) {
//			return false;
//		}
//
//		if (!world.isRemote && world.captureBlockSnapshots) {
//			world.capturedBlockSnapshots.add(new BlockSnapshot(world, pos.immutable(), oldState, flags));
//		} else {
//			world.markAndNotifyBlock(pos, chunk, oldState, state, flags);
//		}
//
//		return true;
//	}
//
//	@Nullable
//	private static BlockState setBlockState(World world, Chunk chunk, ChunkSection blockStorage, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity) {
//		int x = pos.getX() & 15;
//		int y = pos.getY() & 15;
//		int z = pos.getZ() & 15;
//		BlockState oldState = setBlockState(blockStorage, x, y, z, state);
//		if (oldState == null) {
//			return null;
//		}
//
//		int l = z << 4 | x;
//		if (pos.getY() >= chunk.precipitationHeightMap[l] - 1) {
//			chunk.precipitationHeightMap[l] = -999;
//		}
//
//		Block block = state.getBlock();
//		Block oldBlock = oldState.getBlock();
//
//		if (!world.isRemote && oldBlock != block) {
//			oldBlock.breakBlock(world, pos, oldState);
//		}
//		if (oldBlock.hasTileEntity(oldState)) {
//			TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
//			if (te != null && te.shouldRefresh(world, pos, oldState, state)) {
//				world.removeTileEntity(pos);
//			}
//		}
//
//		if (!world.isRemote && block != oldBlock && (!world.captureBlockSnapshots || block.hasTileEntity(state))) {
//			// block.onBlockAdded(world, pos, state);
//		}
//		if (block.hasTileEntity(state)) {
//			if (tileEntity != null) {
//				world.setBlockEntity(pos, tileEntity);
//				tileEntity.updateContainingBlockInfo();
//			} else {
//				TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
//				if (te == null) {
//					te = block.createTileEntity(world, state);
//					world.setBlockEntity(pos, te);
//				}
//				if (te != null) {
//					te.updateContainingBlockInfo();
//				}
//			}
//		}
//
//		chunk.markUnsaved();
//		return oldState;
//	}
//
//	@Nullable
//	private static BlockState setBlockState(ChunkSection blockStorage, int x, int y, int z, BlockState state) {
//		if (state instanceof IExtendedBlockState) {
//			state = ((IExtendedBlockState) state).getClean();
//		}
//		BlockState oldState = blockStorage.getData().get(x, y, z);
//		if (state == oldState) {
//			return null;
//		}
//
//		Block block = state.getBlock();
//		Block oldBlock = oldState.getBlock();
//		if (oldBlock != Blocks.AIR) {
//			blockStorage.blockRefCount -= 1;
//
//			if (oldBlock.getTickRandomly()) {
//				blockStorage.tickRefCount -= 1;
//			}
//		}
//		if (block != Blocks.AIR) {
//			blockStorage.blockRefCount += 1;
//
//			if (block.isRandomlyTicking(state)) {
//				blockStorage.tickRefCount += 1;
//			}
//		}
//
//		blockStorage.getData().set(x, y, z, state);
//		return oldState;
//	}
//
//}
