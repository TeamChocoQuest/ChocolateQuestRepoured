package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdOpenDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.open.StrongholdFloorOpen;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class StrongholdOpenGenerator implements IDungeonGenerator {

	private StrongholdOpenDungeon dungeon;
	
	private List<String> blacklistedRooms = new ArrayList<String>();
	private Tuple<Integer, Integer> structureBounds;
	
	private PlacementSettings settings = new PlacementSettings();
	
	private StrongholdFloorOpen[] floors;
	
	private int dunX;
	private int dunZ;
	
	public StrongholdOpenGenerator(StrongholdOpenDungeon dungeon) {
		super();
		this.dungeon = dungeon;
		
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);
		
		this.floors = new StrongholdFloorOpen[dungeon.getRandomFloorCount()];
		searchStructureBounds();
		computeNotFittingStructures();
	}
	
	private void computeNotFittingStructures() {
		for(File f : dungeon.getRoomFolder().listFiles(DungeonGenUtils.getStructureFileFilter())) {
			CQStructure struct = new CQStructure(f, dungeon, 0, 0, false);
			if(struct.getSizeX() != structureBounds.getFirst() || struct.getSizeZ() != structureBounds.getSecond()) {
				blacklistedRooms.add(f.getParent() + "/" + f.getName());
			}
		}
	}
	
	public StrongholdOpenDungeon getDungeon() {
		return dungeon;
	}

	private void searchStructureBounds() {
		
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		this.dunX = x;
		this.dunZ = z;
		BlockPos initPos = new BlockPos(x,y,z);
		initPos = initPos.add(0,dungeon.getYOffset(),0);
		initPos = initPos.subtract(new Vec3i(0,dungeon.getUnderGroundOffset(),0));
		for(int i = 0; i < floors.length; i++) {
			StrongholdFloorOpen floor = new StrongholdFloorOpen(this);
			File stair = null;
			boolean isFirst = i==0;
			if(isFirst) {
				stair = dungeon.getEntranceStair();
			} else {
				stair = dungeon.getStairRoom();
			}
			floor.setIsFirstFloor(isFirst);
			int dY = initPos.getY() - (new CQStructure(stair, this.dungeon, x, z, false)).getSizeY();
			if(dY <= (this.dungeon.getRoomSizeY() +2) ) {
				floors[i-1].setExitIsBossRoom(true);
			} else {
				initPos = initPos.subtract(new Vec3i(0,(new CQStructure(stair, this.dungeon, x, z, false)).getSizeY(),0));
				if(!isFirst) {
					initPos = initPos.add(0,dungeon.getRoomSizeY(),0);
				}
				if((i+1) == floors.length) {
					floor.setExitIsBossRoom(true);
				}
				floor.setEntranceStairPosition(stair, initPos.getX(), initPos.getY(), initPos.getZ());
				floor.calculatePositions();
				initPos = new BlockPos(floor.getExitCoordinates().getFirst(), initPos.getY(), floor.getExitCoordinates().getSecond());
			}
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		CQStructure structure = new CQStructure(dungeon.getEntranceBuilding(), dungeon, chunk.x, chunk.z, dungeon.isProtectedFromModifications());
		if(this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			supportBuilder.createSupportHill(new Random(), world, new BlockPos(x, y + this.dungeon.getUnderGroundOffset() + this.dungeon.getYOffset(), z), structure.getSizeX(), structure.getSizeZ(), EPosType.CENTER_XZ_LAYER);
		}
		structure.placeBlocksInWorld(world, new BlockPos(x, y + this.dungeon.getYOffset(), z), this.settings, EPosType.CENTER_XZ_LAYER);
		
		/*CQStructure stairs = new CQStructure(dungeon.getStairRoom(), dungeon, chunk.x, chunk.z, dungeon.isProtectedFromModifications());
		BlockPos pastePosForStair = new BlockPos(x, y - stairs.getSizeY(), z);
		stairs.placeBlocksInWorld(world, pastePosForStair, settings, EPosType.CENTER_XZ_LAYER);*/
		//Will generate the structure
		//Algorithm: while(genRooms < rooms && genFloors < maxFloors) do {
		//while(genRoomsOnFloor < roomsPerFloor) {
		//    choose structure, calculate next pos and chose next structure (System: structures in different folders named to where they may attach
		//  build Staircase at next position
		//  genRoomsOnFloor++
		//  genFloors++
		//build staircase to bossroom at next position, then build boss room
		
		//Structure gen information: stored in map with location and structure file
		for(StrongholdFloorOpen floor : floors) {
			floor.generateRooms(world);
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		//build all the structures in the map
		for(StrongholdFloorOpen floor : floors) {
			floor.buildWalls(world);
		}
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		//Unused
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		//Unused
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		//MAKES SENSE ONLY FOR ENTRANCE BUILDING
	}

	public int getDunX() {
		return dunX;
	}
	public int getDunZ() {
		return dunZ;
	}
	
	public PlacementSettings getPlacementSettings() {
		return settings;
	}

}
