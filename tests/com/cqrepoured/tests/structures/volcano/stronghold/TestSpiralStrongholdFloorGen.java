package com.cqrepoured.tests.structures.volcano.stronghold;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress.ESpiralStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress.SpiralStrongholdFloor;

import net.minecraft.util.Tuple;

class TestSpiralStrongholdFloorGen {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void test() {
		int sideL = 5;
		SpiralStrongholdFloor floor = new SpiralStrongholdFloor(new Tuple<>(0,0), 0, Math.floorDiv(sideL, 2), false, sideL, (sideL * 4) -3);
		floor.calculateRoomGrid(ESpiralStrongholdRoomType.CURVE_WN, false);
		floor.calculateCoordinates(10, 15, 15);
		//floor.buildRooms(null, 100, 100, null);
		for(int x = 0; x < sideL; x++) {
			for(int z = 0; z < sideL; z++) {
				if(x == 0 || x == (sideL -1) || z == 0 || z == (sideL -1)) {
					if(floor.getRoomGrid() != null && floor.getRoomGrid()[x][z] != null) {
						System.out.println("Room at " + x + " | " + z + " is type: " + floor.getRoomGrid()[x][z].toString());
					}
				}
			}
		}
	}

}
