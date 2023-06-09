package team.cqr.cqrepoured.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableEntityInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparablePosInfo;

import java.util.*;

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

	public boolean addChestWithLootTable(Level world, BlockPos pos, Direction facing, ResourceLocation lootTable, GenerationPhase phase) {
		if (lootTable != null) {
			Block chestBlock = Blocks.CHEST;
			BlockState state = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
			ChestTileEntity chest = (ChestTileEntity) chestBlock.createTileEntity(state, world);
			if (chest != null) {
				long seed = WorldDungeonGenerator.getSeed(0, pos.getX() + pos.getY(), pos.getZ() + pos.getY());
				chest.setLootTable(lootTable, seed);
				CompoundTag nbt = chest.save(new CompoundTag());
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
		return false;
		//return this.addInternal(phase, new PreparableBlockInfo(pos, blockState, null), priority);
	}

	public boolean addBlockState(BlockPos pos, BlockState blockState, CompoundTag nbt, GenerationPhase phase, EnumPriority priority) {
		return false;
		//return this.addInternal(phase, new PreparableBlockInfo(pos, blockState, nbt), priority);
	}

	public boolean addSpawner(BlockPos pos, BlockState blockState, CompoundTag nbt, GenerationPhase phase, EnumPriority priority) {
		return false;
		//return this.addInternal(phase, new PreparableBlockInfo(pos, blockState, nbt), priority);
	}

	public boolean addEntity(BlockPos structurePos, Entity entity) {
		return this.addInternal(new PreparableEntityInfo(structurePos, entity));
	}

	public boolean addInternal(GenerationPhase phase, PreparablePosInfo blockInfo, EnumPriority priority) {
		boolean added = false;
		Map<BlockPos, PriorityBlockInfo> mapToAdd = this.getMapFromPhase(phase);
		BlockPos p = BlockPos.ZERO;//new BlockPos(blockInfo.getX(), blockInfo.getY(), blockInfo.getZ());
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
