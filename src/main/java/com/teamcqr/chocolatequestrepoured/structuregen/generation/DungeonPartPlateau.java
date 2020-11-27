package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.Perlin3D;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class DungeonPartPlateau extends AbstractDungeonPart {

	private IBlockState supportHillBlock;
	private IBlockState supportHillTopBlock;
	private int wallSize;
	private BlockPos.MutableBlockPos pos1 = new BlockPos.MutableBlockPos();
	private BlockPos.MutableBlockPos pos2 = new BlockPos.MutableBlockPos();
	private Perlin3D perlin1;
	private Perlin3D perlin2;

	public DungeonPartPlateau(World world, DungeonGenerator dungeonGenerator) {
		this(world, dungeonGenerator, 0, 0, 0, 0, 0, null, null, 0);
	}

	public DungeonPartPlateau(World world, DungeonGenerator dungeonGenerator, int startX, int startZ, int endX, int endY, int endZ, IBlockState supportHillBlock, IBlockState supportHillTopBlock, int wallSize) {
		super(world, dungeonGenerator, new BlockPos(Math.min(startX, endX) - wallSize, endY, Math.min(startZ, endZ) - wallSize));
		this.maxPos = new BlockPos(Math.max(startX, endX) + wallSize, endY, Math.max(startZ, endZ) + wallSize);
		this.supportHillBlock = supportHillBlock;
		this.supportHillTopBlock = supportHillTopBlock;
		this.wallSize = wallSize;
		this.pos1.setPos(this.minPos.getX(), 0, this.minPos.getZ());
		this.pos2.setPos(this.minPos.getX(), 0, this.minPos.getZ());
		this.perlin1 = new Perlin3D(this.world.getSeed(), this.wallSize);
		this.perlin2 = new Perlin3D(this.world.getSeed(), this.wallSize * 4);
	}

	@Override
	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = super.writeToNBT();
		if (this.supportHillBlock != null) {
			compound.setString("supportHillBlock", this.supportHillBlock.getBlock().getRegistryName().toString());
			compound.setInteger("supportHillBlockMeta", this.supportHillBlock.getBlock().getMetaFromState(this.supportHillBlock));
		}
		if (this.supportHillTopBlock != null) {
			compound.setString("supportHillTopBlock", this.supportHillTopBlock.getBlock().getRegistryName().toString());
			compound.setInteger("supportHillTopBlockMeta", this.supportHillTopBlock.getBlock().getMetaFromState(this.supportHillTopBlock));
		}
		compound.setInteger("wallSize", this.wallSize);
		compound.setInteger("x1", this.pos1.getX());
		compound.setInteger("z1", this.pos1.getZ());
		compound.setInteger("x2", this.pos2.getX());
		compound.setInteger("z2", this.pos2.getZ());
		return compound;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.supportHillBlock = null;
		if (compound.hasKey("supportHillBlock", Constants.NBT.TAG_STRING)) {
			Block b1 = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("supportHillBlock")));
			if (b1 != null) {
				this.supportHillBlock = b1.getStateFromMeta(compound.getInteger("supportHillBlockMeta"));
			}
		}
		this.supportHillTopBlock = null;
		if (compound.hasKey("supportHillTopBlock", Constants.NBT.TAG_STRING)) {
			Block b2 = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("supportHillTopBlock")));
			if (b2 != null) {
				this.supportHillTopBlock = b2.getStateFromMeta(compound.getInteger("supportHillTopBlockMeta"));
			}
		}
		this.wallSize = compound.getInteger("wallSize");
		this.pos1.setPos(compound.getInteger("x1"), 0, compound.getInteger("z1"));
		this.pos2.setPos(compound.getInteger("x2"), 0, compound.getInteger("z2"));
		this.perlin1 = new Perlin3D(this.world.getSeed(), this.wallSize);
		this.perlin2 = new Perlin3D(this.world.getSeed(), this.wallSize * 4);
	}

	@Override
	public String getId() {
		return DUNGEON_PART_PLATEAU_ID;
	}

	@Override
	public void generateNext() {
		if (this.pos1.getX() <= this.maxPos.getX()) {
			Biome biome = this.world.getBiome(this.pos1);
			IBlockState state1 = this.supportHillBlock != null ? this.supportHillBlock : biome.fillerBlock;
			IBlockState state2 = this.supportHillTopBlock != null ? this.supportHillTopBlock : biome.topBlock;
			int posY = this.world.getTopSolidOrLiquidBlock(new BlockPos(this.pos1.getX(), 0, this.pos1.getZ())).getY();
			int i = Math.max((this.maxPos.getY() - 1) - posY, 1);
			int y1 = posY;

			while (y1 < this.maxPos.getY()) {
				if ((this.pos1.getX() >= this.minPos.getX() + this.wallSize) && (this.pos1.getX() <= this.maxPos.getX() - this.wallSize) && (this.pos1.getZ() >= this.minPos.getZ() + this.wallSize) && (this.pos1.getZ() <= this.maxPos.getZ() - this.wallSize)) {
					this.pos1.setY(y1);
					this.world.setBlockState(this.pos1, state1, 18);
				} else {
					float noiseVar = (y1 - (this.maxPos.getY() - 1)) / (i * 1.5F);

					noiseVar += Math.max((this.wallSize - (this.pos1.getX() - this.minPos.getX())) / 8.0F, 0.0F);
					noiseVar += Math.max((this.wallSize - ((this.maxPos.getX() + 1) - this.pos1.getX())) / 8.0F, 0.0F);

					noiseVar += Math.max((this.wallSize - (this.pos1.getZ() - this.minPos.getZ())) / 8.0F, 0.0F);
					noiseVar += Math.max((this.wallSize - ((this.maxPos.getZ() + 1) - this.pos1.getZ())) / 8.0F, 0.0F);

					if (noiseVar / 3.0D + (y1 - posY) / i * 0.25D >= 0.5D) {
						break;
					}

					double value = (this.perlin1.getNoiseAt(this.pos1.getX(), y1, this.pos1.getZ()) + this.perlin2.getNoiseAt(this.pos1.getX(), y1, this.pos1.getZ()) + noiseVar) / 3.0D + (y1 - posY) / i * 0.25D;

					if (value < 0.5D) {
						this.world.setBlockState(new BlockPos(this.pos1.getX(), y1, this.pos1.getZ()), state1, 18);
					} else {
						break;
					}
				}
				y1++;
			}

			if (y1 <= this.maxPos.getY()) {
				this.world.setBlockState(new BlockPos(this.pos1.getX(), y1, this.pos1.getZ()), state2, 18);
			}

			this.pos1.setPos(this.pos1.getX(), this.pos1.getY(), this.pos1.getZ() + 1);
			if (this.pos1.getZ() > this.maxPos.getZ()) {
				this.pos1.setPos(this.pos1.getX() + 1, this.pos1.getY(), this.minPos.getZ());
			}
		} else if (this.pos2.getX() <= this.maxPos.getX()) {
			this.pos2.setY(255);

			if (this.world.getBlockState(this.pos2).getBlock() == Blocks.AIR) {
				this.pos2.setY(this.pos2.getY() - 1);

				while (this.pos2.getY() > 0) {
					IBlockState state = this.world.getBlockState(this.pos2);

					if (state.getBlock() == Blocks.AIR) {
						this.pos2.setY(this.pos2.getY() - 1);
					} else {
						if (this.pos2.getY() == this.maxPos.getY()) {
							Biome biome = this.world.getBiome(this.pos2);
							if (DungeonGenUtils.percentageRandom(biome.decorator.grassPerChunk / 512.0D)) {
								biome.getRandomWorldGenForGrass(this.world.rand).generate(this.world, this.world.rand, this.pos2);
							}
						}
						break;
					}
				}
			}

			this.pos2.setPos(this.pos2.getX(), this.pos2.getY(), this.pos2.getZ() + 1);
			if (this.pos2.getZ() > this.maxPos.getZ()) {
				this.pos2.setPos(this.pos2.getX() + 1, this.pos2.getY(), this.minPos.getZ());
			}
		}
	}

	@Override
	public boolean isGenerated() {
		return this.pos2.getX() > this.maxPos.getX();
	}

}
