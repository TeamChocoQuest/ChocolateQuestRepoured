package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.block.BlockStairs;
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
    private static final int UPPER_STAIR_X = 3;
    private static final int LOWER_LANDING_Z = 2;
    private static final int LOWER_STAIRS_Z = 2;
    private static final int LOWER_STAIRS_LEN = 2;
    private static final int FLOOR_HEIGHT = 1;
    private static final int MID_STAIR_LENGTH = 2;

    private int endX;
    private int lenX;
    private int endZ;
    private int lenZ;
    private int maxHeightIdx;
    private int topStairLength;

    private int mainLandingXStartIdx;
    private int mainLandingXEndIdx;
    private int mainLandingZStartIdx;

    private int upperStairXStartIdx;
    private int upperStairXEndIdx;

    private int lowerStair1XStartIdx;
    private int lowerStair1XEndIdx;
    private int lowerStair2XStartIdx;
    private int lowerStair2XEndIdx;

    private int lowerLanding1XStartIdx;
    private int lowerLanding1XEndIdx;
    private int lowerLanding2XStartIdx;
    private int lowerLanding2XEndIdx;
    private int lowerLandingZStartIdx;
    private int midStairsZStartIdx;
    private int mainLandingMaxHeightIdx;
    private int lowerLandingMaxHeightIdx;

    public CastleRoomBossStairMain(BlockPos startPos, int sideLength, int height, EnumFacing doorSide)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.STAIRCASE_BOSS;

        this.doorSide = doorSide;
        this.numRotations = getNumYRotationsFromStartToEndFacing(EnumFacing.NORTH, this.doorSide);

        this.endX = ROOMS_LONG * sideLength - 2; // minus 1 for the wall and 1 so it's at the last index
        this.lenX = endX + 1;
        this.endZ = ROOMS_SHORT * sideLength - 2; // minus 1 for the wall and 1 so it's at the last index
        this.lenZ = endZ + 1;
        this.maxHeightIdx = height - 1;

        this.topStairLength = lenZ - TOP_LANDING_BUFFER_Z - MAIN_LANDING_Z;
        final int lowerStairLength = height - FLOOR_HEIGHT - MID_STAIR_LENGTH - topStairLength;

        this.mainLandingXStartIdx = sideLength - 4;
        this.mainLandingXEndIdx = mainLandingXStartIdx + MAIN_LANDING_X - 1;
        this.mainLandingZStartIdx = endZ - MAIN_LANDING_Z + 1;

        this.upperStairXStartIdx = sideLength - 2;
        this.upperStairXEndIdx = upperStairXStartIdx + UPPER_STAIR_X - 1;

        this.lowerLanding1XStartIdx = upperStairXStartIdx - 2;
        this.lowerLanding1XEndIdx = lowerLanding1XStartIdx + 1;
        this.lowerLanding2XStartIdx = upperStairXEndIdx + 1;
        this.lowerLanding2XEndIdx = lowerLanding2XStartIdx + 1;

        this.lowerStair1XStartIdx = lowerLanding1XStartIdx - lowerStairLength;
        this.lowerStair1XEndIdx = lowerStair1XStartIdx + lowerStairLength - 1;
        this.lowerStair2XStartIdx = lowerLanding2XEndIdx + 1;
        this.lowerStair2XEndIdx = lowerStair2XStartIdx + lowerStairLength - 1;

        this.midStairsZStartIdx = mainLandingZStartIdx - LOWER_STAIRS_Z;
        this.lowerLandingZStartIdx = midStairsZStartIdx - LOWER_LANDING_Z;

        this.mainLandingMaxHeightIdx = height - topStairLength - 1;
        this.lowerLandingMaxHeightIdx = mainLandingMaxHeightIdx - LOWER_STAIRS_LEN;
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
                    IBlockState blockToBuild = getBlockToBuild(dungeon, x, y, z);

                    offset = DungeonGenUtils.rotateMatrixOffsetCW(new Vec3i(x, y, z), lenX, lenZ, numRotations);
                    world.setBlockState(origin.add(offset), blockToBuild);
                }
            }
        }
    }

    private IBlockState getBlockToBuild(CastleDungeon dungeon, int x, int y, int z)
    {
        IBlockState blockToBuild = Blocks.AIR.getDefaultState();

        if (y == 0)
        {
            blockToBuild = getFloorBlock(dungeon);
        }
        else if (y == maxHeightIdx)
        {
            if ((x >= upperStairXStartIdx && x <= upperStairXEndIdx) &&
                    (z >= TOP_LANDING_BUFFER_Z) && (z <= TOP_LANDING_BUFFER_Z + topStairLength - 1))
            {
                blockToBuild = Blocks.AIR.getDefaultState();
            }
            else
            {
                blockToBuild = dungeon.getWallBlock().getDefaultState();
            }
        }
        else if ((x >= mainLandingXStartIdx && x <= mainLandingXEndIdx) &&
                (z >= mainLandingZStartIdx))
        {
            blockToBuild = getMainLandingBlock(x, y, z);
        }
        else if ((x >= upperStairXStartIdx && x <= upperStairXEndIdx) &&
                ((z >= TOP_LANDING_BUFFER_Z) && (z <= TOP_LANDING_BUFFER_Z + topStairLength - 1)))
        {
            blockToBuild = getUpperStairBlock(x, y, z);
        }
        else if ((x >= lowerLanding1XStartIdx && x <= lowerLanding1XEndIdx) ||
                (x >= lowerLanding2XStartIdx && x <= lowerLanding2XEndIdx))
        {
            if (z == midStairsZStartIdx || z == midStairsZStartIdx + 1)
            {
                blockToBuild = getMidStairBlock(x, y, z);
            }
            else if (z == lowerLandingZStartIdx || z == lowerLandingZStartIdx + 1)
            {
                blockToBuild = getLowerLandingBlock(x, y, z);
            }
        }
        else if ((x >= lowerStair1XStartIdx && x <= lowerStair1XEndIdx) &&
                (z == lowerLandingZStartIdx || z == lowerLandingZStartIdx + 1))
        {
            blockToBuild = getLowerStair1Block(x, y, z);
        }
        else if ((x >= lowerStair2XStartIdx && x <= lowerStair2XEndIdx) &&
                (z == lowerLandingZStartIdx || z == lowerLandingZStartIdx + 1))
        {
            blockToBuild = getLowerStair2Block(x, y, z);
        }

        return blockToBuild;
    }

    private IBlockState getLowerStair1Block(int x, int y, int z)
    {
        if (y == lowerLandingMaxHeightIdx - (lowerStair1XEndIdx - x))
        {
            EnumFacing stairFacing = rotateFacingNTimesAboutY(EnumFacing.EAST, numRotations);
            return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
        }
        else if (y <= lowerLandingMaxHeightIdx - (lowerLanding1XEndIdx - x))
        {
            return Blocks.STONEBRICK.getDefaultState();
        }
        else
        {
            return Blocks.AIR.getDefaultState();
        }
    }

    private IBlockState getLowerStair2Block(int x, int y, int z)
    {
        if (y == lowerLandingMaxHeightIdx - (x - lowerStair2XStartIdx))
        {
            EnumFacing stairFacing = rotateFacingNTimesAboutY(EnumFacing.WEST, numRotations);
            return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
        }
        else if (y <= lowerLandingMaxHeightIdx - (x - lowerStair2XStartIdx))
        {
            return Blocks.STONEBRICK.getDefaultState();
        }
        else
        {
            return Blocks.AIR.getDefaultState();
        }
    }

    private IBlockState getLowerLandingBlock(int x, int y, int z)
    {
        if (y >= 1 && y <= lowerLandingMaxHeightIdx)
        {
            return Blocks.STONEBRICK.getDefaultState();
        }
        else
        {
            return Blocks.AIR.getDefaultState();
        }
    }

    private IBlockState getMidStairBlock(int x, int y, int z)
    {
        if (y == mainLandingMaxHeightIdx - (endZ - z - MAIN_LANDING_Z))
        {
            EnumFacing stairFacing = rotateFacingNTimesAboutY(EnumFacing.SOUTH, numRotations);
            return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
        }
        else if (y < mainLandingMaxHeightIdx - (endZ - z - MAIN_LANDING_Z))
        {
            return Blocks.STONEBRICK.getDefaultState();
        }
        else
        {
            return Blocks.AIR.getDefaultState();
        }
    }

    private IBlockState getUpperStairBlock(int x, int y, int z)
    {
        if (y == (maxHeightIdx - (z - TOP_LANDING_BUFFER_Z)))
        {
            EnumFacing stairFacing = rotateFacingNTimesAboutY(EnumFacing.NORTH, numRotations);
            return Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
        }
        else if ((y < maxHeightIdx - (z - TOP_LANDING_BUFFER_Z)))
        {
            return Blocks.STONEBRICK.getDefaultState();
        }
        else
        {
            return Blocks.AIR.getDefaultState();
        }
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
