package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.DecorationSelector;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorBase;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Map;

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
    public void generateRoom(World world)
    {
        setupDecoration();

        for (EnumFacing side : EnumFacing.HORIZONTALS)
        {
            ArrayList<BlockPos> edge = getDecorationEdge(side);
            for (BlockPos pos : edge)
            {
                if (decoMap.containsKey(pos))
                {
                    //This position is already decorated, so keep going
                    continue;
                }

                int attempts = 0;

                while (attempts < MAX_DECO_ATTEMPTS)
                {
                    RoomDecorBase decor = decoSelector.randomEdgeDecor();
                    if (decor.wouldFit(pos, side, decoArea, decoMap))
                    {
                        decor.build(pos, side, decoMap);
                        break;
                    }
                    ++attempts;
                }
                if (attempts >= MAX_DECO_ATTEMPTS)
                {
                    decoMap.put(pos, Blocks.AIR.getDefaultState());
                }
            }
        }

        for (Map.Entry<BlockPos, IBlockState> entry : decoMap.entrySet())
        {
            world.setBlockState(entry.getKey(), entry.getValue());
        }
    }
}
