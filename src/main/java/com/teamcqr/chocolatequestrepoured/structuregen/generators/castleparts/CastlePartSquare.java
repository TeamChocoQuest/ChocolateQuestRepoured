package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons.CastleAddonRoof;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.addons.ICastleAddon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomSelector;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
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
public class CastlePartSquare implements ICastlePart
{
    private BlockPos start;
    private int sizeX;
    private int sizeZ;
    private int roomsX;
    private int roomsZ;
    private int floors;
    private EnumFacing facing;
    private CastleDungeon dungeon;
    private Random random;
    private int startLayer;
    private boolean isTopFloor;
    private CastleRoomSelector roomHelper;

    public CastlePartSquare(BlockPos origin, int roomsX, int roomsZ, int floors, CastleDungeon dungeon, EnumFacing facing, int startLayer)
    {
        this.dungeon = dungeon;
        this.floors = floors;
        this.random = this.dungeon.getRandom();
        this.roomsX = roomsX;
        this.roomsZ = roomsZ;

        this.start = origin;
        this.sizeX = roomsX * dungeon.getRoomSize();
        this.sizeZ = roomsZ * dungeon.getRoomSize();
        this.facing = facing;
        this.startLayer = startLayer;
        this.isTopFloor = false;

        roomHelper = new CastleRoomSelector(start, dungeon.getRoomSize(), dungeon.getFloorHeight(), floors, roomsX, roomsZ, random);
        roomHelper.fillRooms();
        System.out.println(roomHelper.printGrid());
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
        ArrayList<ICastleAddon> addonList = new ArrayList<>();

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
                    //blockToBuild = this.dungeon.getFloorBlock().getDefaultState();
                    //buildList.add(new BlockPlacement(x + i, currentY, z + j, blockToBuild));
                    // place a ceiling
                    blockToBuild = this.dungeon.getWallBlock().getDefaultState();
                    buildList.add(new BlockPlacement(x + i, currentY + floorHeight, z + j, blockToBuild));

                }
            }
            //Build walls

            /*
            //Add x walls
            for (int i = 0; i <= sizeX; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    if ((i % 4 == 0) &&
                            (i != 0) &&
                            (i != sizeX - 1) &&
                            ((j == floorHeight / 2) || (j - 1 == floorHeight / 2)))
                    {
                        blockToBuild = Blocks.GLASS_PANE.getDefaultState();
                        buildList.add(new BlockPlacement(x + i, currentY + j, z, blockToBuild));
                        buildList.add(new BlockPlacement(x + i, currentY + j, z + sizeZ, blockToBuild));
                    }
                    else
                    {
                        blockToBuild = dungeon.getWallBlock().getDefaultState();
                        buildList.add(new BlockPlacement(x + i, currentY + j, z, blockToBuild));
                        buildList.add(new BlockPlacement(x + i, currentY + j, z + sizeZ, blockToBuild));
                    }
                }
            }

            //Add z walls
            for (int i = 0; i <= sizeZ; i++)
            {
                for (int j = 0; j < floorHeight; j++)
                {
                    if ((i % 4 == 0) &&
                            (i != 0) &&
                            (i != sizeX - 1) &&
                            ((j == floorHeight / 2) || (j - 1 == floorHeight / 2)))
                    {
                        blockToBuild = Blocks.GLASS_PANE.getDefaultState();
                        buildList.add(new BlockPlacement(x, currentY + j, z + i, blockToBuild));
                        buildList.add(new BlockPlacement(x + sizeX, currentY + j, z + i, blockToBuild));
                    }
                    else
                    {
                        blockToBuild = dungeon.getWallBlock().getDefaultState();
                        buildList.add(new BlockPlacement(x, currentY + j, z + i, blockToBuild));
                        buildList.add(new BlockPlacement(x + sizeX, currentY + j, z + i, blockToBuild));
                    }
                }
            }

            if (currentFloor == 0)
            {
                addonList.add(new CastleAddonDoor(x + sizeX / 2 - 2, currentY + 1, z, 5, 4, CastleAddonDoor.DoorType.FENCE_BORDER, true));
                addonList.add(new CastleAddonDoor(x + sizeX / 2 - 2, currentY + 1, z + sizeZ, 5, 4, CastleAddonDoor.DoorType.FENCE_BORDER, true));
                addonList.add(new CastleAddonDoor(x, currentY + 1, z + sizeZ / 2 - 2, 5, 4, CastleAddonDoor.DoorType.FENCE_BORDER, false));
                addonList.add(new CastleAddonDoor(x + sizeX, currentY + 1, z + sizeZ / 2 - 2, 5, 4, CastleAddonDoor.DoorType.FENCE_BORDER, false));
            }
            */
        }

        roomHelper.generateRooms(buildList);

        //Build the roof
        currentY = y + floors * (floorHeight + 1);

        // Always make walkable if there is more castle above; otherwise 50% chance of walkable
        CastleAddonRoof.RoofType roofType;
        if (isTopFloor && random.nextBoolean())
        {
            System.out.println("Adding 4 sided roof");
            roofType = CastleAddonRoof.RoofType.FOURSIDED;
        }
        else
        {
            System.out.println("Adding walkable roof");
            roofType = CastleAddonRoof.RoofType.WALKABLE;
        }
        CastleAddonRoof roof = new CastleAddonRoof(x, currentY, z, sizeX + 1, sizeZ + 1, roofType, facing);
        addonList.add(roof);

        if (!addonList.isEmpty())
        {
            for (ICastleAddon addon : addonList)
            {
                addon.generate(buildList);
            }
        }

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
