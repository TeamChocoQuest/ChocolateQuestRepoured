package com.teamcqr.chocolatequestrepoured.dungeongen;

import java.util.List;

public class StructureConfig {
	
	private String structureFolderPath;
	private boolean ageBlocks;
	private boolean turnOffSomeLights;
	private boolean spawnOnce;
	private boolean spawnEveryStructureOnce;
	private List<String> biomeNameList;
	private List<Integer> blackListedDimensions;
	private int spawnChance;
	private IDungeonGenerator generator;
	//private IconItem spawnItem
	public String getStructureFolderPath() {
		return structureFolderPath;
	}
	public void setStructureFolderPath(String structureFolderPath) {
		this.structureFolderPath = structureFolderPath;
	}
	public boolean isAgeBlocks() {
		return ageBlocks;
	}
	public void setAgeBlocks(boolean ageBlocks) {
		this.ageBlocks = ageBlocks;
	}
	public boolean isTurnOffSomeLights() {
		return turnOffSomeLights;
	}
	public void setTurnOffSomeLights(boolean turnOffSomeLights) {
		this.turnOffSomeLights = turnOffSomeLights;
	}
	public boolean isSpawnOnce() {
		return spawnOnce;
	}
	public void setSpawnOnce(boolean spawnOnce) {
		this.spawnOnce = spawnOnce;
	}
	public boolean isSpawnEveryStructureOnce() {
		return spawnEveryStructureOnce;
	}
	public void setSpawnEveryStructureOnce(boolean spawnEveryStructureOnce) {
		this.spawnEveryStructureOnce = spawnEveryStructureOnce;
	}
	public List<Integer> getBlackListedDimensions() {
		return blackListedDimensions;
	}
	public void setBlackListedDimensions(List<Integer> blackListedDimensions) {
		this.blackListedDimensions = blackListedDimensions;
	}
	public List<String> getBiomeNameList() {
		return biomeNameList;
	}
	public void setBiomeNameList(List<String> biomeNameList) {
		this.biomeNameList = biomeNameList;
	}
	public int getSpawnChance() {
		return spawnChance;
	}
	public void setSpawnChance(int spawnChance) {
		this.spawnChance = spawnChance;
	}
	public IDungeonGenerator getGenerator() {
		return generator;
	}
	public void setGenerator(IDungeonGenerator generator) {
		this.generator = generator;
	}

}
