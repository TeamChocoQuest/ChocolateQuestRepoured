package com.teamcqr.chocolatequestrepoured.dungeongen.thewall.wallparts;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

class WallPartWall implements IWallPart {

	public WallPartWall() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getTopY() {
		return Reference.CONFIG_HELPER.getWallTopY() - 7;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk) {
		// TODO Auto-generated method stub
		
	}

}
