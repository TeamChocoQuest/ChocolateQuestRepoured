package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts;

import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 01.06.2019 Developed by KalgogSmash:
 * https://github.com/kalgogsmash
 */
public class CastlePartSquare implements ICastlePart
{
    boolean hasBoss;
    CastleDungeon dungeon;

    public CastlePartSquare(boolean hasBoss, CastleDungeon dungeon)
    {
        this.hasBoss = hasBoss;
        this.dungeon = dungeon;
    }

    public CastlePartSquare()
    {
        this.hasBoss = false;
    }

    @Override
    public void generatePart(World world, int x, int y, int z, int sizeX, int sizeZ, int roomSize, int floors)
    {
        int roomsX = Math.max(1, sizeX / roomSize);
        int roomsZ = Math.max(1, sizeZ / roomSize);
        int roomSizeX = sizeX / roomsX;
        int roomSizeZ = sizeZ / roomsZ;
        //int lastRoomOffsetX = sizeX - roomsX * roomSizeX;
        //int lastRoomOffsetZ = sizeZ - roomsZ * roomSizeZ;
        int currentY;
        int floorHeight = dungeon.getFloorHeight();

        List<BlockPos> floorBlocks = new ArrayList<>();
        List<BlockPos> wallBlocks = new ArrayList<>();
        List<BlockPos> ceilingBlocks = new ArrayList<>();

        System.out.println("Building a square structure at " + x + ", " + y + ", " + z + ". sizeX = " + sizeX + ", sizeZ = " + sizeZ + ". Floors = " + floors);
        //build the first floor

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
            //Add x walls
            for (int i = 0; i < sizeX; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    wallBlocks.add(new BlockPos(x + i, currentY + j, z));
                    wallBlocks.add(new BlockPos(x + i, currentY + j, z + sizeZ - 1));
                }
            }
            //Add y walls
            for (int i = 0; i < sizeZ; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    wallBlocks.add(new BlockPos(x, currentY + j, z + i));
                    wallBlocks.add(new BlockPos(x + sizeX - 1, currentY + j, z + i));
                }
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
        }
    }
}
