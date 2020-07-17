package com.teamcqr.chocolatequestrepoured.util;

import javax.annotation.Nullable;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

/**
 * Copyright (c) 20.04.2020 Developed by KalgogSmash GitHub: https://github.com/KalgogSmash
 *
 * This is an enum to map a handful of common block types to their associated blockstate in Forge 1.12. For example, chiseled stone bricks are not a Block type in 1.12, rather they are a special variant state of STONE_BRICKS that cannot be looked up
 * by a namespace ID alone. Trying to parse through all the blocks and their property values is needlessly tedious, so this is just a shortcut so players can use these block varieties in config files as if they had their own namespace ID.
 *
 * Note: I named the enum values after the block namespace IDs on the minecraft Wiki so that if we move to a later version of forge, the config values should just map to registry values instead of coming here.
 */
public enum EnumForgeBlockVariant {
	MOSSY_STONE_BRICKS(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY)), CRACKED_STONE_BRICKS(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT,
			BlockStoneBrick.EnumType.CRACKED)), CHISELED_STONE_BRICKS(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED)), CHISELED_SANDSTONE(Blocks.SANDSTONE.getDefaultState().withProperty(
					BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED)), SMOOTH_SANDSTONE(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH)), SMOOTH_RED_SANDSTONE(Blocks.RED_SANDSTONE
							.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH)), CHISELED_RED_SANDSTONE(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE,
									BlockRedSandstone.EnumType.CHISELED)), PRISMARINE_BRICKS(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS)), DARK_PRISMARINE(Blocks.PRISMARINE
											.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK)), SANDSTONE_SLAB(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT,
													BlockStoneSlab.EnumType.SAND)), NETHER_BRICK_SLAB(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.NETHERBRICK)), QUARTZ_SLAB(Blocks.STONE_SLAB
															.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.QUARTZ));

	private final IBlockState blockState;
	private final String nameSpaceID;

	EnumForgeBlockVariant(IBlockState blockState) {
		this.blockState = blockState;
		this.nameSpaceID = "minecraft:" + this.name().toLowerCase();
	}

	@Nullable
	static IBlockState getVariantStateFromName(String name) {
		for (EnumForgeBlockVariant blockVariant : values()) {
			if (name.toLowerCase().equals(blockVariant.nameSpaceID)) {
				return blockVariant.blockState;
			}
		}
		return null;
	}
}
