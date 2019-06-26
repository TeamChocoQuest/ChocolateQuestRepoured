package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts;

import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CastlePartTower implements ICastlePart
{
    public enum towerType
    {
        SQUARE, ROUND
    }

    private BlockPos start;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private int floors;
    private EnumFacing facing;
    CastleDungeon dungeon;
    private Random random;
    private towerType type;

    public CastlePartTower(BlockPos origin, int sizeX, int sizeZ, CastleDungeon dungeon, EnumFacing facing)
    {
        this.dungeon = dungeon;
        this.start = origin;
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.random = new Random(origin.getX() + origin.getY() + origin.getZ());
        this.floors = 1;
        this.facing = facing;
        this.type = random.nextBoolean() ? towerType.SQUARE : towerType.ROUND;
    }

    public void setFloors(int floors)
    {
        this.floors = floors;
    }

    public void randomizeFloors(int maxFloors)
    {
        this.floors = random.nextInt(maxFloors) + 1; //Add 1, don't want to have 0 floors
    }

    public void setType(towerType type)
    {
        this.type = type;
    }

    @Override
    public void generatePart(World world)
    {
        List<BlockPos> wallBlocks = new ArrayList<>();
        List<BlockPos> stairBlocks = new ArrayList<>();

        //System.out.println("Building a square tower at " + x + ", " + y + ", " + z + ". sizeX = " + sizeX + ", sizeZ = " + sizeZ + ". Floors = " + floors + ". Facing = " + facing.toString());

        if (type == towerType.SQUARE)
        {
            assembleSquare(wallBlocks, stairBlocks);
        }
        else
        {
            //TODO Assemble round instead
            assembleSquare(wallBlocks, stairBlocks);
        }

        if(!wallBlocks.isEmpty()) {
            for(BlockPos pos : wallBlocks) {
                world.setBlockState(pos, dungeon.getWallBlock().getDefaultState());
            }
        }

        if(!stairBlocks.isEmpty()) {
            for(BlockPos pos : stairBlocks) {
                world.setBlockState(pos, dungeon.getWallBlock().getDefaultState());
            }
        }
    }

    @Override
    public boolean isTower()
    {
        return true;
    }

    public void assembleSquare(List<BlockPos> wallblocks, List<BlockPos> stairBlocks)
    {
        int currentY;
        int floorHeight = dungeon.getFloorHeight();
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();

        //for each floor
        for (int currentFloor = 0; currentFloor < floors; currentFloor++)
        {
            currentY = y + currentFloor * (floorHeight + 1);

            //over the entire x/z area
            for (int i = 0; i < sizeX; i++)
            {
                for (int j = 0; j < sizeZ; j++)
                {
                    // place a floor
                    wallblocks.add(new BlockPos(x + i, currentY, z + j));
                    // place another layer of floor
                    wallblocks.add(new BlockPos(x + i, currentY + floorHeight, z + j));

                }
            }
            //Add x walls
            for (int i = 0; i < sizeX; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    wallblocks.add(new BlockPos(x + i, currentY + j, z));
                    wallblocks.add(new BlockPos(x + i, currentY + j, z + sizeZ - 1));
                }
            }
            //Add y walls
            for (int i = 0; i < sizeZ; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    wallblocks.add(new BlockPos(x, currentY + j, z + i));
                    wallblocks.add(new BlockPos(x + sizeX - 1, currentY + j, z + i));
                }
            }
        }
    }

    public void assembleRound(List<BlockPos> wallblocks, List<BlockPos> stairBlocks)
    {
        return;
    }

    @Override
    public void setAsTopFloor()
    {

    }

    @Override
    public int getStartLayer()
    {
        return 0;
    }
}
