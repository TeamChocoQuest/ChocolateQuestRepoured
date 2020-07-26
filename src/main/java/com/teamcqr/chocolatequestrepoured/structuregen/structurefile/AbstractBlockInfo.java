package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractBlockInfo implements IGeneratable {

	public static final int BLOCK_INFO_ID = 0;
	public static final int BANNER_INFO_ID = 1;
	public static final int BOSS_INFO_ID = 2;
	public static final int NEXUS_INFO_ID = 3;
	public static final int CHEST_INFO_ID = 4;
	public static final int SPAWNER_INFO_ID = 5;

	// protected BlockPos pos;
	private int pos;

	public AbstractBlockInfo(BlockPos pos) {
		// this.pos = pos;
		this.pos = (pos.getX() << 20) | ((pos.getY() & 0xFF) << 12) | (pos.getZ() & 0xFFF);
	}

	public AbstractBlockInfo(BlockPos pos, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		// this.pos = pos;
		this.pos = (pos.getX() << 20) | ((pos.getY() & 0xFF) << 12) | (pos.getZ() & 0xFFF);
		this.readFromNBT(nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	public abstract int getId();

	public NBTTagIntArray writeToNBT(BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		return new NBTTagIntArray(new int[] { this.getId() });
	}

	public void readFromNBT(NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {

	}

	@Nullable
	public static AbstractBlockInfo create(BlockPos pos, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		if (nbtTagIntArray.getIntArray().length == 0) {
			return null;
		}
		switch (nbtTagIntArray.getIntArray()[0]) {
		case BLOCK_INFO_ID:
			return new BlockInfo(pos, nbtTagIntArray, blockStatePalette, compoundTagList);
		case BANNER_INFO_ID:
			return new BlockInfoBanner(pos, nbtTagIntArray, blockStatePalette, compoundTagList);
		case BOSS_INFO_ID:
			return new BlockInfoBoss(pos, nbtTagIntArray);
		case NEXUS_INFO_ID:
			return new BlockInfoForceFieldNexus(pos, nbtTagIntArray);
		case CHEST_INFO_ID:
			return new BlockInfoLootChest(pos, nbtTagIntArray);
		case SPAWNER_INFO_ID:
			return new BlockInfoSpawner(pos, nbtTagIntArray, blockStatePalette, compoundTagList);
		default:
			return null;
		}
	}

	public BlockPos getPos() {
		// return this.pos;
		return new BlockPos((this.pos >> 20), ((this.pos >> 12) & 0xFF), (this.pos & 0xFFF));
	}

}
