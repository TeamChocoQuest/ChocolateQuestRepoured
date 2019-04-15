package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.dungeongen.dungeons.DefaultSurfaceDungeon;
import com.teamcqr.chocolatequestrepoured.structurefile.CQStructure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class SimplePasteGenerator implements IDungeonGenerator{
	
	private CQStructure structure;
	private PlacementSettings placeSettings;
	private DefaultSurfaceDungeon dungeon;
	
	public SimplePasteGenerator(DefaultSurfaceDungeon dun, CQStructure struct, PlacementSettings settings) {
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
			supportBuilder.generate(new Random(), world, x, y + this.dungeon.getUnderGroundOffset(), z, this.structure.getSizeX() + 16, this.structure.getSizeZ() + 16);
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		//Simply puts the structure at x,y,z
		this.structure.placeBlocksInWorld(world, new BlockPos(x, y, z), this.placeSettings);
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
