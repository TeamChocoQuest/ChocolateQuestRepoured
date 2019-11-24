package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear;

import java.util.ArrayList;
import java.util.List;

public class StrongholdFloor {
	
	private int floorY;
	private int roomCount;
	
	private List<AbstractStrongholdRoom> rooms = new ArrayList<>();
	private List<AbstractStrongholdRoom> roomsWithFreeNeighbors = new ArrayList<>();

	public StrongholdFloor(int roomCount) {
		this.roomCount = roomCount;
	}
	
	public int getRoomSize() {
		//TODO Calculate this
		return 16;
	}

}
