package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.linear;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdLinearGenerator;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

import net.minecraft.util.Tuple;

public class StrongholdFloor {

	private StrongholdLinearGenerator generator;
	private int sideLength;
	private EStrongholdRoomType[][] roomPattern;
	
	public StrongholdFloor(int size, StrongholdLinearGenerator generator, boolean isLastFloor) {
		this.generator = generator;
		this.sideLength = size;
		this.roomPattern = new EStrongholdRoomType[size * 2 +1][size * 2 +1];
	}
	
	public void generateRoomPattern(int gridPosX, int gridPosZ) {
		
	}
	
	public void generateRooms(int centerX, int centerZ, int y, List<List<? extends IStructure>> lists) {
		
	}
	
	public Tuple<Integer, Integer> getLastRoomGridPos() {
		return new Tuple<>(0,0);
	}
	
	public Tuple<Integer, Integer> getFirstRoomGridPos() {
		return new Tuple<>(0,0);
	}
	
	private Tuple<Integer, Integer> gridPosToArrayIndices(Tuple<Integer, Integer> gridPosIn) {
		int x = (int) Math.floor(sideLength /2D);
		return new Tuple<>(gridPosIn.getFirst() + x, gridPosIn.getSecond() + x);
	}
	
	private Tuple<Integer, Integer> arrayIndiciesToGridPos(Tuple<Integer, Integer> arrayIndiciesIn) {
		int x = (int) Math.floor(sideLength /2D);
		return new Tuple<>(arrayIndiciesIn.getFirst() - x, arrayIndiciesIn.getSecond() - x);
	}
	
	public ESkyDirection getExitDirection() {
		return ESkyDirection.EAST;
	}
	
	private boolean isCurveRoom(int gpX, int gpZ) {
		return Math.abs(gpX) == Math.abs(gpZ);
	}

}
