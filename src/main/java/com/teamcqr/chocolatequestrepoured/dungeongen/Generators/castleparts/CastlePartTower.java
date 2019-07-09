package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts;

import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.addons.CastleAddonRoof;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockInfo;
import com.teamcqr.chocolatequestrepoured.util.Circle2D;
import net.minecraft.block.state.IBlockState;
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
    private int sideLen;
    private int sizeY;
    private int startFloor;
    private int floors;
    private EnumFacing facing;
    private CastleDungeon dungeon;
    private Random random;
    private towerType type;

    public CastlePartTower(BlockPos origin, int sideLen, int startFloor, CastleDungeon dungeon, EnumFacing facing)
    {
        this.dungeon = dungeon;
        this.start = origin;
        this.sideLen = sideLen;
        this.startFloor = startFloor;
        this.random = this.dungeon.getRandom();
        this.floors = 1;
        this.facing = facing;
        this.type = towerType.ROUND;
    }

    public void setFloors(int floors)
    {
        this.floors = floors;
    }

    public void randomizeFloors(int maxFloors)
    {
        this.floors = Math.max(random.nextInt(maxFloors - startFloor), 4);
    }

    public void setType(towerType type)
    {
        this.type = type;
    }

    @Override
    public void generatePart(World world)
    {
        ArrayList<BlockInfo> buildList = new ArrayList<>();

        //System.out.println("Building a square tower at " + x + ", " + y + ", " + z + ". sizeX = " + sizeX + ", sizeZ = " + sizeZ + ". Floors = " + floors + ". Facing = " + facing.toString());

        if (type == towerType.SQUARE)
        {
            assembleSquare(buildList);
        }
        else
        {
            assembleRound(buildList);
        }

        if(!buildList.isEmpty()) {
            for(BlockInfo blockPlace : buildList) {
                blockPlace.build(world);
            }
        }

    }

    @Override
    public boolean isTower()
    {
        return true;
    }

    private void assembleSquare(ArrayList<BlockInfo> buildList)
    {
        int currentY;
        int floorHeight = dungeon.getFloorHeight();
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        IBlockState blockToBuild;
        System.out.println("Building a square tower at " + x + ", " + y + ", " + z + ". side length = " + sideLen + ". Floors = " + floors + ". Facing = " + facing.toString());

        //for each floor
        for (int currentFloor = 0; currentFloor < floors; currentFloor++)
        {
            currentY = y + currentFloor * (floorHeight + 1);
            blockToBuild = this.dungeon.getWallBlock().getDefaultState();

            //over the entire x/z area
            for (int i = 0; i < sideLen; i++)
            {
                for (int j = 0; j < sideLen; j++)
                {
                    // place a floor
                    buildList.add(new BlockInfo(x + i, currentY, z + j, blockToBuild));
                    buildList.add(new BlockInfo(x + i, currentY + floorHeight, z + j, blockToBuild));

                }
            }
            //Add x walls
            for (int i = 0; i < sideLen; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    buildList.add(new BlockInfo(x + i, currentY + j, z, blockToBuild));
                    buildList.add(new BlockInfo(x + i, currentY + j, z + sideLen - 1, blockToBuild));
                }
            }
            //Add z walls
            for (int i = 0; i < sideLen; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    buildList.add(new BlockInfo(x, currentY + j, z + i, blockToBuild));
                    buildList.add(new BlockInfo(x + sideLen - 1, currentY + j, z + i, blockToBuild));
                }
            }
        }

        currentY = y + floors * (floorHeight + 1);
        CastleAddonRoof roof = new CastleAddonRoof(x, currentY, z, sideLen, sideLen, CastleAddonRoof.RoofType.FOURSIDED, facing);
        roof.generate(buildList);
    }

    private void assembleRound(ArrayList<BlockInfo> buildList)
    {
        int currentY;
        int floorHeight = dungeon.getFloorHeight();

        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        IBlockState blockToBuild;
        System.out.println("Building a round tower at " + x + ", " + y + ", " + z + ". side length = " + sideLen + ". Floors = " + floors + ". Facing = " + facing.toString());

        // Tower length must be an odd number for the circle to work, so shrink if we have to
        if (sideLen % 2 == 0)
        {
            sideLen -= 1;
            if (facing == EnumFacing.NORTH)
            {
                z += 1;
            }
            else if (facing == EnumFacing.WEST)
            {
                x += 1;
            }
        }

        int radius = (sideLen - 1) / 2; // Subtract 1 because the final diameter ends up at (r*2+1)
        int midX = x + radius;
        int midZ = z + radius;
        //CircleCoord[][] towerGrid = new CircleCoord[sizeX][sizeX];
        Circle2D circle = new Circle2D(midX, midZ, radius);
        //Circle2D.CircleRegion[][] layout = circle.toArray(x, z, sizeX, sizeX);


        for (int currentFloor = 0; currentFloor < floors; currentFloor++)
        {
            currentY = y + currentFloor * (floorHeight + 1);
            blockToBuild = this.dungeon.getWallBlock().getDefaultState();

            ArrayList<Circle2D.Coord> floorCoords = circle.getFloorArray();
            ArrayList<Circle2D.Coord> wallCoords = circle.getWallArray();

            for (Circle2D.Coord coord : floorCoords)
            {
                buildList.add(new BlockInfo(coord.x, currentY, coord.z, blockToBuild));
                buildList.add(new BlockInfo(coord.x, currentY + floorHeight, coord.z, blockToBuild));
            }
            for (Circle2D.Coord coord : wallCoords)
            {
                for (int i = 0; i < floorHeight + 1; i++)
                {
                    buildList.add(new BlockInfo(coord.x, currentY + i, coord.z, blockToBuild));
                }
            }

        }

        currentY = y + floors * (floorHeight + 1);
        CastleAddonRoof roof = new CastleAddonRoof(x, currentY, z, sideLen, sideLen, CastleAddonRoof.RoofType.FOURSIDED, facing);
        roof.generate(buildList);
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
