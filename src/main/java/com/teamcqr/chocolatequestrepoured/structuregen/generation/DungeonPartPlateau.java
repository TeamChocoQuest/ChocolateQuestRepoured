package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class DungeonPartPlateau extends AbstractDungeonPart {

	private IBlockState supportHillBlock;
	private IBlockState supportHillTopBlock;
	private int wallSize;
	private int x1;
	private int z1;
	private Perlin3D perlin1;
	private Perlin3D perlin2;

	public DungeonPartPlateau(World world, DungeonGenerator dungeonGenerator) {
		this(world, dungeonGenerator, 0, 0, 0, 0, 0, Blocks.STONE.getDefaultState(), Blocks.GRASS.getDefaultState(), 0);
	}

	public DungeonPartPlateau(World world, DungeonGenerator dungeonGenerator, int startX, int startZ, int endX, int endY, int endZ, IBlockState supportHillBlock, IBlockState supportHillTopBlock, int wallSize) {
		super(world, dungeonGenerator, new BlockPos(Math.min(startX, endX) - wallSize, endY, Math.min(startZ, endZ) - wallSize));
		this.maxPos = new BlockPos(Math.max(startX, endX) + wallSize, endY, Math.max(startZ, endZ) + wallSize);
		this.supportHillBlock = supportHillBlock;
		this.supportHillTopBlock = supportHillTopBlock;
		this.wallSize = wallSize;
		this.x1 = this.minPos.getX();
		this.z1 = this.minPos.getZ();
		Random rand = new Random();
		this.perlin1 = new Perlin3D(this.world.getSeed(), this.wallSize, rand);
		this.perlin2 = new Perlin3D(this.world.getSeed(), this.wallSize * 4, rand);
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = super.writeToNBT();
		compound.setString("supportHillBlock", this.supportHillBlock.getBlock().getRegistryName().toString());
		compound.setInteger("supportHillBlockMeta", this.supportHillBlock.getBlock().getMetaFromState(this.supportHillBlock));
		compound.setString("supportHillTopBlock", this.supportHillTopBlock.getBlock().getRegistryName().toString());
		compound.setInteger("supportHillTopBlockMeta", this.supportHillTopBlock.getBlock().getMetaFromState(this.supportHillTopBlock));
		compound.setInteger("wallSize", this.wallSize);
		compound.setInteger("x1", this.x1);
		compound.setInteger("z1", this.z1);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		Block b1 = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("supportHillBlock")));
		if (b1 != null) {
			this.supportHillTopBlock = b1.getStateFromMeta(compound.getInteger("supportHillBlockMeta"));
		} else {
			this.supportHillTopBlock = Blocks.STONE.getDefaultState();
		}
		Block b2 = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("supportHillBlockMeta")));
		if (b2 != null) {
			this.supportHillBlock = b2.getStateFromMeta(compound.getInteger("supportHillTopBlockMeta"));
		} else {
			this.supportHillBlock = Blocks.GRASS.getDefaultState();
		}
		this.wallSize = compound.getInteger("wallSize");
		this.x1 = compound.getInteger("x1");
		this.z1 = compound.getInteger("z1");
		Random rand = new Random();
		this.perlin1 = new Perlin3D(this.world.getSeed(), this.wallSize, rand);
		this.perlin2 = new Perlin3D(this.world.getSeed(), this.wallSize * 4, rand);
	}

	@Override
	public String getId() {
		return DUNGEON_PART_PLATEAU_ID;
	}

	@Override
	public void generateNext() {
		if (this.x1 <= this.maxPos.getX()) {
			int posY = this.world.getTopSolidOrLiquidBlock(new BlockPos(this.x1, 0, this.z1)).getY();
			int i = Math.max((this.maxPos.getY() - 1) - posY, 1);
			int y1 = posY;

			while (y1 < this.maxPos.getY()) {
				if ((this.x1 >= this.minPos.getX() + this.wallSize) && (this.x1 <= this.maxPos.getX() - this.wallSize) && (this.z1 >= this.minPos.getZ() + this.wallSize) && (this.z1 <= this.maxPos.getZ() - this.wallSize)) {
					this.world.setBlockState(new BlockPos(this.x1, y1, this.z1), this.supportHillBlock, 18);
				} else {
					float noiseVar = (y1 - (this.maxPos.getY() - 1)) / (i * 1.5F);

					noiseVar += Math.max((this.wallSize - (this.x1 - this.minPos.getX())) / 8.0F, 0.0F);
					noiseVar += Math.max((this.wallSize - ((this.maxPos.getX() + 1) - this.x1)) / 8.0F, 0.0F);

					noiseVar += Math.max((this.wallSize - (this.z1 - this.minPos.getZ())) / 8.0F, 0.0F);
					noiseVar += Math.max((this.wallSize - ((this.maxPos.getZ() + 1) - this.z1)) / 8.0F, 0.0F);

					if (noiseVar / 3.0D + (y1 - posY) / i * 0.25D >= 0.5D) {
						break;
					}

					double value = (this.perlin1.getNoiseAt(this.x1, y1, this.z1) + this.perlin2.getNoiseAt(this.x1, y1, this.z1) + noiseVar) / 3.0D + (y1 - posY) / i * 0.25D;

					if (value < 0.5D) {
						this.world.setBlockState(new BlockPos(this.x1, y1, this.z1), this.supportHillBlock, 18);
					} else {
						break;
					}
				}
				y1++;
			}

			if (y1 <= this.maxPos.getY()) {
				this.world.setBlockState(new BlockPos(this.x1, y1, this.z1), this.supportHillTopBlock, 18);
			}

			this.z1++;
			if (this.z1 > this.maxPos.getZ()) {
				this.z1 = this.minPos.getZ();
				this.x1++;
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return this.x1 > this.maxPos.getX();
	}

}
