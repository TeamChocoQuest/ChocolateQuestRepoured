package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class LootChestInfo {

	private BlockPos position;
	private EnumFacing facing;
	private ResourceLocation lootTable;

	public LootChestInfo(BlockPos position, EnumFacing facing, ResourceLocation lootTable) {
		this.position = position;
		this.facing = facing;
		this.lootTable = lootTable;
	}

	public LootChestInfo(NBTTagCompound compound) {
		this.position = NBTUtil.getPosFromTag(compound.getCompoundTag("position"));
		this.facing = EnumFacing.getHorizontal(compound.getInteger("facing"));
		this.lootTable = new ResourceLocation(compound.getString("loottable"));
	}

	public NBTTagCompound getAsNBTTag() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setTag("position", NBTUtil.createPosTag(this.position));
		compound.setInteger("facing", this.facing.getHorizontalIndex());
		compound.setString("loottable", this.lootTable.toString());

		return compound;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	public EnumFacing getFacing() {
		return this.facing;
	}

	public ResourceLocation getLootTable() {
		return this.lootTable;
	}

}
