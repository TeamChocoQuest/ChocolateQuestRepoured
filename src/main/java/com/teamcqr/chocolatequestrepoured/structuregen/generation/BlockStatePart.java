package com.teamcqr.chocolatequestrepoured.structuregen.generation;

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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockStatePart implements IStructure {

	private BlockPos pos;
	private BlockPos size;
	private IBlockState[][][] blockstates;

	public BlockStatePart(NBTTagCompound compound) {
		this.readFromNBT(compound);
	}

	public BlockStatePart(BlockPos pos, BlockPos size, IBlockState[][][] blockstates) {
		this.pos = pos;
		this.size = size;
		this.blockstates = new IBlockState[this.size.getX()][this.size.getY()][this.size.getZ()];

		for (int x = 0; x < this.size.getX() && x < blockstates.length; x++) {
			for (int y = 0; y < this.size.getY() && y < blockstates[x].length; y++) {
				for (int z = 0; z < this.size.getZ() && z < blockstates[x][y].length; z++) {
					this.blockstates[x][y][z] = blockstates[x][y][z];
				}
			}
		}
	}

	@Override
	public void generate(World world) {
		BlockPlacingHelper.setBlockStates(world, this.pos, this.blockstates, 3);
	}

	@Override
	public boolean canGenerate(World world) {
		return world.isAreaLoaded(this.pos, this.pos.add(this.size));
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setString("id", "blockStatePart");
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setTag("size", NBTUtil.createPosTag(this.size));

		NBTTagList nbtTagList = new NBTTagList();
		for (int x = 0; x < this.size.getX(); x++) {
			for (int y = 0; y < this.size.getY(); y++) {
				for (int z = 0; z < this.size.getZ(); z++) {
					NBTTagCompound tag = new NBTTagCompound();
					IBlockState state = this.blockstates[x][y][z];

					if (state != null) {
						Block block = state.getBlock();
						tag.setString("block", block.getRegistryName().toString());
						tag.setInteger("meta", block.getMetaFromState(state));
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
		this.blockstates = new IBlockState[this.size.getX()][this.size.getY()][this.size.getZ()];

		NBTTagList nbtTagList = compound.getTagList("blocks", 10);
		for (int x = 0; x < this.size.getX(); x++) {
			for (int y = 0; y < this.size.getY(); y++) {
				for (int z = 0; z < this.size.getZ(); z++) {
					NBTTagCompound tag = nbtTagList.getCompoundTagAt(x * this.size.getY() * this.size.getZ() + y * this.size.getZ() + z);

					if (tag.hasKey("block")) {
						Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("block")));
						this.blockstates[x][y][z] = block.getStateFromMeta(tag.getInteger("meta"));
					}
				}
			}
		}
	}

	public static List<BlockStatePart> split(Map<BlockPos, IBlockState> map) {
		return BlockStatePart.split(new ArrayList(map.entrySet()));
	}

	public static List<BlockStatePart> split(BlockPos pos, Map<BlockPos, IBlockState> map) {
		return BlockStatePart.split(pos, new ArrayList(map.entrySet()));
	}

	public static List<BlockStatePart> split(List<Map.Entry<BlockPos, IBlockState>> entryList) {
		if (!entryList.isEmpty()) {
			int startX = entryList.get(0).getKey().getX();
			int startY = entryList.get(0).getKey().getY();
			int startZ = entryList.get(0).getKey().getZ();
			int endX = startX;
			int endY = startY;
			int endZ = startZ;

			for (Map.Entry<BlockPos, IBlockState> entry : entryList) {
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

			IBlockState[][][] blocks = new IBlockState[endX - startX][endY - startY][endZ - startZ];

			for (Map.Entry<BlockPos, IBlockState> entry : entryList) {
				BlockPos pos = entry.getKey();
				blocks[pos.getX() - startX][pos.getY() - startY][pos.getZ() - startZ] = entry.getValue();
			}

			return BlockStatePart.split(new BlockPos(startX, startY, startZ), blocks);
		}

		return Collections.emptyList();
	}

	public static List<BlockStatePart> split(BlockPos pos, List<Map.Entry<BlockPos, IBlockState>> entryList) {
		if (!entryList.isEmpty()) {
			int endX = entryList.get(0).getKey().getX();
			int endY = entryList.get(0).getKey().getY();
			int endZ = entryList.get(0).getKey().getZ();

			for (Map.Entry<BlockPos, IBlockState> entry : entryList) {
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

			IBlockState[][][] blocks = new IBlockState[endX - pos.getX()][endY - pos.getY()][endZ - pos.getZ()];

			for (Map.Entry<BlockPos, IBlockState> entry : entryList) {
				BlockPos position = entry.getKey();
				blocks[position.getX()][position.getY()][position.getZ()] = entry.getValue();
			}

			return BlockStatePart.split(pos, blocks);
		}

		return Collections.emptyList();
	}

	public static List<BlockStatePart> split(BlockPos pos, IBlockState[][][] array) {
		List<BlockStatePart> list = new ArrayList<>();

		if (array.length > 0 && array[0].length > 0 && array[0][0].length > 0) {
			int xIterations = array.length / 16;
			int yIterations = array[0].length / 16;
			int zIterations = array[0][0].length / 16;

			for (int y = 0; y <= yIterations; y++) {
				for (int x = 0; x <= xIterations; x++) {
					for (int z = 0; z <= zIterations; z++) {
						BlockPos partStartPos = pos.add(x * 16, y * 16, z * 16);
						BlockPos partEndPos = partStartPos.add(x == xIterations ? array.length - x * 16 : 16, y == yIterations ? array[x].length - y * 16 : 16,
								z == zIterations ? array[x][y].length - z * 16 : 16);
						BlockPos partSize = partEndPos.subtract(partStartPos);
						BlockPos partOffset = partStartPos.subtract(pos);
						IBlockState[][][] blockstates = new IBlockState[partSize.getX()][partSize.getY()][partSize.getZ()];
						boolean empty = true;

						for (int x1 = 0; x1 < partSize.getX(); x1++) {
							for (int y1 = 0; y1 < partSize.getY(); y1++) {
								for (int z1 = 0; z1 < partSize.getZ(); z1++) {
									int x2 = partOffset.getX() + x1;
									int y2 = partOffset.getY() + y1;
									int z2 = partOffset.getZ() + z1;
									if (x2 < array.length && y2 < array[x2].length && z2 < array[x2][y2].length) {
										blockstates[x1][y1][z1] = array[x2][y2][z2];
										if (empty && blockstates[x1][y1][z1] != null) {
											empty = false;
										}
									}
								}
							}
						}

						if (!empty) {
							list.add(new BlockStatePart(partStartPos, partSize, blockstates));
						}
					}
				}
			}
		}

		return list;
	}

	public static List<BlockStatePart> split(BlockPos pos, IBlockState[][][] array, int size) {
		List<BlockStatePart> list = new ArrayList<>();

		if (array.length > 0 && array[0].length > 0 && array[0][0].length > 0) {
			int xIterations = array.length / size;
			int yIterations = array[0].length / size;
			int zIterations = array[0][0].length / size;

			for (int y = 0; y <= yIterations; y++) {
				for (int x = 0; x <= xIterations; x++) {
					for (int z = 0; z <= zIterations; z++) {
						BlockPos partStartPos = pos.add(x * size, y * size, z * size);
						BlockPos partEndPos = partStartPos.add(x == xIterations ? array.length - x * size : size, y == yIterations ? array[x].length - y * size : size,
								z == zIterations ? array[x][y].length - z * size : size);
						BlockPos partSize = partEndPos.subtract(partStartPos);
						BlockPos partOffset = partStartPos.subtract(pos);
						IBlockState[][][] blockstates = new IBlockState[partSize.getX()][partSize.getY()][partSize.getZ()];
						boolean empty = true;

						for (int x1 = 0; x1 < partSize.getX(); x1++) {
							for (int y1 = 0; y1 < partSize.getY(); y1++) {
								for (int z1 = 0; z1 < partSize.getZ(); z1++) {
									int x2 = partOffset.getX() + x1;
									int y2 = partOffset.getY() + y1;
									int z2 = partOffset.getZ() + z1;
									if (x2 < array.length && y2 < array[x2].length && z2 < array[x2][y2].length) {
										blockstates[x1][y1][z1] = array[x2][y2][z2];
										if (empty && blockstates[x1][y1][z1] != null) {
											empty = false;
										}
									}
								}
							}
						}

						if (!empty) {
							list.add(new BlockStatePart(partStartPos, partSize, blockstates));
						}
					}
				}
			}
		}

		return list;
	}

}
