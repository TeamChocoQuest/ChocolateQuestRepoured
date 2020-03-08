package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class DecoBlockBase {
    public Vec3i offset;
    public IBlockState blockState;

    protected DecoBlockBase(int x, int y, int z, IBlockState block) {
        this.offset = new Vec3i(x, y, z);
        this.blockState = block;
    }

    protected DecoBlockBase(Vec3i offset, IBlockState block) {
        this.offset = offset;
        this.blockState = block;
    }

    protected IBlockState getState(EnumFacing side) {
        return blockState;
    }
}
