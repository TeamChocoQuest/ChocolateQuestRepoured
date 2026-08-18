package com.example.chocolatequest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import com.example.chocolatequest.ChocolateQuestReDone;

public class BlockStateGenArray {

	public enum GenerationPhase {
		MAIN, POST
	}

	public enum EnumPriority {
		LOWEST(0), LOW(1), MEDIUM(2), HIGH(3), HIGHEST(4);

		private final int value;

		EnumPriority(final int valueIn) {
			this.value = valueIn;
		}

		public int getValue() {
			return this.value;
		}
	}

	private class PriorityBlockInfo {
		private BlockState blockInfo;
		private EnumPriority priority;

		private PriorityBlockInfo(BlockState blockInfo, EnumPriority priority) {
			this.blockInfo = blockInfo;
			this.priority = priority;
		}

		public BlockState getBlockInfo() {
			return this.blockInfo;
		}

		public EnumPriority getPriority() {
			return this.priority;
		}
	}

	private final Random random;
	private Map<BlockPos, PriorityBlockInfo> mainMap = new HashMap<>();
	private Map<BlockPos, PriorityBlockInfo> postMap = new HashMap<>();
	private List<Entity> entityList = new ArrayList<>();

	public BlockStateGenArray(Random rand) {
		this.random = rand;
	}

	public Random getRandom() {
		return this.random;
	}

	public Map<BlockPos, BlockState> getMainMap() {
		Map<BlockPos, BlockState> result = new HashMap<>();
		this.mainMap.forEach((key, value) -> result.put(key, value.getBlockInfo()));
		return result;
	}

	public Map<BlockPos, BlockState> getPostMap() {
		Map<BlockPos, BlockState> result = new HashMap<>();
		this.postMap.forEach((key, value) -> result.put(key, value.getBlockInfo()));
		return result;
	}

	public List<Entity> getEntityMap() {
		return this.entityList;
	}

	public boolean addChestWithLootTable(Level world, BlockPos pos, Direction facing, ResourceLocation lootTable, GenerationPhase phase) {
		if (lootTable != null) {
			ChestBlock chestBlock = (ChestBlock) Blocks.CHEST;
			BlockState state = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
			ChestBlockEntity chest = (ChestBlockEntity) chestBlock.newBlockEntity(pos, state);
			if (chest != null) {
				long seed = 0; chest.setLootTable(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, lootTable), seed);
				CompoundTag nbt = new CompoundTag();
				return this.addBlockState(pos, state, nbt, phase, EnumPriority.MEDIUM);
			}
		} else {
			ChocolateQuestReDone.LOGGER.warn("Tried to place a chest with a null loot table");
		}

		return false;
	}

	public void addBlockStateMap(Map<BlockPos, BlockState> map, GenerationPhase phase, EnumPriority priority) {
		map.entrySet().forEach(entry -> this.addBlockState(entry.getKey(), entry.getValue(), phase, priority));
	}

	public boolean addBlockState(BlockPos pos, BlockState blockState, GenerationPhase phase, EnumPriority priority) {
		return false;
	}

	public boolean addBlockState(BlockPos pos, BlockState blockState, CompoundTag nbt, GenerationPhase phase, EnumPriority priority) {
		return false;
	}

	public boolean addSpawner(BlockPos pos, BlockState blockState, CompoundTag nbt, GenerationPhase phase, EnumPriority priority) {
		return false;
	}

	public boolean addEntity(BlockPos structurePos, Entity entity) {
		return this.addInternal(null);
	}

	public boolean addInternal(GenerationPhase phase, BlockState blockInfo, EnumPriority priority) {
		boolean added = false;
		Map<BlockPos, PriorityBlockInfo> mapToAdd = this.getMapFromPhase(phase);
		BlockPos p = BlockPos.ZERO;
		PriorityBlockInfo old = mapToAdd.get(p);

		if (old == null || (priority.getValue() > old.getPriority().getValue())) {
			mapToAdd.put(p, new PriorityBlockInfo(blockInfo, priority));
			added = true;
		}

		return added;
	}

	private boolean addInternal(Entity entityInfo) {
		this.entityList.add(entityInfo);
		return true;
	}

	private Map<BlockPos, PriorityBlockInfo> getMapFromPhase(GenerationPhase phase) {
		switch (phase) {
		case POST:
			return this.postMap;
		case MAIN:
		default:
			return this.mainMap;

		}
	}
}

