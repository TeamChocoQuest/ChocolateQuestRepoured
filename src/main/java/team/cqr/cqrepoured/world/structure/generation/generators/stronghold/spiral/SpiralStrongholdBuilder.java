package team.cqr.cqrepoured.world.structure.generation.generators.stronghold.spiral;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.ESkyDirection;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonVolcano;
import team.cqr.cqrepoured.world.structure.generation.generation.GeneratableDungeon;
import team.cqr.cqrepoured.world.structure.generation.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.generators.stronghold.EStrongholdRoomType;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;
import team.cqr.cqrepoured.world.structure.generation.structurefile.Offset;

import java.io.File;
import java.util.Random;

public class SpiralStrongholdBuilder {

	private final Random random;
	private AbstractDungeonGenerator<DungeonVolcano> generator;
	private GeneratableDungeon.Builder dungeonBuilder;
	// Direction the entryway is facing (beginning from the volcano center)
	private ESkyDirection allowedDirection;
	private DungeonVolcano dungeon;
	private SpiralStrongholdFloor[] floors;
	private int floorCount = 0;
	private int roomCount = 0;
	private int floorSideLength = 0;
	private boolean buildDownwards = true;
	private boolean buildInwards = true;

	public SpiralStrongholdBuilder(AbstractDungeonGenerator<DungeonVolcano> generator, GeneratableDungeon.Builder dungeonBuilder, ESkyDirection expansionDirection, DungeonVolcano dungeon, Random rand) {
		this.generator = generator;
		this.dungeonBuilder = dungeonBuilder;
		this.allowedDirection = expansionDirection;
		this.dungeon = dungeon;
		this.random = rand;

		this.floorSideLength = DungeonGenUtils.randomBetween(dungeon.getMinStrongholdRadius(), dungeon.getMaxStrongholdRadius(), this.random) * 2 + 1;
		this.roomCount = DungeonGenUtils.randomBetween(dungeon.getMinStrongholdRooms(), dungeon.getMaxStrongholdRooms(), this.random);
		if (this.roomCount % ((this.floorSideLength - 1) * 4) < 2) {
			this.roomCount++;
		}
		this.floorCount = MathHelper.ceil((double) this.roomCount / ((this.floorSideLength - 1) * 4));
		this.floors = new SpiralStrongholdFloor[this.floorCount];
	}

	public void calculateFloors(BlockPos strongholdEntrancePos, World world) {
		Tuple<Integer, Integer> posTuple = new Tuple<>(strongholdEntrancePos.getX(), strongholdEntrancePos.getZ());
		int middle = this.floorSideLength / 2;
		int entranceX = 0;
		int entranceZ = 0;
		int roomCounter = this.roomCount;
		final int maxRoomsPerFloor = this.floorSideLength * 4 - 4;
		int y = strongholdEntrancePos.getY();

		// If we generate inwards our first step is to place a stairwell to invert the entrance direction
		EStrongholdRoomType stairwellType = EStrongholdRoomType.STAIR_NN;
		Vector3i offsetVector = null;
		if (this.buildInwards) {
			switch (this.allowedDirection) {
			case NORTH:
				stairwellType = EStrongholdRoomType.STAIR_SS;
				offsetVector = new Vector3i(0, 0, this.dungeon.getRoomSizeZ());
				break;
			case EAST:
				stairwellType = EStrongholdRoomType.STAIR_WW;
				offsetVector = new Vector3i(-this.dungeon.getRoomSizeX(), 0, 0);
				break;
			case SOUTH:
				stairwellType = EStrongholdRoomType.STAIR_NN;
				offsetVector = new Vector3i(0, 0, -this.dungeon.getRoomSizeZ());
				break;
			case WEST:
				stairwellType = EStrongholdRoomType.STAIR_EE;
				offsetVector = new Vector3i(this.dungeon.getRoomSizeX(), 0, 0);
				break;
			}

			y -= this.dungeon.getRoomSizeY();

			this.allowedDirection = this.allowedDirection.getOpposite();
			File file = this.dungeon.getRoomNBTFileForType(stairwellType, this.random);
			if (file != null) {
				CQStructure room = this.generator.loadStructureFromFile(file);
				room.addAll(this.dungeonBuilder, new BlockPos(strongholdEntrancePos.getX(), y, strongholdEntrancePos.getZ()), Offset.CENTER);
			}
			strongholdEntrancePos = strongholdEntrancePos.offset(offsetVector);
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
			int floorRoomCount = i < this.floors.length - 1 ? maxRoomsPerFloor : this.roomCount % maxRoomsPerFloor;
			SpiralStrongholdFloor floor = new SpiralStrongholdFloor(this.generator, this.dungeonBuilder, posTuple, entranceX, entranceZ, roomCounter <= 0 || i == (this.floors.length - 1), this.floorSideLength, floorRoomCount, this.random);
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
			entranceX = floor.getExitIndex().getA();
			entranceZ = floor.getExitIndex().getB();
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

	public void buildFloors(BlockPos strongholdEntrancePos, World world) {
		// BlockPos currentPos = strongholdEntrancePos;
		for (int i = 0; i < this.floorCount; i++) {
			SpiralStrongholdFloor floor = this.floors[i];
			floor.buildRooms(this.dungeon, world);
		}
	}

}
