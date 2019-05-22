package com.teamcqr.chocolatequestrepoured.dungeongen.thewall;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.dungeongen.thewall.wallparts.IWallPart;
import com.teamcqr.chocolatequestrepoured.dungeongen.thewall.wallparts.WallPartRailingTower;
import com.teamcqr.chocolatequestrepoured.dungeongen.thewall.wallparts.WallPartRailingWall;
import com.teamcqr.chocolatequestrepoured.dungeongen.thewall.wallparts.WallPartTower;
import com.teamcqr.chocolatequestrepoured.dungeongen.thewall.wallparts.WallPartWall;
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
				IWallPart wallPart = null;
				IWallPart railingPart = null;
				//GENERATE THE WALL
				if(chunkX % Reference.CONFIG_HELPER.getWallTowerDistance() == 0) {
					//Build tower
					wallPart = new WallPartTower();
					railingPart = new WallPartRailingTower();
				} else {
					//Build wall
					wallPart = new WallPartWall();
					railingPart = new WallPartRailingWall();
				}
				if(wallPart != null) {
					wallPart.generateWall(chunkX, chunkZ, world, world.getChunkFromChunkCoords(chunkX, chunkZ));
				}
				if(railingPart != null) {
					railingPart.generateWall(chunkX, chunkZ, world, world.getChunkFromChunkCoords(chunkX, chunkZ));
				}
			}
		}
	}

}
