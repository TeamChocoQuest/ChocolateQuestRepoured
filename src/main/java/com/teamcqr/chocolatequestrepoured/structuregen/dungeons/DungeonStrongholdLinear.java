package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.GeneratorStronghold;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DungeonStrongholdLinear extends DungeonBase {

	private File entranceStairFolder;
	private File entranceStairSegmentFolder;
	private File entranceBuildingFolder;
	private File bossRoomFolder;

	// IMPORTANT: the structure paste location MUST BE in its middle !!!
	// --> calculate position B E F O R E pasting -> pre-process method
	private File curveENFolder;
	private File curveNEFolder;
	private File curveSEFolder;
	private File curveESFolder;
	private File curveWSFolder;
	private File curveSWFolder;
	private File curveNWFolder;
	private File curveWNFolder;
	private File hallSNFolder;
	private File hallNSFolder;
	private File hallWEFolder;
	private File hallEWFolder;
	private File stairNFolder;
	private File stairEFolder;
	private File stairSFolder;
	private File stairWFolder;

	private int minFloors = 2;
	private int maxFloors = 3;

	private int minFloorSize = 3;
	private int maxFloorSize = 5;

	private int roomSizeX = 15;
	private int roomSizeY = 10;
	private int roomSizeZ = 15;

	private boolean useStairSegments = true;

	// Generator for the old strongholds which were basic linear dungeons

	// Important: All rooms need to have the same x and z size, the height must be the same for all, except the stair rooms: They must have the double height
	// Also all stair rooms must have exits and entries to ALL sides (N, E, S, W)

	public DungeonStrongholdLinear(String name, Properties prop) {
		super(name, prop);

		this.minFloors = PropertyFileHelper.getIntProperty(prop, "minFloors", 2);
		this.maxFloors = PropertyFileHelper.getIntProperty(prop, "maxFloors", 3);
		this.minFloorSize = PropertyFileHelper.getIntProperty(prop, "minFloorSize", 3);
		this.maxFloorSize = PropertyFileHelper.getIntProperty(prop, "maxFloorSize", 5);

		this.entranceStairFolder = PropertyFileHelper.getStructureFolderProperty(prop, "entranceStairFolder", "stronghold/linear/entranceStairs/");
		this.entranceStairSegmentFolder = PropertyFileHelper.getStructureFolderProperty(prop, "entranceStairSegmentFolder", "stronghold/linear/entranceStairSegments");
		this.entranceBuildingFolder = PropertyFileHelper.getStructureFolderProperty(prop, "entranceFolder", "stronghold/linear/entrances/");
		this.bossRoomFolder = PropertyFileHelper.getStructureFolderProperty(prop, "bossRoomFolder", "stronghold/linear/bossrooms/");

		this.useStairSegments = PropertyFileHelper.getBooleanProperty(prop, "useStairSegments", true);

		this.curveENFolder = PropertyFileHelper.getStructureFolderProperty(prop, "curveENFolder", "stronghold/rooms/curves/EN");
		this.curveESFolder = PropertyFileHelper.getStructureFolderProperty(prop, "curveESFolder", "stronghold/rooms/curves/ES");
		this.curveNEFolder = PropertyFileHelper.getStructureFolderProperty(prop, "curveNEFolder", "stronghold/rooms/curves/NE");
		this.curveNWFolder = PropertyFileHelper.getStructureFolderProperty(prop, "curveNWFolder", "stronghold/rooms/curves/NW");
		this.curveSEFolder = PropertyFileHelper.getStructureFolderProperty(prop, "curveSEFolder", "stronghold/rooms/curves/SE");
		this.curveSWFolder = PropertyFileHelper.getStructureFolderProperty(prop, "curveSWFolder", "stronghold/rooms/curves/SW");
		this.curveWNFolder = PropertyFileHelper.getStructureFolderProperty(prop, "curveWNFolder", "stronghold/rooms/curves/WN");
		this.curveWSFolder = PropertyFileHelper.getStructureFolderProperty(prop, "curveWSFolder", "stronghold/rooms/curves/WS");
		this.hallEWFolder = PropertyFileHelper.getStructureFolderProperty(prop, "hallwayEWFolder", "stronghold/rooms/hallway/EW");
		this.hallNSFolder = PropertyFileHelper.getStructureFolderProperty(prop, "hallwayNSFolder", "stronghold/rooms/hallway/NS");
		this.hallSNFolder = PropertyFileHelper.getStructureFolderProperty(prop, "hallwaySNFolder", "stronghold/rooms/hallway/SN");
		this.hallWEFolder = PropertyFileHelper.getStructureFolderProperty(prop, "hallwayWEFolder", "stronghold/rooms/hallway/WE");
		this.stairEFolder = PropertyFileHelper.getStructureFolderProperty(prop, "stairEFolder", "stronghold/stairs/E");
		this.stairNFolder = PropertyFileHelper.getStructureFolderProperty(prop, "stairNFolder", "stronghold/stairs/N");
		this.stairSFolder = PropertyFileHelper.getStructureFolderProperty(prop, "stairSFolder", "stronghold/stairs/S");
		this.stairWFolder = PropertyFileHelper.getStructureFolderProperty(prop, "stairWFolder", "stronghold/stairs/W");

		this.roomSizeX = PropertyFileHelper.getIntProperty(prop, "roomSizeX", 15);
		this.roomSizeY = PropertyFileHelper.getIntProperty(prop, "roomSizeY", 10);
		this.roomSizeZ = PropertyFileHelper.getIntProperty(prop, "roomSizeZ", 15);
	}

	@Override
	public AbstractDungeonGenerator<DungeonStrongholdLinear> createDungeonGenerator(World world, int x, int y, int z) {
		return new GeneratorStronghold(world, new BlockPos(x, y, z), this);
	}

	public int getMinFloors() {
		return this.minFloors;
	}

	public int getMaxFloors() {
		return this.maxFloors;
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

	public File getEntranceStairRoom() {
		return this.getStructureFileFromDirectory(this.entranceStairFolder);
	}

	public File getEntranceStairSegment() {
		return this.getStructureFileFromDirectory(this.entranceStairSegmentFolder);
	}

	public File getBossRoom() {
		return this.getStructureFileFromDirectory(this.bossRoomFolder);
	}

	public boolean useStairSegments() {
		return this.useStairSegments;
	}

	@Nullable
	public File getStairRoom(ESkyDirection direction) {
		return this.getRoom(direction, direction);
	}

	@Nullable
	public File getRoom(ESkyDirection entranceD, ESkyDirection exitD) {
		File folder = null;
		if (entranceD == exitD) {
			switch (entranceD) {
			case EAST:
				folder = this.stairEFolder;
				break;
			case NORTH:
				folder = this.stairNFolder;
				break;
			case SOUTH:
				folder = this.stairSFolder;
				break;
			case WEST:
				folder = this.stairWFolder;
				break;
			}
		} else {
			switch (entranceD) {
			case EAST:
				switch (exitD) {
				case NORTH:
					folder = this.curveENFolder;
					break;
				case SOUTH:
					folder = this.curveESFolder;
					break;
				case WEST:
					folder = this.hallEWFolder;
					break;
				default:
					break;
				}
				break;
			case NORTH:
				switch (exitD) {
				case EAST:
					folder = this.curveNEFolder;
					break;
				case SOUTH:
					folder = this.hallNSFolder;
					break;
				case WEST:
					folder = this.curveNWFolder;
					break;
				default:
					break;
				}
				break;
			case SOUTH:
				switch (exitD) {
				case EAST:
					folder = this.curveSEFolder;
					break;
				case NORTH:
					folder = this.hallSNFolder;
					break;
				case WEST:
					folder = this.curveSWFolder;
					break;
				default:
					break;
				}
				break;
			case WEST:
				switch (exitD) {
				case EAST:
					folder = this.hallWEFolder;
					break;
				case NORTH:
					folder = this.curveWNFolder;
					break;
				case SOUTH:
					folder = this.curveWSFolder;
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}

		if (folder != null) {
			return this.getStructureFileFromDirectory(folder);
		}
		return null;
	}

	public int getFloorSize(Random random) {
		int size = DungeonGenUtils.randomBetween(this.minFloorSize, this.maxFloorSize);
		if (size < 3) {
			size = 3;
		}
		if (size % 2 == 0) {
			size += 1;
		}
		return size;
	}

	public File getRoom(EStrongholdRoomType type) {
		File folder = null;
		switch (type) {
		case BOSS:
			folder = this.bossRoomFolder;
			break;
		case CURVE_EN:
			folder = this.curveENFolder;
			break;
		case CURVE_ES:
			folder = this.curveESFolder;
			break;
		case CURVE_NE:
			folder = this.curveNEFolder;
			break;
		case CURVE_NW:
			folder = this.curveNWFolder;
			break;
		case CURVE_SE:
			folder = this.curveSEFolder;
			break;
		case CURVE_SW:
			folder = this.curveSWFolder;
			break;
		case CURVE_WN:
			folder = this.curveWNFolder;
			break;
		case CURVE_WS:
			folder = this.curveWSFolder;
			break;
		case HALLWAY_EW:
			folder = this.hallEWFolder;
			break;
		case HALLWAY_NS:
			folder = this.hallNSFolder;
			break;
		case HALLWAY_SN:
			folder = this.hallSNFolder;
			break;
		case HALLWAY_WE:
			folder = this.hallWEFolder;
			break;
		case STAIR_EE:
			folder = this.stairEFolder;
			break;
		case STAIR_NN:
			folder = this.stairNFolder;
			break;
		case STAIR_SS:
			folder = this.stairSFolder;
			break;
		case STAIR_WW:
			folder = this.stairWFolder;
			break;
		default:
			return null;
		}
		if (folder != null) {
			return this.getStructureFileFromDirectory(folder);
		}
		return null;
	}

}
