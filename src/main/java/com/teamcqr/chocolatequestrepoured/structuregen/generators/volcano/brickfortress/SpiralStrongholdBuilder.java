package com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.VolcanoDungeon;
import com.teamcqr.chocolatequestrepoured.util.ESkyDirection;

public class SpiralStrongholdBuilder {

	private ESkyDirection allowedDirection;
	private VolcanoDungeon dungeon;
	private SpiralStrongholdFloor[] floors;
	
	public SpiralStrongholdBuilder(ESkyDirection expansionDirection, VolcanoDungeon dungeon) {
		this.allowedDirection = expansionDirection;
		this.dungeon = dungeon;
	}

}
