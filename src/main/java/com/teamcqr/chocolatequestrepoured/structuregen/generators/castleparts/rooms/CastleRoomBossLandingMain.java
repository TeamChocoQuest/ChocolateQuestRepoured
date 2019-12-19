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

public class CastleRoomBossLandingMain extends CastleRoom
{
    private static final int ROOMS_LONG = 2;
    private static final int ROOMS_SHORT = 1;
    private static final int LANDING_LENGTH_X = 3;
    private static final int LANDING_LENGTH_Z = 2;
    private static final int STAIR_OPENING_LENGTH_Z = 2;
    private static final int BOSS_ROOM_WIDTH = 17; //may want to get this from the boss room itself later

    private EnumFacing doorSide;
    private int numRotations;

    private int endX;
    private int lenX;
    private int endZ;
    private int lenZ;

    private int connectingWallLength;

    private int stairOpeningXStartIdx;
    private int stairOpeningXEndIdx;
    private int stairsDownZIdx;
    private int stairOpeningZStartIdx;
    private int stairOpeningZEndIdx;

    public CastleRoomBossLandingMain(BlockPos startPos, int sideLength, int height, EnumFacing doorSide)
    {
        super(startPos, sideLength, height);
        this.roomType = EnumRoomType.LANDING_BOSS;
        this.doorSide = doorSide;
        this.numRotations = getNumYRotationsFromStartToEndFacing(EnumFacing.NORTH, this.doorSide);
        this.defaultCeiling = true;

        this.endX = ROOMS_LONG * sideLength - 2; // minus 1 for the wall and 1 so it's at the last index
        this.lenX = endX + 1;
        this.endZ = ROOMS_SHORT * sideLength - 2; // minus 1 for the wall and 1 so it's at the last index
        this.lenZ = endZ + 1;

        this.stairOpeningXStartIdx = sideLength - 2;
        this.stairOpeningXEndIdx = stairOpeningXStartIdx + LANDING_LENGTH_X - 1;

        this.stairsDownZIdx = LANDING_LENGTH_Z;
        this.stairOpeningZStartIdx = LANDING_LENGTH_Z + 1;
        this.stairOpeningZEndIdx = stairOpeningZStartIdx + STAIR_OPENING_LENGTH_Z;

        final int gapToBossRoom = 2 + BOSS_ROOM_WIDTH - lenX;
        connectingWallLength = 0;
        if (gapToBossRoom > 0)
        {
            //Determine size of walls that come in from each side so there is no space between this and boss room
            connectingWallLength = (int)Math.ceil((double)(gapToBossRoom) / 2);
        }
    }

    @Override
    public void generateRoom(World world, CastleDungeon dungeon)
    {
        Vec3i offset;

        for (int x = 0; x <= endX; x++)
        {
            for (int y = 0; y < height; y++)
            {
                for (int z = -1; z <= endZ; z++)
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

        if (z == -1)
        {
            if (x < connectingWallLength || x > endX - connectingWallLength || y == height - 1)
            {
                blockToBuild = dungeon.getWallBlock().getDefaultState();
            }
            else if (y == 0)
            {
                return Blocks.QUARTZ_BLOCK.getDefaultState();
            }
        }
        else if (y == 0)
        {
            if ((x >= stairOpeningXStartIdx) && (x <= stairOpeningXEndIdx))
            {
                if (z < stairsDownZIdx)
                {
                    blockToBuild = Blocks.QUARTZ_BLOCK.getDefaultState();
                }
                else if (z == stairsDownZIdx)
                {
                    EnumFacing stairFacing = rotateFacingNTimesAboutY(EnumFacing.NORTH, numRotations);
                    blockToBuild = dungeon.getStairBlock().getDefaultState().withProperty(BlockStairs.FACING, stairFacing);
                }
                else
                {
                    return Blocks.AIR.getDefaultState();
                }
            }
            else
            {
                blockToBuild = Blocks.QUARTZ_BLOCK.getDefaultState();
            }
        }
        else if (y == height - 1)
        {
            blockToBuild = dungeon.getWallBlock().getDefaultState();
        }

        return blockToBuild;
    }

    @Override
    public void addInnerWall(EnumFacing side)
    {
        if (!(doorSide.getAxis() == EnumFacing.Axis.X && side == EnumFacing.SOUTH) &&
            !(doorSide.getAxis() == EnumFacing.Axis.Z && side == EnumFacing.EAST) &&
            !(side == doorSide))
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
