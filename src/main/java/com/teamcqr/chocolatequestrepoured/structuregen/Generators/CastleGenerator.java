package com.teamcqr.chocolatequestrepoured.structuregen.Generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.Generators.castleparts.CastlePartSquare;
import com.teamcqr.chocolatequestrepoured.structuregen.Generators.castleparts.CastlePartTower;
import com.teamcqr.chocolatequestrepoured.structuregen.Generators.castleparts.ICastlePart;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

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
