package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;

import com.teamcqr.chocolatequestrepoured.dungeongen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class LinearDungeon extends DungeonBase {

	private File stairFolder;
	private File entranceStairFolder;
	private File entranceBuildingFolder;
	private File bossRoomFolder;
	
	//IMPORTANT: the structure paste location MUST BE in its middle !!!
	// --> calculate position  B E F O R E  pasting -> pre-process method
	private File roomNSFolder;
	private File roomEWFolder;
	
	private File roomNEFolder;
	private File roomSEFolder;
	private File roomWSFolder;
	private File roomNWFolder;
	
	private int minRooms = 12;
	private int maxRooms = 24;
	private int minRoomsPerFloor = 4;
	private int maxRoomsPerFloor = 8;
	
	//Generator for the old strongholds which were basic linear dungeons
	
	public LinearDungeon(File configFile) {
		super(configFile);
		// TODO Auto-generated constructor stub
	}
	
	public File[] getPossibleRooms(ESkyDirection direction) {
		switch(direction) {
		case EAST:
			return new File[] {this.roomEWFolder, this.roomSEFolder, this.roomNEFolder};
		case NORTH:
			return new File[] {this.roomNSFolder, this.roomNWFolder, this.roomNEFolder};
		case SOUTH:
			return new File[] {this.roomWSFolder, this.roomSEFolder, this.roomNSFolder};
		case WEST:
			return new File[] {this.roomWSFolder, this.roomEWFolder, this.roomNWFolder};
		default:
			return null;		
		}
	}

	public int getMinRooms() {
		return minRooms;
	}

	public void setMinRooms(int minRooms) {
		this.minRooms = minRooms;
	}

	public int getMinRoomsPerFloor() {
		return minRoomsPerFloor;
	}

	public void setMinRoomsPerFloor(int minRoomsPerFloor) {
		this.minRoomsPerFloor = minRoomsPerFloor;
	}

	public int getMaxRooms() {
		return maxRooms;
	}

	public void setMaxRooms(int maxRooms) {
		this.maxRooms = maxRooms;
	}

	public int getMaxRoomsPerFloor() {
		return maxRoomsPerFloor;
	}

	public void setMaxRoomsPerFloor(int maxRoomsPerFloor) {
		this.maxRoomsPerFloor = maxRoomsPerFloor;
	}

	public File getStairFolder() {
		return stairFolder;
	}

	public void setStairFolder(File stairFolder) {
		this.stairFolder = stairFolder;
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

	public File getBossRoomFolder() {
		return bossRoomFolder;
	}

	public void setBossRoomFolder(File bossRoomFolder) {
		this.bossRoomFolder = bossRoomFolder;
	}

}
