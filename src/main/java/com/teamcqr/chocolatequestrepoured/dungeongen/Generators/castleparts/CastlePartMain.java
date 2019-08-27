package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts;

import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.addons.CastleAddonRoof;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.addons.ICastleAddon;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms.CastleRoomSelector;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Copyright (c) 01.06.2019 Developed by KalgogSmash:
 * https://github.com/kalgogsmash
 */
public class CastlePartMain implements ICastlePart
{
    private BlockPos start;
    private int sizeX;
    private int sizeZ;
    private int roomsX;
    private int roomsZ;
    private int floors;
    private CastleDungeon dungeon;
    private Random random;
    private int startLayer;
    private boolean isTopFloor;
    private CastleRoomSelector roomHelper;

    public CastlePartMain(BlockPos origin, int maxRoomsX, int maxRoomsZ, int floors, CastleDungeon dungeon, int startLayer)
    {
        this.dungeon = dungeon;
        this.floors = floors;
        this.random = this.dungeon.getRandom();
        this.roomsX = maxRoomsX;
        this.roomsZ = maxRoomsZ;

        this.start = origin;
        this.sizeX = maxRoomsX * dungeon.getRoomSize();
        this.sizeZ = maxRoomsZ * dungeon.getRoomSize();
        this.startLayer = startLayer;
        this.isTopFloor = false;

        roomHelper = new CastleRoomSelector(start, dungeon.getRoomSize(), dungeon.getFloorHeight(), floors, maxRoomsX, maxRoomsZ, random);
        roomHelper.fillRooms();
    }

    @Override
    public void generatePart(World world)
    {
        int currentY;
        int floorHeight = dungeon.getFloorHeight();
        int x = start.getX();
        int y = start.getY();
        int z = start.getZ();
        IBlockState blockToBuild;

        ArrayList<BlockPlacement> buildList = new ArrayList<>();

        System.out.println("Building a square part at " + x + ", " + y + ", " + z + ". sizeX = " + sizeX + ", sizeZ = " + sizeZ + ". Floors = " + floors);

        roomHelper.generateRooms(buildList);

        //Build the roof
        currentY = y + floors * (floorHeight + 1);

        if(!buildList.isEmpty())
        {
            for(BlockPlacement blockPlace : buildList)
            {
                blockPlace.build(world);
            }
        }

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
