package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 27.06.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 *
 * Description: A helper class to store a block's position and state.
 */
public class BlockPlacement {
	public BlockPos position;
	public IBlockState state;

	public BlockPlacement(BlockPos position, IBlockState state) {
		this.position = position;
		this.state = state;
	}

	public BlockPlacement(int x, int y, int z, IBlockState state) {
		this.position = new BlockPos(x, y, z);
		this.state = state;
	}

	// Shortcut for modifying the block properties
	public <T extends Comparable<T>, V extends T> void applyProperty(IProperty<T> property, V value) {
		this.state = this.state.withProperty(property, value);
	}

	public void build(World world) {
		world.setBlockState(this.position, this.state);
	}
}
