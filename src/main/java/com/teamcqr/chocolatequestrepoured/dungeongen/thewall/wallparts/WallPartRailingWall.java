package com.teamcqr.chocolatequestrepoured.dungeongen.thewall.wallparts;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WallPartRailingWall implements IWallPart {

	public WallPartRailingWall() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getTopY() {
		return Reference.CONFIG_HELPER.getWallTopY() - 12;
	}

	@Override
	public void generateWall(int chunkX, int chunkZ, World world, Chunk chunk) {
		// TODO Auto-generated method stub
		
	}

}
