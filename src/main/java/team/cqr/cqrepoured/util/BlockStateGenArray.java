package team.cqr.cqrepoured.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.structuregen.WorldDungeonGenerator;
import team.cqr.cqrepoured.structuregen.structurefile.AbstractBlockInfo;
import team.cqr.cqrepoured.structuregen.structurefile.BlockInfo;
import team.cqr.cqrepoured.structuregen.structurefile.EntityInfo;

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
		private AbstractBlockInfo blockInfo;
		private EnumPriority priority;

		private PriorityBlockInfo(AbstractBlockInfo blockInfo, EnumPriority priority) {
			this.blockInfo = blockInfo;
			this.priority = priority;
		}

		public AbstractBlockInfo getBlockInfo() {
			return this.blockInfo;
		}

		public EnumPriority getPriority() {
			return this.priority;
		}
	}

	private final Random random;
	private Map<BlockPos, PriorityBlockInfo> mainMap = new HashMap<>();
	private Map<BlockPos, PriorityBlockInfo> postMap = new HashMap<>();
	private Map<BlockPos, EntityInfo> entityMap = new HashMap<>();

	public BlockStateGenArray(Random rand) {
		this.random = rand;
	}

	public Random getRandom() {
		return this.random;
	}

	public Map<BlockPos, AbstractBlockInfo> getMainMap() {
		Map<BlockPos, AbstractBlockInfo> result = new HashMap<>();
		this.mainMap.forEach((key, value) -> result.put(key, value.getBlockInfo()));
		return result;
	}

	public Map<BlockPos, AbstractBlockInfo> getPostMap() {
		Map<BlockPos, AbstractBlockInfo> result = new HashMap<>();
		this.postMap.forEach((key, value) -> result.put(key, value.getBlockInfo()));
		return result;
	}

	public Map<BlockPos, EntityInfo> getEntityMap() {
		return this.entityMap;
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
				return this.addBlockState(pos, state, nbt, phase, EnumPriority.MEDIUM);
			}
		} else {
			CQRMain.logger.warn("Tried to place a chest with a null loot table");
		}

		return false;
	}

	public void addBlockStateMap(Map<BlockPos, IBlockState> map, GenerationPhase phase, EnumPriority priority) {
		for (BlockPos pos : map.keySet()) {
			this.addBlockState(pos, map.get(pos), phase, priority);
		}
	}

	public boolean addBlockState(BlockPos pos, IBlockState blockState, GenerationPhase phase, EnumPriority priority) {
		return this.addInternal(phase, new BlockInfo(pos, blockState, null), priority);
	}

	public boolean addBlockState(BlockPos pos, IBlockState blockState, NBTTagCompound nbt, GenerationPhase phase, EnumPriority priority) {
		return this.addInternal(phase, new BlockInfo(pos, blockState, nbt), priority);
	}

	public boolean addSpawner(BlockPos pos, IBlockState blockState, NBTTagCompound nbt, GenerationPhase phase, EnumPriority priority) {
		return this.addInternal(phase, new BlockInfo(pos, blockState, nbt), priority);
	}

	public boolean addEntity(BlockPos structurePos, Entity entity) {
		return this.addInternal(new EntityInfo(structurePos, entity));
	}

	private boolean addInternal(GenerationPhase phase, AbstractBlockInfo blockInfo, EnumPriority priority) {
		boolean added = false;
		Map<BlockPos, PriorityBlockInfo> mapToAdd = this.getMapFromPhase(phase);

		if ((!mapToAdd.containsKey(blockInfo.getPos())) || (priority.getValue() > mapToAdd.get(blockInfo.getPos()).getPriority().getValue())) {
			mapToAdd.put(blockInfo.getPos(), new PriorityBlockInfo(blockInfo, priority));
			added = true;
		}

		return added;
	}

	private boolean addInternal(EntityInfo entityInfo) {
		this.entityMap.put(entityInfo.getPos(), entityInfo);
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
