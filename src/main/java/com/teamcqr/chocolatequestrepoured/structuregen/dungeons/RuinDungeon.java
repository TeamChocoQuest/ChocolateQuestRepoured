package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.RuinGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class RuinDungeon extends DefaultSurfaceDungeon {
	
	private int chanceForMissingBlock = 25;
	private int chanceForLightedTorch = 5;
	private int chestMissingChance = 20;
	private int flowerDeadChance = 50;
	private int flowerPotEmptyChance = 30;
	private int blockFallDownChance = 30;
	private boolean turnFlowersIntoDeadBushes = true;
	private boolean emptyFlowerPots = true;
	private boolean putOutTorches = true;
	private boolean ageBlocks = true;

	public RuinDungeon(File configFile) {
		super(configFile);
		
		if(super.registeredSuccessful) {
			Properties prop = loadConfig(configFile);
			if(prop != null) {
				this.turnFlowersIntoDeadBushes = PropertyFileHelper.getBooleanProperty(prop, "deadFlowers", true);
				this.emptyFlowerPots = PropertyFileHelper.getBooleanProperty(prop, "emptyPots", true);
				this.putOutTorches = PropertyFileHelper.getBooleanProperty(prop, "burntDownTorches", true);
				this.ageBlocks = PropertyFileHelper.getBooleanProperty(prop, "agedBlocks", true);
				
				this.chanceForLightedTorch = PropertyFileHelper.getIntProperty(prop, "lightChance", 5);
				this.chanceForMissingBlock = PropertyFileHelper.getIntProperty(prop, "structureIntegrity", 25);
				this.blockFallDownChance = PropertyFileHelper.getIntProperty(prop, "blockFallDownChance", 30);
				this.flowerDeadChance = PropertyFileHelper.getIntProperty(prop, "deadFlowerChance", 50);
				this.flowerPotEmptyChance = PropertyFileHelper.getIntProperty(prop, "emptyPotChance", 30);
				this.chestMissingChance = PropertyFileHelper.getIntProperty(prop, "lootchestMissingChance", 20);
				
				closeConfigFile();
			} else {
				registeredSuccessful = false;
			}
		}
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new RuinGenerator(null, null, null);
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		File structure = pickStructure(new Random());
		if(structure != null) {
			CQStructure dungeonStructure = new CQStructure(structure, this.protectFromDestruction);
			
			PlacementSettings settings = new PlacementSettings();
			settings.setMirror(Mirror.NONE);
			settings.setRotation(Rotation.NONE);
			settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
			
			float integrity = (this.chanceForMissingBlock <= 0 ? 50 : this.chanceForMissingBlock) /100.0F; 
			
			settings.setIntegrity(integrity);
			
			int y = DungeonGenUtils.getHighestYAt(chunk, x, z, false);
			//For position locked dungeons, use the positions y
			if(this.isPosLocked()) {
				y = this.getLockedPos().getY();
			}
			
			if(this.getUnderGroundOffset() != 0) {
				y -= this.getUnderGroundOffset();
			}
			if(this.yOffset != 0) {
				y += Math.abs(this.yOffset);
			}
			
			System.out.println("Placing dungeon: " + this.name);
			System.out.println("Generating structure " + structure.getName() + " at X: " + x + "  Y: " + y + "  Z: " + z + "  ...");
			RuinGenerator generator = new RuinGenerator(this, dungeonStructure, settings);
			generator.generate(world, chunk, x, y, z);
		}
	}

	public int getChanceForMissingBlock() {
		return chanceForMissingBlock;
	}
	
	public int getChanceForLightedTorch() {
		return chanceForLightedTorch;
	}

	public int getChestMissingChance() {
		return chestMissingChance;
	}

	public int getFlowerDeadChance() {
		return flowerDeadChance;
	}
	
	public int getFlowerPotEmptyChance() {
		return flowerPotEmptyChance;
	}

	public int getBlockFallDownChance() {
		return blockFallDownChance;
	}

	public boolean isTurnFlowersIntoDeadBushes() {
		return turnFlowersIntoDeadBushes;
	}

	public boolean isEmptyFlowerPots() {
		return emptyFlowerPots;
	}

	public boolean isPutOutTorches() {
		return putOutTorches;
	}

	public boolean isAgeBlocks() {
		return ageBlocks;
	}
	
	
}
