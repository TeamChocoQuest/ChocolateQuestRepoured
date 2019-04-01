package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeon;
import com.teamcqr.chocolatequestrepoured.dungeongen.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.dungeongen.Generators.VolcanoGenerator;

public class DungeonVolcano implements IDungeon{

	@Override
	public IDungeonGenerator getGenerator() {
		return new VolcanoGenerator();
	}

	@Override
	public String[] getBiomes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getAllowedDimensions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "volcano";
	}

	@Override
	public boolean ageBlocks() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean turnOutLights() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUnique() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getStructureFolderPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readCommonData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readSpecialData() {
		// TODO Auto-generated method stub
		
	}

}
