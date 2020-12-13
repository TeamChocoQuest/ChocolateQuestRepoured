package team.cqr.cqrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.structuregen.DungeonDataManager;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.structuregen.generators.stronghold.GeneratorStrongholdOpen;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PropertyFileHelper;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DungeonStrongholdOpen extends DungeonBase {

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

	private IBlockState wallBlock = Blocks.STONEBRICK.getDefaultState();

	// Important: All rooms must have the same dimensions!!!

	// Generator for 1.7 release strongholds -> not linear, but open strongholds, for old strongholds: see linearDungeon

	public DungeonStrongholdOpen(String name, Properties prop) {
		super(name, prop);

		this.stairFolder = PropertyFileHelper.getStructureFolderProperty(prop, "stairFolder", "strongholds/open/stairs");
		this.entranceStairFolder = PropertyFileHelper.getStructureFolderProperty(prop, "entranceStairFolder", "strongholds/open/entrance/stairs");
		this.entranceBuildingFolder = PropertyFileHelper.getStructureFolderProperty(prop, "entranceBuildingFolder", "strongholds/open/entrance/buildings");
		this.roomFolder = PropertyFileHelper.getStructureFolderProperty(prop, "roomFolder", "strongholds/open/rooms");
		this.bossRoomFolder = PropertyFileHelper.getStructureFolderProperty(prop, "bossRoomFolder", "strongholds/open/boss");

		this.minFloors = PropertyFileHelper.getIntProperty(prop, "minFloors", 2);
		this.maxFloors = PropertyFileHelper.getIntProperty(prop, "maxFloors", 4);
		this.minRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "minRoomsPerFloor", 4);
		this.maxRoomsPerFloor = PropertyFileHelper.getIntProperty(prop, "maxRoomsPerFloor", 16);

		this.roomSizeX = PropertyFileHelper.getIntProperty(prop, "roomSizeX", 17);
		this.roomSizeY = PropertyFileHelper.getIntProperty(prop, "roomSizeY", 10);
		this.roomSizeZ = PropertyFileHelper.getIntProperty(prop, "roomSizeZ", 17);

		this.wallBlock = PropertyFileHelper.getBlockStateProperty(prop, "wallBlock", Blocks.STONEBRICK.getDefaultState());
	}

	@Override
	public AbstractDungeonGenerator<DungeonStrongholdOpen> createDungeonGenerator(World world, int x, int y, int z, Random rand, DungeonDataManager.DungeonSpawnType spawnType) {
		return new GeneratorStrongholdOpen(world, new BlockPos(x, y, z), this, rand, spawnType);
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

	public int getRandomFloorCount(Random rand) {
		return DungeonGenUtils.randomBetween(this.minFloors, this.maxFloors, rand);
	}

	public int getRandomRoomCountForFloor(Random rand) {
		return DungeonGenUtils.randomBetween(this.minRoomsPerFloor, this.maxRoomsPerFloor, rand);
	}

	public File getBossRoom(Random rand) {
		return this.getStructureFileFromDirectory(this.bossRoomFolder, rand);
	}

	public File getRoom(Random rand) {
		return this.getStructureFileFromDirectory(this.roomFolder, rand);
	}

	public File getStairRoom(Random rand) {
		return this.getStructureFileFromDirectory(this.stairFolder, rand);
	}

	public File getEntranceBuilding(Random rand) {
		return this.getStructureFileFromDirectory(this.entranceBuildingFolder, rand);
	}

	public File getEntranceStair(Random rand) {
		return this.getStructureFileFromDirectory(this.entranceStairFolder, rand);
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

	public IBlockState getWallBlock() {
		return this.wallBlock;
	}
}
