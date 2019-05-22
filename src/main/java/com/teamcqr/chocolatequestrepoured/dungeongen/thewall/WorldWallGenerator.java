package com.teamcqr.chocolatequestrepoured.dungeongen.thewall;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldWallGenerator implements IWorldGenerator {

	public WorldWallGenerator() {
		
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if(world.provider.getDimension() == 0) {
			if(Reference.CONFIG_HELPER.buildWall() && chunkZ < 0 && Math.abs(chunkZ) >= (Reference.CONFIG_HELPER.getWallSpawnDistance() +6)) {
				//GENERATE THE WALL
			}
		}
	}

}
