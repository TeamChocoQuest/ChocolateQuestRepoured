package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.SpiralStaircaseBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CastleRoomStaircaseSpiral extends CastleRoom
{
    private EnumFacing firstStairSide;
    private BlockPos pillarStart;

    public CastleRoomStaircaseSpiral(BlockPos startPos, int sideLength, int height)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.STAIRCASE_SPIRAL;
        this.defaultCeiling = false;
        this.defaultFloor = false;

        this.firstStairSide = EnumFacing.NORTH;
        recalcPillarStart();
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        recalcPillarStart();
        SpiralStaircaseBuilder stairs = new SpiralStaircaseBuilder(pillarStart, firstStairSide, dungeon.getWallBlock(), dungeon.getStairBlock());

        BlockPos pos;
        IBlockState blockToBuild;

        for (int x = 0; x < buildLengthX - 1; x++)
        {
            for (int z = 0; z < buildLengthZ - 1; z++)
            {
                for (int y = 0; y < height; y++)
                {
                    blockToBuild = Blocks.AIR.getDefaultState();
                    pos = getInteriorBuildStart().add(x, y, z);

                    if (y == 0)
                    {
                        blockToBuild = dungeon.getFloorBlock().getDefaultState();
                    }
                    else if (stairs.isPartOfStairs(pos))
                    {
                        blockToBuild = stairs.getBlock(pos);
                    }
                    else if (y == height - 1)
                    {
                        blockToBuild = dungeon.getWallBlock().getDefaultState();
                    }
                    world.setBlockState(pos, blockToBuild);
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
    }

    @Override
    public void addOuterWall(EnumFacing side)
    {
        super.addOuterWall(side);
    }

    private void recalcPillarStart()
    {
        int centerX = (buildLengthX - 1) / 2;
        int centerZ = (buildLengthZ - 1) / 2;
        pillarStart = getInteriorBuildStart().add(centerX, 0, centerZ);
    }
}
