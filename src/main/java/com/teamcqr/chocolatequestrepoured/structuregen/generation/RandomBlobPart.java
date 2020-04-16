package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class RandomBlobPart implements IStructure {

	private final Random rand = new Random();
	private Block block;
	private BlockPos startPos;
	private BlockPos size;
	private BlockPos partOffset;
	private BlockPos partSize;
	private int wallSize;

	public RandomBlobPart(NBTTagCompound compound) {
		this.readFromNBT(compound);
	}

	public RandomBlobPart(Block block, BlockPos startPos, BlockPos size, BlockPos partOffset, BlockPos partSize, int wallSize) {
		this.block = block;
		this.startPos = startPos;
		this.size = size;
		this.partOffset = partOffset;
		this.partSize = partSize;
		this.wallSize = wallSize;
	}

	@Override
	public void generate(World world, ProtectedRegion protectedRegion) {
		long seed = WorldDungeonGenerator.getSeed(world, this.startPos.getX() >> 4, this.startPos.getZ() >> 4);
		Perlin3D perlinNoise1 = new Perlin3D(seed, 8, this.rand);
		Perlin3D perlinNoise2 = new Perlin3D(seed, 32, this.rand);

		for (int x = 0; x < this.partSize.getX(); x++) {
			for (int y = 0; y < this.partSize.getY(); y++) {
				for (int z = 0; z < this.partSize.getZ(); z++) {
					int x1 = this.partOffset.getX() + x;
					int y1 = this.partOffset.getY() + y;
					int z1 = this.partOffset.getZ() + z;
					float noise = Math.max(0.0F, 2.0F - (float) (this.size.getY() - y1) / 4.0F);
					noise += Math.max(0.0F, (float) this.wallSize - (float) x1 / 2.0F);
					noise += Math.max(0.0F, (float) this.wallSize - (float) (this.size.getX() - x1) / 2.0F);
					noise += Math.max(0.0F, (float) this.wallSize - (float) z1 / 2.0F);
					noise += Math.max(0.0F, (float) this.wallSize - (float) (this.size.getZ() - z1) / 2.0F);

					double perlin1 = perlinNoise1.getNoiseAt(this.startPos.getX() + x1, this.startPos.getY() + y1, this.startPos.getZ() + z1);
					double perlin2 = perlinNoise2.getNoiseAt(this.startPos.getX() + x1, this.startPos.getY() + y1, this.startPos.getZ() + z1);

					if ((perlin1 * perlin2 * (double) noise) < 0.5D) {
						world.setBlockState(this.startPos.add(x1, y1, z1), this.block.getDefaultState(), 2);
					}
				}
			}
		}
	}

	@Override
	public boolean canGenerate(World world) {
		return world.isAreaLoaded(this.startPos.add(this.partOffset), this.startPos.add(this.partOffset).add(this.partSize));
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setString("id", "randomBlockPart");
		compound.setString("block", this.block.getRegistryName().toString());
		compound.setTag("startPos", NBTUtil.createPosTag(this.startPos));
		compound.setTag("size", NBTUtil.createPosTag(this.size));
		compound.setTag("partOffset", NBTUtil.createPosTag(this.partOffset));
		compound.setTag("partSize", NBTUtil.createPosTag(this.partSize));
		compound.setInteger("wallSize", this.wallSize);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("block")));
		if (this.block == null) {
			this.block = Blocks.AIR;
		}
		this.startPos = NBTUtil.getPosFromTag(compound.getCompoundTag("startPos"));
		this.size = NBTUtil.getPosFromTag(compound.getCompoundTag("size"));
		this.partOffset = NBTUtil.getPosFromTag(compound.getCompoundTag("partOffset"));
		this.partSize = NBTUtil.getPosFromTag(compound.getCompoundTag("partSize"));
		this.wallSize = compound.getInteger("wallSize");
	}

	@Override
	public BlockPos getPos() {
		return this.startPos.add(this.partOffset);
	}

	@Override
	public BlockPos getSize() {
		return this.partSize;
	}

}
