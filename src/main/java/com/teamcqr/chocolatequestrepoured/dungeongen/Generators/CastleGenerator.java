package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.dungeongen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright (c) 25.05.2019
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class CastleGenerator implements IDungeonGenerator{

	private CastleDungeon dungeon;
	private int sizeX;
	private int sizeZ;
    private int floors;
    private int roomSize;


	public CastleGenerator(CastleDungeon dungeon) {
		this.dungeon = dungeon;
		this.sizeX = this.dungeon.getSizeX();
		this.sizeZ = this.dungeon.getSizeZ();
		this.floors = 5;
		this.roomSize = this.dungeon.getRoomSize();
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		BlockPos start = new BlockPos(x, y, z);
		//Builds the support hill;
		if(this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			supportBuilder.generate(new Random(), world, x, y + this.dungeon.getUnderGroundOffset(), z, this.sizeX, this.sizeZ);
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		//Simply puts the structure at x,y,z
		BlockPos bp = new BlockPos(x, y, z);
        generateSquaredstructure(world, chunk, x, y, z, this.sizeX, this.sizeZ, this.floors);

		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x,y,z), new BlockPos(this.dungeon.getSizeX(), this.dungeon.getSizeY(), this.dungeon.getSizeZ()),chunk.getPos(),world);
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

	private void placeBlock(World world, int x, int y, int z, IBlockState blockState)
	{
		BlockPos bp = new BlockPos(x, y, z);
		world.setBlockState(bp, blockState);
	}

	private void generateFlatSurface(World world, int x, int y, int z, int sizeX, int sizeZ, IBlockState blockState)
	{
		// build the first floor
		for (int i = 0; i <= sizeX; i++)
		{
			for (int j = 0; j < sizeZ; j++)
			{
				placeBlock(world, x + i, y, z + j, blockState);
			}
		}
	}

	private void generateSquaredstructure(World world, Chunk chunk, int x, int y, int z, int sizeX, int sizeZ, int floors)
	{
		int roomsX = Math.max(1, sizeX / roomSize);
		int roomsZ = Math.max(1, sizeZ / roomSize);
		int roomSizeX = sizeX / roomsX;
		int roomSizeZ = sizeZ / roomsZ;
		int lastRoomOffsetX = sizeX - roomsX * roomSizeX;
		int lastRoomOffsetZ = sizeZ - roomsZ * roomSizeZ;
		int currentY = y;
		int floorHeight = 10;
		IBlockState currentBlockType;

		// build the first floor
		currentBlockType = this.dungeon.getMainBlock().getDefaultState();
		generateFlatSurface(world, x, y, z, sizeX, sizeZ, currentBlockType);

		// for each floor
		for (int currentfloor = 0; currentfloor <= floors; currentfloor++)
		{
			// over the entire x/z area
			for (int i = 0; i < sizeX; i++)
			{
				for (int j = 0; j < sizeZ; j++)
				{
					// place a ceiling
                    placeBlock(world, x + i, currentY + floorHeight, z + j, currentBlockType);

                    // if not at the top floor, place a floor above the ceiling
					if (currentfloor != floors)
					{
					    // TODO Actually generate floor
                        placeBlock(world, x + i, currentY + floorHeight + 1, z + j, currentBlockType);
                    }
					else
                    {
                        placeBlock(world, x + i, currentY + floorHeight + 1, z + j, currentBlockType);
                    }
				}
			}
			currentY++;

			// Build two sides of walls
			for (int i = 0; i < sizeX; i++)
			{
				// for the height of the floor
				for (int j = 0; j < floorHeight; j++)
				{
                    placeBlock(world, x + i, currentY + j, z,  currentBlockType);
                    placeBlock(world, x + i, currentY + j, (z + sizeZ - 1),  currentBlockType);
				}
				if ((i > 0) && (i < sizeX))
				{
					// build windows in the middle (overwrite some of the walls)
				}
			}

			// Generate the other two walls
			for (int i = 0; i < sizeZ; i++)
			{
				for (int j = 0; j < floorHeight; j++)
				{
                    placeBlock(world, x, currentY + j, z + i,  currentBlockType);
                    placeBlock(world, (x + sizeX - 1), currentY + j, z + i,  currentBlockType);
				}
				if ((i > 0) && (i < sizeZ))
				{
				    // windows
				}
			}

            currentY += floorHeight;
		}

		// TODO Generate roof
	}

	private void generateRectangularRoom(World world, Chunk chunk, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(x, y, z);
		int xMax = this.dungeon.getSizeX() - 1;
		int yMax = this.dungeon.getSizeY() - 1;
		int zMax = this.dungeon.getSizeZ() - 1;

		Block airBlock = Block.getBlockFromName("minecraft:air");

		for (int xOffset = 0; xOffset <= xMax; xOffset++)
		{
			for (int yOffset = 0; yOffset <= yMax; yOffset++)
			{
				for (int zOffset = 0; zOffset <= zMax; zOffset++)
				{
					if (xOffset == 0 || xOffset == xMax ||
							yOffset == 0 || yOffset == yMax ||
							zOffset == 0 || zOffset == zMax)
					{
						world.setBlockState(pos, this.dungeon.getMainBlock().getDefaultState());
					}
					else
					{
						world.setBlockState(pos, airBlock.getDefaultState());
					}
				}
			}
		}
	}

	private void generateSquareRoom(World world, Chunk chunk, int sideLength)
	{
		generateRectangularRoom(world, chunk, sideLength, sideLength, sideLength);
	}

}
