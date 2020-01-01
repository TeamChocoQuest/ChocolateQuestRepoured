package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdOpenGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

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

	// Important: All rooms must have the same dimensions!!!

	// Generator for 1.7 release strongholds -> not linear, but open strongholds, for old strongholds: see linearDungeon

	public StrongholdOpenDungeon(File configFile) {
		super(configFile);
		Properties prop = this.loadConfig(configFile);
		if (prop != null) {
			this.stairFolder = PropertyFileHelper.getFileProperty(prop, "stairFolder", "strongholds/open/stairs");
			this.entranceStairFolder = PropertyFileHelper.getFileProperty(prop, "entranceStairFolder", "strongholds/open/entrance/stairs");
			this.entranceBuildingFolder = PropertyFileHelper.getFileProperty(prop, "entranceBuildingFolder", "strongholds/open/entrance/buildings");
			this.roomFolder = PropertyFileHelper.getFileProperty(prop, "roomFolder", "strongholds/open/rooms");
			this.bossRoomFolder = PropertyFileHelper.getFileProperty(prop, "bossRoomFolder", "strongholds/open/boss");

			this.minFloors = PropertyFileHelper.getIntProperty(prop, "minFloors", 2);
			this.maxFloors = PropertyFileHelper.getIntProperty(prop, "maxFloors", 4);
			this.minRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "minRoomsPerFloor", 4);
			this.maxRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "maxRoomsPerFloor", 16);

			this.roomSizeX = PropertyFileHelper.getIntProperty(prop, "roomSizeX", 17);
			this.roomSizeY = PropertyFileHelper.getIntProperty(prop, "roomSizeY", 10);
			this.roomSizeZ = PropertyFileHelper.getIntProperty(prop, "roomSizeZ", 17);

			this.wallBlock = PropertyFileHelper.getBlockProperty(prop, "wallBlock", Blocks.STONEBRICK);

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
		return new StrongholdOpenGenerator(this);
	}

	public File getStairFolder() {
		return this.stairFolder;
	}

	public void setStairFolder(File stairFolder) {
		this.stairFolder = stairFolder;
	}

	public File getBossRoomFolder() {
		return this.bossRoomFolder;
	}

	public void setBossRoomFolder(File bossRoomFolder) {
		this.bossRoomFolder = bossRoomFolder;
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

	public File getRoomFolder() {
		return this.roomFolder;
	}

	public void setRoomFolder(File roomFolder) {
		this.roomFolder = roomFolder;
	}

	public int getRandomFloorCount() {
		return DungeonGenUtils.getIntBetweenBorders(this.minFloors, this.maxFloors);
	}

	public int getRandomRoomCountForFloor() {
		return DungeonGenUtils.getIntBetweenBorders(this.minRoomsPerFloor, this.maxRoomsPerFloor);
	}

	public File getBossRoom() {
		return this.getStructureFileFromDirectory(this.bossRoomFolder);
	}

	public File getRoom() {
		return this.getStructureFileFromDirectory(this.roomFolder);
	}

	public File getStairRoom() {
		return this.getStructureFileFromDirectory(this.stairFolder);
	}

	public File getEntranceBuilding() {
		return this.getStructureFileFromDirectory(this.entranceBuildingFolder);
	}

	public File getEntranceStair() {
		return this.getStructureFileFromDirectory(this.entranceStairFolder);
	}

	public int getRoomSizeX() {
		return this.roomSizeX;
	}

	public int getRoomSizeY() {
		return this.roomSizeY;
	}

	public int getRoomSizeZ() {
		return this.roomSizeZ;
	}

	public Block getWallBlock() {
		return this.wallBlock;
	}
}
