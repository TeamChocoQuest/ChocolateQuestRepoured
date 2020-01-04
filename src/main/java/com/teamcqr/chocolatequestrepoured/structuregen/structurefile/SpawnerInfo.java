package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.objects.blocks.BlockSpawner;
import com.teamcqr.chocolatequestrepoured.util.NBTUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
class SpawnerInfo {

	private BlockPos position;
	// private TileEntitySpawner spawnerTile;
	private NBTTagCompound spawnerData = null;

	public SpawnerInfo(BlockSpawner spawnerBlock, BlockPos pos, World world, NBTTagCompound tileTag) {
		this.position = pos;
		if (!tileTag.hasNoTags()) {
			this.spawnerData = tileTag.copy();
		}
		// DONE: Fix Spawner data not saving ....
	}

	public SpawnerInfo(NBTTagCompound nbtTag) {
		if (nbtTag.getString("type").equalsIgnoreCase("cq_spawner")) {
			this.position = NBTUtil.BlockPosFromNBT(nbtTag.getCompoundTag("position"));
			this.spawnerData = nbtTag.getCompoundTag("data");
		}
	}

	public NBTTagCompound getSpawnerData() {
		return this.spawnerData;
	}

	public NBTTagCompound getAsNBTTag() {
		NBTTagCompound tag = new NBTTagCompound();

		tag.setString("type", "cq_spawner");

		this.appendPos(tag);

		NBTTagCompound spawnerTag = new NBTTagCompound();
		spawnerTag = this.spawnerData;

		tag.setTag("data", spawnerTag);

		return tag;
	}

	private void appendPos(NBTTagCompound compound) {
		compound.setTag("position", NBTUtil.BlockPosToNBTTag(this.position));
	}

	public BlockPos getPos() {
		return this.position;
	}

}
