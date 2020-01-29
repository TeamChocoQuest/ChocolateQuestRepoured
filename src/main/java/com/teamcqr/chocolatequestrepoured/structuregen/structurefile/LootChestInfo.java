package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.structuregen.lootchests.ELootTable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class LootChestInfo {

	private BlockPos position;
	private EnumFacing facing;
	private ELootTable lootTable;

	public LootChestInfo(BlockPos position, EnumFacing facing, ELootTable lootTable) {
		this.position = position;
		this.facing = facing;
		this.lootTable = lootTable;
	}

	public LootChestInfo(NBTTagCompound compound) {
		this.position = NBTUtil.getPosFromTag(compound.getCompoundTag("pos"));
		this.facing = EnumFacing.getHorizontal(compound.getInteger("facing"));
		this.lootTable = ELootTable.valueOf(compound.getInteger("loottable"));
	}

	public NBTTagCompound getAsNBTTag() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setTag("position", NBTUtil.createPosTag(this.position));
		compound.setInteger("facing", this.facing.getHorizontalIndex());
		compound.setInteger("loottable", this.lootTable.getID());

		return compound;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	public EnumFacing getFacing() {
		return this.facing;
	}

	public ELootTable getLootTable() {
		return this.lootTable;
	}

}
