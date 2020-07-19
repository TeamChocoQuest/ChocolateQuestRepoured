package com.teamcqr.chocolatequestrepoured.util;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public enum EnumMCWoodType {
	OAK(BlockPlanks.EnumType.OAK, Blocks.OAK_STAIRS, Blocks.OAK_FENCE, Blocks.OAK_DOOR),
	BIRCH(BlockPlanks.EnumType.BIRCH, Blocks.BIRCH_STAIRS, Blocks.BIRCH_FENCE, Blocks.BIRCH_DOOR),
	SPRUCE(BlockPlanks.EnumType.SPRUCE, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_DOOR),
	JUNGLE(BlockPlanks.EnumType.JUNGLE, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_DOOR),
	ACACIA(BlockPlanks.EnumType.ACACIA, Blocks.ACACIA_STAIRS, Blocks.ACACIA_FENCE, Blocks.ACACIA_DOOR),
	DARK_OAK(BlockPlanks.EnumType.DARK_OAK, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_DOOR);

	private final BlockPlanks.EnumType plankVariant;
	private final Block stairBlock;
	private final Block fenceBlock;
	private final Block doorBlock;

	EnumMCWoodType(BlockPlanks.EnumType slab, Block stair, Block fence, Block door) {
		this.plankVariant = slab;
		this.stairBlock = stair;
		this.fenceBlock = fence;
		this.doorBlock = door;
	}

	public IBlockState getSlabBlockState() {
		return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, this.plankVariant);
	}

	public IBlockState getPlankBlockState() {
		return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, this.plankVariant);
	}

	public IBlockState getStairBlockState() {
		return this.stairBlock.getDefaultState();
	}

	public IBlockState getFenceBlockState() {
		return this.fenceBlock.getDefaultState();
	}

	public IBlockState getDoorBlockState() {
		return this.doorBlock.getDefaultState();
	}

	@Nullable
	public static EnumMCWoodType getTypeFromString(String str) {
		for (EnumMCWoodType type : EnumMCWoodType.values()) {
			if (type.toString().toLowerCase().equals(str.toLowerCase())) {
				return type;
			}
		}
		return null;
	}
}
