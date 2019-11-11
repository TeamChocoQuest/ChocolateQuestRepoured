package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import com.teamcqr.chocolatequestrepoured.util.SpiralStaircaseBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomLandingSpiral extends CastleRoom
{
    private CastleRoomStaircaseSpiral stairsBelow;

    public CastleRoomLandingSpiral(BlockPos startPos, int sideLength, int height, CastleRoomStaircaseSpiral stairsBelow)
    {
        super(startPos, sideLength, height);
        this.roomType = RoomType.LANDING_SPIRAL;
        this.stairsBelow = stairsBelow;
        this.defaultCeiling = true;
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        BlockPos pos;
        IBlockState blockToBuild;
        BlockPos pillarStart = new BlockPos(stairsBelow.getCenterX(), startPos.getY(), stairsBelow.getCenterZ());
        EnumFacing firstStairSide = stairsBelow.getLastStairSide().rotateY();

        SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(pillarStart, firstStairSide);

        for (int x = 0; x < sideLength - 1; x++)
        {
            for (int z = 0; z < sideLength - 1; z++)
            {
                for (int y = 0; y < height; y++)
                {
                    blockToBuild = Blocks.AIR.getDefaultState();
                    pos = startPos.add(x, y, z);

                    // continue stairs for 1 layer through floor
                    if (y == 0)
                    {
                        if (stairs.isPartOfStairs(pos))
                        {
                            blockToBuild = stairs.getBlock(pos);
                        }
                        else
                        {
                            blockToBuild = Blocks.PLANKS.getDefaultState();
                        }
                    }
                    blocks.add(new BlockPlacement(pos, blockToBuild));
                }
            }
        }
    }
}
