package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.util.NBTUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Copyright (c) 26.05.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */

class ForceFieldNexusInfo {

private BlockPos position;
	
	public ForceFieldNexusInfo(BlockPos location) {
		this.position = location;
	}
	
	public NBTTagCompound getAsNBTTag() {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setString("type", "forcefieldcoreinfo");
		
		NBTTagCompound posTag = NBTUtil.BlockPosToNBTTag(this.position);
		
		tag.setTag("position", posTag);
		
		
		return tag;
	}
	
	public ForceFieldNexusInfo(NBTTagCompound nbtTag) {
		if(nbtTag.getString("type").equalsIgnoreCase("forcefieldcoreinfo")) {
			
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
}
