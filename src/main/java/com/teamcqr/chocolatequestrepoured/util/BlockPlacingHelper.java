package com.teamcqr.chocolatequestrepoured.util;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart.ExtendedBlockState;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import net.minecraftforge.common.util.BlockSnapshot;

public class BlockPlacingHelper {

	public static void setBlockStates(World world, BlockPos pos, Block[][][] blocks, int flags) {
		Map<BlockPos, IBlockState> map = new HashMap<>();
		for (int x = 0; x < blocks.length; x++) {
			for (int y = 0; y < blocks[x].length; y++) {
				for (int z = 0; z < blocks[x][y].length; z++) {
					if (blocks[x][y][z] != null) {
						map.put(pos.add(x, y, z), blocks[x][y][z].getDefaultState());
					}
				}
			}
		}
		BlockPlacingHelper.setBlockStates(world, map, flags);
	}

	public static void setBlockStates(World world, BlockPos pos, IBlockState[][][] blockstates, int flags) {
		Map<BlockPos, IBlockState> map = new HashMap<>();
		for (int x = 0; x < blockstates.length; x++) {
			for (int y = 0; y < blockstates[x].length; y++) {
				for (int z = 0; z < blockstates[x][y].length; z++) {
					if (blockstates[x][y][z] != null) {
						map.put(pos.add(x, y, z), blockstates[x][y][z]);
					}
				}
			}
		}
		BlockPlacingHelper.setBlockStates(world, map, flags);
	}

	public static void setBlockStates(World world, BlockPos pos, ExtendedBlockStatePart.ExtendedBlockState[][][] extendedstates, int flags) {
		Map<BlockPos, IBlockState> map = new HashMap<>();

		for (int x = 0; x < extendedstates.length; x++) {
			for (int y = 0; y < extendedstates[x].length; y++) {
				for (int z = 0; z < extendedstates[x][y].length; z++) {
					if (extendedstates[x][y][z] != null) {
						map.put(pos.add(x, y, z), extendedstates[x][y][z].getState());
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
		Map<BlockPos, IBlockState> map = new HashMap<>();

		for (Template.BlockInfo blockInfo : list) {
			BlockPos position = pos.add(Template.transformedBlockPos(placementSettings, blockInfo.pos));
			IBlockState state = blockInfo.blockState.withMirror(placementSettings.getMirror()).withRotation(placementSettings.getRotation());
			map.put(position, state);
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

	public static void setBlockStates(World world, Map<BlockPos, IBlockState> map, int flags) {
		Deque<BlockPos> lightUpdates = new LinkedList<>();
		Deque<BlockPlacingHelper.BlockUpdate> blockUpdates = new LinkedList<>();

		for (Map.Entry<BlockPos, IBlockState> entry : map.entrySet()) {
			BlockPos pos = entry.getKey();
			IBlockState newState = entry.getValue();
			Chunk chunk = world.getChunkFromBlockCoords(pos);
			IBlockState oldState = chunk.getBlockState(pos);
			int oldLight = oldState.getLightValue(world, pos);
			int oldOpacity = oldState.getLightOpacity(world, pos);

			IBlockState iblockstate = chunk.setBlockState(pos, newState);

			if (iblockstate != null) {
				if (newState.getLightOpacity(world, pos) != oldOpacity || newState.getLightValue(world, pos) != oldLight) {
					lightUpdates.push(pos);
				}

				if (!world.captureBlockSnapshots) {
					blockUpdates.push(new BlockUpdate(world, pos, chunk, oldState, newState, flags));
				} else {
					world.capturedBlockSnapshots.add(new BlockSnapshot(world, pos, oldState, flags));
				}
			}
		}

		for (BlockPos pos : lightUpdates) {
			world.checkLight(pos);
		}

		for (BlockPlacingHelper.BlockUpdate blockUpdate : blockUpdates) {
			blockUpdate.markAndNotifyBlock();
		}
	}

	private static class BlockUpdate {

		private World world;
		private BlockPos pos;
		private Chunk chunk;
		private IBlockState oldState;
		private IBlockState newState;
		private int flags;

		public BlockUpdate(World world, BlockPos pos, Chunk chunk, IBlockState oldState, IBlockState newState, int flags) {
			this.world = world;
			this.pos = pos;
			this.chunk = chunk;
			this.oldState = oldState;
			this.newState = newState;
			this.flags = flags;
		}

		public void markAndNotifyBlock() {
			this.world.markAndNotifyBlock(this.pos, this.chunk, this.oldState, this.newState, this.flags);
		}

	}

}
