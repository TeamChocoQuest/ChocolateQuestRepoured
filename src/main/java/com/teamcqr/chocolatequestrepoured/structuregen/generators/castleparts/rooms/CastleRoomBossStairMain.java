package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class CastleRoomBossStairMain extends CastleRoom
{
    private EnumFacing doorSide;
    private int numRotations;
    private static final int ROOMS_LONG = 2;
    private static final int ROOMS_SHORT = 1;
    private static final int TOP_LANDING_BUFFER_Z = 3;
    private static final int MAIN_LANDING_Z = 2;
    private static final int MAIN_LANDING_X = 7;
    private static final int FLOOR_HEIGHT = 1;
    private static final int MID_STAIR_LENGH = 2;

    int rotations;

    int endX;
    int lenX;
    int endZ;
    int lenZ;
    int topStairLength;
    int botStairLength;
    int mainLandingXStartIdx;
    int mainLandingXEndIdx;
    int mainLandingMaxHeightIdx;

    public CastleRoomBossStairMain(BlockPos startPos, int sideLength, int height, EnumFacing doorSide)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.STAIRCASE_DIRECTED;
        this.doorSide = doorSide;
        this.numRotations = getNumYRotationsFromStartToEndFacing(EnumFacing.NORTH, this.doorSide);
        this.defaultCeiling = false;

        this.endX = ROOMS_LONG * sideLength - 2; // minus 1 for the wall and 1 so it's at the last index
        this.lenX = endX + 1;
        this.endZ = ROOMS_SHORT * sideLength - 2; // minus 1 for the wall and 1 so it's at the last index
        this.lenZ = endZ + 1;
        this.topStairLength = lenZ - TOP_LANDING_BUFFER_Z - MAIN_LANDING_Z;
        this.botStairLength = height - FLOOR_HEIGHT - MID_STAIR_LENGH - topStairLength;
        this.mainLandingXStartIdx = sideLength - 3;
        this.mainLandingXEndIdx = mainLandingXStartIdx + MAIN_LANDING_X - 1;
        this.mainLandingMaxHeightIdx = height - topStairLength - 1;
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        Vec3i offset;

        for (int x = 0; x <= endX; x++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int z = 0; z <= endZ; z++)
                {
                    IBlockState blockToBuild = Blocks.AIR.getDefaultState();

                    if (y == 0)
                    {
                        blockToBuild = getFloorBlock(dungeon);
                    }
                    else if ((x >= mainLandingXStartIdx) && (x <= mainLandingXEndIdx) && (z > endZ - MAIN_LANDING_Z))
                    {
                        blockToBuild = getMainLandingBlock(x, y, z);
                    }

                    offset = DungeonGenUtils.rotateMatrixOffsetCW(new Vec3i(x, y, z), lenX, lenZ, numRotations);
                    world.setBlockState(origin.add(offset), blockToBuild);
                }
            }
        }
    }


    private void buildFloorBlock(int x, int z, World world, CastleDungeon dungeon)
    {
        IBlockState blockToBuild = dungeon.getFloorBlock().getDefaultState();
        world.setBlockState(origin.add(x, 0, z), blockToBuild);
    }

    public IBlockState getMainLandingBlock(int x, int y, int z)
    {
        if (y >= 1 && y <= mainLandingMaxHeightIdx)
        {
            return Blocks.STONEBRICK.getDefaultState();
        }
        else
        {
            return Blocks.AIR.getDefaultState();
        }
    }

    @Override
    public void addInnerWall(EnumFacing side)
    {
        if (!(doorSide.getAxis() == EnumFacing.Axis.X && side == EnumFacing.SOUTH) &&
            !(doorSide.getAxis() == EnumFacing.Axis.Z && side == EnumFacing.EAST))
        {
            super.addInnerWall(side);
        }
    }

    @Override
    public boolean canBuildDoorOnSide(EnumFacing side)
    {
        return true;
    }

    @Override
    public boolean reachableFromSide(EnumFacing side)
    {
        return true;
    }
}
