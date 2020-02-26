package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.List;
import java.util.Random;

import org.omg.CosNaming.IstringHelper;

import com.teamcqr.chocolatequestrepoured.structuregen.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonOceanFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

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
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Builds the support hill;
		if (this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			lists.add(supportBuilder.createSupportHillList(new Random(), world, new BlockPos(x, y, z), this.structure.getSize().getX(), this.structure.getSize().getZ(), EPosType.DEFAULT));
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Simply puts the structure at x,y,z
		for (List<? extends IStructure> list : this.structure.addBlocksToWorld(world, new BlockPos(x, y, z), this.placeSettings, EPosType.DEFAULT, this.dungeon, chunk.x, chunk.z))
			lists.add(list);

		/*
		 * List<String> bosses = new ArrayList<>();
		 * for(UUID id : structure.getBossIDs()) {
		 * bosses.add(id.toString());
		 * }
		 * CQDungeonStructureGenerateEvent event = new CQDungeonStructureGenerateEvent(this.dungeon, new BlockPos(x, y, z), new BlockPos(this.structure.getSizeX(), this.structure.getSizeY(), this.structure.getSizeZ()), world, bosses);
		 * event.setShieldCorePosition(this.structure.getShieldCorePosition());
		 * MinecraftForge.EVENT_BUS.post(event);
		 */
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// UNUSED HERE

	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// UNUSED HERE

	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// UNUSED HERE

	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// MAKES NO SENSE HERE, COVER BLOCK WOULD BE AT WATER SURFACE
	}

}
