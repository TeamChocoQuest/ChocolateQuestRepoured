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
		
		freeSides.add(ESkyDirection.NORTH);
		freeSides.add(ESkyDirection.EAST);
		freeSides.add(ESkyDirection.SOUTH);
		freeSides.add(ESkyDirection.WEST);
	}
	
	public void setGridIndex(int x, int z) {
		gridIndex = new Tuple<> (x,z);
	}
	
	public Tuple<Integer, Integer> getGridIndex() {
		return gridIndex;
	}
	
	public void connectRoomOnSide(ESkyDirection side) {
		if(freeSides.contains(side)) {
			freeSides.remove(side);
		}
		switch(freeSides.size()) {
		case 0:
			layout = ERoomType.FOUR_SIDED;
			roomtype = ERoomTypeExtended.CROSSING_NESW;
			break;
		case 1:
			layout = ERoomType.T_CROSSING;
			switch(freeSides.get(0)) {
			case EAST:
				roomtype = ERoomTypeExtended.CROSSING_NWS;
				break;
			case NORTH:
				roomtype = ERoomTypeExtended.CROSSING_WSE;
				break;
			case SOUTH:
				roomtype = ERoomTypeExtended.CROSSING_ENW;
				break;
			case WEST:
				roomtype = ERoomTypeExtended.CROSSING_NES;
				break;
			}
			break;
		case 2: 
			ESkyDirection s1 = freeSides.get(0);
			ESkyDirection s2 = freeSides.get(1);
			if(s1.equals(ESkyDirection.NORTH) && s2.equals(ESkyDirection.SOUTH)) {
				layout = ERoomType.HALLWAY;
				roomtype = ERoomTypeExtended.HALLWAY_EW;
				break;
			}
			if(s1.equals(ESkyDirection.EAST) && s2.equals(ESkyDirection.WEST)) {
				layout = ERoomType.HALLWAY;
				roomtype = ERoomTypeExtended.HALLWAY_NS;
				break;
			}
			switch(s1) {
			case EAST:
				if(s1.equals(ESkyDirection.NORTH)) {
					roomtype = ERoomTypeExtended.CURVE_NE;
				} else {
					roomtype = ERoomTypeExtended.CURVE_ES;
				}
				break;
			case NORTH:
				if(s1.equals(ESkyDirection.WEST)) {
					roomtype = ERoomTypeExtended.CURVE_WN;
				} else {
					roomtype = ERoomTypeExtended.CURVE_NE;
				}
				break;
			case SOUTH:
				if(s1.equals(ESkyDirection.EAST)) {
					roomtype = ERoomTypeExtended.CURVE_ES;
				} else {
					roomtype = ERoomTypeExtended.CURVE_SW;
				}
				break;
			case WEST:
				if(s1.equals(ESkyDirection.SOUTH)) {
					roomtype = ERoomTypeExtended.CURVE_SW;
				} else {
					roomtype = ERoomTypeExtended.CURVE_WN;
				}
				break;
			default:
				break;
			
			}
			layout = ERoomType.CURVE;
			break;
		case 3:
			layout = ERoomType.DEAD_END;
			switch(side) {
			case EAST:
				roomtype = ERoomTypeExtended.ROOM_E;
				break;
			case NORTH:
				roomtype = ERoomTypeExtended.ROOM_N;
				break;
			case SOUTH:
				roomtype = ERoomTypeExtended.ROOM_S;
				break;
			case WEST:
				roomtype = ERoomTypeExtended.ROOM_W;
				break;
			}
			break;
		case 4:
			layout = ERoomType.UNSET;
			break;
		}
	}
	
	public void generateRoom(StrongholdLinearDungeon dungeon, BlockPos centeredOnXZPos, World world, PlacementSettings settings) {
		CQStructure structure = null;
		switch(layout) {
		case CURVE:
			structure = new CQStructure(dungeon.getCurveRoom(), dungeon, floor.getGenerator().getDunZ(), floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		case DEAD_END:
			structure = new CQStructure(dungeon.getDeadEndRoom(), dungeon, floor.getGenerator().getDunZ(), floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		case FOUR_SIDED:
			structure = new CQStructure(dungeon.getCrossingRoom(), dungeon, floor.getGenerator().getDunZ(), floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		case HALLWAY:
			structure = new CQStructure(dungeon.getHallwayRoom(), dungeon, floor.getGenerator().getDunZ(), floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		case T_CROSSING:
			structure = new CQStructure(dungeon.getTCrossingRoom(), dungeon, floor.getGenerator().getDunZ(), floor.getGenerator().getDunX(), dungeon.isProtectedFromModifications());
			break;
		default:
			break;
		}
		if(structure != null) {
			generateRoom(dungeon, centeredOnXZPos, world, settings, structure, false);
		}
	}
	
	public void generateRoom(StrongholdLinearDungeon dungeon, BlockPos centeredOnXZPos, World world, PlacementSettings settings, CQStructure structure, boolean ignoreRotating) {
		BlockPos pastePos = this.roomtype.getTransformedPastePos(centeredOnXZPos);
		structure.placeBlocksInWorld(world, pastePos, settings, EPosType.CENTER_XZ_LAYER);
	}

	public ESkyDirection getRandomFreeDirection(Random rdm) {
		if(!freeSides.isEmpty()) {
			return freeSides.get(rdm.nextInt(freeSides.size()));
		} else {
			return null;
		}
	}

	public boolean hasFreeSides() {
		return freeSides.isEmpty();
	}

}
