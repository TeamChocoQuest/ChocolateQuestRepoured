package team.cqr.cqrepoured.structuregen.generators.stronghold.spiral;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonVolcano;
import team.cqr.cqrepoured.structuregen.generation.AbstractDungeonPart;
import team.cqr.cqrepoured.structuregen.generation.DungeonGenerator;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.structurefile.CQStructure;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.ESkyDirection;

public class SpiralStrongholdBuilder {

	private final Random random;
	private AbstractDungeonGenerator<DungeonVolcano> generator;
	private DungeonGenerator dungeonGenerator;
	// Direction the entryway is facing (beginning from the volcano center)
	private ESkyDirection allowedDirection;
	private DungeonVolcano dungeon;
	private SpiralStrongholdFloor[] floors;
	private int floorCount = 0;
	private int roomCount = 0;
	private int floorSideLength = 0;
	private List<AbstractDungeonPart> strongholdParts = new ArrayList<>();
	private boolean buildDownwards = true;
	private boolean buildInwards = true;

	public SpiralStrongholdBuilder(AbstractDungeonGenerator<DungeonVolcano> generator, DungeonGenerator dungeonGenerator, ESkyDirection expansionDirection, DungeonVolcano dungeon, Random rand) {
		this.generator = generator;
		this.dungeonGenerator = dungeonGenerator;
		this.allowedDirection = expansionDirection;
		this.dungeon = dungeon;
		this.random = rand;

		this.floorCount = DungeonGenUtils.randomBetween(dungeon.getMinStrongholdFloors(), dungeon.getMaxStrongholdFloors(), this.random);
		this.roomCount = DungeonGenUtils.randomBetween(dungeon.getMinStrongholdRooms(), dungeon.getMaxStrongholdRooms(), this.random);
		this.floorSideLength = DungeonGenUtils.randomBetween(dungeon.getMinStrongholdRadius(), dungeon.getMaxStrongholdRadius(), this.random) * 2 + 1;
		this.floors = new SpiralStrongholdFloor[this.floorCount];
	}

	public void calculateFloors(BlockPos strongholdEntrancePos, World world, DungeonInhabitant mobType) {
		Tuple<Integer, Integer> posTuple = new Tuple<>(strongholdEntrancePos.getX(), strongholdEntrancePos.getZ());
		int middle = this.floorSideLength / 2;
		int entranceX = 0;
		int entranceZ = 0;
		int roomCounter = this.roomCount;
		final int maxRoomsPerFloor = this.floorSideLength * 4 - 4;
		int y = strongholdEntrancePos.getY();

		// If we generate inwards our first step is to place a stairwell to invert the entrance direction
		EStrongholdRoomType stairwellType = EStrongholdRoomType.STAIR_NN;
		Vec3i offsetVector = null;
		if (this.buildInwards) {
			switch (this.allowedDirection) {
			case NORTH:
				stairwellType = EStrongholdRoomType.STAIR_SS;
				offsetVector = new Vec3i(0, 0, this.dungeon.getRoomSizeZ());
				break;
			case EAST:
				stairwellType = EStrongholdRoomType.STAIR_WW;
				offsetVector = new Vec3i(-this.dungeon.getRoomSizeX(), 0, 0);
				break;
			case SOUTH:
				stairwellType = EStrongholdRoomType.STAIR_NN;
				offsetVector = new Vec3i(0, 0, -this.dungeon.getRoomSizeZ());
				break;
			case WEST:
				stairwellType = EStrongholdRoomType.STAIR_EE;
				offsetVector = new Vec3i(this.dungeon.getRoomSizeX(), 0, 0);
				break;
			}

			y -= this.dungeon.getRoomSizeY();

			this.allowedDirection = this.allowedDirection.getOpposite();
			File file = this.dungeon.getRoomNBTFileForType(stairwellType, this.random);
			if (file != null) {
				CQStructure room = this.generator.loadStructureFromFile(file);
				PlacementSettings settings = new PlacementSettings();
				BlockPos p = DungeonGenUtils.getCentralizedPosForStructure(strongholdEntrancePos, room, settings);
				p = new BlockPos(p.getX(), y, p.getZ());
				room.addAll(world, dungeonGenerator, p, settings, mobType);
			}
			strongholdEntrancePos = strongholdEntrancePos.add(offsetVector);
			posTuple = new Tuple<>(strongholdEntrancePos.getX(), strongholdEntrancePos.getZ());
		}

		EStrongholdRoomType entranceType = EStrongholdRoomType.NONE;
		switch (this.allowedDirection) {
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
			SpiralStrongholdFloor floor = new SpiralStrongholdFloor(this.generator, this.dungeonGenerator, posTuple, entranceX, entranceZ, roomCounter <= 0 || i == (this.floors.length - 1), this.floorSideLength, floorRoomCount, this.random);
			floor.calculateRoomGrid(entranceType, (i + 1) % 2 == 0);
			floor.calculateCoordinates(y, this.dungeon.getRoomSizeX(), this.dungeon.getRoomSizeZ());
			posTuple = floor.getExitCoordinates();
			if (i != 0) {
				if (this.buildDownwards) {
					floor.overrideFirstRoomType(firstRoomOverride);
				} else {
					floor.overrideFirstRoomType(EStrongholdRoomType.NONE);
				}
			}
			entranceX = floor.getExitIndex().getFirst();
			entranceZ = floor.getExitIndex().getSecond();
			if (i == (this.floors.length - 1)) {
				floor.overrideLastRoomType(EStrongholdRoomType.BOSS);
			} else {
				entranceType = floor.getExitRoomType();
				firstRoomOverride = entranceType;
				if (this.buildDownwards) {
					floor.overrideLastRoomType(EStrongholdRoomType.NONE);
				}
			}
			if (this.buildDownwards) {
				y -= this.dungeon.getRoomSizeY();
			} else {
				y += this.dungeon.getRoomSizeY();
			}
			this.floors[i] = floor;
		}
	}

	public void buildFloors(BlockPos strongholdEntrancePos, World world, DungeonInhabitant mobType) {
		// BlockPos currentPos = strongholdEntrancePos;
		List<AbstractDungeonPart> floors = new ArrayList<>();
		for (int i = 0; i < this.floorCount; i++) {
			SpiralStrongholdFloor floor = this.floors[i];
			floors.addAll(floor.buildRooms(this.dungeon, world, mobType));
		}
		this.strongholdParts.addAll(floors);
	}

	public List<AbstractDungeonPart> getStrongholdParts() {
		return this.strongholdParts;
	}

}
