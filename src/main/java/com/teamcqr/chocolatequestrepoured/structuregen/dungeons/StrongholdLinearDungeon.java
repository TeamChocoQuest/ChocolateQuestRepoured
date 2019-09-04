package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class StrongholdLinearDungeon extends DungeonBase {

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
	private File roomSWFolder;
	private File roomNWFolder;
	
	private int minFloors = 2;
	private int maxFloors= 3;
	private int minRoomsPerFloor = 6;
	private int maxRoomsPerFloor = 10;
	
	//Generator for the old strongholds which were basic linear dungeons
	
	//Important: All rooms need to have the same x and z size, the height must be the same for all, except the stair rooms: They must have the double height
	//Also all stair rooms must have exits and entries to ALL sides (N, E, S, W)
	
	public StrongholdLinearDungeon(File configFile) {
		super(configFile);
		
		Properties prop = loadConfig(configFile);
		
		if(prop != null) {
			
			minFloors = PropertyFileHelper.getIntProperty(prop, "minFloors", 2);
			maxFloors = PropertyFileHelper.getIntProperty(prop, "maxFloors", 3);
			minRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "minRoomsPerFloor", 6);
			maxRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "maxRoomsPerFloor", 10);
			
			stairFolder = PropertyFileHelper.getFileProperty(prop, "stairFolder", "stronghold/linear/stairs/");
			entranceStairFolder = PropertyFileHelper.getFileProperty(prop, "entranceStairsFolder", "stronghold/linear/entranceStairs/");
			entranceBuildingFolder = PropertyFileHelper.getFileProperty(prop, "entranceFolder", "stronghold/linear/entrances/");
			bossRoomFolder = PropertyFileHelper.getFileProperty(prop, "bossroomFolder", "stronghold/linear/bossrooms/");
			
			roomNSFolder = PropertyFileHelper.getFileProperty(prop, "hallwaysNorthSouthFolder", "stronghold/linear/rooms/ns/");
			roomEWFolder = PropertyFileHelper.getFileProperty(prop, "hallwaysEastWestFolder", "stronghold/linear/rooms/ew/");
			
			roomNEFolder = PropertyFileHelper.getFileProperty(prop, "hallwaysCurvedNorthEastFolder", "stronghold/linear/rooms/curves/ne/");
			roomSEFolder = PropertyFileHelper.getFileProperty(prop, "hallwaysCurvedSouthEastFolder", "stronghold/linear/rooms/curves/se/");
			roomSWFolder = PropertyFileHelper.getFileProperty(prop, "hallwaysCurvedSouthWestFolder", "stronghold/linear/rooms/curves/sw/");
			roomNWFolder = PropertyFileHelper.getFileProperty(prop, "hallwaysCurvedNorthWestFolder", "stronghold/linear/rooms/curves/nw/");
			
			closeConfigFile();
		} else {
			registeredSuccessful = false;
		}
	}
	
	public File[] getPossibleRoomFolders(ESkyDirection exitDirectionOfPreviousRoom) {
		switch(exitDirectionOfPreviousRoom) {
		case EAST:
			return new File[] {this.roomEWFolder, this.roomSEFolder, this.roomNEFolder};
		case NORTH:
			return new File[] {this.roomNSFolder, this.roomNWFolder, this.roomNEFolder};
		case SOUTH:
			return new File[] {this.roomSWFolder, this.roomSEFolder, this.roomNSFolder};
		case WEST:
			return new File[] {this.roomSWFolder, this.roomEWFolder, this.roomNWFolder};
		default:
			return null;		
		}
	}

	public int getMinFloors() {
		return minFloors;
	}

	public void setMinFloors(int floors) {
		this.minFloors = floors;
	}

	public int getMinRoomsPerFloor() {
		return minRoomsPerFloor;
	}

	public void setMinRoomsPerFloor(int minRoomsPerFloor) {
		this.minRoomsPerFloor = minRoomsPerFloor;
	}

	public int getMaxFloors() {
		return maxFloors;
	}

	public void setMaxFloors(int floorss) {
		this.maxFloors = floorss;
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
