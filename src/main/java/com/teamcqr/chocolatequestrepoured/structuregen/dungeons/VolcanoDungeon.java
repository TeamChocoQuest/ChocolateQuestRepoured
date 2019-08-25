package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.structuregen.Generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.Generators.Volcano.VolcanoGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class VolcanoDungeon extends StrongholdDungeon {
	
	//For smoke: https://github.com/Tropicraft/Tropicraft/blob/1.12.2/src/main/java/net/tropicraft/core/common/block/tileentity/TileEntityVolcano.java
	
	private boolean buildStairwell = true;
	private boolean buildDungeon = false;
	private boolean damagedVolcano = true;
	private boolean ores = true;
	private int oreConcentration = 5;
	private int maxHoleSize = 9;
	private int minHeight = 100;
	private int maxHeight = 130;
	private int innerRadius = 6;
	private int chestChance = 600;
	private int[] chestIDs = {4, 10, 2};
	private double steepness = 0.075D;
	private double lavaChance = 0.005D;
	private double magmaChance = 0.1;
	private String mobName = "minecraft:zombie";
	private Block stoneBlock = Blocks.STONE;
	private Block lavaBlock = Blocks.LAVA;
	private Block magmaBlock = Blocks.MAGMA;
	private Block rampBlock = Blocks.NETHERRACK;
	private Block lowerStoneBlock = Blocks.COBBLESTONE;
	private Block pillarBlock = ModBlocks.GRANITE_BRICK_LARGE;

	public VolcanoDungeon(File configFile) {
		super(configFile);
		Properties prop = new Properties();
		boolean success = true;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(configFile);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			prop = null;
			configFile = null;
			success = false;
		} catch (IOException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			prop = null;
			configFile = null;
			success = false;
		}
		if(prop != null && configFile != null && fis != null) {
			this.buildStairwell = PropertyFileHelper.getBooleanProperty(prop, "buildPath", true);
			this.buildDungeon = PropertyFileHelper.getBooleanProperty(prop, "buildDungeon", true);
			this.minHeight = PropertyFileHelper.getIntProperty(prop, "minHeight", 100);
			this.maxHeight = PropertyFileHelper.getIntProperty(prop, "maxHeight", 130);
			this.innerRadius = PropertyFileHelper.getIntProperty(prop, "innerRadius", 5);
			this.lavaChance = Math.abs(Double.valueOf(prop.getProperty("lavaChance", "0.005")));
			this.magmaChance = Math.abs(Double.valueOf(prop.getProperty("magmaChance", "0.1")));
			this.steepness = Math.abs(Double.valueOf(prop.getProperty("steepness", "0.075")));
			this.damagedVolcano = PropertyFileHelper.getBooleanProperty(prop, "damagedVolcano", true);
			this.chestChance = PropertyFileHelper.getIntProperty(prop, "chestChance", 600);
			this.maxHoleSize = Math.max(Math.abs(PropertyFileHelper.getIntProperty(prop, "maxHoleSize", 9)), 2);
			this.ores = PropertyFileHelper.getBooleanProperty(prop, "ores", true);
			this.oreConcentration = Math.min(Math.max(1, Math.abs(PropertyFileHelper.getIntProperty(prop, "orechance", 5))), 100);
			this.mobName = prop.getProperty("mob", "minecraft:zombie");
			this.chestIDs = PropertyFileHelper.getIntArrayProperty(prop, "chestIDs", new int[]{4, 10, 2});
			this.stoneBlock = PropertyFileHelper.getBlockProperty(prop, "topBlock", Blocks.STONE);
			this.lowerStoneBlock = PropertyFileHelper.getBlockProperty(prop, "lowerBlock", Blocks.COBBLESTONE);
			this.lavaBlock = PropertyFileHelper.getBlockProperty(prop, "lavaBlock", Blocks.LAVA);
			this.magmaBlock = PropertyFileHelper.getBlockProperty(prop, "magmaBlock", Blocks.MAGMA);
			this.rampBlock = PropertyFileHelper.getBlockProperty(prop, "rampBlock", Blocks.NETHERRACK);
			this.pillarBlock = PropertyFileHelper.getBlockProperty(prop, "pillarBlock", ModBlocks.GRANITE_BRICK_LARGE);
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

	@Override
	public IDungeonGenerator getGenerator() {
		return new VolcanoGenerator(this);
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);
		
		//This generator sadly is too slow for no reason so we use the old one *sigh*
		/*System.out.println("Generating dungeon " + this.name + "... with maps");
		
		//this.generator = new VolcanoGenerator(this);
		this.generator = new VolcanoGeneratorWithMaps(this);
		
		this.generator.generate(world, chunk, x, DungeonGenUtils.getHighestYAt(chunk, x, z, true), z);
		
		
		System.out.println("Done!");*/
		
		System.out.println("Generating dungeon " + this.name + "... ");
		
		this.generator = new VolcanoGenerator(this);
		
		this.generator.generate(world, chunk, x, DungeonGenUtils.getHighestYAt(chunk, x, z, true), z);
		
		
		System.out.println("Done!");
	}
	
	public int getMinHeight() {
		return this.minHeight;
	}
	
	public int getMaxHeight() {
		return this.maxHeight;
	}
	
	public double getSteepness() {
		return this.steepness;
	}
	
	public double getLavaChance() {
		return this.lavaChance;
	}
	
	public double getMagmaChance() {
		return this.magmaChance;
	}
	
	public int getInnerRadius() {
		return this.innerRadius;
	}
	
	public boolean doBuildStairs() {
		return this.buildStairwell;
	}
	
	public boolean doBuildDungeon() {
		return this.buildDungeon;
	}
	
	public boolean isVolcanoDamaged() {
		return this.damagedVolcano;
	}
	
	public int getMaxHoleSize() {
		return this.maxHoleSize;
	}
	
	public int getOreChance() {
		return this.oreConcentration;
	}
	
	public boolean generateOres() {
		return this.ores;
	}
	public int getChestChance() {
		return this.chestChance;
	}
	public int[] getChestIDs() {
		return this.chestIDs;
	}
	public Block getUpperMainBlock() {
		return this.stoneBlock;
	}
	public Block getLowerMainBlock() {
		return this.lowerStoneBlock;
	}
	public Block getLavaBlock() {
		return this.lavaBlock;
	}
	public Block getMagmaBlock() {
		return this.magmaBlock;
	}
	public Block getRampBlock() {
		return this.rampBlock;
	}
	public Block getPillarBlock() {
		return this.pillarBlock;
	}

	public ResourceLocation getMob() {
		String[] bossString = this.mobName.split(":");
		
		return new ResourceLocation(bossString[0], bossString[1]);
	}
	
}
