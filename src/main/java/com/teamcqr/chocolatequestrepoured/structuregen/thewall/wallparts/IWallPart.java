package com.teamcqr.chocolatequestrepoured.structuregen.thewall.wallparts;

import com.teamcqr.chocolatequestrepoured.structuregen.generation.DungeonGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 23.05.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public interface IWallPart {

	int getTopY();

	void generateWall(int chunkX, int chunkZ, World world, Chunk chunk, DungeonGenerator dungeonGenerator);

	default int getBottomY(World world, int x1, int z1) {
		int lowestY = this.getTopY() - 16;
		for (int x2 = 0; x2 < 16; x2++) {
			for (int z2 = 0; z2 < 16; z2++) {
				int y = DungeonGenUtils.getYForPos(world, x1 + x2, z1 + z2, true);
				if (y < lowestY) {
					lowestY = y;
				}
			}
		}
		return Math.max(lowestY - 6, 1);
	}

}
