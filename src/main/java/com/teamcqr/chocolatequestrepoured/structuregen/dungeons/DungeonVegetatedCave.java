package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModLoottables;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorVegetatedCave;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class DungeonVegetatedCave extends DungeonBase {

	private Block vineBlock;
	private Block vineLatchBlock;
	private Block airBlock;
	private Block pumpkinBlock;
	private Block[] flowerBlocks;
	private Block[] mushrooms;
	private Block[] grassBlocks;
	private Block[] floorBlocks;
	private int centralCaveSize = 20;
	private int tunnelCountMin = 3;
	private int tunnelCountMax = 5;
	private int tunnelStartSize = 8;
	private int caveSegmentCount = 8;
	private int posY = 30;
	private int vineChance = 20;
	private ResourceLocation[] chestIDs;
	private double vineLengthModifier = 2;
	private boolean placeVines;
	private boolean crossVine;
	private boolean placeVegetation;
	private boolean placeBuilding;
	private boolean skipCeilingFiltering = false;
	private File buildingFolder;

	public DungeonVegetatedCave(String name, Properties prop) {
		super(name, prop);
		this.vineBlock = PropertyFileHelper.getBlockProperty(prop, "vineBlock", Blocks.VINE);
		// DONE: Add a non-cross-shape vine thing
		this.crossVine = (this.vineBlock instanceof BlockVine);
		this.airBlock = PropertyFileHelper.getBlockProperty(prop, "airBlock", Blocks.AIR);
		this.pumpkinBlock = PropertyFileHelper.getBlockProperty(prop, "lanternBlock", Blocks.LIT_PUMPKIN);
		this.flowerBlocks = PropertyFileHelper.getBlockArrayProperty(prop, "flowerBlocks", new Block[] { Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER });
		this.mushrooms = PropertyFileHelper.getBlockArrayProperty(prop, "mushroomBlocks", new Block[] { Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM });
		this.floorBlocks = PropertyFileHelper.getBlockArrayProperty(prop, "floorBlocks", new Block[] { Blocks.GRASS, });
		this.grassBlocks = PropertyFileHelper.getBlockArrayProperty(prop, "grassBlocks", new Block[] { Blocks.AIR, });
		this.vineLengthModifier = Math.max(1, PropertyFileHelper.getDoubleProperty(prop, "vineLengthModifier", 2));
		this.vineChance = PropertyFileHelper.getIntProperty(prop, "vineChance", 20);
		this.placeVines = PropertyFileHelper.getBooleanProperty(prop, "placeVines", true);
		this.placeVegetation = PropertyFileHelper.getBooleanProperty(prop, "placeVegetation", true);
		this.placeBuilding = PropertyFileHelper.getBooleanProperty(prop, "placeBuilding", true);
		this.buildingFolder = PropertyFileHelper.getFileProperty(prop, "buildingFolder", "caves/swamp");
		this.centralCaveSize = PropertyFileHelper.getIntProperty(prop, "centralCaveSize", 15);
		this.posY = PropertyFileHelper.getIntProperty(prop, "posY", 30);
		this.tunnelCountMin = PropertyFileHelper.getIntProperty(prop, "tunnelCountMin", 3);
		this.tunnelCountMax = PropertyFileHelper.getIntProperty(prop, "tunnelCountMax", 5);
		this.caveSegmentCount = PropertyFileHelper.getIntProperty(prop, "caveSegmentCount", 8);
		this.vineLatchBlock = PropertyFileHelper.getBlockProperty(prop, "vineLatchBlock", Blocks.COBBLESTONE);
		this.tunnelStartSize = PropertyFileHelper.getIntProperty(prop, "tunnelStartSize", 10);
		this.chestIDs = PropertyFileHelper.getResourceLocationArrayProperty(prop, "chestIDs", new ResourceLocation[] { LootTableList.CHESTS_ABANDONED_MINESHAFT, LootTableList.CHESTS_NETHER_BRIDGE, ModLoottables.CHESTS_FOOD });
		this.skipCeilingFiltering = PropertyFileHelper.getBooleanProperty(prop, "skipCeilingFiltering", false);
	}

	@Override
	public AbstractDungeonGenerator<DungeonVegetatedCave> createDungeonGenerator(World world, int x, int y, int z) {
		return new GeneratorVegetatedCave(world, new BlockPos(x, y, z), this);
	}

	@Override
	public void generate(World world, int x, int z) {
		this.generate(world, x, this.posY, z);
	}

	public File getRandomCentralBuilding() {
		return this.getStructureFileFromDirectory(this.buildingFolder);
	}

	public Block getVineBlock() {
		return this.vineBlock;
	}

	public Block getFlowerBlock(Random rdm) {
		return this.flowerBlocks[rdm.nextInt(this.flowerBlocks.length)];
	}

	public Block getMushroomBlock(Random rdm) {
		return this.mushrooms[rdm.nextInt(this.mushrooms.length)];
	}

	public Block getFloorBlock(Random rdm) {
		return this.floorBlocks[rdm.nextInt(this.floorBlocks.length)];
	}

	public Block getGrassBlock(Random rdm) {
		return this.grassBlocks[rdm.nextInt(this.grassBlocks.length)];
	}

	public boolean placeVegetation() {
		return this.placeVegetation;
	}

	public boolean placeBuilding() {
		return this.placeBuilding;
	}

	public boolean isVineShapeCross() {
		return this.crossVine;
	}

	public boolean placeVines() {
		return this.placeVines;
	}

	public boolean skipCeilingFiltering() {
		return this.skipCeilingFiltering;
	}

	public Block getAirBlock() {
		return this.airBlock;
	}

	public Block getPumpkinBlock() {
		return this.pumpkinBlock;
	}

	public Block getVineLatchBlock() {
		return this.vineLatchBlock;
	}

	public int getCentralCaveSize() {
		return this.centralCaveSize;
	}

	public int getTunnelCount(Random random) {
		return DungeonGenUtils.getIntBetweenBorders(this.tunnelCountMin, this.tunnelCountMax, random);
	}

	public int getCaveSegmentCount() {
		return this.caveSegmentCount;
	}

	public ResourceLocation[] getChestIDs() {
		return this.chestIDs;
	}

	public int getTunnelStartSize() {
		return this.tunnelStartSize;
	}

	public double getVineLengthModifier() {
		return this.vineLengthModifier;
	}

	public int getVineChance() {
		return this.vineChance;
	}

}
