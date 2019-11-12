package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.DecorationSelector;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.IRoomDecor;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public abstract class CastleRoomGeneric extends CastleRoom
{
    protected static final int MAX_DECO_ATTEMPTS = 3;
    protected DecorationSelector decoSelector;

    public CastleRoomGeneric(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.decoSelector = new DecorationSelector(random);
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        setupDecoration(world);

        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            ArrayList<BlockPos> edge = getDecorationEdge(side);
            for (BlockPos pos : edge)
            {
                if (decoMap.contains(pos))
                {
                    //This position is already decorated, so keep going
                    continue;
                }

                int attempts = 0;

                while (attempts < MAX_DECO_ATTEMPTS)
                {
                    IRoomDecor decor = decoSelector.randomEdgeDecor();
                    if (decor.wouldFit(pos, side, decoArea, decoMap))
                    {
                        decor.build(world, dungeon, pos, side, decoMap);
                        break;
                    }
                    ++attempts;
                }
                if (attempts >= MAX_DECO_ATTEMPTS)
                {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    decoMap.add(pos);
                }
            }
        }
    }
}
