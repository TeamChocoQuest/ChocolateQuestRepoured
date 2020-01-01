package com.teamcqr.chocolatequestrepoured.util.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

class DataEntryDungeon {

	private String dungeonName = "missingNo";
	private BlockPos pos = new BlockPos(0, 0, 0);

	public DataEntryDungeon(String name, BlockPos pos) {
		this.dungeonName = name;
		this.pos = pos;
	}

	public NBTTagCompound getNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("name", this.dungeonName);
		compound.setTag("position", NBTUtil.createPosTag(this.pos));

		return compound;
	}

}
