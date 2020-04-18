package com.teamcqr.chocolatequestrepoured.structuregen.generators;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonDataManager;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerationManager;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.Structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public interface IDungeonGenerator {

	void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists);

	void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists);

	void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists);

	void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists);

	void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists);

	void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists);

	default void generate(World world, Chunk chunk, int x, int y, int z) {
		if (world.isRemote) {
			return;
		}

		DungeonBase dungeon = this.getDungeon();
		CQRMain.logger.info("Start generating dungeon {} at x={}, y={}, z={}.", dungeon, x, y, z);
		Structure structure = new Structure(world);
		List<List<? extends IStructure>> lists = new ArrayList<>();

		this.preProcess(world, chunk, x, y, z, lists);
		this.buildStructure(world, chunk, x, y, z, lists);
		this.postProcess(world, chunk, x, y, z, lists);
		this.fillChests(world, chunk, x, y, z, lists);
		this.placeSpawners(world, chunk, x, y, z, lists);
		this.placeCoverBlocks(world, chunk, x, y, z, lists);

		for (List<? extends IStructure> list : lists) {
			structure.addList(list);
		}
		structure.addLightParts();
		structure.setupProtectedRegion(dungeon.preventBlockBreaking(), dungeon.preventBlockPlacing(), dungeon.preventExplosionsTNT(), dungeon.preventExplosionsOther(), dungeon.preventFireSpreading(), dungeon.preventEntitySpawning(), dungeon.ignoreNoBossOrNexus());

		DungeonDataManager.addDungeonEntry(world, getDungeon(), new BlockPos(x,y,z));
		
		DungeonGenerationManager.addStructure(world, structure);
	}

	DungeonBase getDungeon();

}
