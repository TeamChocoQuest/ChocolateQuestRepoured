package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments.DoorPlacement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class RoomDecorBase
{
    protected class DecoPlacement
    {
        public Vec3i offset;
        public IBlockState block;

        protected DecoPlacement(int x, int y, int z, IBlockState block)
        {
            this.offset = new Vec3i(x, y, z);
            this.block = block;
        }
    }

    protected List<DecoPlacement> schematic; //Array of blockstates and their offsets

    protected RoomDecorBase()
    {
        this.schematic = new ArrayList<>();
        makeSchematic();
    }

    protected abstract void makeSchematic();

    public boolean wouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashMap<BlockPos, IBlockState> decoMap)
    {
        ArrayList<DecoPlacement> rotated = alignSchematic(side);

        for (DecoPlacement placement : rotated)
        {
            BlockPos pos = start.add(placement.offset);
            if (!decoArea.contains(pos) || decoMap.containsKey(pos))
            {
                return false;
            }
        }

        return true;
    }

    public void build(BlockPos start, EnumFacing side, HashMap<BlockPos, IBlockState> decoMap)
    {
        ArrayList<DecoPlacement> rotated = alignSchematic(side);

        for (DecoPlacement placement : rotated)
        {
            decoMap.put(start.add(placement.offset), placement.block);
        }
    }

    private ArrayList<DecoPlacement> alignSchematic(EnumFacing side)
    {
        ArrayList<DecoPlacement> result = new ArrayList<>();

        if (side == EnumFacing.SOUTH)
        {
            for (DecoPlacement p : schematic)
            {
                result.add(new DecoPlacement(p.offset.getX(), p.offset.getY(), -p.offset.getZ(), p.block));
            }
        }
        if (side == EnumFacing.WEST)
        {
            for (DecoPlacement p : schematic)
            {
                result.add(new DecoPlacement(p.offset.getZ(), p.offset.getY(), p.offset.getX(), p.block));
            }
        }
        if (side == EnumFacing.EAST)
        {
            for (DecoPlacement p : schematic)
            {
                result.add(new DecoPlacement(-p.offset.getZ(), p.offset.getY(), p.offset.getX(), p.block));
            }
        }
        if (side == EnumFacing.NORTH)
        {
            result.addAll(schematic);
        }

        return result;
    }
}
