package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class VolcanoPart implements IStructure {
	
	private int partStartX;
	private int partStartY;
	private int partStartZ;
	private int partSizeX = 16;
	private int partSizeY = 16;
	private int partSizeZ = 16;
	
	private Block[][][] blocks = new Block[16][16][16];

	public VolcanoPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void generate(World world) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canGenerate(World world) {
		return world.isAreaLoaded(new BlockPos(this.partStartX, this.partStartY, this.partStartZ), new BlockPos(this.partStartX + this.partSizeX, this.partStartY + this.partSizeY, this.partStartZ + this.partSizeZ));
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		
		compound.setTag("start", NBTUtil.createPosTag(new BlockPos(partStartX, partStartY, partStartZ)));
		
		NBTTagList blocksTag = compound.getTagList("blocks", Constants.NBT.TAG_COMPOUND);
		Map<Block, Set<BlockPos>> blockMap = new HashMap<>();
		for(int ix = 0; ix < 16; ix++) {
			for(int iy = 0; iy < 16; iy++) {
				for(int iz = 0; iz < 16; iz++) {
					//No block for this location
					if(blocks[ix][iy][iz] == null) {
						continue;
					}
					Set<BlockPos> positions = blockMap.getOrDefault(blocks[ix][iy][iz], new HashSet<BlockPos>());
					positions.add(new BlockPos(ix,iy,iz));
					blockMap.put(blocks[ix][iy][iz], positions);
				}
			}
		}
		for(Map.Entry<Block, Set<BlockPos>> entry : blockMap.entrySet()) {
			NBTTagCompound blockCompound = new NBTTagCompound();
			blockCompound.setString("block", entry.getKey().getRegistryName().toString());
			NBTTagList positions = blockCompound.getTagList("positions", Constants.NBT.TAG_COMPOUND);
			for(BlockPos p : entry.getValue()) {
				positions.appendTag(NBTUtil.createPosTag(p));
			}
			blockCompound.setTag("positions", positions);
			blocksTag.appendTag(blockCompound);
		}
		compound.setTag("blocks", blocksTag);
		
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		BlockPos startTag = NBTUtil.getPosFromTag(compound.getCompoundTag("start"));
		this.partStartX = startTag.getX();
		this.partStartY = startTag.getY();
		this.partStartZ = startTag.getZ();
		
		NBTTagList blocksTag = compound.getTagList("blocks", Constants.NBT.TAG_COMPOUND);
		blocksTag.forEach(new Consumer<NBTBase>() {

			@Override
			public void accept(NBTBase t) {
				if(t instanceof NBTTagCompound) {
					NBTTagCompound tag = (NBTTagCompound) t; 
					Block b = Block.REGISTRY.getObject(new ResourceLocation(tag.getString("block")));
					NBTTagList positions = tag.getTagList("positions", Constants.NBT.TAG_COMPOUND);
					positions.forEach(new Consumer<NBTBase>() {

						@Override
						public void accept(NBTBase t2) {
							if(t2 instanceof NBTTagCompound) {
								BlockPos p = NBTUtil.getPosFromTag((NBTTagCompound) t2);
								blocks[p.getX()][p.getY()][p.getZ()] = b;
							}
						}
					});
				}
			}
		});
	}

}
