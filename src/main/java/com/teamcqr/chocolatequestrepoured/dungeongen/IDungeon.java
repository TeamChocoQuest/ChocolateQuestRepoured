package com.teamcqr.chocolatequestrepoured.dungeongen;

public interface IDungeon {
	
	IDungeonGenerator getGenerator();
	String[] getBiomes();
	int getChance();
	int[] getAllowedDimensions();
	String getName();
	boolean ageBlocks();
	boolean turnOutLights();
	boolean isUnique();
	String getStructureFolderPath();
	
	void readCommonData();
	void readSpecialData();
	default void readData() {
		readCommonData();
		readSpecialData();
	}
}
