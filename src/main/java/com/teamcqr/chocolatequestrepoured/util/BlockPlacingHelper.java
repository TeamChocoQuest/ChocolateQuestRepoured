package com.teamcqr.chocolatequestrepoured.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.BlockSnapshot;

public class BlockPlacingHelper {

	public static void setBlockStates(World world, BlockPos pos, Block[][][] blocks, int flags) {
		List<Map.Entry<BlockPos, IBlockState>> map = new ArrayList<>();
		for (int x = 0; x < blocks.length; x++) {
			for (int y = 0; y < blocks[x].length; y++) {
				for (int z = 0; z < blocks[x][y].length; z++) {
					if (blocks[x][y][z] != null) {
						map.add(new AbstractMap.SimpleEntry(pos.add(x, y, z), blocks[x][y][z].getDefaultState()));
					}
				}
			}
		}
		BlockPlacingHelper.setBlockStates(world, map, flags);
	}

	public static void setBlockStates(World world, BlockPos pos, IBlockState[][][] blockstates, int flags) {
		List<Map.Entry<BlockPos, IBlockState>> map = new ArrayList<>();
		for (int x = 0; x < blockstates.length; x++) {
			for (int y = 0; y < blockstates[x].length; y++) {
				for (int z = 0; z < blockstates[x][y].length; z++) {
					if (blockstates[x][y][z] != null) {
						map.add(new AbstractMap.SimpleEntry(pos.add(x, y, z), blockstates[x][y][z]));
					}
				}
			}
		}
		BlockPlacingHelper.setBlockStates(world, map, flags);
	}

	public static void setBlockStates(World world, BlockPos pos, ExtendedBlockStatePart.ExtendedBlockState[][][] extendedstates, int flags) {
		List<Map.Entry<BlockPos, IBlockState>> map = new ArrayList<>();

		for (int x = 0; x < extendedstates.length; x++) {
			for (int y = 0; y < extendedstates[x].length; y++) {
				for (int z = 0; z < extendedstates[x][y].length; z++) {
					if (extendedstates[x][y][z] != null) {
						map.add(new AbstractMap.SimpleEntry(pos.add(x, y, z), extendedstates[x][y][z].getState()));
					}
				}
			}
		}

		BlockPlacingHelper.setBlockStates(world, map, flags);

		for (int x = 0; x < extendedstates.length; x++) {
			for (int y = 0; y < extendedstates[x].length; y++) {
				for (int z = 0; z < extendedstates[x][y].length; z++) {
					if (extendedstates[x][y][z] != null) {
						NBTTagCompound tileentitydata = extendedstates[x][y][z].getTileentitydata();
						if (tileentitydata != null) {
							BlockPos position = pos.add(x, y, z);
							TileEntity tileEntity = world.getTileEntity(position);
							if (tileEntity != null) {
								tileentitydata.setInteger("x", position.getX());
								tileentitydata.setInteger("y", position.getY());
								tileentitydata.setInteger("z", position.getZ());
								tileEntity.readFromNBT(tileentitydata);
							}
						}
					}
				}
			}
		}
	}

	public static void setBlockStates(World world, BlockPos pos, List<Template.BlockInfo> list, PlacementSettings placementSettings, int flags) {
		List<Map.Entry<BlockPos, IBlockState>> map = new ArrayList<>();

		for (Template.BlockInfo blockInfo : list) {
			BlockPos position = pos.add(Template.transformedBlockPos(placementSettings, blockInfo.pos));
			IBlockState state = blockInfo.blockState.withMirror(placementSettings.getMirror()).withRotation(placementSettings.getRotation());
			map.add(new AbstractMap.SimpleEntry(position, state));
		}

		BlockPlacingHelper.setBlockStates(world, map, flags);

		for (Template.BlockInfo blockInfo : list) {
			if (blockInfo.tileentityData != null) {
				BlockPos position = pos.add(Template.transformedBlockPos(placementSettings, blockInfo.pos));
				TileEntity tileEntity = world.getTileEntity(position);

				if (tileEntity != null) {
					blockInfo.tileentityData.setInteger("x", position.getX());
					blockInfo.tileentityData.setInteger("y", position.getY());
					blockInfo.tileentityData.setInteger("z", position.getZ());
					tileEntity.readFromNBT(blockInfo.tileentityData);
					tileEntity.mirror(placementSettings.getMirror());
					tileEntity.rotate(placementSettings.getRotation());
				}
			}
		}
	}

	public static void setBlockStates(World world, List<Map.Entry<BlockPos, IBlockState>> map, int flags) {
		if (!world.isRemote && world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			return;
		}

		Set<Chunk> generateSkylightMap = new HashSet<>();
		Set<BlockPos> relightBlock = new HashSet<>();
		Set<BlockPos> propagateSkylightOcclusion = new HashSet<>();
		List<BlockPos> lightUpdates = new LinkedList<>();
		List<BlockPlacingHelper.BlockUpdate> blockUpdates = new LinkedList<>();

		for (Map.Entry<BlockPos, IBlockState> entry : map) {
			BlockPos pos = entry.getKey();
			IBlockState newState = entry.getValue();
			if (world.isOutsideBuildHeight(pos)) {
				continue;
			}
			pos = pos.toImmutable();
			Chunk chunk = world.getChunkFromBlockCoords(pos);
			IBlockState oldState = chunk.getBlockState(pos);
			int oldLight = oldState.getLightValue(world, pos);
			int oldOpacity = oldState.getLightOpacity(world, pos);

			IBlockState iblockstate = setBlockState(world, chunk, pos, newState, generateSkylightMap, relightBlock, propagateSkylightOcclusion);

			if (iblockstate != null) {
				if (newState.getLightOpacity(world, pos) != oldOpacity || newState.getLightValue(world, pos) != oldLight) {
					lightUpdates.add(pos);
				}

				if (world.captureBlockSnapshots && !world.isRemote) {
					world.capturedBlockSnapshots.add(new BlockSnapshot(world, pos, oldState, flags));
				} else {
					blockUpdates.add(new BlockUpdate(world, pos, chunk, iblockstate, newState, flags));
				}
			}
		}

		/*
		for (Chunk chunk : generateSkylightMap) {
			chunk.generateSkylightMap();
		}

		for (BlockPos pos : relightBlock) {
			relightBlock(world.getChunkFromBlockCoords(pos), pos.getX() & 15, pos.getY(), pos.getZ() & 15);
		}

		for (BlockPos pos : propagateSkylightOcclusion) {
			propagateSkylightOcclusion(world.getChunkFromBlockCoords(pos), pos.getX() & 15, pos.getZ() & 15);
		}

		world.profiler.startSection("checkLight");
		for (BlockPos pos : lightUpdates) {
			world.checkLight(pos);
		}
		world.profiler.endSection();
		*/

		for (BlockPlacingHelper.BlockUpdate blockUpdate : blockUpdates) {
			blockUpdate.markAndNotifyBlock();
		}
	}

	@Nullable
	public static IBlockState setBlockState(World world, Chunk chunk, BlockPos pos, IBlockState state, Set<Chunk> generateSkylightMap, Set<BlockPos> relightBlock, Set<BlockPos> propagateSkylightOcclusion) {
		int i = pos.getX() & 15;
		int j = pos.getY();
		int k = pos.getZ() & 15;
		int l = k << 4 | i;

		int[] precipitationHeightMap = getPrecipitationHeightMap(chunk);
		if (j >= precipitationHeightMap[l] - 1) {
			precipitationHeightMap[l] = -999;
		}

		int i1 = chunk.getHeightMap()[l];
		IBlockState iblockstate = chunk.getBlockState(pos);

		if (iblockstate == state) {
			return null;
		} else {
			Block block = state.getBlock();
			Block block1 = iblockstate.getBlock();
			int k1 = iblockstate.getLightOpacity(world, pos); // Relocate old light value lookup here, so that it is called before TE is removed.
			ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[j >> 4];
			boolean flag = false;

			if (extendedblockstorage == Chunk.NULL_BLOCK_STORAGE) {
				if (block == Blocks.AIR) {
					return null;
				}

				extendedblockstorage = new ExtendedBlockStorage(j >> 4 << 4, world.provider.hasSkyLight());
				chunk.getBlockStorageArray()[j >> 4] = extendedblockstorage;
				flag = j >= i1;
			}

			extendedblockstorage.set(i, j & 15, k, state);

			if (!world.isRemote) {
				// Only fire block breaks when the block changes.
				if (block1 != block) {
					block1.breakBlock(world, pos, iblockstate);
				}
				TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
				if (te != null && te.shouldRefresh(world, pos, iblockstate, state)) {
					world.removeTileEntity(pos);
				}
			} else if (block1.hasTileEntity(iblockstate)) {
				TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
				if (te != null && te.shouldRefresh(world, pos, iblockstate, state)) {
					world.removeTileEntity(pos);
				}
			}

			if (extendedblockstorage.get(i, j & 15, k).getBlock() != block) {
				return null;
			} else {
				if (flag) {
					generateSkylightMap.add(chunk);
				} else {
					int j1 = state.getLightOpacity(world, pos);

					if (j1 > 0) {
						if (j >= i1) {
							relightBlock.add(pos.up());
						}
					} else if (j == i1 - 1) {
						relightBlock.add(pos);
					}

					if (j1 != k1 && (j1 < k1 || chunk.getLightFor(EnumSkyBlock.SKY, pos) > 0 || chunk.getLightFor(EnumSkyBlock.BLOCK, pos) > 0)) {
						propagateSkylightOcclusion.add(pos);
					}
				}

				// If capturing blocks, only run block physics for TE's. Non-TE's are handled in ForgeHooks.onPlaceItemIntoWorld
				if (!world.isRemote && block1 != block && (!world.captureBlockSnapshots || block.hasTileEntity(state))) {
					block.onBlockAdded(world, pos, state);
				}

				if (block.hasTileEntity(state)) {
					TileEntity tileentity1 = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

					if (tileentity1 == null) {
						tileentity1 = block.createTileEntity(world, state);
						world.setTileEntity(pos, tileentity1);
					}

					if (tileentity1 != null) {
						tileentity1.updateContainingBlockInfo();
					}
				}

				chunk.markDirty();
				return iblockstate;
			}
		}
	}

	private static Field precipitationHeightMapField = null;

	private static int[] getPrecipitationHeightMap(Chunk chunk) {
		try {
			if (precipitationHeightMapField == null) {
				try {
					precipitationHeightMapField = Chunk.class.getDeclaredField("field_76638_b");
				} catch (NoSuchFieldException e) {
					precipitationHeightMapField = Chunk.class.getDeclaredField("precipitationHeightMap");
				}
				precipitationHeightMapField.setAccessible(true);
			}
			return (int[]) precipitationHeightMapField.get(chunk);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error("Failed to get value of Chunk.precipitationHeightMap field", e);
		}
		return new int[256];
	}

	private static Method relightBlockMethod = null;

	private static void relightBlock(Chunk chunk, int x, int y, int z) {
		try {
			if (relightBlockMethod == null) {
				try {
					relightBlockMethod = Chunk.class.getDeclaredMethod("func_76615_h", int.class, int.class, int.class);
				} catch (NoSuchMethodException e) {
					relightBlockMethod = Chunk.class.getDeclaredMethod("relightBlock", int.class, int.class, int.class);
				}
				relightBlockMethod.setAccessible(true);
			}
			relightBlockMethod.invoke(chunk, x, y, z);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to invoke Chunk.relightBlock method", e);
		}
	}

	private static Method propagateSkylightOcclusionMethod = null;

	private static void propagateSkylightOcclusion(Chunk chunk, int x, int z) {
		try {
			if (propagateSkylightOcclusionMethod == null) {
				try {
					propagateSkylightOcclusionMethod = Chunk.class.getDeclaredMethod("func_76595_e", int.class, int.class);
				} catch (NoSuchMethodException e) {
					propagateSkylightOcclusionMethod = Chunk.class.getDeclaredMethod("propagateSkylightOcclusion", int.class, int.class);
				}
				propagateSkylightOcclusionMethod.setAccessible(true);
			}
			propagateSkylightOcclusionMethod.invoke(chunk, x, z);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to invoke Chunk.propagateSkylightOcclusion method", e);
		}
	}

	private static class BlockUpdate {

		private World world;
		private BlockPos pos;
		private Chunk chunk;
		private IBlockState iblockstate;
		private IBlockState newState;
		private int flags;

		public BlockUpdate(World world, BlockPos pos, Chunk chunk, IBlockState iblockstate, IBlockState newState, int flags) {
			this.world = world;
			this.pos = pos;
			this.chunk = chunk;
			this.iblockstate = iblockstate;
			this.newState = newState;
			this.flags = flags;
		}

		public void markAndNotifyBlock() {
			this.world.markAndNotifyBlock(this.pos, this.chunk, this.iblockstate, this.newState, this.flags);
		}

	}

}
