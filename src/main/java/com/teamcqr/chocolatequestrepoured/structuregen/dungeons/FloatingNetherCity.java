package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.NetherCityHangingGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class FloatingNetherCity extends DungeonBase {

	private int minBuildings = 6;
	private int maxBuildings = 12;
	private int minIslandDistance = 15;
	private int maxIslandDistance = 30;
	private int yFactorHeight = 20;
	private int heightVariation = 10;
	private boolean digAirCave = true;
	private Block islandMaterial = Blocks.NETHERRACK;
	private Block chainBlock = Blocks.OBSIDIAN;
	// private Block bridgeBlock = Blocks.NETHER_BRICK;
	// private int bridgeChance = 20;
	private int posY = 50; // lava level is 32 in the nether
	private boolean buildChains = true;
	// private boolean buildBridges = false;
	private File structureFolder;
	private File centralStructureFolder;

	public FloatingNetherCity(File configFile) {
		super(configFile);
		Properties prop = this.loadConfig(configFile);
		if (prop != null) {
			this.minBuildings = PropertyFileHelper.getIntProperty(prop, "minBuildings", 6);
			this.maxBuildings = PropertyFileHelper.getIntProperty(prop, "maxBuildings", 12);
			this.minIslandDistance = PropertyFileHelper.getIntProperty(prop, "minIslandDistance", 15);
			this.maxIslandDistance = PropertyFileHelper.getIntProperty(prop, "maxIslandDistance", 30);
			this.yFactorHeight = PropertyFileHelper.getIntProperty(prop, "islandFloorCeilingsDistance", 20);
			this.heightVariation = PropertyFileHelper.getIntProperty(prop, "islandHeightVariation", 10);
			this.posY = PropertyFileHelper.getIntProperty(prop, "yPosition", 50);

			this.digAirCave = PropertyFileHelper.getBooleanProperty(prop, "digAirCave", true);
			this.buildChains = PropertyFileHelper.getBooleanProperty(prop, "buildChains", true);

			this.structureFolder = PropertyFileHelper.getFileProperty(prop, "structureFolder", "floatingCity/islands");
			this.centralStructureFolder = PropertyFileHelper.getFileProperty(prop, "centralStructureFolder", "floatingCity/centers");

			this.islandMaterial = PropertyFileHelper.getBlockProperty(prop, "islandBlock", Blocks.NETHERRACK);
			this.chainBlock = PropertyFileHelper.getBlockProperty(prop, "chainBlock", Blocks.OBSIDIAN);

			this.registeredSuccessful = true;

			this.closeConfigFile();
		} else {
			this.registeredSuccessful = false;
		}
	}

	@Override
	public void generate(BlockPos pos, World world) {
		System.out.println("Generating structure " + this.name + " at X: " + pos.getX() + "  Y: " + pos.getY() + "  Z: " + pos.getZ() + "  ...");
		this.getGenerator().generate(world, world.getChunkFromBlockCoords(pos), pos.getX(), pos.getY(), pos.getZ());
		System.out.println("Generated structure " + this.name + " at X: " + pos.getX() + "  Y: " + pos.getY() + "  Z: " + pos.getZ() + "!");
	}

	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		System.out.println("Generating structure " + this.name + " at X: " + x + "  Y: " + this.posY + "  Z: " + z + "  ...");
		this.getGenerator().generate(world, chunk, x, this.posY, z);
		System.out.println("Generated structure " + this.name + " at X: " + x + "  Y: " + this.posY + "  Z: " + z + "!");
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new NetherCityHangingGenerator(this);
	}

	// Generator: Radius of the island circle is the longer side (x or z) -1 of the structure to spawn!!
	// IMPORTANT: Calculate the "center" of the structure and begin to draw the circle from there

	// Code i used in a plugin to generate a round platform.... i may recycle parts of it....
	// On each step downwards, the radius gets smaller by decrement, decrement increases per step down and it starts by 1
	// It stops going down, when decremt *2 is greater than the current decremented radius
	/**
	 * private int radius; @SuppressWarnings("unused") private int platformThickness;
	 * 
	 * public WorldGenEndIsland(int r, int pt) { this.radius = r; this.platformThickness = pt; }
	 * 
	 * @Override public boolean generate(World world, Random random, BlockPosition startPos) { float rdmFloat = (float) (random.nextInt(3) + 4);
	 * 
	 *           for (int y = 0; rdmFloat > 0.5F; --y) { for (int x = MathHelper.d(-rdmFloat); x <= MathHelper.f(rdmFloat); ++x) { for (int z = MathHelper.d(-rdmFloat); z <= MathHelper.f(rdmFloat); ++z) { if ((float) (x * x + z * z) <=
	 *           (rdmFloat + 1.0F) * (rdmFloat + 1.0F)) { if(isAllowed(x) && isAllowed(z)) { this.a(world, startPos.a(x, y, z), Blocks.END_STONE.getBlockData()); } } } }
	 * 
	 *           rdmFloat = (float) ((double) rdmFloat - ((double) random.nextInt(2) + 0.5D)); }
	 * 
	 *           return true; }
	 * 
	 *           private boolean isAllowed(int distanceToCenter) { double divisionResult = ((double)distanceToCenter) / ((double)this.radius); if(divisionResult < 1.0D) { return true; } return false; }
	 **/

	public File pickStructure() {
		if (this.structureFolder == null) {
			return null;
		}
		return this.getStructureFileFromDirectory(this.structureFolder);
	}

	public File pickCentralStructure() {
		if (this.centralStructureFolder == null) {
			return null;
		}
		return this.getStructureFileFromDirectory(this.centralStructureFolder);
	}

	public boolean doBuildChains() {
		return this.buildChains;
	}

	/*
	 * public boolean doBuildBridges() { return this.buildBridges; } public int getBridgeChance() { return this.bridgeChance; }
	 */
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

	/*
	 * public Block getBridgeBlock() { return this.bridgeBlock; }
	 */
	public int getPosY() {
		return this.posY;
	}

	public int getMinIslandDistance() {
		return this.minIslandDistance;
	}

	public int getMaxIslandDistance() {
		return this.maxIslandDistance;
	}

	public int getYFactorHeight() {
		return this.yFactorHeight;
	}

	public boolean digAirCave() {
		return this.digAirCave;
	}

	public int getRandomHeightVariation() {
		if (this.heightVariation != 0) {
			int var = Math.abs(this.heightVariation);
			int rvar = DungeonGenUtils.getIntBetweenBorders(0, var);
			return var / 2 - rvar;
		}
		return 0;
	}

}
