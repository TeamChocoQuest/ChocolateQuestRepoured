package team.cqr.cqrepoured.structuregen.generators.stronghold;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import team.cqr.cqrepoured.structuregen.dungeons.DungeonStrongholdLinear;
import team.cqr.cqrepoured.structuregen.generation.part.PlateauDungeonPart;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.structuregen.generators.stronghold.linear.StrongholdFloor;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitant;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.structuregen.structurefile.CQStructure;
import team.cqr.cqrepoured.structuregen.structurefile.Offset;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.ESkyDirection;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class GeneratorStronghold extends AbstractDungeonGenerator<DungeonStrongholdLinear> {

	private int dunX;
	private int dunZ;

	private StrongholdFloor[] floors;

	public GeneratorStronghold(World world, BlockPos pos, DungeonStrongholdLinear dungeon, Random rand) {
		super(world, pos, dungeon, rand);
	}

	@Override
	public void preProcess() {
		// calculates the positions for rooms, stairs, bossroom, entrance, entrance stairs
		int count = DungeonGenUtils.randomBetween(this.dungeon.getMinFloors(), this.dungeon.getMaxFloors(), this.random);
		int floorSize = this.dungeon.getFloorSize(this.random);
		this.floors = new StrongholdFloor[count];
		this.dunX = this.pos.getX();
		this.dunZ = this.pos.getZ();

		int sX = 0;
		int sZ = 0;
		ESkyDirection exitDir = ESkyDirection.values()[this.random.nextInt(ESkyDirection.values().length)];
		for (int i = 0; i < this.floors.length; i++) {
			// System.out.println("Calculating floor" + (i+1));
			StrongholdFloor floor = new StrongholdFloor(floorSize, this, i == (this.floors.length - 1), this.random);
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
		DungeonInhabitant mobType = DungeonInhabitantManager.instance().getInhabitantByDistanceIfDefault(this.dungeon.getDungeonMob(), this.world, this.pos.getX(), this.pos.getZ());
		PlacementSettings settings = new PlacementSettings();
		CQStructure structureStair = this.loadStructureFromFile(this.dungeon.getEntranceStairRoom(this.random));
		CQStructure structureEntrance = this.loadStructureFromFile(this.dungeon.getEntranceBuilding(this.random));

		int segCount = 0;
		CQStructure stairSeg = null;
		if (this.dungeon.useStairSegments()) {
			int ySurface = y;

			int yTmp = 3;
			yTmp += (this.floors.length - 1) * this.dungeon.getRoomSizeY();
			yTmp += structureStair.getSize().getY();

			if (yTmp < ySurface) {
				y = yTmp;
				stairSeg = this.loadStructureFromFile(this.dungeon.getEntranceStairSegment(this.random));
				while (y < ySurface) {
					segCount++;
					y += stairSeg.getSize().getY();
				}
			}
		}

		if (this.dungeon.doBuildSupportPlatform()) {
			PlateauDungeonPart.Builder partBuilder = new PlateauDungeonPart.Builder(this.pos.getX() + 4 + structureEntrance.getSize().getX() / 2,
					this.pos.getZ() + 4 + structureEntrance.getSize().getZ() / 2, this.pos.getX() - 4 - structureEntrance.getSize().getX() / 2,
					y + this.dungeon.getUnderGroundOffset() - 1, this.pos.getZ() - 4 - structureEntrance.getSize().getZ() / 2, 8);
			partBuilder.setSupportHillBlock(this.dungeon.getSupportBlock());
			partBuilder.setSupportHillTopBlock(this.dungeon.getSupportTopBlock());
			this.dungeonBuilder.add(partBuilder);
		}
		structureEntrance.addAll(this.dungeonBuilder, new BlockPos(this.pos.getX(), y, this.pos.getZ()), Offset.CENTER);

		if (segCount > 0) {
			while (segCount > 0) {
				segCount--;
				y -= stairSeg.getSize().getY();
				stairSeg.addAll(this.dungeonBuilder, new BlockPos(this.pos.getX(), y, this.pos.getZ()), Offset.CENTER);
			}
		}

		int yFloor = y;
		yFloor -= structureStair.getSize().getY();
		structureStair.addAll(this.dungeonBuilder, new BlockPos(this.pos.getX(), yFloor, this.pos.getZ()), Offset.CENTER);

		for (int i = 0; i < this.floors.length; i++) {
			StrongholdFloor floor = this.floors[i];

			floor.generateRooms(this.pos.getX(), this.pos.getZ(), yFloor, settings, this.dungeonBuilder, this.world, mobType);
			yFloor -= this.dungeon.getRoomSizeY();
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
		return this.dungeon;
	}

}
