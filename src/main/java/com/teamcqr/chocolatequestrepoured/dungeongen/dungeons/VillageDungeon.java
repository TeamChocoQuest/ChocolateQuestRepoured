package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.VillageGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class VillageDungeon extends DungeonBase {
	
	private List<File> structureFolder = new ArrayList<File>();
	private File centerStructureFolder;
	
	private int minBuildings = 7;
	private int maxBuilding = 14;
	
	private int minDistance = 15;
	private int maxDistance = 30;
	
	private boolean buildPaths = true;
	private boolean placeInCircle = false;
	private Block pathBlock = Blocks.GRASS_PATH;
	
	@Override
	public IDungeonGenerator getGenerator() {
		return new VillageGenerator(this);
	}
	
	
	public VillageDungeon(File configFile) {
		super(configFile);
		boolean success = true;
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(configFile);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			prop = null;
			configFile = null;
			success = false;
		} catch (IOException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			prop = null;
			configFile = null;
			success = false;
		}
		if(prop != null && configFile != null && fis != null) {
			String structureFolderName = prop.getProperty("structurefolder", "village_buildings");
			File structureFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER.getAbsolutePath() + "/" + structureFolderName);
			if(!structureFolder.exists() || structureFolder.isFile()) {
				if(structureFolder.isFile()) {
					structureFolder.delete();
				}
				structureFolder.mkdirs();
			}
			for(File f : structureFolder.listFiles()) {
				addFiles(f);
			}
			
			String centerStructFolder = prop.getProperty("centerstructurefolder", "village_centers");
			this.centerStructureFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER.getAbsolutePath() + "/" + centerStructFolder);
			if(!this.centerStructureFolder.exists() || this.centerStructureFolder.isFile()) {
				if(this.centerStructureFolder.isFile()) {
					this.centerStructureFolder.delete();
				}
				this.centerStructureFolder.mkdirs();
			}
			this.minBuildings = PropertyFileHelper.getIntProperty(prop, "minbuildings", 6);
			this.maxBuilding = PropertyFileHelper.getIntProperty(prop, "maxbuildings", 10);
			
			this.minDistance = PropertyFileHelper.getIntProperty(prop, "mindistance", 20);
			this.maxDistance = PropertyFileHelper.getIntProperty(prop, "maxdistance", 40);
			
			this.placeInCircle = PropertyFileHelper.getBooleanProperty(prop, "circle", false);
			
			this.buildPaths = PropertyFileHelper.getBooleanProperty(prop, "buildroads", true);
			
			this.pathBlock = Blocks.GRASS_PATH;
			try {
				Block tmp = Block.getBlockFromName(prop.getProperty("pathblock", "minecraft:gravel"));
				if(tmp != null) {
					this.pathBlock = tmp;
				}
			} catch(Exception ex) {
				this.pathBlock = Blocks.GRAVEL;
				System.out.println("couldnt load path block! using default value (gravel block)...");
			}
			
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
				success = false;
			}
		} else {
			success = false;
		}
		if(success) {
			this.registeredSuccessful = true;
		}
	}
	
	private void addFiles(File folder) {
		if(folder.isDirectory()) {
			for(File f : folder.listFiles()) {
				addFiles(f);
			}
		} else {
			this.structureFolder.add(folder);
		}
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);
		
		this.generator = new VillageGenerator(this);
		
		int buildings = DungeonGenUtils.getIntBetweenBorders(this.minBuildings, this.maxBuilding, random);
		for(int i = 0; i < buildings; i++) {
			File building = getRandomBuilding(random);
			((VillageGenerator)this.generator).addStructure(building);
			building = this.centerStructureFolder;
			if(this.centerStructureFolder.isDirectory()) {
				building = this.centerStructureFolder.listFiles()[new Random().nextInt(this.centerStructureFolder.listFiles().length)];
			}
			((VillageGenerator)this.generator).setCenterStructure(building);
		}
		//Generating it...
		int y = DungeonGenUtils.getHighestYAt(chunk, x, z, false);
		System.out.println("Generating structure " + this.name + " at X: " + x + "  Y: " + y + "  Z: " + z + "  ...");
		this.generator.generate(world, chunk, x, y, z);
	}
	
	private File getRandomBuilding(Random random) {	
		//int chance = random.nextInt(100) +1;
		//List<Integer> indexes = new ArrayList<Integer>();
		
		/*for(Integer i : this.chanceFileMap.keySet()) {
			if(i >= chance) {
				indexes.add(i);
			}
		}
		if(!indexes.isEmpty()) {*/
			//int i = indexes.get(random.nextInt(indexes.size()));
			//File f = this.chanceFileMap.get(i).get(random.nextInt(this.chanceFileMap.get(i).size()));
		File f = this.structureFolder.get(random.nextInt(this.structureFolder.size()));
			if(f.isDirectory()) {
				return f.listFiles()[random.nextInt(f.listFiles().length)];
			}
			return f;
		//}
		//return null;
	}
	
	public int getMinDistance() {
		return this.minDistance;
	}
	public int getMaxDistance() {
		return this.maxDistance;
	}
	public boolean buildPaths() {
		return this.buildPaths;
	}
	public boolean placeInCircle() {
		return this.placeInCircle;
	}
	public Block getPathMaterial() {
		return this.pathBlock;
	}
	
}
