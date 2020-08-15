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
	private short x;
	private short y;
	private short z;

	public AbstractBlockInfo(int x, int y, int z) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (short) z;
	}

	public AbstractBlockInfo(BlockPos pos) {
		// this.pos = pos;
		this.x = (short) pos.getX();
		this.y = (short) pos.getY();
		this.z = (short) pos.getZ();
	}

	public AbstractBlockInfo(int x, int y, int z, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		// this.pos = pos;
		this.x = (short) x;
		this.y = (short) y;
		this.z = (short) z;
		this.readFromNBT(nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	public AbstractBlockInfo(BlockPos pos, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		// this.pos = pos;
		this.x = (short) pos.getX();
		this.y = (short) pos.getY();
		this.z = (short) pos.getZ();
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
		return create(pos.getX(), pos.getY(), pos.getZ(), nbtTagIntArray, blockStatePalette, compoundTagList);
	}

	@Nullable
	public static AbstractBlockInfo create(int x, int y, int z, NBTTagIntArray nbtTagIntArray, BlockStatePalette blockStatePalette, NBTTagList compoundTagList) {
		if (nbtTagIntArray.getIntArray().length == 0) {
			return null;
		}
		switch (nbtTagIntArray.getIntArray()[0]) {
		case BLOCK_INFO_ID:
			return new BlockInfo(x, y, z, nbtTagIntArray, blockStatePalette, compoundTagList);
		case BANNER_INFO_ID:
			return new BlockInfoBanner(x, y, z, nbtTagIntArray, blockStatePalette, compoundTagList);
		case BOSS_INFO_ID:
			return new BlockInfoBoss(x, y, z, nbtTagIntArray);
		case NEXUS_INFO_ID:
			return new BlockInfoForceFieldNexus(x, y, z, nbtTagIntArray);
		case CHEST_INFO_ID:
			return new BlockInfoLootChest(x, y, z, nbtTagIntArray);
		case SPAWNER_INFO_ID:
			return new BlockInfoSpawner(x, y, z, nbtTagIntArray, blockStatePalette, compoundTagList);
		default:
			return null;
		}
	}

	public BlockPos getPos() {
		// return this.pos;
		return new BlockPos(this.x, this.y, this.z);
	}

}
