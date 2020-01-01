package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public interface IWallPart {

	int getTopY();

	void generateWall(int chunkX, int chunkZ, World world, Chunk chunk);

	default int getLowerY(World world, Chunk chunk) {
		int[] yValues = new int[256];
		int index = 0;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int y = world.getTopSolidOrLiquidBlock(new BlockPos((chunk.x * 16) + x, 0, (chunk.z * 16) + z)).getY();
				yValues[index] = y;
				index++;
			}
		}
		int lowestY = yValues[0];
		for (int i = 1; i < yValues.length; i++) {
			int y = yValues[i];
			if (y < lowestY) {
				lowestY = y;
			}
		}
		if (lowestY <= 10) {
			lowestY = 3;
		} else {
			lowestY -= 6;
		}
		return lowestY;
	}

}
