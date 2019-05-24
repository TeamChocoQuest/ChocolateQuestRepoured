package com.teamcqr.chocolatequestrepoured.util;

public class ConfigFileHelper {

	private int dungeonDistance = 20;
	private int dungeonSpawnDistance = 25;
	//private int spawnerActivationDistance = 32;
	//private int turnBackIntoSpawnerDistance = 48;
	private int wallDistance = 500;  //Measured in chunks  500 = 8000 blocks
	private int wallTopY = 140;
	private int wallTowerDistance = 3; //3 -> 2 chunks between each tower
	private boolean dungeonsInFlat = false;
	private boolean enableWallInTheNorth = true;
	
	public ConfigFileHelper() {
		
	}
	//TODO: fill out method
	public void loadValues() {
		
	}
	
	public int getDungeonDistance() {
		return this.dungeonDistance;
	}
	public int getDungeonSpawnDistance() {
		return this.dungeonSpawnDistance;
	}
	/*public int getSpawnerActivationDistance() {
		return this.spawnerActivationDistance;
	}*/
	public int getWallTopY() {
		return this.wallTopY;
	}
	public int getWallTowerDistance() {
		return this.wallTowerDistance;
	}
	public int getWallSpawnDistance() {
		return this.wallDistance;
	}
	/*public int getTurnBackIntoSpawnerDistance() {
		return this.turnBackIntoSpawnerDistance;
	}*/
	public boolean buildWall() {
		return this.enableWallInTheNorth;
	}
	public boolean generateDungeonsInFlat() {
		return this.dungeonsInFlat;
	}

}
