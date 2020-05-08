package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.GeneratorStronghold;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.world.World;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
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
		
		this.rotateDungeon = false;

		this.minFloors = PropertyFileHelper.getIntProperty(prop, "minFloors", 2);
		this.maxFloors = PropertyFileHelper.getIntProperty(prop, "maxFloors", 3);
		this.minFloorSize = PropertyFileHelper.getIntProperty(prop, "minFloorSize", 3);
		this.maxFloorSize = PropertyFileHelper.getIntProperty(prop, "maxFloorSize", 5);

		this.entranceStairFolder = PropertyFileHelper.getFileProperty(prop, "entranceStairFolder", "stronghold/linear/entranceStairs/");
		this.entranceStairSegmentFolder = PropertyFileHelper.getFileProperty(prop, "entranceStairSegmentFolder", "stronghold/linear/entranceStairSegments");
		this.entranceBuildingFolder = PropertyFileHelper.getFileProperty(prop, "entranceFolder", "stronghold/linear/entrances/");
		this.bossRoomFolder = PropertyFileHelper.getFileProperty(prop, "bossRoomFolder", "stronghold/linear/bossrooms/");
		
		this.useStairSegments = PropertyFileHelper.getBooleanProperty(prop, "useStairSegments", true);
		
		this.curveENFolder = PropertyFileHelper.getFileProperty(prop, "curveENFolder", "stronghold/rooms/curves/EN");
		this.curveESFolder = PropertyFileHelper.getFileProperty(prop, "curveESFolder", "stronghold/rooms/curves/ES");
		this.curveNEFolder = PropertyFileHelper.getFileProperty(prop, "curveNEFolder", "stronghold/rooms/curves/NE");
		this.curveNWFolder = PropertyFileHelper.getFileProperty(prop, "curveNWFolder", "stronghold/rooms/curves/NW");
		this.curveSEFolder = PropertyFileHelper.getFileProperty(prop, "curveSEFolder", "stronghold/rooms/curves/SE");
		this.curveSWFolder = PropertyFileHelper.getFileProperty(prop, "curveSWFolder", "stronghold/rooms/curves/SW");
		this.curveWNFolder = PropertyFileHelper.getFileProperty(prop, "curveWNFolder", "stronghold/rooms/curves/WN");
		this.curveWSFolder = PropertyFileHelper.getFileProperty(prop, "curveWSFolder", "stronghold/rooms/curves/WS");
		this.hallEWFolder = PropertyFileHelper.getFileProperty(prop, "hallwayEWFolder", "stronghold/rooms/hallway/EW");
		this.hallNSFolder = PropertyFileHelper.getFileProperty(prop, "hallwayNSFolder", "stronghold/rooms/hallway/NS");
		this.hallSNFolder = PropertyFileHelper.getFileProperty(prop, "hallwaySNFolder", "stronghold/rooms/hallway/SN");
		this.hallWEFolder = PropertyFileHelper.getFileProperty(prop, "hallwayWEFolder", "stronghold/rooms/hallway/WE");
		this.stairEFolder = PropertyFileHelper.getFileProperty(prop, "stairEFolder", "stronghold/stairs/E");
		this.stairNFolder = PropertyFileHelper.getFileProperty(prop, "stairNFolder", "stronghold/stairs/N");
		this.stairSFolder = PropertyFileHelper.getFileProperty(prop, "stairSFolder", "stronghold/stairs/S");
		this.stairWFolder = PropertyFileHelper.getFileProperty(prop, "stairWFolder", "stronghold/stairs/W");

		this.roomSizeX = PropertyFileHelper.getIntProperty(prop, "roomSizeX", 15);
		this.roomSizeY = PropertyFileHelper.getIntProperty(prop, "roomSizeY", 10);
		this.roomSizeZ = PropertyFileHelper.getIntProperty(prop, "roomSizeZ", 15);
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		IDungeonGenerator generator = new GeneratorStronghold(this);
		generator.generate(world, world.getChunkFromChunkCoords(x >> 4, z >> 4), x, y, z);
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
		return getRoom(direction, direction);
	}
	
	@Nullable
	public File getRoom(ESkyDirection entranceD, ESkyDirection exitD) {
		File folder = null;
		if(entranceD == exitD) {
			switch(entranceD) {
			case EAST:
				folder = stairEFolder;
				break;
			case NORTH:
				folder = stairNFolder;
				break;
			case SOUTH:
				folder = stairSFolder;
				break;
			case WEST:
				folder = stairWFolder;
				break;
			}
		} else {
			switch(entranceD) {
			case EAST:
				switch(exitD) {
				case NORTH:
					folder = curveENFolder;
					break;
				case SOUTH:
					folder = curveESFolder;
					break;
				case WEST:
					folder = hallEWFolder;
					break;
				default: break;
				}
				break;
			case NORTH:
				switch(exitD) {
				case EAST:
					folder = curveNEFolder;
					break;
				case SOUTH:
					folder = hallNSFolder;
					break;
				case WEST:
					folder = curveNWFolder;
					break;
				default: break;
				}
				break;
			case SOUTH:
				switch(exitD) {
				case EAST:
					folder = curveSEFolder;
					break;
				case NORTH:
					folder = hallSNFolder;
					break;
				case WEST:
					folder = curveSWFolder;
					break;
				default: break;
				}
				break;
			case WEST:
				switch(exitD) {
				case EAST:
					folder = hallWEFolder;
					break;
				case NORTH:
					folder = curveWNFolder;
					break;
				case SOUTH:
					folder = curveWSFolder;
					break;
				default: break;
				}
				break;
			default:
				break;
			}
		}
		
		if(folder != null) {
			return this.getStructureFileFromDirectory(folder);
		}
		return null;
	}
	
	public int getFloorSize(Random random) {
		int size = DungeonGenUtils.getIntBetweenBorders(minFloorSize, maxFloorSize);
		if (size < 3) {
			size = 3;
		}
		if( size % 2 == 0) {
			size += 1;
		}
		return size;
	}
	
	public File getRoom(EStrongholdRoomType type) {
		File folder = null;
		switch(type) {
		case BOSS:
			folder = bossRoomFolder;
			break;
		case CURVE_EN:
			folder = curveENFolder;
			break;
		case CURVE_ES:
			folder = curveESFolder;
			break;
		case CURVE_NE:
			folder = curveNEFolder;
			break;
		case CURVE_NW:
			folder = curveNWFolder;
			break;
		case CURVE_SE:
			folder = curveSEFolder;
			break;
		case CURVE_SW:
			folder = curveSWFolder;
			break;
		case CURVE_WN:
			folder = curveWNFolder;
			break;
		case CURVE_WS:
			folder = curveWSFolder;
			break;
		case HALLWAY_EW:
			folder = hallEWFolder;
			break;
		case HALLWAY_NS:
			folder = hallNSFolder;
			break;
		case HALLWAY_SN:
			folder = hallSNFolder;
			break;
		case HALLWAY_WE:
			folder = hallWEFolder;
			break;
		case STAIR_EE:
			folder = stairEFolder;
			break;
		case STAIR_NN:
			folder = stairNFolder;
			break;
		case STAIR_SS:
			folder = stairSFolder;
			break;
		case STAIR_WW:
			folder = stairWFolder;
			break;
		default:
			return null;
		}
		if(folder != null) {
			return this.getStructureFileFromDirectory(folder);
		}
		return null;
	}

}
