package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear;

import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.block.Block;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractStrongholdRoom {

	private StrongholdFloor floor;
	private boolean generatedRoom = false;
	private boolean generatedWalls = false;
	
	private BlockPos position;
			
	private AbstractStrongholdRoom neighborRoomN, neighborRoomE, neighborRoomS, neighborRoomW;
	
	public AbstractStrongholdRoom(StrongholdFloor floor, BlockPos position) {
		this.floor = floor;
		this.position = position;
	}
	
	public boolean isRoomPositionConflicting(BlockPos newRoomPos) {
		return (position.getX() == newRoomPos.getX() && position.getZ() == newRoomPos.getZ()); 
	}
	
	public boolean hasFreeNeighbor() {
		return neighborRoomN == null ||
				neighborRoomE == null ||
				neighborRoomS == null ||
				neighborRoomW == null;
	}
	
	public ESkyDirection[] getFreeNeighborSlots() {
		int size = 0;
		if(neighborRoomN == null) size++;
		if(neighborRoomE == null) size++;
		if(neighborRoomS == null) size++;
		if(neighborRoomW == null) size++;
		if(size > 0) {
			ESkyDirection[] ret = new ESkyDirection[size];
			int indx = 0;
			if(neighborRoomN == null) {
				ret[indx] = ESkyDirection.NORTH;
				indx++;
			}
			if(neighborRoomE == null) {
				ret[indx] = ESkyDirection.EAST;
				indx++;
			}
			if(neighborRoomS == null) {
				ret[indx] = ESkyDirection.SOUTH;
				indx++;
			}
			if(neighborRoomW == null) {
				ret[indx] = ESkyDirection.WEST;
				indx++;
			}
		}
		return null;
	}
	
	public AbstractStrongholdRoom getNeighborRoom(ESkyDirection direction) {
		switch(direction) {
		case EAST:
			return neighborRoomE;
		case NORTH:
			return neighborRoomN;
		case SOUTH:
			return neighborRoomS;
		case WEST:
			return neighborRoomW;
		default:
			break;
		}
		return null;
	}

	abstract void buildWalls(Block wallBlock);
	abstract void constructRoomAt(BlockPos pos, Rotation rotation);
	
}
