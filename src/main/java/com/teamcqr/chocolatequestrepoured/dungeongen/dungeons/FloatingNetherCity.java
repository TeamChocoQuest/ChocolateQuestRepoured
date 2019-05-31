package com.teamcqr.chocolatequestrepoured.dungeongen.dungeons;

import java.io.File;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class FloatingNetherCity extends ClassicNetherCity {

	private int minBuildings = 6;
	private int maxBuildings = 12;
	private int minIslandDistance = 15;
	private int maxIslandDistance = 30;
	private Block islandMaterial = Blocks.NETHERRACK;
	private Block chainBlock = Blocks.OBSIDIAN;
	private Block bridgeBlock = Blocks.NETHER_BRICK;
	private int bridgeChance = 20;
	private boolean buildChains = true;
	private boolean buildBridges = false;
	
	public FloatingNetherCity(File configFile) {
		super(configFile);
	}
	
	//Generator: Radius of the island circle is the longer side (x or z) of the structure to spawn!!
	//IMPORTANT: Calculate the "center" of the structure and begin to draw the circle from there
	
	//Code i used in a plugin to generate a round platform.... i may recycle parts of it....
	//On each step downwards, the radius gets smaller by decrement, decrement increases per step down and it starts by 1
	//It stops going down, when decremt *2 is greater than or equals the current decremented radius
	/**private int radius;
	@SuppressWarnings("unused")
	private int platformThickness;
	
	public WorldGenEndIsland(int r, int pt) {
		this.radius = r;
		this.platformThickness = pt;
	}

	@Override
	public boolean generate(World world, Random random, BlockPosition startPos) {
		float rdmFloat = (float) (random.nextInt(3) + 4);

		for (int y = 0; rdmFloat > 0.5F; --y) {
			for (int x = MathHelper.d(-rdmFloat); x <= MathHelper.f(rdmFloat); ++x) {
				for (int z = MathHelper.d(-rdmFloat); z <= MathHelper.f(rdmFloat); ++z) {
					if ((float) (x * x + z * z) <= (rdmFloat + 1.0F) * (rdmFloat + 1.0F)) {
						if(isAllowed(x) && isAllowed(z)) {
							this.a(world, startPos.a(x, y, z), Blocks.END_STONE.getBlockData());
						}
					}
				}
			}

			rdmFloat = (float) ((double) rdmFloat - ((double) random.nextInt(2) + 0.5D));
		}

		return true;
	}
	
	private boolean isAllowed(int distanceToCenter) {
		double divisionResult = ((double)distanceToCenter) / ((double)this.radius);
		if(divisionResult < 1.0D) {
			return true;
		}
		return false;
	}**/
	
	public boolean doBuildChains() {
		return this.buildChains;
	}
	public boolean doBuildBridges() {
		return this.buildBridges;
	}
	public int getBridgeChance() {
		return this.bridgeChance;
	}
	public int getBuildingCount(Random random) {
		return DungeonGenUtils.getIntBetweenBorders(this.minBuildings, this.maxBuildings, random);
	}
	public int getIslandDistance() {
		return DungeonGenUtils.getIntBetweenBorders(this.minIslandDistance, this.maxIslandDistance, new Random());
	}
	public Block getChainBlock() {
		return this.chainBlock;
	}
	public Block getIslandBlock() {
		return this.islandMaterial;
	}
	public Block getBridgeBlock() {
		return this.bridgeBlock;
	}

}
