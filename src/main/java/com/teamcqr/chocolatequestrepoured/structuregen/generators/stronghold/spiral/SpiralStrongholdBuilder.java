package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.spiral;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonVolcano;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.AbstractDungeonPart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartBlock;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonPartEntity;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class SpiralStrongholdBuilder {

	private AbstractDungeonGenerator<DungeonVolcano> generator;
	private DungeonGenerator dungeonGenerator;
	//Direction the entryway is facing (beginning from the volcano center)
	private ESkyDirection allowedDirection;
	private DungeonVolcano dungeon;
	private SpiralStrongholdFloor[] floors;
	private int floorCount = 0;
	private int roomCount = 0;
	private int floorSideLength = 0;
	private List<AbstractDungeonPart> strongholdParts = new ArrayList<>();
	private boolean buildDownwards = true;
	private boolean buildInwards = true;

	public SpiralStrongholdBuilder(AbstractDungeonGenerator<DungeonVolcano> generator, DungeonGenerator dungeonGenerator, ESkyDirection expansionDirection, DungeonVolcano dungeon) {
		this.generator = generator;
		this.dungeonGenerator = dungeonGenerator;
		this.allowedDirection = expansionDirection;
		this.dungeon = dungeon;

		this.floorCount = DungeonGenUtils.randomBetween(dungeon.getMinStrongholdFloors(), dungeon.getMaxStrongholdFloors());
		this.roomCount = DungeonGenUtils.randomBetween(dungeon.getMinStrongholdRooms(), dungeon.getMaxStrongholdRooms());
		this.floorSideLength = DungeonGenUtils.randomBetween(dungeon.getMinStrongholdRadius(), dungeon.getMaxStrongholdRadius()) * 2 + 1;
		this.floors = new SpiralStrongholdFloor[this.floorCount];
	}

	public void calculateFloors(BlockPos strongholdEntrancePos, World world, String mobType) {
		Tuple<Integer, Integer> posTuple = new Tuple<>(strongholdEntrancePos.getX(), strongholdEntrancePos.getZ());

		int middle = this.floorSideLength / 2;
		int entranceX = 0;
		int entranceZ = 0;
		int roomCounter = this.roomCount;
		final int maxRoomsPerFloor = this.floorSideLength * 4 - 4;

		int y = strongholdEntrancePos.getY();
		
		//If we generate inwards our first step is to place a stairwell to invert the entrance direction
		EStrongholdRoomType stairwellType = EStrongholdRoomType.STAIR_NN;
		Vec3i offsetVector = null;
		if(buildInwards) {
			switch(this.allowedDirection) {
			case NORTH:
				stairwellType = EStrongholdRoomType.STAIR_SS;
				offsetVector = new Vec3i(0,0,dungeon.getRoomSizeZ());
				break;
			case EAST:
				stairwellType = EStrongholdRoomType.STAIR_WW;
				offsetVector = new Vec3i(-dungeon.getRoomSizeX(),0,0);
				break;
			case SOUTH:
				stairwellType = EStrongholdRoomType.STAIR_NN;
				offsetVector = new Vec3i(0,0,-dungeon.getRoomSizeZ());
				break;
			case WEST:
				stairwellType = EStrongholdRoomType.STAIR_EE;
				offsetVector = new Vec3i(dungeon.getRoomSizeX(),0,0);
				break;
			}
			
			y -= this.dungeon.getRoomSizeY();
			
			this.allowedDirection = this.allowedDirection.getOpposite();
			File file = dungeon.getRoomNBTFileForType(stairwellType);
			if (file != null) {
				CQStructure room = this.generator.loadStructureFromFile(file);
				PlacementSettings settings = new PlacementSettings();
				BlockPos p = DungeonGenUtils.getCentralizedPosForStructure(strongholdEntrancePos, room, settings);
				p = new BlockPos(p.getX(), y, p.getZ());
				strongholdParts.add(new DungeonPartBlock(world, this.dungeonGenerator, p, room.getBlockInfoList(), settings, mobType));
				strongholdParts.add(new DungeonPartBlock(world, this.dungeonGenerator, p, room.getSpecialBlockInfoList(), settings, mobType));
				strongholdParts.add(new DungeonPartEntity(world, this.dungeonGenerator, p, room.getEntityInfoList(), settings, mobType));
			}
			strongholdEntrancePos = strongholdEntrancePos.add(offsetVector);
			posTuple = new Tuple<>(strongholdEntrancePos.getX(), strongholdEntrancePos.getZ());
		}
		
		EStrongholdRoomType entranceType = EStrongholdRoomType.NONE;
		switch(allowedDirection) {
		case WEST:
			entranceType = EStrongholdRoomType.CURVE_EN;
			entranceX = this.floorSideLength - 1;
			entranceZ = middle;
			break;
		case NORTH:
			entranceType = EStrongholdRoomType.CURVE_SE;
			entranceX = middle;
			entranceZ = this.floorSideLength - 1;
			break;
		case SOUTH:
			entranceType = EStrongholdRoomType.CURVE_NW;
			entranceX = middle;
			entranceZ = 0;
			break;
		case EAST:
			entranceType = EStrongholdRoomType.CURVE_WS;
			entranceX = 0;
			entranceZ = middle;
			break;
		default:
			break;
		}
		
		EStrongholdRoomType firstRoomOverride = entranceType;
		for (int i = 0; i < this.floors.length; i++) {
			if (posTuple == null || roomCounter <= 0) {
				this.floorCount--;
				continue;
			}
			int floorRoomCount = maxRoomsPerFloor;
			if (roomCounter >= maxRoomsPerFloor) {
				roomCounter -= maxRoomsPerFloor;
				/* We add one cause the room above the stair does not count as room */
				roomCounter++;
			} else {
				floorRoomCount = roomCounter;
				roomCounter = 0;
			}
			SpiralStrongholdFloor floor = new SpiralStrongholdFloor(this.generator, this.dungeonGenerator, posTuple, entranceX, entranceZ, roomCounter <= 0 || i == (this.floors.length - 1), this.floorSideLength, floorRoomCount);
			floor.calculateRoomGrid(entranceType, (i + 1) % 2 == 0);
			floor.calculateCoordinates(y, this.dungeon.getRoomSizeX(), this.dungeon.getRoomSizeZ());
			posTuple = floor.getExitCoordinates();

			if (i != 0) {
				if(buildDownwards) {
					floor.overrideFirstRoomType(firstRoomOverride);
				} else {
					floor.overrideFirstRoomType(EStrongholdRoomType.NONE);
				}
			}
			entranceX = floor.getExitIndex().getFirst();
			entranceZ = floor.getExitIndex().getSecond();
			if(i == (floors.length -1)) {
				floor.overrideLastRoomType(EStrongholdRoomType.BOSS);
			} else {
				entranceType = floor.getExitRoomType();
				firstRoomOverride = entranceType;
				if(buildDownwards) {
					floor.overrideLastRoomType(EStrongholdRoomType.NONE);
				}
			}
			if(buildDownwards) {
				y -= this.dungeon.getRoomSizeY();
			} else {
				y += this.dungeon.getRoomSizeY();
			}
			this.floors[i] = floor;
		}
	}

	public void buildFloors(BlockPos strongholdEntrancePos, World world, String mobType) {
		// BlockPos currentPos = strongholdEntrancePos;
		List<AbstractDungeonPart> floors = new ArrayList<>();
		for(int i = 0; i < floorCount; i++) {
			SpiralStrongholdFloor floor = this.floors[i];
			floors.addAll(floor.buildRooms(this.dungeon, world, mobType));
		}
		strongholdParts.addAll(floors);
	}

	public List<AbstractDungeonPart> getStrongholdParts() {
		return strongholdParts;
	}

}
