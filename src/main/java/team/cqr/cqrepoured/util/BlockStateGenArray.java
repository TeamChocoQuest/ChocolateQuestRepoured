package team.cqr.cqrepoured.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.Entity;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableBlockInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableEntityInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo;

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
		private PreparablePosInfo blockInfo;
		private EnumPriority priority;

		private PriorityBlockInfo(PreparablePosInfo blockInfo, EnumPriority priority) {
			this.blockInfo = blockInfo;
			this.priority = priority;
		}

		public PreparablePosInfo getBlockInfo() {
			return this.blockInfo;
		}

		public EnumPriority getPriority() {
			return this.priority;
		}
	}

	private final Random random;
	private Map<BlockPos, PriorityBlockInfo> mainMap = new HashMap<>();
	private Map<BlockPos, PriorityBlockInfo> postMap = new HashMap<>();
	private List<PreparableEntityInfo> entityList = new ArrayList<>();

	public BlockStateGenArray(Random rand) {
		this.random = rand;
	}

	public Random getRandom() {
		return this.random;
	}

	public Map<BlockPos, PreparablePosInfo> getMainMap() {
		Map<BlockPos, PreparablePosInfo> result = new HashMap<>();
		this.mainMap.forEach((key, value) -> result.put(key, value.getBlockInfo()));
		return result;
	}

	public Map<BlockPos, PreparablePosInfo> getPostMap() {
		Map<BlockPos, PreparablePosInfo> result = new HashMap<>();
		this.postMap.forEach((key, value) -> result.put(key, value.getBlockInfo()));
		return result;
	}

	public List<PreparableEntityInfo> getEntityMap() {
		return this.entityList;
	}

	public boolean addChestWithLootTable(World world, BlockPos pos, Direction facing, ResourceLocation lootTable, GenerationPhase phase) {
		if (lootTable != null) {
			Block chestBlock = Blocks.CHEST;
			BlockState state = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
			ChestTileEntity chest = (ChestTileEntity) chestBlock.createTileEntity(state, world);
			if (chest != null) {
				long seed = WorldDungeonGenerator.getSeed(world, pos.getX() + pos.getY(), pos.getZ() + pos.getY());
				chest.setLootTable(lootTable, seed);
				CompoundNBT nbt = chest.save(new CompoundNBT());
				return this.addBlockState(pos, state, nbt, phase, EnumPriority.MEDIUM);
			}
		} else {
			CQRMain.logger.warn("Tried to place a chest with a null loot table");
		}

		return false;
	}

	public void addBlockStateMap(Map<BlockPos, BlockState> map, GenerationPhase phase, EnumPriority priority) {
		map.entrySet().forEach(entry -> this.addBlockState(entry.getKey(), entry.getValue(), phase, priority));
	}

	public boolean addBlockState(BlockPos pos, BlockState blockState, GenerationPhase phase, EnumPriority priority) {
		return this.addInternal(phase, new PreparableBlockInfo(pos, blockState, null), priority);
	}

	public boolean addBlockState(BlockPos pos, BlockState blockState, CompoundNBT nbt, GenerationPhase phase, EnumPriority priority) {
		return this.addInternal(phase, new PreparableBlockInfo(pos, blockState, nbt), priority);
	}

	public boolean addSpawner(BlockPos pos, BlockState blockState, CompoundNBT nbt, GenerationPhase phase, EnumPriority priority) {
		return this.addInternal(phase, new PreparableBlockInfo(pos, blockState, nbt), priority);
	}

	public boolean addEntity(BlockPos structurePos, Entity entity) {
		return this.addInternal(new PreparableEntityInfo(structurePos, entity));
	}

	public boolean addInternal(GenerationPhase phase, PreparablePosInfo blockInfo, EnumPriority priority) {
		boolean added = false;
		Map<BlockPos, PriorityBlockInfo> mapToAdd = this.getMapFromPhase(phase);
		BlockPos p = new BlockPos(blockInfo.getX(), blockInfo.getY(), blockInfo.getZ());
		PriorityBlockInfo old = mapToAdd.get(p);

		if (old == null || (priority.getValue() > old.getPriority().getValue())) {
			mapToAdd.put(p, new PriorityBlockInfo(blockInfo, priority));
			added = true;
		}

		return added;
	}

	private boolean addInternal(PreparableEntityInfo entityInfo) {
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
