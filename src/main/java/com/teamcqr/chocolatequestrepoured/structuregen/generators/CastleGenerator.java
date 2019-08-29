package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.CastlePartMain;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.*;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;

import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright (c) 25.05.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class CastleGenerator implements IDungeonGenerator
{

    private CastleDungeon dungeon;
    private int maxSize;
    private int roomSize;
    private int floorHeight;
    private Random random;
    private List<ICastlePart> parts;
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
    }

    @Override
    public void preProcess(World world, Chunk chunk, int x, int y, int z) {
        //Builds the support hill;
        if(dungeon.doBuildSupportPlatform()) {
            PlateauBuilder supportBuilder = new PlateauBuilder();
            supportBuilder.load(dungeon.getSupportBlock(), dungeon.getSupportTopBlock());
            supportBuilder.createSupportHill(random, world, new BlockPos(x, z, y), maxSize, maxSize, EPosType.CENTER_XZ_LAYER);
        }

        int sizeX;
        int sizeZ;
        int offsetX;
        int offsetZ;
        int numRoomsX;
        int numRoomsZ;
        int maxRoomsX;
        int maxRoomsZ;
        int buildAreaX = maxSize;
        int buildAreaZ = maxSize;
        int currentLayer = 0;
        int layerFloors = 2;
        int totalFloors = 0;

        //numRoomsX = randomizeNumRoomsFromSize(maxSize, 25);
        //numRoomsZ = randomizeNumRoomsFromSize(maxSize, 25);
        maxRoomsX = maxSize / roomSize;
        maxRoomsZ = maxSize / roomSize;
        numRoomsX = maxRoomsX / 2 + random.nextInt(maxRoomsX / 2);
        numRoomsZ = maxRoomsZ / 2 + random.nextInt(maxRoomsZ / 2);

        // Calculate random size based on maximum size
        sizeX = numRoomsX * roomSize;
        sizeZ = numRoomsZ * roomSize;

        // Add the main building
        CastlePartMain mainPart = new CastlePartMain(new BlockPos(x, y, z), maxRoomsX, maxRoomsZ, layerFloors, this.dungeon, currentLayer);
        parts.add(mainPart);

        // Randomize the height of the towers to rise above the nearby structure areas
        //if (!towers.isEmpty())
        //{
        //    for (CastlePartTower tower : towers)
        //    {
        //        tower.randomizeFloors(totalFloors); //make each tower a random height
        //    }
        //}
    }

    @Override
    public void buildStructure(World world, Chunk chunk, int x, int y, int z)
    {

        for (ICastlePart part : parts)
        {
            part.generatePart(world);
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
