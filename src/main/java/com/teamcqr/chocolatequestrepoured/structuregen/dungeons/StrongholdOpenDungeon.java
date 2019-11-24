package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdOpenGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

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
	
	private int roomSizeX = 17;
	private int roomSizeY = 10;
	private int roomSizeZ = 17;
	
	private Block wallBlock = Blocks.STONEBRICK;
	
	//Important: All rooms must have the same dimensions!!!

	//Generator for 1.7 release strongholds -> not linear, but open strongholds, for old strongholds: see linearDungeon
	
	public StrongholdOpenDungeon(File configFile) {
		super(configFile);
		Properties prop = loadConfig(configFile);
		if(prop != null) {
			stairFolder = PropertyFileHelper.getFileProperty(prop, "stairFolder", "strongholds/open/stairs");
			entranceStairFolder = PropertyFileHelper.getFileProperty(prop, "entranceStairFolder", "strongholds/open/entrance/stairs");
			entranceBuildingFolder = PropertyFileHelper.getFileProperty(prop, "entranceBuildingFolder", "strongholds/open/entrance/buildings");
			roomFolder = PropertyFileHelper.getFileProperty(prop, "roomFolder", "strongholds/open/rooms");
			bossRoomFolder = PropertyFileHelper.getFileProperty(prop, "bossRoomFolder", "strongholds/open/boss");
			
			minFloors = PropertyFileHelper.getIntProperty(prop, "minFloors", 2);
			maxFloors = PropertyFileHelper.getIntProperty(prop, "maxFloors", 4);
			minRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "minRoomsPerFloor", 4);
			maxRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "maxRoomsPerFloor", 16);
			
			roomSizeX = PropertyFileHelper.getIntProperty(prop, "roomSizeX", 17);
			roomSizeY = PropertyFileHelper.getIntProperty(prop, "roomSizeY", 10);
			roomSizeZ = PropertyFileHelper.getIntProperty(prop, "roomSizeZ", 17);
			
			wallBlock = PropertyFileHelper.getBlockProperty(prop, "wallBlock", Blocks.STONEBRICK);
			
			closeConfigFile();
		} else {
			registeredSuccessful = false;
		}
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
	public int getRoomSizeX() {
		return roomSizeX;
	}
	public int getRoomSizeY() {
		return roomSizeY;
	}
	public int getRoomSizeZ() {
		return roomSizeZ;
	}
	public Block getWallBlock() {
		return wallBlock;
	}
}
