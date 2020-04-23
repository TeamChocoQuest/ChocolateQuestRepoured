package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class DungeonSwampCave extends DungeonBase {
	
	private Block vineBlock;
	private Block[] flowerBlocks;
	private Block[] mushrooms;
	private Block[] floorBlocks;
	private boolean placeVines;
	private boolean placeVegetation;
	private boolean placeBuilding;
	private File buildingFolder;
	private int size;
	private int height;
	
	public DungeonSwampCave(String name, Properties prop) {
		super(name, prop);
		this.vineBlock = PropertyFileHelper.getBlockProperty(prop, "vineBlock", Blocks.VINE);
		this.flowerBlocks = PropertyFileHelper.getBlockArrayProperty(prop, "flowerBlocks", new Block[] {
			Blocks.RED_FLOWER,
			Blocks.YELLOW_FLOWER
		});
		this.mushrooms = PropertyFileHelper.getBlockArrayProperty(prop, "mushroomBlocks", new Block[] {
			Blocks.BROWN_MUSHROOM,
			Blocks.RED_MUSHROOM
		});
		this.floorBlocks = PropertyFileHelper.getBlockArrayProperty(prop, "floorBlocks", new Block[] {
			Blocks.GRASS,
			Blocks.GRASS,
			Blocks.MOSSY_COBBLESTONE,
		});
		this.placeVines = PropertyFileHelper.getBooleanProperty(prop, "placeVines", true);
		this.placeVegetation = PropertyFileHelper.getBooleanProperty(prop, "placeVegetation", true);
		this.placeBuilding = PropertyFileHelper.getBooleanProperty(prop, "placeBuilding", true);
		this.size = PropertyFileHelper.getIntProperty(prop, "size", 72);
		this.height = PropertyFileHelper.getIntProperty(prop, "height", 40);
		this.buildingFolder = PropertyFileHelper.getFileProperty(prop, "buildingFolder", "caves/swamp");
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		// TODO Auto-generated method stub

	}
	
	public File getRandomCentralBuilding() {
		return getStructureFileFromDirectory(buildingFolder);
	}
	
	public int getSize() {
		return size;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Block getVineBlock() {
		return vineBlock;
	}
	
	public Block getFlowerBlock(Random rdm) {
		return flowerBlocks[rdm.nextInt(flowerBlocks.length)];
	}
	
	public Block getMushroomBlock(Random rdm) {
		return mushrooms[rdm.nextInt(mushrooms.length)];
	}
	
	public Block getFloorBlock(Random rdm) {
		return floorBlocks[rdm.nextInt(floorBlocks.length)];
	}
	
	public boolean placeVegetation() {
		return placeVegetation;
	}
	
	public boolean placeBuilding() {
		return placeBuilding;
	}
	
	public boolean placeVines() {
		return placeVines;
	}

}
