package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.CastlePartSquare;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.CastlePartTower;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.ICastlePart;
import com.teamcqr.chocolatequestrepoured.dungeongen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright (c) 25.05.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class CastleGenerator implements IDungeonGenerator{

    private CastleDungeon dungeon;
    private int maxSize;
    private int roomSize;
    private int floorHeight;
    private Random random;
    private ArrayList<ICastlePart> parts;
    private List<CastlePartTower> towers;
    private int totalX;
    private int totalY;
    private int totalZ;
    private static final int MIN_TOWER_SIZE = 5;

    private final static EnumFacing[] ExpansionDirections = {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};

    public CastleGenerator(CastleDungeon dungeon) {
        this.dungeon = dungeon;
        this.maxSize = this.dungeon.getMaxSize();
        this.roomSize = this.dungeon.getRoomSize();
        this.floorHeight = this.dungeon.getFloorHeight();
        this.random = this.dungeon.getRandom();
        this.parts = new ArrayList<>();
        this.towers = new ArrayList<>();
    }

    @Override
    public void preProcess(World world, Chunk chunk, int x, int y, int z) {
        //Builds the support hill;
        if(dungeon.doBuildSupportPlatform()) {
            PlateauBuilder supportBuilder = new PlateauBuilder();
            supportBuilder.load(dungeon.getSupportBlock(), dungeon.getSupportTopBlock());
            supportBuilder.generateSupportHill(dungeon.getRandom(), world, x, y + dungeon.getUnderGroundOffset(), z, maxSize, maxSize);
        }

        int sizeX;
        int sizeZ;
        int offsetX;
        int offsetZ;
        int numRoomsX;
        int numRoomsZ;
        int buildAreaX = maxSize;
        int buildAreaZ = maxSize;
        int currentLayer = 0;
        int layerFloors = 2;
        int totalFloors = 0;

        numRoomsX = randomizeNumRoomsFromSize(maxSize, 25);
        numRoomsZ = randomizeNumRoomsFromSize(maxSize, 25);
        // Calculate random size based on maximum size
        sizeX = numRoomsX * roomSize;
        sizeZ = numRoomsZ * roomSize;

        // Each iteration through this loop is one "layer" of castle - each layer is generated the same way, just with a shrinking build area
        while (Math.min(sizeX, sizeZ) > roomSize)
        {
            offsetX = roundToRoomSize(random.nextInt(buildAreaX - sizeX));
            offsetZ = roundToRoomSize(random.nextInt(buildAreaZ - sizeZ));

            // Apply the offset
            x += offsetX;
            z += offsetZ;

            // Add the main building
            CastlePartSquare mainPart = new CastlePartSquare(new BlockPos(x, y, z), numRoomsX, numRoomsZ, layerFloors, this.dungeon, EnumFacing.UP, currentLayer);

            // Build out each of the side structures to the North, East, South, and West. The structures keep building out
            // in that direction while there is room. It might be cool to eventually branch in all directions recursively
            // while there is room, but it will require better tracking of the available space on a given layer.
            for (EnumFacing side : EnumFacing.HORIZONTALS)
            {
                buildSide(mainPart, x, y, z, sizeX, sizeZ, offsetX, offsetZ, buildAreaX, buildAreaZ, side, layerFloors, currentLayer, totalFloors);
            }
            parts.add(mainPart);

            buildAreaX = sizeX;
            buildAreaZ = sizeZ;
            // Now try to build a new structure on top of this one
            numRoomsX = randomizeNumRoomsFromSize(sizeX, 50);
            numRoomsZ = randomizeNumRoomsFromSize(sizeZ, 50);
            // Calculate random size based on maximum size
            sizeX = numRoomsX * roomSize;
            sizeZ = numRoomsZ * roomSize;
            System.out.println("Calculated next layer (x, z) size: (" + sizeX + ", " + sizeZ + ")");

            totalFloors += layerFloors;
            y += (floorHeight + 1) * layerFloors;
            currentLayer++;
        }

        // Figure out what the top later is since the top floor has slightly different build behavior
        if (!parts.isEmpty())
        {
            int topLayer = parts.get(parts.size()-1).getStartLayer();
            System.out.println("Top layer is  " + topLayer);
            for (ICastlePart part : parts)
            {
                if (!part.isTower() && part.getStartLayer() == topLayer)
                {
                    part.setAsTopFloor();
                    System.out.println("Set a top layer because its layer was  " + part.getStartLayer());
                }
            }
        }

        // Randomize the height of the towers to rise above the nearby structure areas
        if (!towers.isEmpty())
        {
            for (CastlePartTower tower : towers)
            {
                tower.randomizeFloors(totalFloors); //make each tower a random height
            }
        }
    }

    private void buildSide(CastlePartSquare mainPart, int x, int y, int z, int sizeX, int sizeZ, int offsetX, int offsetZ, int buildAreaX,
                           int buildAreaZ, EnumFacing facing, int layerFloors, int currentLayer, int currentFloor)
    {
        int roomToBuildX;
        int roomToBuildZ;
        int numRoomsX = 0;
        int numRoomsZ = 0;
        int subSizeX;
        int subSizeZ;
        int subX;
        int subZ;

        //determine the available area to build a substructure, based on the total available build area
        //of the main structure and where it was build within that area
        switch (facing)
        {
            case NORTH:
                roomToBuildX = sizeX;
                roomToBuildZ = offsetZ;
                break;
            case EAST:
                roomToBuildX = buildAreaX - sizeX - offsetX;
                roomToBuildZ = sizeZ;
                break;
            case SOUTH:
                roomToBuildX = sizeX;
                roomToBuildZ = buildAreaZ - sizeZ - offsetZ;
                break;
            case WEST:
            default:
                roomToBuildX = offsetX;
                roomToBuildZ = sizeZ;
                break;

        }

        //While there is at least room for a tower
        while (roomToBuildX > MIN_TOWER_SIZE && roomToBuildZ > MIN_TOWER_SIZE)
        {
            //determine if there is room for an entire additional structure
            boolean buildSquarePart = (roomToBuildX > roomSize && roomToBuildZ > roomSize);

            if (buildSquarePart)
            {
                numRoomsX = randomizeNumRoomsFromSize(sizeX, 50);
                numRoomsZ = randomizeNumRoomsFromSize(sizeZ, 50);
                subSizeX = numRoomsX * roomSize;
                subSizeZ = numRoomsZ * roomSize;
            }
            else
            {
                // building a tower
                subSizeX = Math.min(roomToBuildX, roomToBuildZ);
                subSizeZ = subSizeX;
            }

            //determine the top left corner of this structure
            boolean alignToFarSide = random.nextBoolean();
            subX = getSideStructureX(x, sizeX, subSizeX, facing, alignToFarSide);

            subZ = getSideStructureZ(z, sizeZ, subSizeZ, facing, alignToFarSide);

            if (buildSquarePart)
            {
                CastlePartSquare sidePart = new CastlePartSquare(new BlockPos(subX, y, subZ), numRoomsX, numRoomsZ, layerFloors, this.dungeon, facing, currentLayer);
                parts.add(sidePart);
                mainPart.registerSideBuilding(facing, numRoomsX, numRoomsZ, alignToFarSide);
                mainPart = sidePart;

            }
            else
            {
                towers.add(new CastlePartTower(new BlockPos(subX, y, subZ), subSizeX, currentFloor, this.dungeon, facing));
                break; //don't want to build anything after a tower
            }

            //recalculate how much build room we have given the structure we just built
            roomToBuildX = (facing.getAxis() == EnumFacing.Axis.X) ? (roomToBuildX - subSizeX) : (subSizeX);
            roomToBuildZ = (facing.getAxis() == EnumFacing.Axis.Z) ? (roomToBuildZ - subSizeZ) : (subSizeZ);

            //this structure is now considered the "main" structure as we build outwards
            sizeX = subSizeX;
            sizeZ = subSizeZ;

            //set the new top left corner
            x = subX;
            z = subZ;
        }
    }

    // Get the leftmost (lowest) X location of a side structure building
    private int getSideStructureX(int x, int mainSizeX, int sideSizeX, EnumFacing facing, boolean leftAligned)
    {
        switch (facing)
        {
            case NORTH:
            case SOUTH:
                return leftAligned ? x + mainSizeX - sideSizeX : x;
            case EAST:
                return x + mainSizeX;
            case WEST:
                return x - sideSizeX;
            default:
                return x;
        }
    }

    // Get the topmost (lowest) Z location of a side structure building
    private int getSideStructureZ(int z, int mainSizeZ, int sideSizeZ, EnumFacing facing, boolean bottomAligned)
    {
        switch (facing)
        {
            case NORTH:
                return z - sideSizeZ;
            case SOUTH:
                return z + mainSizeZ;
            case EAST:
            case WEST:
                return bottomAligned ? z + mainSizeZ - sideSizeZ : z;

            default:
                return z;
        }
    }

    @Override
    public void buildStructure(World world, Chunk chunk, int x, int y, int z)
    {

        for (ICastlePart part : parts)
        {
            part.generatePart(world);
        }
        for (ICastlePart tower : towers)
        {
            tower.generatePart(world);
        }


        CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x,y,z), new BlockPos(x + totalX, y + totalY, z + totalZ), world);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public void postProcess(World world, Chunk chunk, int x, int y, int z) {
        //Does nothing here
    }

    @Override
    public void fillChests(World world, Chunk chunk, int x, int y, int z) {
        //Also does nothing
    }

    @Override
    public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
        //Also does nothing
    }

    @Override
    public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
        // TODO Auto-generated method stub

    }

    private EnumFacing getRandomFacing()
    {
        int idx = random.nextInt(4);
        switch (idx)
        {
            case 0:
                return EnumFacing.NORTH;
            case 1:
                return EnumFacing.EAST;
            case 2:
                return EnumFacing.SOUTH;
            case 3:
            default:
                return EnumFacing.WEST;
        }
    }

    private int roundToRoomSize(int size)
    {
        return Math.max((size - (size % dungeon.getRoomSize())), 0);
    }

    private int randomizeNumRoomsFromSize(int size, int minPercentOfSizeUsed)
    {
        int divisor = 100 / minPercentOfSizeUsed;
        if (size < roomSize)
        {
            return 0;
        }
        else
        {
            int minSizeUsed = size / divisor;
            int sizeUsed = minSizeUsed + random. nextInt(size - minSizeUsed);
            return sizeUsed / roomSize;
        }
    }

}
