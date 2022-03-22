package team.cqr.cqrepoured.world.structure.generation.dungeons;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.loot.LootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRLoottables;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PropertyFileHelper;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.generators.GeneratorVegetatedCave;

import java.io.File;
import java.util.Properties;
import java.util.Random;

public class DungeonVegetatedCave extends DungeonBase {

	private BlockState vineBlock;
	private BlockState vineLatchBlock;
	private BlockState airBlock;
	private BlockState pumpkinBlock;
	private BlockState[] flowerBlocks;
	private BlockState[] mushrooms;
	private BlockState[] grassBlocks;
	private BlockState[] floorBlocks;
	private int centralCaveSize = 20;
	private int tunnelCountMin = 3;
	private int tunnelCountMax = 5;
	private int tunnelStartSize = 8;
	private int caveSegmentCount = 8;
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
		this.vineBlock = PropertyFileHelper.getBlockStateProperty(prop, "vineBlock", Blocks.VINE.defaultBlockState());
		// DONE: Add a non-cross-shape vine thing
		this.crossVine = (this.vineBlock.getBlock() instanceof VineBlock);
		this.airBlock = PropertyFileHelper.getBlockStateProperty(prop, "airBlock", Blocks.AIR.defaultBlockState());
		this.pumpkinBlock = PropertyFileHelper.getBlockStateProperty(prop, "lanternBlock", Blocks.JACK_O_LANTERN.defaultBlockState());
		this.flowerBlocks = PropertyFileHelper.getBlockStateArrayProperty(prop, "flowerBlocks", new BlockState[] { Blocks.POPPY.defaultBlockState(), Blocks.DANDELION.defaultBlockState() }, false);
		this.mushrooms = PropertyFileHelper.getBlockStateArrayProperty(prop, "mushroomBlocks", new BlockState[] { Blocks.BROWN_MUSHROOM.defaultBlockState(), Blocks.RED_MUSHROOM.defaultBlockState() }, false);
		this.floorBlocks = PropertyFileHelper.getBlockStateArrayProperty(prop, "floorBlocks", new BlockState[] { Blocks.GRASS.defaultBlockState() }, false);
		this.grassBlocks = PropertyFileHelper.getBlockStateArrayProperty(prop, "grassBlocks", new BlockState[] { Blocks.AIR.defaultBlockState() }, false);
		this.vineLengthModifier = Math.max(1, PropertyFileHelper.getDoubleProperty(prop, "vineLengthModifier", 2));
		this.vineChance = PropertyFileHelper.getIntProperty(prop, "vineChance", 20);
		this.placeVines = PropertyFileHelper.getBooleanProperty(prop, "placeVines", true);
		this.placeVegetation = PropertyFileHelper.getBooleanProperty(prop, "placeVegetation", true);
		this.placeBuilding = PropertyFileHelper.getBooleanProperty(prop, "placeBuilding", true);
		this.buildingFolder = PropertyFileHelper.getStructureFolderProperty(prop, "buildingFolder", "caves/swamp");
		this.centralCaveSize = PropertyFileHelper.getIntProperty(prop, "centralCaveSize", 15);
		this.tunnelCountMin = PropertyFileHelper.getIntProperty(prop, "tunnelCountMin", 3);
		this.tunnelCountMax = PropertyFileHelper.getIntProperty(prop, "tunnelCountMax", 5);
		this.caveSegmentCount = PropertyFileHelper.getIntProperty(prop, "caveSegmentCount", 8);
		this.vineLatchBlock = PropertyFileHelper.getBlockStateProperty(prop, "vineLatchBlock", Blocks.COBBLESTONE.defaultBlockState());
		this.tunnelStartSize = PropertyFileHelper.getIntProperty(prop, "tunnelStartSize", 10);
		this.chestIDs = PropertyFileHelper.getResourceLocationArrayProperty(prop, "chestIDs", new ResourceLocation[] { LootTables.ABANDONED_MINESHAFT, LootTables.NETHER_BRIDGE, CQRLoottables.CHESTS_FOOD }, false);
		this.skipCeilingFiltering = PropertyFileHelper.getBooleanProperty(prop, "skipCeilingFiltering", false);
	}

	@Override
	public AbstractDungeonGenerator<DungeonVegetatedCave> createDungeonGenerator(World world, int x, int y, int z, Random rand, DungeonDataManager.DungeonSpawnType spawnType) {
		return new GeneratorVegetatedCave(world, new BlockPos(x, y, z), this, rand);
	}

	public File getRandomCentralBuilding(Random rand) {
		return this.getStructureFileFromDirectory(this.buildingFolder, rand);
	}

	public BlockState getVineBlock() {
		return this.vineBlock;
	}

	public BlockState getFlowerBlock(Random rdm) {
		return this.flowerBlocks[rdm.nextInt(this.flowerBlocks.length)];
	}

	public BlockState getMushroomBlock(Random rdm) {
		return this.mushrooms[rdm.nextInt(this.mushrooms.length)];
	}

	public BlockState getFloorBlock(Random rdm) {
		return this.floorBlocks[rdm.nextInt(this.floorBlocks.length)];
	}

	public BlockState getGrassBlock(Random rdm) {
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

	public BlockState getAirBlock() {
		return this.airBlock;
	}

	public BlockState getPumpkinBlock() {
		return this.pumpkinBlock;
	}

	public BlockState getVineLatchBlock() {
		return this.vineLatchBlock;
	}

	public int getCentralCaveSize() {
		return this.centralCaveSize;
	}

	public int getTunnelCount(Random random) {
		return DungeonGenUtils.randomBetween(this.tunnelCountMin, this.tunnelCountMax, random);
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
