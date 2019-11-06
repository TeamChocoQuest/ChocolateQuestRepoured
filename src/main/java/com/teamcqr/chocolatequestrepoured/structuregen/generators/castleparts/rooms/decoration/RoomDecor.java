package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public abstract class RoomDecor
{
    protected class DecoPlacement
    {
        private Vec3i offset;
        private IBlockState block;

        protected DecoPlacement(int x, int y, int z, IBlockState block)
        {
            this.offset = new Vec3i(x, y, z);
            this.block = block;
        }
    }

    protected Vec3i xyzSize; //Total # of blocks the object takes up in (x, y, z) format
    protected List<DecoPlacement> schematic; //Array of blockstates and their offsets

    protected RoomDecor(int sizeX, int sizeY, int sizeZ)
    {
        this.xyzSize = new Vec3i(sizeX, sizeY, sizeZ);
        this.schematic = new ArrayList<>();
        makeSchematic();
    }

    protected abstract void makeSchematic();

    /*
    * Rotate the size footprint of this decoration clockwise
     */
    protected Vec3i rotateSize(EnumFacing endSide)
    {
        Vec3i result = xyzSize;

        if (endSide == EnumFacing.SOUTH)
        {
            result = new Vec3i(-result.getX(), result.getY(), -result.getZ());
        }
        else if (endSide == EnumFacing.EAST)
        {
            result = new Vec3i(-result.getZ(), result.getY(), result.getX());
        }
        else if (endSide == EnumFacing.WEST)
        {
            result = new Vec3i(result.getZ(), result.getY(), -result.getX());
        }

        return result;
    }
}
