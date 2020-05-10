package com.teamcqr.chocolatequestrepoured.structuregen.structurefile;

import com.teamcqr.chocolatequestrepoured.util.CQRLootTableList;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.util.Constants;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class LootChestInfo {

	private BlockPos position;
	private EnumFacing facing;
	private ResourceLocation lootTable;

	public LootChestInfo(BlockPos position, EnumFacing facing, ResourceLocation lootTable) {
		this.position = position;
		this.facing = facing;
		this.lootTable = lootTable;
	}

	public LootChestInfo(NBTTagCompound compound) {
		this.position = NBTUtil.getPosFromTag(compound.getCompoundTag("position"));
		this.facing = EnumFacing.getHorizontal(compound.getInteger("facing"));
		if (compound.hasKey("loottable", Constants.NBT.TAG_STRING)) {
			this.lootTable = new ResourceLocation(compound.getString("loottable"));
		} else {
			this.lootTable = this.getOldLootTable(compound);
		}
	}

	public NBTTagCompound getAsNBTTag() {
		NBTTagCompound compound = new NBTTagCompound();

		compound.setTag("position", NBTUtil.createPosTag(this.position));
		compound.setInteger("facing", this.facing.getHorizontalIndex());
		compound.setString("loottable", this.lootTable.toString());

		return compound;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	public EnumFacing getFacing() {
		return this.facing;
	}

	public ResourceLocation getLootTable() {
		return this.lootTable;
	}

	private ResourceLocation getOldLootTable(NBTTagCompound compound) {
		switch (compound.getInteger("loottable")) {
		case 0:
			return CQRLootTableList.CHESTS_TREASURE;
		case 1:
			return CQRLootTableList.CHESTS_EQUIPMENT;
		case 2:
			return CQRLootTableList.CHESTS_FOOD;
		case 3:
			return CQRLootTableList.CHESTS_MATERIAL;
		case 4:
			return LootTableList.CHESTS_ABANDONED_MINESHAFT;
		case 5:
			return LootTableList.CHESTS_DESERT_PYRAMID;
		case 6:
			return LootTableList.CHESTS_END_CITY_TREASURE;
		case 7:
			return LootTableList.CHESTS_IGLOO_CHEST;
		case 8:
			return LootTableList.CHESTS_JUNGLE_TEMPLE;
		case 9:
			return LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER;
		case 10:
			return LootTableList.CHESTS_NETHER_BRIDGE;
		case 11:
			return LootTableList.CHESTS_SPAWN_BONUS_CHEST;
		case 12:
			return LootTableList.CHESTS_STRONGHOLD_CORRIDOR;
		case 13:
			return LootTableList.CHESTS_STRONGHOLD_CROSSING;
		case 14:
			return LootTableList.CHESTS_STRONGHOLD_LIBRARY;
		case 15:
			return LootTableList.CHESTS_VILLAGE_BLACKSMITH;
		case 16:
			return LootTableList.CHESTS_WOODLAND_MANSION;
		default:
			return new ResourceLocation(Reference.MODID, "custom_" + (compound.getInteger("loottable") - 16));
		}
	}

}
