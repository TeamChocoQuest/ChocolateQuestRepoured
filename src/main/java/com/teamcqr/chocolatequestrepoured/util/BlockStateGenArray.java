package com.teamcqr.chocolatequestrepoured.util;

import java.util.HashMap;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.AbstractBlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.BlockInfo;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EntityInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStateGenArray {
	public enum GenerationPhase {
		MAIN, POST
	}

	private Map<BlockPos, AbstractBlockInfo> mainMap = new HashMap<>();
	private Map<BlockPos, AbstractBlockInfo> postMap = new HashMap<>();
	private Map<BlockPos, EntityInfo> entityMap = new HashMap<>();

	public BlockStateGenArray() {
	}

	public Map<BlockPos, AbstractBlockInfo> getMainMap() {
		return mainMap;
	}

	public Map<BlockPos, AbstractBlockInfo> getPostMap() {
		return postMap;
	}

	public Map<BlockPos, EntityInfo> getEntityMap() {
		return entityMap;
	}

	public boolean addChestWithLootTable(World world, BlockPos pos, EnumFacing facing, ResourceLocation lootTable, GenerationPhase phase) {
		if (lootTable != null) {
			Block chestBlock = Blocks.CHEST;
			IBlockState state = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing);
			TileEntityChest chest = (TileEntityChest) chestBlock.createTileEntity(world, state);
			if (chest != null) {
				ResourceLocation resLoc = null;
				try {
					resLoc = lootTable;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (resLoc != null) {
					long seed = WorldDungeonGenerator.getSeed(world, pos.getX() + pos.getY(), pos.getZ() + pos.getY());
					chest.setLootTable(resLoc, seed);
				}
				NBTTagCompound nbt = chest.writeToNBT(new NBTTagCompound());
				return this.addBlockState(pos, state, nbt, phase);
			}
		} else {
			CQRMain.logger.warn("Tried to place a chest with a null loot table");
		}

		return false;
	}

	public void addBlockStateMap(Map<BlockPos, IBlockState> map, GenerationPhase phase) {
		for (BlockPos pos : map.keySet()) {
			addBlockState(pos, map.get(pos), phase);
		}
	}

	public void forceAddBlockStateMap(Map<BlockPos, IBlockState> map, GenerationPhase phase) {
		for (BlockPos pos : map.keySet()) {
			forceAddBlockState(pos, map.get(pos), phase);
		}
	}

	public boolean addBlockState(BlockPos pos, IBlockState blockState, GenerationPhase phase) {
		return addInternal(phase, new BlockInfo(pos, blockState, null), false);
	}

	public boolean forceAddBlockState(BlockPos pos, IBlockState blockState, GenerationPhase phase) {
		return addInternal(phase, new BlockInfo(pos, blockState, null), true);
	}

	public boolean addBlockState(BlockPos pos, IBlockState blockState, NBTTagCompound nbt, GenerationPhase phase) {
		return addInternal(phase, new BlockInfo(pos, blockState, nbt), false);
	}

	public boolean forceAddBlockState(BlockPos pos, IBlockState blockState, NBTTagCompound nbt, GenerationPhase phase) {
		return addInternal(phase, new BlockInfo(pos, blockState, nbt), true);
	}

	public boolean addSpawner(BlockPos pos, IBlockState blockState, NBTTagCompound nbt, GenerationPhase phase, boolean overwrite) {
		return addInternal(phase, new BlockInfo(pos, blockState, nbt), false);
	}

	public boolean addEntity(BlockPos structurePos, Entity entity) {
		return addInternal(new EntityInfo(structurePos, entity));
	}

	private boolean addInternal(GenerationPhase phase, AbstractBlockInfo blockInfo, boolean overwrite) {
		boolean added = false;
		Map<BlockPos, AbstractBlockInfo> mapToAdd = getMapFromPhase(phase);

		if (overwrite || !mapToAdd.containsKey(blockInfo.getPos())) {
			mapToAdd.put(blockInfo.getPos(), blockInfo);
			added = true;
		}

		return added;
	}

	private boolean addInternal(EntityInfo entityInfo) {
		entityMap.put(entityInfo.getPos(), entityInfo);
		return true;
	}

	private Map<BlockPos, AbstractBlockInfo> getMapFromPhase(GenerationPhase phase) {
		switch (phase) {
		case POST:
			return postMap;
		case MAIN:
		default:
			return mainMap;

		}
	}
}
