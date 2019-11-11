package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import com.teamcqr.chocolatequestrepoured.util.SpiralStaircaseBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class CastleRoomStaircaseSpiral extends CastleRoom
{
    private EnumFacing firstStairSide;
    private BlockPos pillarStart;

    public CastleRoomStaircaseSpiral(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = RoomType.STAIRCASE_SPIRAL;

        this.firstStairSide = EnumFacing.NORTH;
        recalcPillarStart();
    }

    @Override
    public void generateRoom(ArrayList<BlockPlacement> blocks)
    {
        SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(pillarStart, firstStairSide);

        BlockPos pos;
        IBlockState blockToBuild;

        for (int x = 0; x < sideLength - 1; x++)
        {
            for (int z = 0; z < sideLength - 1; z++)
            {
                for (int y = 0; y < height; y++)
                {
                    blockToBuild = Blocks.AIR.getDefaultState();
                    pos = startPos.add(x, y, z);

                    if (y == 0)
                    {
                        blockToBuild = Blocks.PLANKS.getDefaultState();
                    }
                    else if (stairs.isPartOfStairs(pos))
                    {
                        blockToBuild = stairs.getBlock(pos);
                    }
                    else if (y == height - 1)
                    {
                        blockToBuild = Blocks.STONEBRICK.getDefaultState();
                    }
                    blocks.add(new BlockPlacement(pos, blockToBuild));
                }
            }
        }
    }

    public EnumFacing getLastStairSide()
    {
        EnumFacing result = EnumFacing.NORTH;
        for (int i = 0; i < height - 1; i++)
        {
            result = result.rotateY();
        }
        return result;
    }

    public int getCenterX()
    {
        return pillarStart.getX();
    }

    public int getCenterZ()
    {
        return pillarStart.getZ();
    }

    @Override
    public void addInnerWall(EnumFacing side)
    {
        super.addInnerWall(side);
        recalcPillarStart();
    }

    @Override
    public void addOuterWall(EnumFacing side)
    {
        super.addOuterWall(side);
        recalcPillarStart();
    }

    private void recalcPillarStart()
    {
        int centerX = (int)Math.ceil((double)getDecorationLengthX() / 2);
        int centerZ = (int)Math.ceil((double)getDecorationLengthZ() / 2);
        pillarStart = getDecorationStartPos().add(centerX, 0, centerZ);
    }
}
