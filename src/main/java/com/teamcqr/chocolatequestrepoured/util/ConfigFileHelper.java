package com.teamcqr.chocolatequestrepoured.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigFileHelper {

	private int dungeonDistance = 20;
	private int dungeonSpawnDistance = 25;
	private double spawnerActivationDistance = 25.0;
	private int turnBackIntoSpawnerDistance = 48;
	private int maxLootTableRolls = 3;
	private int blockPlacerThreadCount = 4;
	private int wallDistance = 500;  //Measured in chunks  500 = 8000 blocks
	private int wallTopY = 140;
	private int wallTowerDistance = 3; //3 -> 2 chunks between each tower
	private int supportHillWallSize = 8;
	private int factionKillRepuChangeRadius = 60;
	private int mobChangeDistance = 1500;
	private boolean dungeonsInFlat = false;
	private boolean enableWallInTheNorth = true;
	private boolean wallHasObsiCore = true;
	private boolean mobsSpawnedFromCQSpawnersArePersistent = true;
	private boolean reinstallDefaultFiles = false;
	
	//Mobs
	//health
	private double baseHealthDistanceDivisor = 1000;
	private int defaultHealingPotionCount = 1;
	
	public ConfigFileHelper() {
		
	}
	//DONE: fill out method (?)
	public void loadValues(Configuration config) {
		config.load();
		
		// Distance is measured in C H U N K S
		Property prop = config.get("general", "dungeonSeparation", 20);
		this.dungeonDistance = prop.getInt(20);
		
		// Distance of chunks to spawn
		prop = config.get("general", "dungeonSpawnDistance", 25);
		this.dungeonSpawnDistance = prop.getInt(25);
		
		//Spawner activation distance
		prop = config.get("general", "spawnerActivationDistance", 25.0);
		this.spawnerActivationDistance = prop.getDouble(25.0);
		
		// Retreat into spawner distance
		prop = config.get("general", "despawnDistance", 48);
		this.turnBackIntoSpawnerDistance = prop.getInt(48);
		
		// Spawn dungeons in flat
		prop = config.get("general", "dungeonsInFlat", false);
		this.dungeonsInFlat = prop.getBoolean(false);
		
		//Dungeons
		prop = config.get("general", "supportHillWallSize", 8);
		this.supportHillWallSize = prop.getInt(8);
		
		//Loot Table rolls
		prop = config.get("general", "maxloottablepoolrolls", 3);
		this.maxLootTableRolls = prop.getInt(3);
		
		//Reinstall Option
		prop = config.get("general", "reinstallDefaultConfigs", false);
		this.reinstallDefaultFiles = prop.getBoolean(false);
		
		// Wall
		// enabled
		prop = config.get("wall", "enabled", true);
		this.enableWallInTheNorth = prop.getBoolean(true);
		
		// position !! IN CHUNKS !!
		prop = config.get("wall", "distance", 500);
		this.wallDistance = Math.abs(prop.getInt(500));
		
		// TowerDistance
		prop = config.get("wall", "towerdistance", 3);
		this.wallTowerDistance = Math.abs(prop.getInt(3));
		
		// Top Y
		prop = config.get("wall", "topY", 140);
		int yTmp = Math.abs(prop.getInt(140));
		this.wallTopY = yTmp >= 255 ? 140:yTmp;
		
		// If the wall has a obsidian core
		prop = config.get("wall", "obsidianCore", true);
		this.wallHasObsiCore = prop.getBoolean(true);
		
		// How many threads are used for block placing
		prop = config.get("advanced", "threadCount", 4);
		blockPlacerThreadCount = prop.getInt(4);
		
		//Mobs
		//Health
		//Divisor of distance
		prop = config.get("mobs", "distanceDivisor", 1000.0D);
		baseHealthDistanceDivisor = prop.getDouble(1000.0D);
		
		//Healing potion count
		prop = config.get("mobs", "defaultHealingPotionCount", 1);
		defaultHealingPotionCount = prop.getInt(1);
		
		//radius of the faction repu that receives updates in a certain radius
		prop = config.get("mobs", "factionUpdateRadius", 100);
		factionKillRepuChangeRadius = prop.getInt(100);
		
		prop = config.get("general", "mobsFromCQSpawnersDontDespawn", true);
		mobsSpawnedFromCQSpawnersArePersistent = prop.getBoolean(true);
		
		//Divisor for mob type distance
		prop = config.get("general", "mobTypeChangeDistance", 1500);
		mobChangeDistance = prop.getInt(1500);
		
		config.save();
	}
	
	public int getDungeonDistance() {
		return this.dungeonDistance;
	}
	public int getDungeonSpawnDistance() {
		return this.dungeonSpawnDistance;
	}
	public double getSpawnerActivationDistance() {
		return this.spawnerActivationDistance;
	}
	public int getWallTopY() {
		return this.wallTopY;
	}
	public int getWallTowerDistance() {
		return this.wallTowerDistance;
	}
	public int getWallSpawnDistance() {
		return this.wallDistance;
	}
	public int getTurnBackIntoSpawnerDistance() {
		return this.turnBackIntoSpawnerDistance;
	}
	public boolean buildWall() {
		return this.enableWallInTheNorth;
	}
	public boolean generateDungeonsInFlat() {
		return this.dungeonsInFlat;
	}
	public boolean wallHasObsiCore() {
		return this.wallHasObsiCore;
	}
	public int getSupportHillWallSize() {
		return supportHillWallSize;
	}
	public int getMaxLootTablePoolRolls() {
		return Math.abs(this.maxLootTableRolls);
	}
	public int getBlockPlacerThreadCount() {
		return blockPlacerThreadCount;
	}
	public double getHealthDistanceDivisor() {
		return baseHealthDistanceDivisor;
	}
	public int getFactionRepuChangeRadius() {
		return factionKillRepuChangeRadius;
	}
	public boolean areMobsFromCQSpawnersPersistent() {
		return mobsSpawnedFromCQSpawnersArePersistent;
	}
	public int getMobChangeDistanceDivisor() {
		return mobChangeDistance;
	}
	public int getDefaultHealingPotionCount() {
		return defaultHealingPotionCount;
	}
	public boolean reInstallDefaultFiles() {
		return reinstallDefaultFiles;
	}

}
