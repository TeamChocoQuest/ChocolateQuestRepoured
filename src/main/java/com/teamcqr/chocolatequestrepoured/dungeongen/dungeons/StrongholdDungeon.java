package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.StrongholdGenerator;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class StrongholdDungeon extends DungeonBase {
	
	private File stairFolder;
	private File bossRoomFolder;
	private File entranceStairFolder;
	private File entranceBuildingFolder;
	private File roomFolder;

	//Generator for 1.7 release strongholds -> not linear, but open strongholds, for old strongholds: see linearDungeon
	
	public StrongholdDungeon(File configFile) {
		super(configFile);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new StrongholdGenerator();
	}

	public File getStairFolder() {
		return stairFolder;
	}

	public void setStairFolder(File stairFolder) {
		this.stairFolder = stairFolder;
	}

	public File getBossRoomFolder() {
		return bossRoomFolder;
	}

	public void setBossRoomFolder(File bossRoomFolder) {
		this.bossRoomFolder = bossRoomFolder;
	}

	public File getEntranceStairFolder() {
		return entranceStairFolder;
	}

	public void setEntranceStairFolder(File entranceStairFolder) {
		this.entranceStairFolder = entranceStairFolder;
	}

	public File getEntranceBuildingFolder() {
		return entranceBuildingFolder;
	}

	public void setEntranceBuildingFolder(File entranceBuildingFolder) {
		this.entranceBuildingFolder = entranceBuildingFolder;
	}

	public File getRoomFolder() {
		return roomFolder;
	}

	public void setRoomFolder(File roomFolder) {
		this.roomFolder = roomFolder;
	}
}
