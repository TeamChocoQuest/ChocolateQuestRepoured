package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

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
	private File roomCurveFolder;
	private File roomRoomFolder;
	private File roomCrossingFolder;
	private File roomtCrossingFolder;
	private File roomHallwayFolder;
	
	private int minFloors = 2;
	private int maxFloors= 3;
	private int minRoomsPerFloor = 6;
	private int maxRoomsPerFloor = 10;
	
	private int roomSizeX = 15;
	private int roomSizeZ = 15;
	
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

			roomRoomFolder = PropertyFileHelper.getFileProperty(prop, "deadEndFolder", "stronghold/linear/rooms/deadEnds");
			roomHallwayFolder = PropertyFileHelper.getFileProperty(prop, "hallwayStraightFolder", "stronghold/linear/rooms/hallways/");
			roomCurveFolder = PropertyFileHelper.getFileProperty(prop, "hallwayCurveFolder", "stronghold/linear/rooms/curves/");
			roomtCrossingFolder = PropertyFileHelper.getFileProperty(prop, "hallwayTCrossingFolder", "stronghold/linear/rooms/crossings/threesided/");
			roomCrossingFolder = PropertyFileHelper.getFileProperty(prop, "hallwayTCrossingFolder", "stronghold/linear/rooms/crossings/foursided/");
			
			roomSizeX = PropertyFileHelper.getIntProperty(prop, "roomSizeX", 15);
			roomSizeZ = PropertyFileHelper.getIntProperty(prop, "roomSizeZ", 15);
			
			closeConfigFile();
		} else {
			registeredSuccessful = false;
		}
	}
	
	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);
		
		int y = DungeonGenUtils.getHighestYAt(chunk, x, z, false);
		//For position locked dungeons, use the positions y
		if(this.isPosLocked()) {
			y = this.getLockedPos().getY();
		}
		y += getYOffset();
		
		getGenerator().generate(world, chunk, x, y, z);
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
	
	public File getCurveRoom() {
		return getStructureFileFromDirectory(roomCurveFolder);
	}
	public File getTCrossingRoom() {
		return getStructureFileFromDirectory(roomtCrossingFolder);
	}
	public File getCrossingRoom() {
		return getStructureFileFromDirectory(roomCrossingFolder);
	}
	public File getHallwayRoom() {
		return getStructureFileFromDirectory(roomHallwayFolder);
	}
	public File getDeadEndRoom() {
		return getStructureFileFromDirectory(roomRoomFolder);
	}
	public File getEntranceStairRoom() {
		return getStructureFileFromDirectory(entranceStairFolder);
	}
	public File getStairRoom() {
		return getStructureFileFromDirectory(stairFolder);
	}
	public File getBossRoom() {
		return getStructureFileFromDirectory(bossRoomFolder);
	}
	public int getRoomSizeX() {
		return roomSizeX;
	}
	public int getRoomSizeZ() {
		return roomSizeZ;
	}

	public File getEntranceBuilding() {
		return getStructureFileFromDirectory(entranceBuildingFolder);
	}

}
