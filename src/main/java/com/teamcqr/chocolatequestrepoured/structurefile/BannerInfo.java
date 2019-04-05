package com.teamcqr.chocolatequestrepoured.structurefile;

import com.teamcqr.chocolatequestrepoured.util.NBTUtil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

class BannerInfo {
	
	private int bannerRotation = 0;
	private BlockPos position;
	private boolean onWall = false;
	
	public BannerInfo(BlockPos location, int rotation, boolean onWall) {
	
		if(!(0 <= rotation && rotation < 15)) {
			rotation -= (rotation / 16) * 16;
		}
		this.bannerRotation = rotation;
		this.onWall = onWall;
		this.position = location;
	}
	
	public NBTTagCompound getAsNBTTag() {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setString("type", "bannerPos");
		tag.setBoolean("onWall", this.onWall);
		
		NBTTagCompound posTag = NBTUtil.BlockPosToNBTTag(this.position);
		
		tag.setTag("position", posTag);
		
		tag.setInteger("rotation", this.bannerRotation);
		
		return tag;
	}
	
	public BannerInfo(NBTTagCompound nbtTag) {
		if(nbtTag.getString("type").equalsIgnoreCase("bannerPos")) {
			this.bannerRotation = nbtTag.getInteger("rotation");
			this.onWall = nbtTag.getBoolean("onWall");
			
			NBTTagCompound posTag = nbtTag.getCompoundTag("position");
			int x = posTag.getInteger("x");
			int y = posTag.getInteger("y");
			int z = posTag.getInteger("z");
			
			this.position = new BlockPos(x, y, z);
		}
	}
	public boolean isOnWall() {
		return this.onWall;
	}
	public BlockPos getPos() {
		return this.position;
	}

}
