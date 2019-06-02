package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.CastlePartSquare;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.ICastlePart;
import com.teamcqr.chocolatequestrepoured.dungeongen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.CastleDungeon;

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

	public CastleGenerator(CastleDungeon dungeon) {
		this.dungeon = dungeon;
		this.maxSize = this.dungeon.getMaxSize();
		this.roomSize = this.dungeon.getRoomSize();
		this.floorHeight = this.dungeon.getFloorHeight();
		this.random = new Random();
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		BlockPos start = new BlockPos(x, y, z);
		//Builds the support hill;
		if(this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			supportBuilder.generate(new Random(), world, x, y + this.dungeon.getUnderGroundOffset(), z, maxSize, maxSize);
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z)
	{
		// Calculate random size based on maximum size
		int quarterSizeX = maxSize / 4;
		int quarterSizeZ = maxSize / 4;
		int sizeX = quarterSizeX + random.nextInt(quarterSizeX * 3);
		int sizeZ = quarterSizeZ + random.nextInt(quarterSizeZ * 3);
		int offsetX = random.nextInt(quarterSizeX);
		int offsetZ = random.nextInt(quarterSizeZ);

		// Size of building must be at least 1 room
		sizeX = Math.max(roomSize, sizeX);
		sizeZ = Math.max(roomSize, sizeZ);

		// Apply the offset
		x += offsetX;
		z += offsetZ;

		List<ICastlePart> parts = new ArrayList<>();

		parts.add(new CastlePartSquare(false, this.dungeon));

		for (ICastlePart part : parts)
		{
			part.generatePart(world, x, y, z, sizeX, sizeZ, roomSize, 2);
		}
		//generateSideStructure(world, x, y, z, sizeX, offsetZ, 1, Facing.NORTH);
		//generateSideStructure(world, x + sizeX, y, z, maxSize - sizeX - offsetX, sizeZ, 1, Facing.EAST);
		//generateSideStructure(world, x, y, z + sizeZ, sizeX, maxSize - sizeZ - offsetZ, 1, Facing.SOUTH);
		//generateSideStructure(world, x , y, z, offsetX, sizeZ, 1, Facing.WEST);



		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x,y,z), new BlockPos(x + sizeX, y, z + sizeZ), chunk.getPos(), world);
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

}
