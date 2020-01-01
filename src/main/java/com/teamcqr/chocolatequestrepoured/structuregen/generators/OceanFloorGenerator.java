package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonOceanFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.MinecraftForge;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class OceanFloorGenerator implements IDungeonGenerator {

	private CQStructure structure;
	private PlacementSettings placeSettings;
	private DefaultSurfaceDungeon dungeon;

	public OceanFloorGenerator() {
	}

	public OceanFloorGenerator(DungeonOceanFloor dun, CQStructure struct, PlacementSettings settings) {
		this.dungeon = dun;
		this.structure = struct;
		this.placeSettings = settings;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// Builds the support hill;
		if (this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			supportBuilder.createSupportHill(new Random(), world, new BlockPos(x, y, z), this.structure.getSizeX(), this.structure.getSizeZ(), EPosType.DEFAULT);
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// Simply puts the structure at x,y,z
		this.structure.placeBlocksInWorld(world, new BlockPos(x, y, z), this.placeSettings, EPosType.DEFAULT);

		CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x, y, z), new BlockPos(this.structure.getSizeX(), this.structure.getSizeY(), this.structure.getSizeZ()), world);
		event.setShieldCorePosition(this.structure.getShieldCorePosition());
		MinecraftForge.EVENT_BUS.post(event);
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		// UNUSED HERE

	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		// UNUSED HERE

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
