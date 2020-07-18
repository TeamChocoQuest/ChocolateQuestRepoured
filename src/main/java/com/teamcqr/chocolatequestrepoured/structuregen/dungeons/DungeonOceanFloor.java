package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class DungeonOceanFloor extends DungeonSurface {

	public DungeonOceanFloor(String name, Properties prop) {
		super(name, prop);
		this.useCoverBlock = false;
	}

	@Override
	public void generate(World world, int x, int z) {
		Chunk chunk = world.getChunk(x >> 4, z >> 4);
		int y = 0;
		for (int ix = 0; ix < 16; ix++) {
			for (int iz = 0; iz < 16; iz++) {
				y += DungeonGenUtils.getYForPos(world, chunk.x * 16 + ix, chunk.z * 16 + iz, true);
			}
		}
		y >>= 8;
		y -= this.getUnderGroundOffset();
		y += this.getYOffset();
		this.generate(world, x, y, z);
	}

}
