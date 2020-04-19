package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold;

import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonStrongholdLinear;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
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

	private DungeonStrongholdLinear dungeon;
	private int dunX;
	private int dunZ;

	public int getDunX() {
		return this.dunX;
	}

	public int getDunZ() {
		return this.dunZ;
	}

	private Random rdm;
	private StrongholdFloor[] floors;

	public StrongholdLinearGenerator(DungeonStrongholdLinear dungeon) {
		// Set floor count
		// Set room per floor count
		this.dungeon = dungeon;
	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// calculates the positions for rooms, stairs, bossroom, entrance, entrance stairs
		long seed = WorldDungeonGenerator.getSeed(world, chunk.x, chunk.z);
		this.rdm = new Random();
		this.rdm.setSeed(seed);
		int count = DungeonGenUtils.getIntBetweenBorders(this.dungeon.getMinFloors(), this.dungeon.getMaxFloors(), this.rdm);
		int floorSize = this.dungeon.getFloorSize(this.rdm);
		this.floors = new StrongholdFloor[count];
		this.dunX = x;
		this.dunZ = z;

		int sX = 0;
		int sZ = 0;
		for (int i = 0; i < this.floors.length; i++) {
			StrongholdFloor floor = new StrongholdFloor(floorSize, this, i == (this.floors.length -1));
			floor.generateRoomPattern(sX, sZ);
			this.floors[i] = floor;
			sX = floor.getLastRoomGridPos().getFirst();
			sZ = floor.getLastRoomGridPos().getSecond();
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// places the structures
		BlockPos initPos = new BlockPos(x, y, z);
		// CQStructure entranceStair = new CQStructure(dungeon.getEntranceStairRoom(), dungeon, dunX, dunZ, dungeon.isProtectedFromModifications());
		// initPos = initPos.subtract(new Vec3i(0,entranceStair.getSizeY(),0));

		PlacementSettings settings = new PlacementSettings();
		settings.setMirror(Mirror.NONE);
		settings.setRotation(Rotation.NONE);
		settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		settings.setIntegrity(1.0F);

		CQStructure structure = new CQStructure(this.dungeon.getEntranceBuilding());
		if (this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			lists.add(supportBuilder.createSupportHillList(new Random(), world, new BlockPos(x, y + this.dungeon.getUnderGroundOffset(), z), structure.getSize().getX(), structure.getSize().getZ(), EPosType.CENTER_XZ_LAYER));
		}
		for (List<? extends IStructure> list : structure.addBlocksToWorld(world, new BlockPos(x, y, z), settings, EPosType.CENTER_XZ_LAYER, this.dungeon, chunk.x, chunk.z)) {
			lists.add(list);
		}
		structure = new CQStructure(this.dungeon.getEntranceStairRoom());
		for (List<? extends IStructure> list : structure.addBlocksToWorld(world, new BlockPos(x, y, z), settings, EPosType.CENTER_XZ_LAYER, this.dungeon, chunk.x, chunk.z)) {
			lists.add(list);
		}
		
		int yFloor = y;
		yFloor -= structure.getSize().getY();
		for (int i = 0; i < this.floors.length; i++) {
			System.out.println("Init pos for floor #" + i + " :" + initPos.toString());
			StrongholdFloor floor = this.floors[i];
			floor.generateRooms(x, z, yFloor, lists);
			//initPos = floor.getLastRoomPastePos(initPos, this.dungeon).add(0, this.dungeon.getRoomSizeY(), 0);
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Constructs walls around the rooms ? #TODO
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Unused
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Unused
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Only for entrance
	}

	@Override
	public DungeonBase getDungeon() {
		return this.dungeon;
	}

}
