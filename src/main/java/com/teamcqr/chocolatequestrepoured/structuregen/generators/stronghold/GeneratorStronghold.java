package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.WorldDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonStrongholdLinear;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlockSpecial;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartEntity;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartPlateau;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear.StrongholdFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.EDefaultInhabitants;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class GeneratorStronghold extends AbstractDungeonGenerator<DungeonStrongholdLinear> {

	private int dunX;
	private int dunZ;

	private Random rdm;
	private StrongholdFloor[] floors;

	public GeneratorStronghold(World world, BlockPos pos, DungeonStrongholdLinear dungeon) {
		// Set floor count
		// Set room per floor count
		super(world, pos, dungeon);
	}

	@Override
	public void preProcess() {
		// calculates the positions for rooms, stairs, bossroom, entrance, entrance stairs
		long seed = WorldDungeonGenerator.getSeed(world, this.pos.getX() >> 4, this.pos.getZ() >> 4);
		this.rdm = new Random();
		this.rdm.setSeed(seed);
		int count = DungeonGenUtils.getIntBetweenBorders(this.dungeon.getMinFloors(), this.dungeon.getMaxFloors(), this.rdm);
		int floorSize = this.dungeon.getFloorSize(this.rdm);
		this.floors = new StrongholdFloor[count];
		this.dunX = this.pos.getX();
		this.dunZ = this.pos.getZ();

		int sX = 0;
		int sZ = 0;
		ESkyDirection exitDir = ESkyDirection.values()[rdm.nextInt(ESkyDirection.values().length)];
		for (int i = 0; i < this.floors.length; i++) {
			// System.out.println("Calculating floor" + (i+1));
			StrongholdFloor floor = new StrongholdFloor(floorSize, this, i == (this.floors.length - 1));
			floor.generateRoomPattern(sX, sZ, exitDir);
			this.floors[i] = floor;
			exitDir = floor.getExitDirection();
			sX = floor.getLastRoomGridPos().getFirst();
			sZ = floor.getLastRoomGridPos().getSecond();
		}
	}

	@Override
	public void buildStructure() {
		// places the structures
		// CQStructure entranceStair = new CQStructure(dungeon.getEntranceStairRoom(), dungeon, dunX, dunZ, dungeon.isProtectedFromModifications());
		// initPos = initPos.subtract(new Vec3i(0,entranceStair.getSizeY(),0));

		int y = this.pos.getY();
		EDefaultInhabitants mobType = dungeon.getDungeonMob();
		if (mobType == EDefaultInhabitants.DEFAULT) {
			mobType = EDefaultInhabitants.getMobTypeDependingOnDistance(world, this.pos.getX(), this.pos.getZ());
		}
		PlacementSettings settings = new PlacementSettings();
		CQStructure structureStair = this.loadStructureFromFile(this.dungeon.getEntranceStairRoom());
		CQStructure structureEntrance = this.loadStructureFromFile(this.dungeon.getEntranceBuilding());

		int segCount = 0;
		CQStructure stairSeg = null;
		if (this.dungeon.useStairSegments()) {
			int ySurface = y;

			int yTmp = 3;
			yTmp += (this.floors.length - 1) * this.dungeon.getRoomSizeY();
			yTmp += structureStair.getSize().getY();

			if (yTmp < ySurface) {
				y = yTmp;
				stairSeg = this.loadStructureFromFile(this.dungeon.getEntranceStairSegment());
				while (y < ySurface) {
					segCount++;
					y += stairSeg.getSize().getY();
				}
			}
		}

		if (this.dungeon.doBuildSupportPlatform()) {
			this.dungeonGenerator.add(new DungeonPartPlateau(world, dungeonGenerator, this.pos.getX() + 4 + structureEntrance.getSize().getX() / 2, this.pos.getZ() + 4 + structureEntrance.getSize().getZ() / 2, this.pos.getX() - 4 - structureEntrance.getSize().getX() / 2, /*this.pos.getY()*/y + this.dungeon.getUnderGroundOffset() -1, this.pos.getZ() - 4 - structureEntrance.getSize().getZ() / 2,
					this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock(), 8));
		}
		BlockPos p1 = DungeonGenUtils.getCentralizedPosForStructure(new BlockPos(this.pos.getX(), y, this.pos.getZ()), structureEntrance, settings);
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, p1, structureEntrance.getBlockInfoList(), settings, mobType));
		this.dungeonGenerator.add(new DungeonPartEntity(this.world, this.dungeonGenerator, p1, structureEntrance.getEntityInfoList(), settings, mobType));
		this.dungeonGenerator.add(new DungeonPartBlockSpecial(this.world, this.dungeonGenerator, p1, structureEntrance.getSpecialBlockInfoList(), settings, mobType));

		if (segCount > 0) {
			while (segCount > 0) {
				segCount--;
				y -= stairSeg.getSize().getY();
				BlockPos p2 = DungeonGenUtils.getCentralizedPosForStructure(new BlockPos(this.pos.getX(), y, this.pos.getZ()), stairSeg, settings);
				this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, p2, stairSeg.getBlockInfoList(), settings, mobType));
				this.dungeonGenerator.add(new DungeonPartEntity(this.world, this.dungeonGenerator, p2, stairSeg.getEntityInfoList(), settings, mobType));
				this.dungeonGenerator.add(new DungeonPartBlockSpecial(this.world, this.dungeonGenerator, p2, stairSeg.getSpecialBlockInfoList(), settings, mobType));
			}
		}

		int yFloor = y;
		yFloor -= structureStair.getSize().getY();
		BlockPos p3 = DungeonGenUtils.getCentralizedPosForStructure(new BlockPos(this.pos.getX(), yFloor, this.pos.getZ()), structureStair, settings);
		this.dungeonGenerator.add(new DungeonPartBlock(this.world, this.dungeonGenerator, p3, structureStair.getBlockInfoList(), settings, mobType));
		this.dungeonGenerator.add(new DungeonPartEntity(this.world, this.dungeonGenerator, p3, structureStair.getEntityInfoList(), settings, mobType));
		this.dungeonGenerator.add(new DungeonPartBlockSpecial(this.world, this.dungeonGenerator, p3, structureStair.getSpecialBlockInfoList(), settings, mobType));

		for (int i = 0; i < this.floors.length; i++) {
			StrongholdFloor floor = this.floors[i];

			floor.generateRooms(this.pos.getX(), this.pos.getZ(), yFloor, settings, this.dungeonGenerator, world, mobType);
			yFloor -= dungeon.getRoomSizeY();
			// initPos = floor.getLastRoomPastePos(initPos, this.dungeon).add(0, this.dungeon.getRoomSizeY(), 0);
		}
	}

	@Override
	public void postProcess() {
		// Constructs walls around the rooms ? #TODO
	}

	public int getDunX() {
		return this.dunX;
	}

	public int getDunZ() {
		return this.dunZ;
	}

	public DungeonStrongholdLinear getDungeon() {
		return dungeon;
	}

}
