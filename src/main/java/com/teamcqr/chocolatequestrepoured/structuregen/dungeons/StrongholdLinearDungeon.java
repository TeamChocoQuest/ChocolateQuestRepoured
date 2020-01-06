package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdLinearGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

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

	// IMPORTANT: the structure paste location MUST BE in its middle !!!
	// --> calculate position B E F O R E pasting -> pre-process method
	private File roomCurveFolder;
	private File roomRoomFolder;
	private File roomCrossingFolder;
	private File roomtCrossingFolder;
	private File roomHallwayFolder;

	private int minFloors = 2;
	private int maxFloors = 3;
	private int minRoomsPerFloor = 6;
	private int maxRoomsPerFloor = 10;

	private int roomSizeX = 15;
	private int roomSizeY = 10;
	private int roomSizeZ = 15;

	// Generator for the old strongholds which were basic linear dungeons

	// Important: All rooms need to have the same x and z size, the height must be the same for all, except the stair rooms: They must have the double height
	// Also all stair rooms must have exits and entries to ALL sides (N, E, S, W)

	public StrongholdLinearDungeon(File configFile) {
		super(configFile);

		Properties prop = this.loadConfig(configFile);

		if (prop != null) {

			this.minFloors = PropertyFileHelper.getIntProperty(prop, "minFloors", 2);
			this.maxFloors = PropertyFileHelper.getIntProperty(prop, "maxFloors", 3);
			this.minRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "minRoomsPerFloor", 6);
			this.maxRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "maxRoomsPerFloor", 10);

			this.stairFolder = PropertyFileHelper.getFileProperty(prop, "stairFolder", "stronghold/linear/stairs/");
			this.entranceStairFolder = PropertyFileHelper.getFileProperty(prop, "entranceStairFolder", "stronghold/linear/entranceStairs/");
			this.entranceBuildingFolder = PropertyFileHelper.getFileProperty(prop, "entranceFolder", "stronghold/linear/entrances/");
			this.bossRoomFolder = PropertyFileHelper.getFileProperty(prop, "bossroomFolder", "stronghold/linear/bossrooms/");

			this.roomRoomFolder = PropertyFileHelper.getFileProperty(prop, "deadEndFolder", "stronghold/linear/rooms/deadEnds");
			this.roomHallwayFolder = PropertyFileHelper.getFileProperty(prop, "hallwayStraightFolder", "stronghold/linear/rooms/hallways/");
			this.roomCurveFolder = PropertyFileHelper.getFileProperty(prop, "hallwayCurveFolder", "stronghold/linear/rooms/curves/");
			this.roomtCrossingFolder = PropertyFileHelper.getFileProperty(prop, "hallwayTCrossingFolder", "stronghold/linear/rooms/crossings/threesided/");
			this.roomCrossingFolder = PropertyFileHelper.getFileProperty(prop, "hallwayCrossingFolder", "stronghold/linear/rooms/crossings/foursided/");

			this.roomSizeX = PropertyFileHelper.getIntProperty(prop, "roomSizeX", 15);
			this.roomSizeY = PropertyFileHelper.getIntProperty(prop, "roomSizeY", 10);
			this.roomSizeZ = PropertyFileHelper.getIntProperty(prop, "roomSizeZ", 15);

			this.closeConfigFile();
		} else {
			this.registeredSuccessful = false;
		}
	}

	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);

		int y = DungeonGenUtils.getHighestYAt(chunk, x, z, false);
		// For position locked dungeons, use the positions y
		if (this.isPosLocked()) {
			y = this.getLockedPos().getY();
		}
		y += this.getYOffset();

		this.getGenerator().generate(world, chunk, x, y, z);
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new StrongholdLinearGenerator(this);
	}

	public int getMinFloors() {
		return this.minFloors;
	}

	public void setMinFloors(int floors) {
		this.minFloors = floors;
	}

	public int getMinRoomsPerFloor() {
		return this.minRoomsPerFloor;
	}

	public void setMinRoomsPerFloor(int minRoomsPerFloor) {
		this.minRoomsPerFloor = minRoomsPerFloor;
	}

	public int getMaxFloors() {
		return this.maxFloors;
	}

	public void setMaxFloors(int floorss) {
		this.maxFloors = floorss;
	}

	public int getMaxRoomsPerFloor() {
		return this.maxRoomsPerFloor;
	}

	public void setMaxRoomsPerFloor(int maxRoomsPerFloor) {
		this.maxRoomsPerFloor = maxRoomsPerFloor;
	}

	public File getStairFolder() {
		return this.stairFolder;
	}

	public void setStairFolder(File stairFolder) {
		this.stairFolder = stairFolder;
	}

	public File getEntranceStairFolder() {
		return this.entranceStairFolder;
	}

	public void setEntranceStairFolder(File entranceStairFolder) {
		this.entranceStairFolder = entranceStairFolder;
	}

	public File getEntranceBuildingFolder() {
		return this.entranceBuildingFolder;
	}

	public void setEntranceBuildingFolder(File entranceBuildingFolder) {
		this.entranceBuildingFolder = entranceBuildingFolder;
	}

	public File getBossRoomFolder() {
		return this.bossRoomFolder;
	}

	public void setBossRoomFolder(File bossRoomFolder) {
		this.bossRoomFolder = bossRoomFolder;
	}

	public File getCurveRoom() {
		return this.getStructureFileFromDirectory(this.roomCurveFolder);
	}

	public File getTCrossingRoom() {
		return this.getStructureFileFromDirectory(this.roomtCrossingFolder);
	}

	public File getCrossingRoom() {
		return this.getStructureFileFromDirectory(this.roomCrossingFolder);
	}

	public File getHallwayRoom() {
		return this.getStructureFileFromDirectory(this.roomHallwayFolder);
	}

	public File getDeadEndRoom() {
		return this.getStructureFileFromDirectory(this.roomRoomFolder);
	}

	public File getEntranceStairRoom() {
		return this.getStructureFileFromDirectory(this.entranceStairFolder);
	}

	public File getStairRoom() {
		return this.getStructureFileFromDirectory(this.stairFolder);
	}

	public File getBossRoom() {
		return this.getStructureFileFromDirectory(this.bossRoomFolder);
	}

	public int getRoomSizeX() {
		return this.roomSizeX;
	}

	public int getRoomSizeZ() {
		return this.roomSizeZ;
	}

	public int getRoomSizeY() {
		return this.roomSizeY;
	}

	public File getEntranceBuilding() {
		return this.getStructureFileFromDirectory(this.entranceBuildingFolder);
	}

}
