package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts;

import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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


    public CastlePartSquare(BlockPos origin, int sizeX, int sizeZ, int floors, CastleDungeon dungeon, EnumFacing facing)
    {
        this.dungeon = dungeon;
        this.floors = floors;
        this.random = new Random(origin.getX() + origin.getY() + origin.getZ());

        this.start = origin;
        this.sizeX = sizeX;
        this.sizeY = this.dungeon.getFloorHeight() * floors;
        this.sizeZ = sizeZ;
        this.facing = facing;
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

    @Override
    public boolean isTower()
    {
        return false;
    }
}
