package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.DungeonOceanFloor;
import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class OceanFloorGenerator implements IDungeonGenerator{

	private CQStructure structure;
	private PlacementSettings placeSettings;
	private DefaultSurfaceDungeon dungeon;
	
	public OceanFloorGenerator() {}
	
	public OceanFloorGenerator(DungeonOceanFloor dun, CQStructure struct, PlacementSettings settings) {
		this.dungeon = dun;
		this.structure = struct;
		this.placeSettings = settings;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		//Builds the support hill;
		if(this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			supportBuilder.generate(new Random(), world, x, y, z, this.structure.getSizeX() + 16, this.structure.getSizeZ() + 16);
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		//Simply puts the structure at x,y,z
		this.structure.placeBlocksInWorld(world, new BlockPos(x, y, z), this.placeSettings);
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		//UNUSED HERE
		
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//UNUSED HERE
		
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// UNUSED HERE
		
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		// MAKES NO SENSE HERE, COVER BLOCK WOULD BE AT WATER SURFACE
	}

}
