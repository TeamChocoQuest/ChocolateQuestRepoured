package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CoverBlockPart implements IStructure {
	
	private Block coverBlock;
	private BlockPos startPos;
	private BlockPos size;
	private BlockPos partSize;
	private BlockPos partOffset;

	public CoverBlockPart(NBTTagCompound compound) {
		this.readFromNBT(compound);
	}
	
	public CoverBlockPart(Block coverBlock, BlockPos startPos, BlockPos size, BlockPos partOffset, BlockPos partSize) {
		this.coverBlock = coverBlock;
		this.startPos = startPos;
		this.size = size;
		this.partOffset = partOffset;
		this.partSize = partSize;
	}

	@Override
	public void generate(World world) {
		for (int x = 0; x < this.partSize.getX(); x++) {
			for (int z = 0; z < this.partSize.getZ(); z++) {
				int x1 = this.partOffset.getX() + x;
				int z1 = this.partOffset.getZ() + z;

				int y1 = world.getTopSolidOrLiquidBlock(this.startPos.add(x1, 0, z1)).getY();
				if (!Block.isEqualTo(world.getBlockState(this.startPos.add(x1, y1, z1).down()).getBlock(), this.coverBlock)) {
					world.setBlockState(this.startPos.add(x1, y1, z1), this.coverBlock.getDefaultState(), 2);
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

		compound.setString("id", "coverBlockPart");
		compound.setString("coverBlock", this.coverBlock.getRegistryName().toString());
		compound.setTag("startPos", NBTUtil.createPosTag(this.startPos));
		compound.setTag("size", NBTUtil.createPosTag(this.size));
		compound.setTag("partOffset", NBTUtil.createPosTag(this.partOffset));
		compound.setTag("partSize", NBTUtil.createPosTag(this.partSize));

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.coverBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("coverBlock")));
		if (this.coverBlock == null) {
			this.coverBlock = Blocks.AIR;
		}
		this.startPos = NBTUtil.getPosFromTag(compound.getCompoundTag("startPos"));
		this.size = NBTUtil.getPosFromTag(compound.getCompoundTag("size"));
		this.partOffset = NBTUtil.getPosFromTag(compound.getCompoundTag("partOffset"));
		this.partSize = NBTUtil.getPosFromTag(compound.getCompoundTag("partSize"));
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
