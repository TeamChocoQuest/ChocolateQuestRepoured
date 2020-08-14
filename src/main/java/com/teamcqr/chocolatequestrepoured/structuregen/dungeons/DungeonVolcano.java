package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.init.ModLoottables;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.GeneratorVolcano;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonVolcano extends DungeonBase {

	// For smoke: https://github.com/Tropicraft/Tropicraft/blob/1.12.2/src/main/java/net/tropicraft/core/common/block/tileentity/TileEntityVolcano.java
	private boolean buildStairwell = true;
	private boolean buildDungeon = false;
	private boolean damagedVolcano = true;
	private boolean ores = true;
	private int oreConcentration = 5;
	private int maxHoleSize = 9;
	private int minHeight = 100;
	private int maxHeight = 130;
	private int innerRadius = 6;
	private int chestChance = 600;
	private ResourceLocation[] chestIDs;
	private double steepness = 0.075D;
	private double lavaChance = 0.005D;
	private double magmaChance = 0.1;
	private String rampMobName = "minecraft:zombie";
	private IBlockState stoneBlock = Blocks.STONE.getDefaultState();
	private IBlockState lavaBlock = Blocks.LAVA.getDefaultState();
	private IBlockState magmaBlock = Blocks.MAGMA.getDefaultState();
	private IBlockState rampBlock = Blocks.NETHERRACK.getDefaultState();
	private IBlockState lowerStoneBlock = Blocks.COBBLESTONE.getDefaultState();
	private IBlockState pillarBlock = ModBlocks.GRANITE_LARGE.getDefaultState();
	private IBlockState[] oreBlocks = {};

	// Stronghold
	private int minStrongholdFloors = 3;
	private int maxStrongholdFloors = 5;
	private int strongholdSideLength = 3;
	private int minStrongholdRooms = 26;
	private int maxStrongholdRooms = 32;
	private int roomSizeX = 15;
	private int roomSizeY = 10;
	private int roomSizeZ = 15;
	private File curveENFolder;
	private File curveNEFolder;
	private File curveSEFolder;
	private File curveESFolder;
	private File curveWSFolder;
	private File curveSWFolder;
	private File curveNWFolder;
	private File curveWNFolder;
	private File hallSNFolder;
	private File hallNSFolder;
	private File hallWEFolder;
	private File hallEWFolder;
	private File stairNFolder;
	private File stairEFolder;
	private File stairSFolder;
	private File stairWFolder;
	private File bossFolder;

	public DungeonVolcano(String name, Properties prop) {
		super(name, prop);

		this.buildStairwell = PropertyFileHelper.getBooleanProperty(prop, "buildPath", true);
		this.buildDungeon = PropertyFileHelper.getBooleanProperty(prop, "buildDungeon", true);
		this.minHeight = PropertyFileHelper.getIntProperty(prop, "minHeight", 100);
		this.maxHeight = PropertyFileHelper.getIntProperty(prop, "maxHeight", 130);
		this.innerRadius = PropertyFileHelper.getIntProperty(prop, "innerRadius", 5);
		this.lavaChance = Math.abs(Double.valueOf(prop.getProperty("lavaChance", "0.005")));
		this.magmaChance = Math.abs(Double.valueOf(prop.getProperty("magmaChance", "0.1")));
		this.steepness = Math.abs(Double.valueOf(prop.getProperty("steepness", "0.075")));
		this.damagedVolcano = PropertyFileHelper.getBooleanProperty(prop, "damagedVolcano", true);
		this.chestChance = PropertyFileHelper.getIntProperty(prop, "chestChance", 600);
		this.maxHoleSize = Math.max(Math.abs(PropertyFileHelper.getIntProperty(prop, "maxHoleSize", 9)), 2);
		this.ores = PropertyFileHelper.getBooleanProperty(prop, "ores", true);
		this.oreConcentration = Math.min(Math.max(1, Math.abs(PropertyFileHelper.getIntProperty(prop, "orechance", 5))), 100);
		this.rampMobName = prop.getProperty("rampMob", "minecraft:zombie");

		this.chestIDs = PropertyFileHelper.getResourceLocationArrayProperty(prop, "chestIDs", new ResourceLocation[] { LootTableList.CHESTS_ABANDONED_MINESHAFT, LootTableList.CHESTS_NETHER_BRIDGE, ModLoottables.CHESTS_FOOD });
		this.stoneBlock = PropertyFileHelper.getBlockStateProperty(prop, "topBlock", Blocks.STONE.getDefaultState());
		this.lowerStoneBlock = PropertyFileHelper.getBlockStateProperty(prop, "lowerBlock", Blocks.COBBLESTONE.getDefaultState());
		this.lavaBlock = PropertyFileHelper.getBlockStateProperty(prop, "lavaBlock", Blocks.LAVA.getDefaultState());
		this.magmaBlock = PropertyFileHelper.getBlockStateProperty(prop, "magmaBlock", Blocks.MAGMA.getDefaultState());
		this.rampBlock = PropertyFileHelper.getBlockStateProperty(prop, "rampBlock", Blocks.NETHERRACK.getDefaultState());
		this.pillarBlock = PropertyFileHelper.getBlockStateProperty(prop, "pillarBlock", ModBlocks.GRANITE_LARGE.getDefaultState());
		this.oreBlocks = PropertyFileHelper.getBlockStateArrayProperty(prop, "oreBlocks", new IBlockState[] { Blocks.COAL_ORE.getDefaultState(), Blocks.IRON_ORE.getDefaultState(), Blocks.GOLD_BLOCK.getDefaultState(), Blocks.EMERALD_ORE.getDefaultState(), Blocks.REDSTONE_ORE.getDefaultState(), Blocks.DIAMOND_ORE.getDefaultState() });

		// Stronghold
		this.minStrongholdFloors = PropertyFileHelper.getIntProperty(prop, "minStrongholdFloors", 3);
		this.maxStrongholdFloors = PropertyFileHelper.getIntProperty(prop, "maxStrongholdFloors", 5);
		this.strongholdSideLength = PropertyFileHelper.getIntProperty(prop, "strongholdSideLength", 3);
		this.strongholdSideLength = this.strongholdSideLength < 3 ? 3 : this.strongholdSideLength;
		this.strongholdSideLength += this.strongholdSideLength % 2 == 0 ? 1 : 0;
		this.minStrongholdRooms = PropertyFileHelper.getIntProperty(prop, "minStrongholdRooms", 24);
		this.maxStrongholdRooms = PropertyFileHelper.getIntProperty(prop, "maxStrongholdRooms", 80);
		this.roomSizeX = PropertyFileHelper.getIntProperty(prop, "roomSizeX", 15);
		this.roomSizeY = PropertyFileHelper.getIntProperty(prop, "roomSizeY", 10);
		this.roomSizeZ = PropertyFileHelper.getIntProperty(prop, "roomSizeZ", 15);
		this.curveENFolder = PropertyFileHelper.getFileProperty(prop, "curveENFolder", "volcano/rooms/curves/EN");
		this.curveESFolder = PropertyFileHelper.getFileProperty(prop, "curveESFolder", "volcano/rooms/curves/ES");
		this.curveNEFolder = PropertyFileHelper.getFileProperty(prop, "curveNEFolder", "volcano/rooms/curves/NE");
		this.curveNWFolder = PropertyFileHelper.getFileProperty(prop, "curveNWFolder", "volcano/rooms/curves/NW");
		this.curveSEFolder = PropertyFileHelper.getFileProperty(prop, "curveSEFolder", "volcano/rooms/curves/SE");
		this.curveSWFolder = PropertyFileHelper.getFileProperty(prop, "curveSWFolder", "volcano/rooms/curves/SW");
		this.curveWNFolder = PropertyFileHelper.getFileProperty(prop, "curveWNFolder", "volcano/rooms/curves/WN");
		this.curveWSFolder = PropertyFileHelper.getFileProperty(prop, "curveWSFolder", "volcano/rooms/curves/WS");
		this.hallEWFolder = PropertyFileHelper.getFileProperty(prop, "hallwayEWFolder", "volcano/rooms/hallway/EW");
		this.hallNSFolder = PropertyFileHelper.getFileProperty(prop, "hallwayNSFolder", "volcano/rooms/hallway/NS");
		this.hallSNFolder = PropertyFileHelper.getFileProperty(prop, "hallwaySNFolder", "volcano/rooms/hallway/SN");
		this.hallWEFolder = PropertyFileHelper.getFileProperty(prop, "hallwayWEFolder", "volcano/rooms/hallway/WE");
		this.stairEFolder = PropertyFileHelper.getFileProperty(prop, "stairEFolder", "volcano/stairs/E");
		this.stairNFolder = PropertyFileHelper.getFileProperty(prop, "stairNFolder", "volcano/stairs/N");
		this.stairSFolder = PropertyFileHelper.getFileProperty(prop, "stairSFolder", "volcano/stairs/S");
		this.stairWFolder = PropertyFileHelper.getFileProperty(prop, "stairWFolder", "volcano/stairs/W");
		this.bossFolder = PropertyFileHelper.getFileProperty(prop, "bossRoomFolder", "volcano/rooms/boss/");
	}

	@Override
	public AbstractDungeonGenerator<DungeonVolcano> createDungeonGenerator(World world, int x, int y, int z) {
		return new GeneratorVolcano(world, new BlockPos(x, y, z), this);
	}

	public int getMinHeight() {
		return this.minHeight;
	}

	public int getMaxHeight() {
		return this.maxHeight;
	}

	public double getSteepness() {
		return this.steepness;
	}

	public double getLavaChance() {
		return this.lavaChance;
	}

	public double getMagmaChance() {
		return this.magmaChance;
	}

	public int getInnerRadius() {
		return this.innerRadius;
	}

	public File getRoomNBTFileForType(EStrongholdRoomType type) {
		File dir = null;
		switch (type) {
		case BOSS:
			dir = this.bossFolder;
			break;
		case CURVE_EN:
			dir = this.curveENFolder;
			break;
		case CURVE_ES:
			dir = this.curveESFolder;
			break;
		case CURVE_NE:
			dir = this.curveNEFolder;
			break;
		case CURVE_NW:
			dir = this.curveNWFolder;
			break;
		case CURVE_SE:
			dir = this.curveSEFolder;
			break;
		case CURVE_SW:
			dir = this.curveSWFolder;
			break;
		case CURVE_WN:
			dir = this.curveWNFolder;
			break;
		case CURVE_WS:
			dir = this.curveWSFolder;
			break;
		case HALLWAY_EW:
			dir = this.hallEWFolder;
			break;
		case HALLWAY_NS:
			dir = this.hallNSFolder;
			break;
		case HALLWAY_SN:
			dir = this.hallSNFolder;
			break;
		case HALLWAY_WE:
			dir = this.hallWEFolder;
			break;
		case STAIR_EE:
			dir = this.stairEFolder;
			break;
		case STAIR_NN:
			dir = this.stairNFolder;
			break;
		case STAIR_SS:
			dir = this.stairSFolder;
			break;
		case STAIR_WW:
			dir = this.stairWFolder;
			break;
		default:
			break;
		}
		if (dir != null && dir.isDirectory() && dir.list(FileIOUtil.getNBTFileFilter()).length > 0) {
			return this.getStructureFileFromDirectory(dir);
		}
		return null;
	}

	public int getFloorCount(Random rdm) {
		return DungeonGenUtils.getIntBetweenBorders(this.minStrongholdFloors, this.maxStrongholdFloors, rdm);
	}

	public int getFloorSideLength() {
		return this.strongholdSideLength;
	}

	public int getStrongholdRoomCount(Random rdm) {
		return DungeonGenUtils.getIntBetweenBorders(this.minStrongholdRooms, this.maxStrongholdRooms, rdm);
	}

	public int getRoomSizeX() {
		return this.roomSizeX;
	}

	public int getRoomSizeY() {
		return this.roomSizeY;
	}

	public int getRoomSizeZ() {
		return this.roomSizeZ;
	}

	public boolean doBuildStairs() {
		return this.buildStairwell;
	}

	public boolean doBuildDungeon() {
		return this.buildDungeon;
	}

	public boolean isVolcanoDamaged() {
		return this.damagedVolcano;
	}

	public int getMaxHoleSize() {
		return this.maxHoleSize;
	}

	public int getOreChance() {
		return this.oreConcentration;
	}

	public boolean generateOres() {
		return this.ores;
	}

	public int getChestChance() {
		return this.chestChance;
	}

	public ResourceLocation[] getChestIDs() {
		return this.chestIDs;
	}

	public IBlockState getUpperMainBlock() {
		return this.stoneBlock;
	}

	public IBlockState getLowerMainBlock() {
		return this.lowerStoneBlock;
	}

	public IBlockState getLavaBlock() {
		return this.lavaBlock;
	}

	public IBlockState getMagmaBlock() {
		return this.magmaBlock;
	}

	public IBlockState getRampBlock() {
		return this.rampBlock;
	}

	public IBlockState[] getOres() {
		return this.oreBlocks;
	}

	public IBlockState getPillarBlock() {
		return this.pillarBlock;
	}

	public ResourceLocation getRampMob() {
		String[] bossString = this.rampMobName.split(":");

		return new ResourceLocation(bossString[0], bossString[1]);
	}

}
