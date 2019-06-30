package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

/**
 * Copyright (c) 27.06.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 *
 * Description: A helper class to store a block's position and state.
 */
public class BlockInfo
{
    public BlockPos position;
    public IBlockState state;

    public BlockInfo(BlockPos position, IBlockState state)
    {
        this.position = position;
        this.state = state;
    }

    // Shortcut for modifying the block properties
    public <T extends Comparable<T>, V extends T> void applyProperty(IProperty<T> property, V value)
    {
        state = state.withProperty(property, value);
    }
}
