package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts;

import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockInfo;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Copyright (c) 01.06.2019 Developed by KalgogSmash:
 * https://github.com/kalgogsmash
 */
public class CastlePartSquare implements ICastlePart
{
    private BlockPos start;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private int floors;
    private EnumFacing facing;
    CastleDungeon dungeon;
    private Random random;
    private int startLayer;
    private boolean isTopFloor;

    public CastlePartSquare(BlockPos origin, int sizeX, int sizeZ, int floors, CastleDungeon dungeon, EnumFacing facing, int startLayer)
    {
        this.dungeon = dungeon;
        this.floors = floors;
        this.random = new Random(origin.getX() + origin.getY() + origin.getZ());

        this.start = origin;
        this.sizeX = sizeX;
        this.sizeY = this.dungeon.getFloorHeight() * floors;
        this.sizeZ = sizeZ;
        this.facing = facing;
        this.startLayer = startLayer;
        this.isTopFloor = false;
    }

    @Override
    public void generatePart(World world)
    {
        int roomsX = Math.max(1, sizeX / dungeon.getRoomSize());
        int roomsZ = Math.max(1, sizeZ / dungeon.getRoomSize());
        int roomSizeX = sizeX / roomsX;
        int roomSizeZ = sizeZ / roomsZ;
        //int lastRoomOffsetX = sizeX - roomsX * roomSizeX;
        //int lastRoomOffsetZ = sizeZ - roomsZ * roomSizeZ;
        int currentY;
        int floorHeight = dungeon.getFloorHeight();
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();

        List<BlockPos> floorBlocks = new ArrayList<>();
        List<BlockPos> wallBlocks = new ArrayList<>();
        List<BlockPos> ceilingBlocks = new ArrayList<>();
        List<BlockPos> windowBlocks = new ArrayList<>();
        List<BlockInfo> roofBlocks = new ArrayList<>();

        System.out.println("Building a square part at " + x + ", " + y + ", " + z + ". sizeX = " + sizeX + ", sizeZ = " + sizeZ + ". Floors = " + floors + ". Facing = " + facing.toString());

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
                    floorBlocks.add(new BlockPos(x + i, currentY, z + j));
                    // place a ceiling
                    ceilingBlocks.add(new BlockPos(x + i, currentY + floorHeight, z + j));

                }
            }
            //Build walls

            //Add x walls
            for (int i = 0; i < sizeX; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    if ((i % 4 == 0) &&
                            (i != 0) &&
                            (i != sizeX - 1) &&
                            ((j == floorHeight / 2) || (j - 1 == floorHeight / 2)))
                    {
                        windowBlocks.add(new BlockPos(x + i, currentY + j, z));
                        windowBlocks.add(new BlockPos(x + i, currentY + j, z + sizeZ - 1));
                    }
                    else
                    {
                        wallBlocks.add(new BlockPos(x + i, currentY + j, z));
                        wallBlocks.add(new BlockPos(x + i, currentY + j, z + sizeZ - 1));
                    }
                }
            }
            //Add z walls
            for (int i = 0; i < sizeZ; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    if ((i % 4 == 0) &&
                            (i != 0) &&
                            (i != sizeX - 1) &&
                            ((j == floorHeight / 2) || (j - 1 == floorHeight / 2)))
                    {
                        windowBlocks.add(new BlockPos(x, currentY + j, z + i));
                        windowBlocks.add(new BlockPos(x + sizeX - 1, currentY + j, z + i));
                    }
                    else
                    {
                        wallBlocks.add(new BlockPos(x, currentY + j, z + i));
                        wallBlocks.add(new BlockPos(x + sizeX - 1, currentY + j, z + i));
                    }
                }
            }
        }

        //Build the roof
        currentY = y + floors * (floorHeight + 1);

        // Always make walkable if there is more castle above; otherwise 50% chance of walkable
        if (isTopFloor && random.nextBoolean())
        {
            addRoofPyramid(wallBlocks, roofBlocks, x, currentY, z, sizeX + 1, sizeZ + 1);
        }
        else
        {
            addRoofWalkable(wallBlocks, x, currentY, z, sizeX, sizeZ);
        }



        if(!floorBlocks.isEmpty()) {
            for(BlockPos pos : floorBlocks) {
                world.setBlockState(pos, dungeon.getFloorBlock().getDefaultState());
            }
        }

        if(!ceilingBlocks.isEmpty()) {
            for(BlockPos pos : ceilingBlocks) {
                world.setBlockState(pos, dungeon.getWallBlock().getDefaultState());
            }
        }

        if(!wallBlocks.isEmpty()) {
            for(BlockPos pos : wallBlocks) {
                world.setBlockState(pos, dungeon.getWallBlock().getDefaultState());
            }
        }

        if(!windowBlocks.isEmpty()) {
            for(BlockPos pos : windowBlocks) {
                world.setBlockState(pos, Blocks.GLASS_PANE.getDefaultState());
            }
        }

        if(!roofBlocks.isEmpty()) {
            for(BlockInfo blockPlace : roofBlocks) {
                world.setBlockState(blockPlace.position, blockPlace.state);
            }
        }
    }

    private void addRoofWalkable(List<BlockPos> wallBlocks, int x, int y, int z, int lenX, int lenZ)
    {
        for (int i = 0; i < lenX; i++)
        {
            if (this.facing != EnumFacing.SOUTH)
            {
                wallBlocks.add(new BlockPos(x + i, y, z));
            }
            if (this.facing != EnumFacing.NORTH)
            {
                wallBlocks.add(new BlockPos(x + i, y, z + lenZ - 1));
            }
            if ((i == 0) || (i % 4 == 0) || ((i + 1) % 4 == 0))
            {
                if (this.facing != EnumFacing.SOUTH)
                {
                    wallBlocks.add(new BlockPos(x + i, y + 1, z));
                }
                if (this.facing != EnumFacing.NORTH)
                {
                    wallBlocks.add(new BlockPos(x + i, y + 1, z + lenZ - 1));
                }
            }
        }
        for (int i = 0; i < lenZ; i++)
        {
            if (this.facing != EnumFacing.EAST)
            {
                wallBlocks.add(new BlockPos(x, y, z + i));
            }
            if (this.facing != EnumFacing.WEST)
            {
                wallBlocks.add(new BlockPos(x + lenX - 1, y, z + i));
            }
            if ((i == 0) || (i % 4 == 0) || ((i + 1) % 4 == 0))
            {
                if (this.facing != EnumFacing.EAST)
                {
                    wallBlocks.add(new BlockPos(x, y + 1, z + i));
                }
                if (this.facing != EnumFacing.WEST)
                {
                    wallBlocks.add(new BlockPos(x + lenX - 1, y + 1, z + i));
                }
            }
        }
    }

    private void addRoofPyramid(List<BlockPos> wallBlocks, List<BlockInfo> roofBlocks, int x, int y, int z, int lenX, int lenZ)
    {
        int roofX;
        int roofZ;
        int roofLenX;
        int roofLenZ;
        IBlockState stairState;

        do
        {
            // Add the foundation under the roof
            for (int i = 0; i < lenX; i++)
            {
                wallBlocks.add(new BlockPos(x + i, y, z));
                wallBlocks.add(new BlockPos(x + i, y, z + lenZ - 1));
            }
            for (int j = 0; j < lenZ; j++)
            {
                wallBlocks.add(new BlockPos(x, y, z + j));
                wallBlocks.add(new BlockPos(x + lenX - 1, y, z + j));
            }

            roofX = x - 1;
            roofZ = z - 1;
            roofLenX = lenX + 2;
            roofLenZ = lenZ + 2;

            //add the north row
            for (int i = 0; i < roofLenX; i++)
            {
                BlockInfo bInfo = new BlockInfo(new BlockPos(roofX + i, y, roofZ), Blocks.SPRUCE_STAIRS.getDefaultState());
                bInfo.applyProperty(BlockStairs.FACING, EnumFacing.SOUTH);

                //Apply properties to corner pieces
                if (i == 0)
                {
                    bInfo.applyProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                }
                else if (i == roofLenX - 1)
                {
                    bInfo.applyProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                }

                roofBlocks.add(bInfo);
            }
            //add the south row
            for (int i = 0; i < roofLenX; i++)
            {
                BlockInfo bInfo = new BlockInfo(new BlockPos(roofX + i, y, roofZ + roofLenZ - 1), Blocks.SPRUCE_STAIRS.getDefaultState());
                bInfo.applyProperty(BlockStairs.FACING, EnumFacing.NORTH);

                //Apply properties to corner pieces
                if (i == 0)
                {
                    bInfo.applyProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                }
                else if (i == roofLenX - 1)
                {
                    bInfo.applyProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                }
                roofBlocks.add(bInfo);
            }

            for (int i = 0; i < roofLenZ; i++)
            {
                BlockInfo bInfo = new BlockInfo(new BlockPos(roofX, y, roofZ + i), Blocks.SPRUCE_STAIRS.getDefaultState());
                bInfo.applyProperty(BlockStairs.FACING, EnumFacing.EAST);

                roofBlocks.add(bInfo);

                bInfo = new BlockInfo(new BlockPos(roofX + roofLenX - 1, y, roofZ + i), Blocks.SPRUCE_STAIRS.getDefaultState());
                bInfo.applyProperty(BlockStairs.FACING, EnumFacing.WEST);

                roofBlocks.add(bInfo);
            }

            x++;
            y++;
            z++;
            lenX -= 2;
            lenZ -= 2;
        } while (lenX >= 0 && lenZ >= 0);

    }

    @Override
    public boolean isTower()
    {
        return false;
    }

    @Override
    public void setAsTopFloor()
    {
        this.isTopFloor = true;
    }

    @Override
    public int getStartLayer()
    {
        return this.startLayer;
    }
}
