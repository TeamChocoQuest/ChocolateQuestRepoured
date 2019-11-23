package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdOpenGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class StrongholdOpenDungeon extends DungeonBase {
	
	private File stairFolder;
	private File bossRoomFolder;
	private File entranceStairFolder;
	private File entranceBuildingFolder;
	private File roomFolder;
	
	private int minFloors = 2;
	private int maxFloors = 4;
	private int minRoomsPerFloor = 7;
	private int maxRoomsPerFloor = 10;
	
	//Important: All rooms must have the same dimensions!!!

	//Generator for 1.7 release strongholds -> not linear, but open strongholds, for old strongholds: see linearDungeon
	
	public StrongholdOpenDungeon(File configFile) {
		super(configFile);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new StrongholdOpenGenerator(this);
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
	
	public int getRandomFloorCount() {
		return DungeonGenUtils.getIntBetweenBorders(minFloors, maxFloors);
	}
	public int getRandomRoomCountForFloor() {
		return DungeonGenUtils.getIntBetweenBorders(minRoomsPerFloor, maxRoomsPerFloor);
	}
	
	public File getBossRoom() {
		return getStructureFileFromDirectory(bossRoomFolder);
	}
	public File getRoom() {
		return getStructureFileFromDirectory(roomFolder);
	}
	public File getStairRoom() {
		return getStructureFileFromDirectory(stairFolder);
	}
	public File getEntranceBuilding() {
		return getStructureFileFromDirectory(entranceBuildingFolder);
	}
	public File getEntranceStair() {
		return getStructureFileFromDirectory(entranceStairFolder);
	}
}
