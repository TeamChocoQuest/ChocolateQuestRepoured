package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class NBTUtil {

	public static NBTTagCompound BlockPosToNBTTag(BlockPos pos) {
		NBTTagCompound posTag = new NBTTagCompound();
		posTag.setInteger("x", pos.getX());
		posTag.setInteger("y", pos.getY());
		posTag.setInteger("z", pos.getZ());

		return posTag;
	}

	public static BlockPos BlockPosFromNBT(NBTTagCompound posTag) {
		int x = posTag.getInteger("x");
		int y = posTag.getInteger("y");
		int z = posTag.getInteger("z");

		return new BlockPos(x, y, z);
	}
}