package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacingHelper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ExtendedBlockStatePart implements IStructure {

	private BlockPos pos;
	private BlockPos size;
	private ExtendedBlockState[][][] extendedstates;

	public ExtendedBlockStatePart(NBTTagCompound compound) {
		this.readFromNBT(compound);
	}

	public ExtendedBlockStatePart(BlockPos pos, BlockPos size, Block[][][] blockArray) {
		this.pos = pos;
		this.size = size;
		this.extendedstates = new ExtendedBlockState[this.size.getX()][this.size.getY()][this.size.getZ()];

		for (int x = 0; x < this.size.getX() && x < blockArray.length; x++) {
			for (int y = 0; y < this.size.getY() && y < blockArray[x].length; y++) {
				for (int z = 0; z < this.size.getZ() && z < blockArray[x][y].length; z++) {
					if (blockArray[x][y][z] != null) {
						this.extendedstates[x][y][z] = new ExtendedBlockState(blockArray[x][y][z].getDefaultState(), null);
					}
				}
			}
		}
	}

	public ExtendedBlockStatePart(BlockPos pos, BlockPos size, IBlockState[][][] blockStateArray) {
		this.pos = pos;
		this.size = size;
		this.extendedstates = new ExtendedBlockState[this.size.getX()][this.size.getY()][this.size.getZ()];

		for (int x = 0; x < this.size.getX() && x < blockStateArray.length; x++) {
			for (int y = 0; y < this.size.getY() && y < blockStateArray[x].length; y++) {
				for (int z = 0; z < this.size.getZ() && z < blockStateArray[x][y].length; z++) {
					if (blockStateArray[x][y][z] != null) {
						this.extendedstates[x][y][z] = new ExtendedBlockState(blockStateArray[x][y][z], null);
					}
				}
			}
		}
	}

	public ExtendedBlockStatePart(BlockPos pos, BlockPos size, ExtendedBlockState[][][] extendedBlockStateArray) {
		this.pos = pos;
		this.size = size;
		this.extendedstates = new ExtendedBlockState[this.size.getX()][this.size.getY()][this.size.getZ()];

		for (int x = 0; x < this.size.getX() && x < extendedBlockStateArray.length; x++) {
			for (int y = 0; y < this.size.getY() && y < extendedBlockStateArray[x].length; y++) {
				for (int z = 0; z < this.size.getZ() && z < extendedBlockStateArray[x][y].length; z++) {
					this.extendedstates[x][y][z] = extendedBlockStateArray[x][y][z];
				}
			}
		}
	}

	@Override
	public void generate(World world) {
		BlockPlacingHelper.setBlockStates(world, this.pos, this.extendedstates, 3);
	}

	@Override
	public boolean canGenerate(World world) {
		return world.isAreaLoaded(this.pos, this.pos.add(this.size));
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setString("id", "extendedBlockStatePart");
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setTag("size", NBTUtil.createPosTag(this.size));

		NBTTagList nbtTagList = new NBTTagList();
		for (int x = 0; x < this.size.getX(); x++) {
			for (int y = 0; y < this.size.getY(); y++) {
				for (int z = 0; z < this.size.getZ(); z++) {
					NBTTagCompound tag = new NBTTagCompound();
					ExtendedBlockState extendedstate = this.extendedstates[x][y][z];

					if (extendedstate != null) {
						IBlockState blockstate = extendedstate.getState();
						Block block = blockstate.getBlock();
						tag.setString("block", block.getRegistryName().toString());
						tag.setInteger("meta", block.getMetaFromState(blockstate));
						NBTTagCompound tileentitydata = extendedstate.getTileentitydata();

						if (tileentitydata != null) {
							tag.setTag("nbt", tileentitydata);
						}
					}
					nbtTagList.appendTag(tag);
				}
			}
		}
		compound.setTag("blocks", nbtTagList);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.pos = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.size = NBTUtil.getPosFromTag(compound.getCompoundTag("size"));
		this.extendedstates = new ExtendedBlockState[this.size.getX()][this.size.getY()][this.size.getZ()];

		NBTTagList nbtTagList = compound.getTagList("blocks", 10);
		for (int x = 0; x < this.size.getX(); x++) {
			for (int y = 0; y < this.size.getY(); y++) {
				for (int z = 0; z < this.size.getZ(); z++) {
					NBTTagCompound tag = nbtTagList.getCompoundTagAt(x * this.size.getY() * this.size.getZ() + y * this.size.getZ() + z);

					if (tag.hasKey("block")) {
						Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("block")));

						if (block != null) {
							IBlockState blockstate = block.getStateFromMeta(tag.getInteger("meta"));
							NBTTagCompound tileentitydata = tag.hasKey("nbt") ? tag.getCompoundTag("nbt") : null;
							this.extendedstates[x][y][z] = new ExtendedBlockState(blockstate, tileentitydata);
						}
					}
				}
			}
		}
	}

	/**
	 * @param map BlockPos keys are coordinates in the world
	 */
	public static List<ExtendedBlockStatePart> splitBlockMap(Map<BlockPos, Block> map) {
		return ExtendedBlockStatePart.splitBlockList(new ArrayList(map.entrySet()));
	}

	/**
	 * @param pos Start position
	 * @param map BlockPos keys are the relative coordinates to the start position
	 */
	public static List<ExtendedBlockStatePart> splitBlockMap(BlockPos pos, Map<BlockPos, Block> map) {
		return ExtendedBlockStatePart.splitBlockList(pos, new ArrayList(map.entrySet()));
	}

	/**
	 * @param map BlockPos keys are coordinates in the world
	 */
	public static List<ExtendedBlockStatePart> splitBlockStateMap(Map<BlockPos, IBlockState> map) {
		return ExtendedBlockStatePart.splitBlockStateList(new ArrayList(map.entrySet()));
	}

	/**
	 * @param pos Start position
	 * @param map BlockPos keys are the relative coordinates to the start position
	 */
	public static List<ExtendedBlockStatePart> splitBlockStateMap(BlockPos pos, Map<BlockPos, IBlockState> map) {
		return ExtendedBlockStatePart.splitBlockStateList(pos, new ArrayList(map.entrySet()));
	}

	/**
	 * @param map BlockPos keys are coordinates in the world
	 */
	public static List<ExtendedBlockStatePart> splitExtendedBlockStateMap(Map<BlockPos, ExtendedBlockState> map) {
		return ExtendedBlockStatePart.splitExtendedBlockStateList(new ArrayList(map.entrySet()));
	}

	/**
	 * @param pos Start position
	 * @param map BlockPos keys are the relative coordinates to the start position
	 */
	public static List<ExtendedBlockStatePart> splitExtendedBlockStateMap(BlockPos pos, Map<BlockPos, ExtendedBlockState> map) {
		return ExtendedBlockStatePart.splitExtendedBlockStateList(pos, new ArrayList(map.entrySet()));
	}

	/**
	 * @param entryList BlockPos keys are coordinates in the world
	 */
	public static List<ExtendedBlockStatePart> splitBlockList(List<Map.Entry<BlockPos, Block>> entryList) {
		if (!entryList.isEmpty()) {
			List<Map.Entry<BlockPos, ExtendedBlockState>> list = new ArrayList<>(entryList.size());
			for (Map.Entry<BlockPos, Block> entry : entryList) {
				list.add(new AbstractMap.SimpleEntry(entry.getKey(), new ExtendedBlockState(entry.getValue().getDefaultState(), null)));
			}
			return ExtendedBlockStatePart.splitExtendedBlockStateList(list);
		}

		return Collections.emptyList();
	}

	/**
	 * @param pos       Start position
	 * @param entryList BlockPos keys are the relative coordinates to the start position
	 */
	public static List<ExtendedBlockStatePart> splitBlockList(BlockPos pos, List<Map.Entry<BlockPos, Block>> entryList) {
		if (!entryList.isEmpty()) {
			List<Map.Entry<BlockPos, ExtendedBlockState>> list = new ArrayList<>(entryList.size());
			for (Map.Entry<BlockPos, Block> entry : entryList) {
				list.add(new AbstractMap.SimpleEntry(entry.getKey(), new ExtendedBlockState(entry.getValue().getDefaultState(), null)));
			}
			return ExtendedBlockStatePart.splitExtendedBlockStateList(pos, list);
		}

		return Collections.emptyList();
	}

	/**
	 * @param entryList BlockPos keys are coordinates in the world
	 */
	public static List<ExtendedBlockStatePart> splitBlockStateList(List<Map.Entry<BlockPos, IBlockState>> entryList) {
		if (!entryList.isEmpty()) {
			List<Map.Entry<BlockPos, ExtendedBlockState>> list = new ArrayList<>(entryList.size());
			for (Map.Entry<BlockPos, IBlockState> entry : entryList) {
				list.add(new AbstractMap.SimpleEntry(entry.getKey(), new ExtendedBlockState(entry.getValue(), null)));
			}
			return ExtendedBlockStatePart.splitExtendedBlockStateList(list);
		}

		return Collections.emptyList();
	}

	/**
	 * @param pos       Start position
	 * @param entryList BlockPos keys are the relative coordinates to the start position
	 */
	public static List<ExtendedBlockStatePart> splitBlockStateList(BlockPos pos, List<Map.Entry<BlockPos, IBlockState>> entryList) {
		if (!entryList.isEmpty()) {
			List<Map.Entry<BlockPos, ExtendedBlockState>> list = new ArrayList<>(entryList.size());
			for (Map.Entry<BlockPos, IBlockState> entry : entryList) {
				list.add(new AbstractMap.SimpleEntry(entry.getKey(), new ExtendedBlockState(entry.getValue(), null)));
			}
			return ExtendedBlockStatePart.splitExtendedBlockStateList(pos, list);
		}

		return Collections.emptyList();
	}

	/**
	 * @param entryList BlockPos keys are coordinates in the world
	 */
	public static List<ExtendedBlockStatePart> splitExtendedBlockStateList(List<Map.Entry<BlockPos, ExtendedBlockState>> entryList) {
		if (!entryList.isEmpty()) {
			int startX = entryList.get(0).getKey().getX();
			int startY = entryList.get(0).getKey().getY();
			int startZ = entryList.get(0).getKey().getZ();
			int endX = startX;
			int endY = startY;
			int endZ = startZ;

			for (Map.Entry<BlockPos, ExtendedBlockState> entry : entryList) {
				BlockPos pos = entry.getKey();
				if (pos.getX() < startX) {
					startX = pos.getX();
				}
				if (pos.getY() < startY) {
					startY = pos.getY();
				}
				if (pos.getZ() < startZ) {
					startZ = pos.getZ();
				}
				if (pos.getX() > endX) {
					endX = pos.getX();
				}
				if (pos.getY() > endY) {
					endY = pos.getY();
				}
				if (pos.getZ() > endZ) {
					endZ = pos.getZ();
				}
			}

			ExtendedBlockState[][][] extendedstates = new ExtendedBlockState[endX - startX + 1][endY - startY + 1][endZ - startZ + 1];

			for (Map.Entry<BlockPos, ExtendedBlockState> entry : entryList) {
				BlockPos pos = entry.getKey();
				extendedstates[pos.getX() - startX][pos.getY() - startY][pos.getZ() - startZ] = entry.getValue();
			}

			return ExtendedBlockStatePart.split(new BlockPos(startX, startY, startZ), extendedstates);
		}

		return Collections.emptyList();
	}

	/**
	 * @param pos       Start position
	 * @param entryList BlockPos keys are the relative coordinates to the start position
	 */
	public static List<ExtendedBlockStatePart> splitExtendedBlockStateList(BlockPos pos, List<Map.Entry<BlockPos, ExtendedBlockState>> entryList) {
		if (!entryList.isEmpty()) {
			int endX = entryList.get(0).getKey().getX();
			int endY = entryList.get(0).getKey().getY();
			int endZ = entryList.get(0).getKey().getZ();

			for (Map.Entry<BlockPos, ExtendedBlockState> entry : entryList) {
				BlockPos position = entry.getKey();
				if (position.getX() > endX) {
					endX = position.getX();
				}
				if (position.getY() > endY) {
					endY = position.getY();
				}
				if (position.getZ() > endZ) {
					endZ = position.getZ();
				}
			}

			ExtendedBlockState[][][] extendedstates = new ExtendedBlockState[endX - pos.getX()][endY - pos.getY()][endZ - pos.getZ()];

			for (Map.Entry<BlockPos, ExtendedBlockState> entry : entryList) {
				BlockPos position = entry.getKey();
				extendedstates[position.getX()][position.getY()][position.getZ()] = entry.getValue();
			}

			return ExtendedBlockStatePart.split(pos, extendedstates);
		}

		return Collections.emptyList();
	}

	/**
	 * @param pos   Start position
	 * @param array Array keys are the relative coordinates to the start position
	 */
	public static List<ExtendedBlockStatePart> split(BlockPos pos, Block[][][] array) {
		return ExtendedBlockStatePart.split(pos, array, 16);
	}

	/**
	 * @param pos   Start position
	 * @param array Array keys are the relative coordinates to the start position
	 */
	public static List<ExtendedBlockStatePart> split(BlockPos pos, IBlockState[][][] array) {
		return ExtendedBlockStatePart.split(pos, array, 16);
	}

	/**
	 * @param pos   Start position
	 * @param array Array keys are the relative coordinates to the start position
	 */
	public static List<ExtendedBlockStatePart> split(BlockPos pos, ExtendedBlockState[][][] array) {
		return ExtendedBlockStatePart.split(pos, array, 16);
	}

	/**
	 * @param pos   Start position
	 * @param array Array keys are the relative coordinates to the start position
	 * @param size  Size of the parts
	 */
	public static List<ExtendedBlockStatePart> split(BlockPos pos, Block[][][] array, int size) {
		if (array.length > 0 && array[0].length > 0 && array[0][0].length > 0) {
			ExtendedBlockState[][][] blocks = new ExtendedBlockState[array.length][array[0].length][array[0][0].length];
			for (int x = 0; x < blocks.length; x++) {
				for (int y = 0; y < blocks[x].length && y < array[x].length; y++) {
					for (int z = 0; z < blocks[x][y].length && z < array[x][y].length; z++) {
						if (array[x][y][z] != null) {
							blocks[x][y][z] = new ExtendedBlockState(array[x][y][z].getDefaultState(), null);
						}
					}
				}
			}

			return ExtendedBlockStatePart.split(pos, blocks, size);
		}

		return Collections.emptyList();
	}

	/**
	 * @param pos   Start position
	 * @param array Array keys are the relative coordinates to the start position
	 * @param size  Size of the parts
	 */
	public static List<ExtendedBlockStatePart> split(BlockPos pos, IBlockState[][][] array, int size) {
		if (array.length > 0 && array[0].length > 0 && array[0][0].length > 0) {
			ExtendedBlockState[][][] blocks = new ExtendedBlockState[array.length][array[0].length][array[0][0].length];
			for (int x = 0; x < blocks.length; x++) {
				for (int y = 0; y < blocks[x].length && y < array[x].length; y++) {
					for (int z = 0; z < blocks[x][y].length && z < array[x][y].length; z++) {
						if (array[x][y][z] != null) {
							blocks[x][y][z] = new ExtendedBlockState(array[x][y][z], null);
						}
					}
				}
			}

			return ExtendedBlockStatePart.split(pos, blocks, size);
		}

		return Collections.emptyList();
	}

	/**
	 * @param pos   Start position
	 * @param array Array keys are the relative coordinates to the start position
	 * @param size  Size of the parts
	 */
	public static List<ExtendedBlockStatePart> split(BlockPos pos, ExtendedBlockState[][][] array, int size) {
		List<ExtendedBlockStatePart> list = new ArrayList<>();

		if (array.length > 0 && array[0].length > 0 && array[0][0].length > 0) {
			int xIterations = MathHelper.ceil(array.length / size);
			int yIterations = MathHelper.ceil(array[0].length / size);
			int zIterations = MathHelper.ceil(array[0][0].length / size);
			for (int y = 0; y < yIterations; y++) {
				for (int x = 0; x < xIterations; x++) {
					for (int z = 0; z < zIterations; z++) {
						int partOffsetX = x * size;
						int partOffsetY = y * size;
						int partOffsetZ = z * size;
						int partSizeX = x == xIterations - 1 ? array.length - partOffsetX : size;
						int partSizeY = y == yIterations - 1 ? array[0].length - partOffsetY : size;
						int partSizeZ = z == zIterations - 1 ? array[0][0].length - partOffsetZ : size;
						ExtendedBlockState[][][] blocks = new ExtendedBlockState[partSizeX][partSizeY][partSizeZ];
						boolean empty = true;

						for (int x1 = 0; x1 < partSizeX; x1++) {
							int x2 = x1 + partOffsetX;
							for (int y1 = 0; y1 < partSizeY && y1 < array[x2].length; y1++) {
								int y2 = y1 + partOffsetY;
								for (int z1 = 0; z1 < partSizeZ && z1 < array[x2][y2].length; z1++) {
									blocks[x1][y1][z1] = array[x2][y2][z1 + partOffsetZ];
									if (empty && blocks[x1][y1][z1] != null) {
										empty = false;
									}
								}
							}
						}

						if (!empty) {
							list.add(new ExtendedBlockStatePart(pos.add(partOffsetX, partOffsetY, partOffsetZ), new BlockPos(partSizeX, partSizeY, partSizeZ), blocks));
						}
					}
				}
			}
		}

		return list;
	}

	public static class ExtendedBlockState {

		private IBlockState state;
		private NBTTagCompound tileentitydata;

		public ExtendedBlockState(IBlockState state, NBTTagCompound tileentitydata) {
			this.state = state;
			this.tileentitydata = tileentitydata;
		}

		public IBlockState getState() {
			return this.state;
		}

		public NBTTagCompound getTileentitydata() {
			return this.tileentitydata;
		}

	}

}
