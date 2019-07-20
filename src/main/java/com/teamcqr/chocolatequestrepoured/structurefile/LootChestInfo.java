package com.teamcqr.chocolatequestrepoured.structurefile;

import com.teamcqr.chocolatequestrepoured.util.NBTUtil;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
class LootChestInfo {

	private int type; //The "type" of the chest, e.g.: 0 = utility, 1 = food, 2 = weapons, 3 = armor, 4 = resources, 5 = bossloot, 6 = defaultLoot, 7 = endcity loot, 8 = jungle loot, ...  
	private BlockPos position;
	private int rotation; // 0 = north, 1 = east, 2 = south, 3 = west
	private boolean isDoubleChest;
	private boolean redstoneChest;
	
	public LootChestInfo(Block chestBlock, BlockPos position, int type) {
		//TODO: Check if chest is double chest
		//TODO: Check chests rotation
		this.isDoubleChest = false;
		this.redstoneChest = false;
		this.rotation = 0;
		this.position = position;
		//TODO: Get Type of chest via comparing blocktypes and then set the type
		this.type = type;
	}
	
	public NBTTagCompound getAsNBTTag() {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setString("type", "lootchest");
		tag.setBoolean("doublechest", this.isDoubleChest);
		tag.setBoolean("redstonechest", this.redstoneChest);
		tag.setInteger("rotation", this.rotation);
		tag.setInteger("loottable", this.type);
		
		NBTTagCompound posTag = NBTUtil.BlockPosToNBTTag(this.position);
		
		tag.setTag("position", posTag);
		
		return tag;
	}
	
	public LootChestInfo(NBTTagCompound nbtTag) {
		if(nbtTag.getString("type").equalsIgnoreCase("lootchest")) {
			this.isDoubleChest = nbtTag.getBoolean("doublechest");
			this.rotation = nbtTag.getInteger("rotation");
			this.redstoneChest = nbtTag.getBoolean("redstonechest");
			this.type = nbtTag.getInteger("loottable");
			
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
	
	public Boolean isRedstoneChest() {
		return this.redstoneChest;
	}
	
	public int getLootType() {
		return this.type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
