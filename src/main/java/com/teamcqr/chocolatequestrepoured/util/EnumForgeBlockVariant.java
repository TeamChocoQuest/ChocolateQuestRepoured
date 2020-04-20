package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import javax.annotation.Nullable;

public enum EnumForgeBlockVariant {
    MOSSY_STONE_BRICKS(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY)),
    CRACKED_STONE_BRICKS(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED)),
    CHISELED_STONE_BRICKS(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED)),
    CHISELED_SANDSTONE(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED)),
    SMOOTH_SANDSTONE(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH)),
    SMOOTH_RED_SANDSTONE(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH)),
    CHISELED_RED_SANDSTONE(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED)),
    PRISMARINE_BRICKS(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS)),
    DARK_PRISMARINE(Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK));


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
