package com.teamcqr.chocolatequestrepoured.dungeongen;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldDungeonGenerator implements IWorldGenerator {

	private DungeonRegistry dungeonRegistry;
	
	public WorldDungeonGenerator() {
		this.dungeonRegistry = CQRMain.dungeonRegistry;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		//Check if Dugneon "can" spawn (decided via a chance)
		if(DungeonGenUtils.PercentageRandom(this.dungeonRegistry.getDungeonSpawnChance(), world.getSeed())) {
			//Now check if any dungeons exist for this biome....
			Biome biome = world.getBiomeProvider().getBiome(new BlockPos(chunkX *16 +1, 100, chunkZ *16 +1));
			//No Dungeons for this biome -> ragequit
			if(this.dungeonRegistry.getDungeonsForBiome(biome).isEmpty()) {
				return;
			}
			
			//Now check if the dungeon is far away enough from the last one
			if(chunkX % this.dungeonRegistry.getDungeonDistance() == 0 && chunkZ % this.dungeonRegistry.getDungeonDistance() == 0) {
				Random rdm = new Random();
				rdm.setSeed(world.getSeed());
				
				int strctrIndex = rdm.nextInt(this.dungeonRegistry.getDungeonsForBiome(biome).size());
				DungeonBase chosenStructure = this.dungeonRegistry.getDungeonsForBiome(biome).get(strctrIndex);
				
				if(DungeonGenUtils.PercentageRandom(chosenStructure.getSpawnChance(), world.getSeed())) {
					boolean dimensionIsOK = false;
					for(int dimID : chosenStructure.getAllowedDimensions()) {
						if(world.provider.getDimension() == dimID) {
							dimensionIsOK = true;
							break;
						}
					}
					if(dimensionIsOK) {
						//DONE: Choose a structure and build it --> Dungeon handles it self!
						chosenStructure.generate(chunkX *16 +1, chunkZ *16 +1, world, world.getChunkFromChunkCoords(chunkX, chunkZ));
						//TODO: Check if dungeon is unique or every structure should generate once and then check if dungeon is already present
					}
				}
			}
		}
	}

}
