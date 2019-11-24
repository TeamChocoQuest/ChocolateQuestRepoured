package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.open;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.StrongholdOpenGenerator;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

public class StrongholdFloorOpen {

	private StrongholdOpenGenerator generator;
	private BlockPos[][] roomGrid;
	private Tuple<Integer, Integer> entranceStairPosition;
	private Tuple<Integer, Integer> exitStairPosition;
	private int yPos;
	
	public StrongholdFloorOpen(StrongholdOpenGenerator generator) {
		this.generator = generator;
		int rgd = generator.getDungeon().getRandomRoomCountForFloor();
		if(rgd % 2 != 0) {
			rgd++;
		}
		rgd = (new Double(Math.ceil(Math.sqrt(rgd)))).intValue();
		roomGrid = new BlockPos[rgd][rgd];
	}

}
