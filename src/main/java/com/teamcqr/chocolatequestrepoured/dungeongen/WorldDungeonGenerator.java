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
		//System.out.println("Trying to generate dungeon at ChunkX=" + chunkX + " ChunkZ=" + chunkZ + "...");
		if(DungeonGenUtils.getLocSpecDungeonsForChunk(chunkX, chunkZ, world) != null && DungeonGenUtils.getLocSpecDungeonsForChunk(chunkX, chunkZ, world).size() > 0) {
			System.out.println("Found location specific Dungeons for ChunkX=" + chunkX + " ChunkZ=" + chunkZ + "!");
			for(DungeonBase dungeon : DungeonGenUtils.getLocSpecDungeonsForChunk(chunkX, chunkZ, world)) {
				boolean dimensionIsOK = false;
				//Here we also need to check if the dimension fits
				for(int dimID : dungeon.getAllowedDimensions()) {
					if(world.provider.getDimension() == dimID) {
						dimensionIsOK = true;
						break;
					}
				}
				
				if(dimensionIsOK) {
					Random rdm = new Random();
					rdm.setSeed(getSeed(world, chunkX, chunkZ));
					dungeon.generate(dungeon.getLockedPos().getX(), dungeon.getLockedPos().getZ(), world, world.getChunkFromChunkCoords(chunkX, chunkZ), rdm);
				}
				
			}
		} else if(DungeonGenUtils.PercentageRandom(this.dungeonRegistry.getDungeonSpawnChance(), world.getSeed())) {
			//Now check if any dungeons exist for this biome....
			Biome biome = world.getBiomeProvider().getBiome(new BlockPos(chunkX *16 +1, 100, chunkZ *16 +1));
			//System.out.println("Searching dungeons for biome " + biome.getBiomeName() + "...");
			//No Dungeons for this biome -> ragequit
			if(this.dungeonRegistry.getDungeonsForBiome(biome).isEmpty()) {
				//System.out.println("No dungeons for biome " + biome.getBiomeName() + "!");
				return;
			} else {
				//System.out.println("Found " + this.dungeonRegistry.getDungeonsForBiome(biome).size() + "dungeons for biome " + biome.getBiomeName() + "!");
				//System.out.println("Checking location...");
			}
			
			//Now check if the dungeon is far away enough from the last one
			if(chunkX % this.dungeonRegistry.getDungeonDistance() == 0 && chunkZ % this.dungeonRegistry.getDungeonDistance() == 0 && DungeonGenUtils.isFarAwayEnoughFromSpawn(chunkX, chunkZ)) {
				//System.out.println("Chunks are far away enough from last dungeon and from spawn!");
				Random rdm = new Random();
				rdm.setSeed(world.getSeed());
				
				if(DungeonGenUtils.isFarAwayEnoughFromLocationSpecifics(chunkX, chunkZ, world) || this.dungeonRegistry.getCoordinateSpecificsMap().isEmpty()) {
					System.out.println("Location is fine! Choosing dungeon...");
					int strctrIndex = rdm.nextInt(this.dungeonRegistry.getDungeonsForBiome(biome).size());
					DungeonBase chosenStructure = this.dungeonRegistry.getDungeonsForBiome(biome).get(strctrIndex);
					System.out.println("Chose dungeon " + chosenStructure.getDungeonName() + "! Calculating chance...");
					
					if(DungeonGenUtils.PercentageRandom(chosenStructure.getSpawnChance(), world.getSeed())) {
						boolean dimensionIsOK = false;
						for(int dimID : chosenStructure.getAllowedDimensions()) {
							if(world.provider.getDimension() == dimID) {
								dimensionIsOK = true;
								break;
							}
						}
						if(dimensionIsOK) {
							System.out.println("Generating dungeon " + chosenStructure.getDungeonName() + " at chunkX=" + chunkX + "  chunkZ=" + chunkZ);
							//DONE: Choose a structure and build it --> Dungeon handles it self!
							Random rdmGen = new Random();
							rdm.setSeed(getSeed(world, chunkX, chunkZ));
							chosenStructure.generate(chunkX *16 +1, chunkZ *16 +1, world, world.getChunkFromChunkCoords(chunkX, chunkZ), rdmGen);
							//TODO: Check if dungeon is unique or every structure should generate once and then check if dungeon is already present
						}
					}
				}
			}
		}
	}
	
	private static long getSeed(World world, int chunkX, int chunkZ) {
	    long mix = xorShift64(chunkX) + Long.rotateLeft(xorShift64(chunkZ), 32) + -1094792450L;
	    long result = xorShift64(mix);
	    
	    return world.getSeed() + result;
	  }
	  
	  private static long xorShift64(long x) {
	    x ^= x << 21;
	    x ^= x >>> 35;
	    x ^= x << 4;
	    return x;
	  }

}
