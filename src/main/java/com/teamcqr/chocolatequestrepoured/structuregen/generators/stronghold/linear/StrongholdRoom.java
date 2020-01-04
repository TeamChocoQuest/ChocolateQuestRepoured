package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.StrongholdLinearDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

public class StrongholdRoom {

	private List<ESkyDirection> freeSides = new ArrayList<>();
	private ERoomType layout = ERoomType.UNSET;
	private ERoomTypeExtended roomtype = ERoomTypeExtended.UNSET;
	private Tuple<Integer, Integer> gridIndex;
	private StrongholdFloor floor;

	public StrongholdRoom(StrongholdFloor floor) {
		this.floor = floor;

		this.freeSides.add(ESkyDirection.NORTH);
		this.freeSides.add(ESkyDirection.EAST);
		this.freeSides.add(ESkyDirection.SOUTH);
		this.freeSides.add(ESkyDirection.WEST);
	}

	public void setGridIndex(int x, int z) {
		this.gridIndex = new Tuple<>(x, z);
	}

	public Tuple<Integer, Integer> getGridIndex() {
		return this.gridIndex;
	}

	public void connectRoomOnSide(ESkyDirection side) {
		if (this.freeSides.contains(side)) {
			this.freeSides.remove(side);
		}
		switch (this.freeSides.size()) {
		case 0:
			this.layout = ERoomType.FOUR_SIDED;
			this.roomtype = ERoomTypeExtended.CROSSING_NESW;
			break;
		case 1:
			this.layout = ERoomType.T_CROSSING;
			switch (this.freeSides.get(0)) {
			case EAST:
				this.roomtype = ERoomTypeExtended.CROSSING_NWS;
				break;
			case NORTH:
				this.roomtype = ERoomTypeExtended.CROSSING_WSE;
				break;
			case SOUTH:
				this.roomtype = ERoomTypeExtended.CROSSING_ENW;
				break;
			case WEST:
				this.roomtype = ERoomTypeExtended.CROSSING_NES;
				break;
			}
			break;
		case 2:
			ESkyDirection s1 = this.freeSides.get(0);
			ESkyDirection s2 = this.freeSides.get(1);
			if (s1.equals(ESkyDirection.NORTH) && s2.equals(ESkyDirection.SOUTH)) {
				this.layout = ERoomType.HALLWAY;
				this.roomtype = ERoomTypeExtended.HALLWAY_EW;
				break;
			}
			if (s1.equals(ESkyDirection.EAST) && s2.equals(ESkyDirection.WEST)) {
				this.layout = ERoomType.HALLWAY;
				this.roomtype = ERoomTypeExtended.HALLWAY_NS;
				break;
			}
			switch (s1) {
			case EAST:
				if (s2.equals(ESkyDirection.NORTH)) {
					this.roomtype = ERoomTypeExtended.CURVE_SW;
				} else {
					this.roomtype = ERoomTypeExtended.CURVE_WN;
				}
				break;
			case NORTH:
				if (s2.equals(ESkyDirection.WEST)) {
					this.roomtype = ERoomTypeExtended.CURVE_ES;
				} else {
					this.roomtype = ERoomTypeExtended.CURVE_SW;
				}
				break;
			case SOUTH:
				if (s2.equals(ESkyDirection.EAST)) {
					this.roomtype = ERoomTypeExtended.CURVE_WN;
				} else {
					this.roomtype = ERoomTypeExtended.CURVE_NE;
				}
				break;
			case WEST:
				if (s2.equals(ESkyDirection.SOUTH)) {
					this.roomtype = ERoomTypeExtended.CURVE_NE;
				} else {
					this.roomtype = ERoomTypeExtended.CURVE_ES;
				}
				break;
			default:
				break;

			}
			this.layout = ERoomType.CURVE;
			break;
		case 3:
			this.layout = ERoomType.DEAD_END;
			switch (side) {
			case EAST:
				this.roomtype = ERoomTypeExtended.ROOM_E;
				break;
			case NORTH:
				this.roomtype = ERoomTypeExtended.ROOM_N;
				break;
			case SOUTH:
				this.roomtype = ERoomTypeExtended.ROOM_S;
				break;
			case WEST:
				this.roomtype = ERoomTypeExtended.ROOM_W;
				break;
			}
			break;
		case 4:
			this.layout = ERoomType.UNSET;
			break;
		}
	}

	public void generateRoom(StrongholdLinearDungeon dungeon, BlockPos centeredOnXZPos, World world, PlacementSettings settings) {
		CQStructure structure = null;
		switch (this.layout) {
		case CURVE:
			structure = new CQStructure(dungeon.getCurveRoom(), dungeon, this.floor.getGenerator().getDunZ(), this.floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		case DEAD_END:
			structure = new CQStructure(dungeon.getDeadEndRoom(), dungeon, this.floor.getGenerator().getDunZ(), this.floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		case FOUR_SIDED:
			structure = new CQStructure(dungeon.getCrossingRoom(), dungeon, this.floor.getGenerator().getDunZ(), this.floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		case HALLWAY:
			structure = new CQStructure(dungeon.getHallwayRoom(), dungeon, this.floor.getGenerator().getDunZ(), this.floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		case T_CROSSING:
			structure = new CQStructure(dungeon.getTCrossingRoom(), dungeon, this.floor.getGenerator().getDunZ(), this.floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		default:
			break;
		}
		if (structure != null) {
			this.generateRoom(dungeon, centeredOnXZPos, world, settings, structure, false);
		}
	}

	public void generateRoom(StrongholdLinearDungeon dungeon, BlockPos centeredOnXZPos, World world, PlacementSettings settings, CQStructure structure, boolean ignoreRotating) {
		settings.setRotation(this.roomtype.getRotation());
		BlockPos pastePos = this.roomtype.getTransformedPastePos(centeredOnXZPos, structure.getSizeX(), structure.getSizeZ(), EPosType.CENTER_XZ_LAYER);
		structure.placeBlocksInWorld(world, pastePos, settings, EPosType.CENTER_XZ_LAYER);
		System.out.println("Generating room: LAYOUT =  " + this.layout.toString() + "    TYPE: " + this.roomtype.toString() + "    AT: " + pastePos.toString());
	}

	public ESkyDirection getRandomFreeDirection(Random rdm) {
		if (!this.freeSides.isEmpty()) {
			return this.freeSides.get(rdm.nextInt(this.freeSides.size()));
		} else {
			return null;
		}
	}

	public boolean hasFreeSides() {
		return this.freeSides.isEmpty();
	}

}
