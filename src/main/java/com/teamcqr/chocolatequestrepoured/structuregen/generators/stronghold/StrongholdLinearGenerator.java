package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdLinearDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear.StrongholdFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.init.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class StrongholdLinearGenerator implements IDungeonGenerator {

	private StrongholdLinearDungeon dungeon;
	private int dunX;
	private int dunZ;

	public int getDunX() {
		return this.dunX;
	}

	public int getDunZ() {
		return this.dunZ;
	}

	private StrongholdFloor[] floors;
	private Random rdm;

	public StrongholdLinearGenerator(StrongholdLinearDungeon dungeon) {
		// Set floor count
		// Set room per floor count
		this.dungeon = dungeon;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) {
		// calculates the positions for rooms, stairs, bossroom, entrance, entrance stairs
		long floorCount = WorldDungeonGenerator.getSeed(world, chunk.x, chunk.z);
		this.rdm = new Random();
		this.rdm.setSeed(floorCount);
		int count = DungeonGenUtils.getIntBetweenBorders(this.dungeon.getMinFloors(), this.dungeon.getMaxFloors(), this.rdm);
		this.floors = new StrongholdFloor[count];
		this.dunX = x;
		this.dunZ = z;

		for (int i = 0; i < this.floors.length; i++) {
			int roomCount = DungeonGenUtils.getIntBetweenBorders(this.dungeon.getMinRoomsPerFloor(), this.dungeon.getMaxRoomsPerFloor(), this.rdm);
			StrongholdFloor floor = new StrongholdFloor(roomCount, this);
			floor.generateRoomPattern();
			this.floors[i] = floor;
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// places the structures
		BlockPos initPos = new BlockPos(x, y, z);
		// CQStructure entranceStair = new CQStructure(dungeon.getEntranceStairRoom(), dungeon, dunX, dunZ, dungeon.isProtectedFromModifications());
		// initPos = initPos.subtract(new Vec3i(0,entranceStair.getSizeY(),0));

		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);

		for (int i = 0; i < this.floors.length; i++) {
			System.out.println("Init pos for floor #" + i + " :" + initPos.toString());
			StrongholdFloor floor = this.floors[i];
			floor.generateRooms(initPos, i == 0, i == (this.floors.length - 1), settings, world, this.dungeon);
			initPos = floor.getLastRoomPastePos(initPos, this.dungeon).add(0, this.dungeon.getRoomSizeY(), 0);
		}

		CQStructure structure = new CQStructure(this.dungeon.getEntranceBuilding(), this.dungeon, chunk.x, chunk.z, this.dungeon.isProtectedFromModifications());
		if (this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			supportBuilder.createSupportHill(new Random(), world, new BlockPos(x, y + this.dungeon.getUnderGroundOffset(), z), structure.getSizeX(), structure.getSizeZ(), EPosType.CENTER_XZ_LAYER);
		}
		structure.placeBlocksInWorld(world, new BlockPos(x, y, z), settings, EPosType.CENTER_XZ_LAYER);
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		// Constructs walls around the rooms ? #TODO
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		// Unused
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// Unused
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		// Only for entrance
	}

}
