package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockPart implements IStructure {

	private BlockPos pos;
	private BlockPos size;
	private Block[][][] blocks;

	public BlockPart(NBTTagCompound compound) {
		this.readFromNBT(compound);
	}

	public BlockPart(BlockPos pos, BlockPos size, Block[][][] blocks) {
		this.pos = pos;
		this.size = size;
		this.blocks = new Block[this.size.getX()][this.size.getY()][this.size.getZ()];
		for (int x = 0; x < this.size.getX(); x++) {
			for (int y = 0; y < this.size.getY(); y++) {
				for (int z = 0; z < this.size.getZ(); z++) {
					if (x < blocks.length && y < blocks[x].length && z < blocks[x][y].length) {
						this.blocks[x][y][z] = blocks[x][y][z];
					} else {
						this.blocks[x][y][z] = null;
					}
				}
			}
		}
	}

	@Override
	public void generate(World world) {
		for (BlockPos.MutableBlockPos position : BlockPos.getAllInBoxMutable(BlockPos.ORIGIN, this.size.add(-1, -1, -1))) {
			Block block = this.blocks[position.getX()][position.getY()][position.getZ()];
			if (block != null) {
				world.setBlockState(position.toImmutable(), block.getDefaultState(), 2);
			}
		}
	}

	@Override
	public boolean canGenerate(World world) {
		return world.isAreaLoaded(this.pos, this.pos.add(this.size));
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setString("id", "blockPart");
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setTag("size", NBTUtil.createPosTag(this.size));

		NBTTagList nbtTagList = new NBTTagList();
		for (int x = 0; x < this.size.getX(); x++) {
			for (int y = 0; y < this.size.getY(); y++) {
				for (int z = 0; z < this.size.getZ(); z++) {
					NBTTagCompound tag = new NBTTagCompound();
					Block block = this.blocks[x][y][z];
					if (block != null) {
						tag.setString("block", block.getRegistryName().toString());
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

		NBTTagList nbtTagList = compound.getTagList("blocks", 10);
		for (int x = 0; x < this.size.getX(); x++) {
			for (int y = 0; y < this.size.getY(); y++) {
				for (int z = 0; z < this.size.getZ(); z++) {
					NBTTagCompound tag = nbtTagList.getCompoundTagAt(x * this.size.getY() * this.size.getZ() + y * this.size.getZ() + z);
					if (tag.hasKey("block")) {
						this.blocks[x][y][z] = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("block")));
					} else {
						this.blocks[x][y][z] = null;
					}
				}
			}
		}
	}

	public static List<BlockPart> split(Map<BlockPos, Block> map) {
		return BlockPart.split(new ArrayList(map.entrySet()));
	}

	public static List<BlockPart> split(BlockPos pos, Map<BlockPos, Block> map) {
		return BlockPart.split(pos, new ArrayList(map.entrySet()));
	}

	public static List<BlockPart> split(List<Map.Entry<BlockPos, Block>> entryList) {
		if (!entryList.isEmpty()) {
			int startX = entryList.get(0).getKey().getX();
			int startY = entryList.get(0).getKey().getY();
			int startZ = entryList.get(0).getKey().getZ();
			int endX = startX;
			int endY = startY;
			int endZ = startZ;

			for (Map.Entry<BlockPos, Block> entry : entryList) {
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

			Block[][][] blocks = new Block[endX - startX][endY - startY][endZ - startZ];

			for (Map.Entry<BlockPos, Block> entry : entryList) {
				BlockPos pos = entry.getKey();
				blocks[pos.getX() - startX][pos.getY() - startY][pos.getZ() - startZ] = entry.getValue();
			}

			return BlockPart.split(new BlockPos(startX, startY, startZ), blocks);
		}

		return Collections.emptyList();
	}

	public static List<BlockPart> split(BlockPos pos, List<Map.Entry<BlockPos, Block>> entryList) {
		if (!entryList.isEmpty()) {
			int endX = entryList.get(0).getKey().getX();
			int endY = entryList.get(0).getKey().getY();
			int endZ = entryList.get(0).getKey().getZ();

			for (Map.Entry<BlockPos, Block> entry : entryList) {
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

			Block[][][] blocks = new Block[endX - pos.getX()][endY - pos.getY()][endZ - pos.getZ()];

			for (Map.Entry<BlockPos, Block> entry : entryList) {
				BlockPos position = entry.getKey();
				blocks[position.getX()][position.getY()][position.getZ()] = entry.getValue();
			}

			return BlockPart.split(pos, blocks);
		}

		return Collections.emptyList();
	}

	public static List<BlockPart> split(BlockPos pos, Block[][][] blockArray) {
		List<BlockPart> list = new ArrayList<>();

		if (blockArray.length > 0 && blockArray[0].length > 0 && blockArray[0][0].length > 0) {
			int xIterations = blockArray.length / 16;
			int yIterations = blockArray[0].length / 16;
			int zIterations = blockArray[0][0].length / 16;

			for (int x = 0; x <= xIterations; x++) {
				for (int y = 0; y <= yIterations; y++) {
					for (int z = 0; z <= zIterations; z++) {
						BlockPos partStartPos = pos.add(x * 16, y * 16, z * 16);
						BlockPos partEndPos = partStartPos.add(x == xIterations ? x % 16 : 16, y == yIterations ? y % 16 : 16, z == zIterations ? z % 16 : 16);
						BlockPos partSize = partEndPos.subtract(partStartPos);
						BlockPos partOffset = partStartPos.subtract(pos);
						Block[][][] blocks = new Block[partSize.getX()][partSize.getY()][partSize.getZ()];

						for (int x1 = 0; x1 < partSize.getX(); x1++) {
							for (int y1 = 0; y1 < partSize.getY(); y1++) {
								for (int z1 = 0; z1 < partSize.getZ(); z1++) {
									int x2 = partOffset.getX() + x1;
									int y2 = partOffset.getY() + y1;
									int z2 = partOffset.getZ() + z1;
									if (x2 < blockArray.length && y2 < blockArray[x2].length && z2 < blockArray[x2][y2].length) {
										blocks[x1][y1][z1] = blockArray[x2][y2][z2];
									}
								}
							}
						}

						list.add(new BlockPart(partStartPos, partSize, blocks));
					}
				}
			}
		}

		return list;
	}

}
