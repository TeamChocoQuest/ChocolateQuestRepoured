package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.CastleDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.CastleRoomSelector;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 01.06.2019 Developed by KalgogSmash: https://github.com/kalgogsmash
 */
public class CastlePartMain implements ICastlePart {
	private BlockPos start;
	private int sizeX;
	private int sizeZ;
	private int roomsX;
	private int roomsZ;
	private int floors;
	private CastleDungeon dungeon;
	private Random random;
	private int startLayer;
	private boolean isTopFloor;
	private CastleRoomSelector roomHelper;

	public CastlePartMain(BlockPos origin, int maxRoomsX, int maxRoomsZ, int floors, CastleDungeon dungeon, int startLayer) {
		this.dungeon = dungeon;
		this.floors = floors;
		this.random = this.dungeon.getRandom();
		this.roomsX = maxRoomsX;
		this.roomsZ = maxRoomsZ;

		this.start = origin;
		this.sizeX = maxRoomsX * dungeon.getRoomSize();
		this.sizeZ = maxRoomsZ * dungeon.getRoomSize();
		this.startLayer = startLayer;
		this.isTopFloor = false;

		this.roomHelper = new CastleRoomSelector(this.start, dungeon.getRoomSize(), dungeon.getFloorHeight(), floors, maxRoomsX, maxRoomsZ, this.random);
		this.roomHelper.randomizeCastle();
	}

	@Override
	public void generatePart(World world) {
		int currentY;
		int floorHeight = this.dungeon.getFloorHeight();
		int x = this.start.getX();
		int y = this.start.getY();
		int z = this.start.getZ();
		IBlockState blockToBuild;

		System.out.println("Building a square part at " + x + ", " + y + ", " + z + ". sizeX = " + this.sizeX + ", sizeZ = " + this.sizeZ + ". Floors = " + this.floors);

		this.roomHelper.generate(world, this.dungeon);

	}

	@Override
	public boolean isTower() {
		return false;
	}

	@Override
	public void setAsTopFloor() {
		this.isTopFloor = true;
	}

	@Override
	public int getStartLayer() {
		return this.startLayer;
	}
}
