package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.DefaultGenerator;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class DefaultSurfaceDungeon extends DungeonBase {
	
	private File structureFolderPath;
	
	public DefaultSurfaceDungeon(File configFile) {
		super();
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(configFile);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			prop = null;
			configFile = null;
		} catch (IOException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			prop = null;
			configFile = null;
		}
		if(prop != null && configFile != null) {
			super.load(prop);
			super.chance = PropertyFileHelper.getIntProperty(prop, "chance", 0);
			super.name = configFile.getName().replaceAll(".prop", "");
			super.allowedDims = PropertyFileHelper.getIntArrayProperty(prop, "allowedDims", new int[]{0});
			super.unique = PropertyFileHelper.getBooleanProperty(prop, "unique", false);
			this.structureFolderPath = new File(prop.getProperty("structurefolder", "defaultFolder"));
		}
	}
	
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
