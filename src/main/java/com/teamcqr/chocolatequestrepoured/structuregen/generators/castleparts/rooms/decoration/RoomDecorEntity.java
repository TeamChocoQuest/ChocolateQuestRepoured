package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class RoomDecorEntity implements IRoomDecor
{
    protected List<Vec3i> footprint; //Array of blockstates and their offsets

    protected RoomDecorEntity()
    {
        this.footprint = new ArrayList<>();
    }

    public boolean wouldFit(BlockPos start, EnumFacing side, HashSet<BlockPos> decoArea, HashSet<BlockPos> decoMap)
    {
        ArrayList<Vec3i> rotated = alignFootprint(side);

        for (Vec3i placement : rotated)
        {
            BlockPos pos = start.add(placement);
            if (!decoArea.contains(pos) || decoMap.contains(pos))
            {
                return false;
            }
        }

        return true;
    }

    public void build(World world, CastleDungeon dungeon, BlockPos start, EnumFacing side, HashSet<BlockPos> decoMap)
    {
        ArrayList<Vec3i> rotated = alignFootprint(side);

        for (Vec3i placement : rotated)
        {
            BlockPos pos = start.add(placement);
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            decoMap.add(pos);
        }
        spawnEntity(world, start);
    }

    protected abstract void spawnEntity(World world, BlockPos pos);

    private ArrayList<Vec3i> alignFootprint(EnumFacing side)
    {
        ArrayList<Vec3i> result = new ArrayList<>();

        footprint.forEach(v -> result.add(DungeonGenUtils.rotateVec3i(v, side)));

        return result;
    }
}
