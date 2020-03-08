package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class DecoBlockBase {
    public Vec3i offset;
    public Block block;

    protected DecoBlockBase(int x, int y, int z, Block block) {
        this.offset = new Vec3i(x, y, z);
        this.block = block;
    }

    protected DecoBlockBase(Vec3i offset, Block block) {
        this.offset = offset;
        this.block = block;
    }

    protected IBlockState getState(EnumFacing side) {
        return block.getDefaultState();
    }
}
