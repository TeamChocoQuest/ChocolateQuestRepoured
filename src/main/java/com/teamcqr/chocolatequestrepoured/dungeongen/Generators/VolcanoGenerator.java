package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class VolcanoGenerator implements IDungeonGenerator{

	/**
	 * Generate: Given height, given base radius, given top inner radius
	 * steepness: In % 
	 * level begins at 0!!
	 * Outer Radius --> y=(level * steepness) * level^2 --> y = steepness *level^3 --> RADIUS = baseRAD - (level/steepness)^1/3
	 * Inner Radius --> given, OR if (OuterRadius - (maxHeight - currHeight)) > given && (OuterRadius - (maxHeight - currHeight)) < maxInnerRad then use (OuterRadius - (maxHeight - currHeight))
	 * Block placing: generate circles. Per outer layer, the probability to place a block is reduced, same for height. A block can only be placed if it has a support block 
	 * probability for blocks: max rad +1: 0%    minRad -1: 100%  --> P(x) = 1- [ (x-MIN)/(MAX-MIN)  -steepNess * level]     x = radius of block -> distance to center - innerRadius
	 * 
	 * 
	 * Config Values:
	 *  > Steepness
	 *  > minRadius
	 *  > maxHeight/topY
	 *  
	 *  Calculate first: minY in affected region
	 *  difference between minY and topY --> height
	 *  
	 *  then: calculate base radius
	 *	baseRad = minRadius + ((maxHeight+1)/steepness)^1/3 
	 *
	 *
	 *	MOVE CALCULATION OF BASERADIUS TO THE DUNGEON OBJECT!!!!
	 * 
	 */
	
	private int baseRadius = 1;
	private int minY = 1;
	private int maxHeight = 10;
	private int minRadius = 1;
	private double steepness = 0.0D;
	private BlockPos centerLoc = null;
	
	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z) { 
		//X Y Z mark the C E N T E R / Middle of the crater!!!!
		this.centerLoc = new BlockPos(x, y, z);
		
		//1) Calculate MinY -> TODO
		//2) Calculate the base radius -> done
		//3) Create a boolean[][] for marking wether a block is supported or not -> done
		//4) calculate all block positions -> done
		//5) Create a new SimpleThread that places all blocks -> TODO
		//6) Place cover blocks -> calculate positions and let the thread place the blocks -> done
		//Builds the volcano and after that the spire
		
		boolean[][] supportStatus = new boolean[this.baseRadius *2][this.baseRadius *2];
		
		List<BlockPos> blocks = new ArrayList<BlockPos>();
		List<BlockPos> lava = new ArrayList<BlockPos>();
		
		//Calculates all the "wall" blocks
		//TODO Place lava
		for(int iY = 0; iY < ((minY + this.maxHeight) < 256 ? this.maxHeight : (255 - minY)); iY++) {
			//RADIUS = baseRAD - (level/steepness)^1/3
			int radiusOuter = new Double(this.baseRadius - Math.pow((iY/this.steepness), new Double(1/3))).intValue();
			int innerRadius = this.minRadius; //TODO calculate minRadius
			
			for(int iX = new Double(x - radiusOuter).intValue(); iX <= new Double(x + radiusOuter).intValue(); iX++) {
				for(int iZ = new Double(z - radiusOuter).intValue(); iZ <= new Double(z + radiusOuter).intValue(); iZ++) {
					//First check if it is within the base radius...
					if(isInsideCircle(iX, iZ, radiusOuter, centerLoc)) {
						//If it is at the bottom and also inside the inner radius -> lava
						if(isInsideCircle(iX, iZ, innerRadius, centerLoc)) {
							if(iY == 0) {
								lava.add(new BlockPos(iX, minY, iZ));
							}
						} else {
							//Else it is a wall block
							double r = Math.sqrt((iX * iX) + (iZ * iZ));
							double blockProb = 1- (( (r - innerRadius) / (radiusOuter - innerRadius)) - (this.steepness * iY));
							
							if(supportStatus[iX][iZ] || iY == 0) {
								if(DungeonGenUtils.PercentageRandom(blockProb, new Random().nextLong())) {
									if(iY > 0) {
										if(supportStatus[iX][iZ]) {
											//Add block to list
											blocks.add(new BlockPos(iX, iY + minY, iZ));
										}
									} else {
										supportStatus[iX][iZ] = true;
										//Add block to list
										blocks.add(new BlockPos(iX, iY + minY, iZ));
									}
									//The block is not supported or probability says no
								} else {
									supportStatus[iX][iZ] = false;
								}
							}
						}
					}
				}
			}
		}
		
		//TODO Pass the list to a simplethread to place the blocks
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		//Generates the stronghold
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z) {
		List<BlockPos> coverBlocks = new ArrayList<>();
		
		for(int iX = new Double(x - (this.baseRadius*1.25)).intValue(); iX <= new Double(x + (this.baseRadius*1.25)).intValue(); iX++) {
			for(int iZ = new Double(z - (this.baseRadius*1.25)).intValue(); iZ <= new Double(z + (this.baseRadius*1.25)).intValue(); iZ++) {
				coverBlocks.add(world.getTopSolidOrLiquidBlock(new BlockPos(iX, 0, iZ).add(0, 1, 0)));
			}
		}
		//TODO Pass the list to a simplethread to place the blocks
	}
	
	private void calculateBaseRadius() {
		
	}
	
	private void calculateMinY() {
		
	}
	
	private boolean isInsideCircle(int x, int z, int radius, BlockPos center) {
		int xR = x - center.getX();
		int zR = z - center.getZ();
		
		if((xR * xR + zR * zR) <= ((radius +1) * (radius +1))) {
			double divResX = ((double)xR) / ((double)radius);
			double divResZ = ((double)zR) / ((double)radius);
			
			if(divResX < 1.0D && divResZ < 1.0D) {
				return true;
			}
		}
		return false;
	}

}
