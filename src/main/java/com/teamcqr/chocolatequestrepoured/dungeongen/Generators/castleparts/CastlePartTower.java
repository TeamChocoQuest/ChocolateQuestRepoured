package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts;

import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.Circle2D;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CastlePartTower implements ICastlePart
{
    private class CircleCoord
    {
        boolean active;
        boolean edge;
    }

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
        //this.type = random.nextBoolean() ? towerType.SQUARE : towerType.ROUND;
        this.type = towerType.ROUND;
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
            assembleRound(wallBlocks, stairBlocks);
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

    public void assembleSquare(List<BlockPos> wallBlocks, List<BlockPos> stairBlocks)
    {
        int currentY;
        int floorHeight = dungeon.getFloorHeight();
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        System.out.println("Building a square tower at " + x + ", " + y + ", " + z + ". sizeX = " + sizeX + ", sizeZ = " + sizeZ + ". Floors = " + floors + ". Facing = " + facing.toString());

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
                    wallBlocks.add(new BlockPos(x + i, currentY, z + j));
                    // place another layer of floor
                    wallBlocks.add(new BlockPos(x + i, currentY + floorHeight, z + j));

                }
            }
            //Add x walls
            for (int i = 0; i < sizeX; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    wallBlocks.add(new BlockPos(x + i, currentY + j, z));
                    wallBlocks.add(new BlockPos(x + i, currentY + j, z + sizeZ - 1));
                }
            }
            //Add z walls
            for (int i = 0; i < sizeZ; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    wallBlocks.add(new BlockPos(x, currentY + j, z + i));
                    wallBlocks.add(new BlockPos(x + sizeX - 1, currentY + j, z + i));
                }
            }
        }
    }

    public void assembleRound(List<BlockPos> wallblocks, List<BlockPos> stairBlocks)
    {
        int currentY;
        int floorHeight = dungeon.getFloorHeight();

        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        System.out.println("Building a round tower at " + x + ", " + y + ", " + z + ". sizeX = " + sizeX + ", sizeZ = " + sizeZ + ". Floors = " + floors + ". Facing = " + facing.toString());

        int radius = (sizeX - 1) / 2; // Subtract 1 because the final diameter ends up at (r*2+1)
        int midX = x + radius;
        int midZ = z + radius;
        //CircleCoord[][] towerGrid = new CircleCoord[sizeX][sizeX];
        Circle2D circle = new Circle2D(midX, midZ, radius);
        Circle2D.CircleRegion[][] layout = circle.toArray(x, z, sizeX, sizeX);

        for (int currentFloor = 0; currentFloor < floors; currentFloor++)
        {
            currentY = y + currentFloor * (floorHeight + 1);


        }
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
