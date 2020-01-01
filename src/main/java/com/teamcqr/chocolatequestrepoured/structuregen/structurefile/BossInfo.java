package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.util.NBTUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class BossInfo {

	private BlockPos position;

	public BossInfo(BlockPos position) {
		this.position = position;
	}

	public NBTTagCompound getAsNBTTag() {
		NBTTagCompound tag = new NBTTagCompound();

		tag.setString("type", "bossPos");

		NBTTagCompound posTag = NBTUtil.BlockPosToNBTTag(this.position);

		tag.setTag("position", posTag);

		return tag;
	}

	public BossInfo(NBTTagCompound nbtTag) {
		if (nbtTag.getString("type").equalsIgnoreCase("bossPos")) {

			NBTTagCompound posTag = nbtTag.getCompoundTag("position");
			int x = posTag.getInteger("x");
			int y = posTag.getInteger("y");
			int z = posTag.getInteger("z");

			this.position = new BlockPos(x, y, z);
		}
	}

	public BlockPos getPos() {
		return this.position;
	}

	public void addToPos(BlockPos offset) {
		this.position = this.position.add(offset);
	}

}
