package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SupportHillPart implements IStructure {

	private final Random rand = new Random();
	private BlockPos startPos;
	private int sizeX;
	private int sizeZ;
	private int partStartX;
	private int partStartZ;
	private int partSizeX;
	private int partSizeZ;
	private int wallSize;
	private Block structureBlock;
	private Block structureTopBlock;

	public SupportHillPart(NBTTagCompound compound) {
		this.readFromNBT(compound);
	}

	public SupportHillPart(BlockPos startPos, int sizeX, int sizeZ, int partStartX, int partStartZ, int partSizeX, int partSizeZ, int wallSize, Block structureBlock, Block structureTopBlock) {
		this.startPos = startPos;
		this.sizeX = sizeX;
		this.sizeZ = sizeZ;
		this.partStartX = partStartX;
		this.partStartZ = partStartZ;
		this.partSizeX = partSizeX;
		this.partSizeZ = partSizeZ;
		this.wallSize = wallSize;
		this.structureBlock = structureBlock;
		this.structureTopBlock = structureTopBlock;
	}

	@Override
	public void generate(World world) {
		int offsetX = this.partStartX - this.startPos.getX();
		int offsetZ = this.partStartZ - this.startPos.getZ();
		Perlin3D p = new Perlin3D(world.getSeed(), this.wallSize, this.rand);
		Perlin3D p2 = new Perlin3D(world.getSeed(), this.wallSize * 4, this.rand);

		for (int x = 0; x < this.partSizeX; x++) {
			for (int z = 0; z < this.partSizeZ; z++) {
				int posY = world.getTopSolidOrLiquidBlock(new BlockPos(this.partStartX + x, 0, this.partStartZ + z)).getY();
				int maxHeight = this.startPos.getY() - PlateauBuilder.TOP_LAYER_HEIGHT - posY;

				for (int y = 0; y <= maxHeight; y++) {
					if ((offsetX + x > this.wallSize) && (offsetZ + z > this.wallSize) && (offsetX + x < this.sizeX - this.wallSize) && (offsetZ + z < this.sizeZ - this.wallSize)) {
						// This generates the "cube" that goes under the structure
						world.setBlockState(new BlockPos(this.partStartX + x, posY + y, this.partStartZ + z), this.structureBlock.getDefaultState(), 2);
					} else {
						// This generates the fancy "curved" walls around the cube
						float noiseVar = (float) (y - maxHeight) / ((float) Math.max(1, maxHeight) * 1.5F);

						noiseVar += Math.max(0.0F, (this.wallSize - (offsetX + x)) / 8.0F);
						noiseVar += Math.max(0.0F, (this.wallSize - (this.sizeX - (offsetX + x))) / 8.0F);

						noiseVar += Math.max(0.0F, (this.wallSize - (offsetZ + z)) / 8.0F);
						noiseVar += Math.max(0.0F, (this.wallSize - (this.sizeZ - (offsetZ + z))) / 8.0F);
						double value = (p.getNoiseAt(this.partStartX + x, y, this.partStartZ + z) + p2.getNoiseAt(this.partStartX + x, y, this.partStartZ + z) + noiseVar) / 3.0D + y / (maxHeight == 0 ? 1 : maxHeight) * 0.25D;
						if (value < 0.5D) {
							world.setBlockState(new BlockPos(this.partStartX + x, posY + y, this.partStartZ + z), this.structureBlock.getDefaultState(), 2);
						}
					}
				}

				// This places the top layer blocks
				maxHeight = world.getTopSolidOrLiquidBlock(new BlockPos(this.partStartX + x, 0, this.partStartZ + z)).getY();
				if (maxHeight <= this.startPos.getY()) {
					world.setBlockState(new BlockPos(this.partStartX + x, maxHeight - 1, this.partStartZ + z), this.structureTopBlock.getDefaultState(), 2);
				}
			}
		}
	}

	@Override
	public boolean canGenerate(World world) {
		return world.isAreaLoaded(new BlockPos(this.partStartX, 0, this.partStartZ), new BlockPos(this.partStartX + this.partSizeX, 0, this.partStartZ + this.partSizeZ));
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("id", "supportHillPart");
		compound.setTag("startPos", NBTUtil.createPosTag(this.startPos));
		compound.setInteger("sizeX", this.sizeX);
		compound.setInteger("sizeZ", this.sizeZ);
		compound.setInteger("partStartX", this.partStartX);
		compound.setInteger("partStartZ", this.partStartZ);
		compound.setInteger("partSizeX", this.partSizeX);
		compound.setInteger("partSizeZ", this.partSizeZ);
		compound.setInteger("wallSize", this.wallSize);
		compound.setString("structureBlock", this.structureBlock.getRegistryName().toString());
		compound.setString("structureTopBlock", this.structureTopBlock.getRegistryName().toString());
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.startPos = NBTUtil.getPosFromTag(compound.getCompoundTag("startPos"));
		this.sizeX = compound.getInteger("sizeX");
		this.sizeZ = compound.getInteger("sizeZ");
		this.partStartX = compound.getInteger("partStartX");
		this.partStartZ = compound.getInteger("partStartZ");
		this.partSizeX = compound.getInteger("partSizeX");
		this.partSizeZ = compound.getInteger("partSizeZ");
		this.wallSize = compound.getInteger("wallSize");
		this.structureBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("structureBlock")));
		if (this.structureBlock == null) {
			this.structureBlock = Blocks.STONE;
		}
		this.structureTopBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("structureTopBlock")));
		if (this.structureTopBlock == null) {
			this.structureTopBlock = Blocks.GRASS;
		}
	}

}
