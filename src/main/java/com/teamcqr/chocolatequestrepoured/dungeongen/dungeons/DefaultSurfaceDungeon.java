package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.DefaultGenerator;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class DefaultSurfaceDungeon extends DungeonBase {
	
	private File structureFolderPath;
	
	@Override
	public IDungeonGenerator getGenerator() {
		return new DefaultGenerator();
	}
	
	private File pickStructure(long worldSeed) {
		Random rdm = new Random();
		rdm.setSeed(worldSeed);
		File chosenStructure = this.structureFolderPath;
		if(this.structureFolderPath.isDirectory()) {
			chosenStructure = this.structureFolderPath.listFiles()[rdm.nextInt(this.structureFolderPath.listFiles().length)];
		}
		if(chosenStructure != null) {
			return chosenStructure;
		}
		return null;
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk) {
		File structure = pickStructure(world.getSeed());
		
	}
	
}
