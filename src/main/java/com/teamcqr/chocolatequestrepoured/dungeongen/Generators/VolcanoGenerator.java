package com.teamcqr.chocolatequestrepoured.dungeongen.Generators;

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
	 * Outer Radius --> (level * steepness) * level^2    RADIUS = baseRAD - (level/steepness)^1/3
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
		// TODO Auto-generated method stub
		//Builds the volcano and after that the spire
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
		// TODO Auto-generated method stub
		
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
