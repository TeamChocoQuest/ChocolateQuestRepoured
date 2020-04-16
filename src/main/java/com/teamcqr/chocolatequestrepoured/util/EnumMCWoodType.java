package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import javax.annotation.Nullable;

public enum EnumMCWoodType {
    OAK(BlockPlanks.EnumType.OAK, Blocks.OAK_STAIRS, Blocks.OAK_FENCE),
    BIRCH(BlockPlanks.EnumType.BIRCH, Blocks.BIRCH_STAIRS, Blocks.BIRCH_FENCE),
    SPRUCE(BlockPlanks.EnumType.SPRUCE, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_FENCE),
    JUNGLE(BlockPlanks.EnumType.JUNGLE, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_FENCE),
    ACACIA(BlockPlanks.EnumType.ACACIA, Blocks.ACACIA_STAIRS, Blocks.ACACIA_FENCE),
    DARK_OAK(BlockPlanks.EnumType.DARK_OAK, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_FENCE);

    private final BlockPlanks.EnumType plankVariant;
    private final Block stairBlock;
    private final Block fenceBlock;

    EnumMCWoodType(BlockPlanks.EnumType slab, Block stair, Block fence) {
        this.plankVariant = slab;
        this.stairBlock = stair;
        this.fenceBlock = fence;
    }

    public IBlockState getSlabBlockState() {
        return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, plankVariant);
    }

    public IBlockState getPlankBlockState() {
        return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, plankVariant);
    }

    public IBlockState getStairBlockState() {
        return stairBlock.getDefaultState();
    }

    public IBlockState getFenceBlockState() {
        return fenceBlock.getDefaultState();
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
