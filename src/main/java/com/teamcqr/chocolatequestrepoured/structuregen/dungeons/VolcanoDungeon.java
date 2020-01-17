package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.VolcanoGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.brickfortress.ESpiralStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class VolcanoDungeon extends DungeonBase {

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
	private int[] chestIDs = { 4, 10, 2 };
	private double steepness = 0.075D;
	private double lavaChance = 0.005D;
	private double magmaChance = 0.1;
	private String rampMobName = "minecraft:zombie";
	private Block stoneBlock = Blocks.STONE;
	private Block lavaBlock = Blocks.LAVA;
	private Block magmaBlock = Blocks.MAGMA;
	private Block rampBlock = Blocks.NETHERRACK;
	private Block lowerStoneBlock = Blocks.COBBLESTONE;
	private Block pillarBlock = ModBlocks.GRANITE_LARGE;
	
	//STronghold
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

	public VolcanoDungeon(File configFile) {
		super(configFile);
		Properties prop = this.loadConfig(configFile);
		if (prop != null) {
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
			this.chestIDs = PropertyFileHelper.getIntArrayProperty(prop, "chestIDs", new int[] { 4, 10, 2 });
			this.stoneBlock = PropertyFileHelper.getBlockProperty(prop, "topBlock", Blocks.STONE);
			this.lowerStoneBlock = PropertyFileHelper.getBlockProperty(prop, "lowerBlock", Blocks.COBBLESTONE);
			this.lavaBlock = PropertyFileHelper.getBlockProperty(prop, "lavaBlock", Blocks.LAVA);
			this.magmaBlock = PropertyFileHelper.getBlockProperty(prop, "magmaBlock", Blocks.MAGMA);
			this.rampBlock = PropertyFileHelper.getBlockProperty(prop, "rampBlock", Blocks.NETHERRACK);
			this.pillarBlock = PropertyFileHelper.getBlockProperty(prop, "pillarBlock", ModBlocks.GRANITE_LARGE);
			
			//Stronghold
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
			
			this.closeConfigFile();
		} else {
			this.registeredSuccessful = false;
		}
	}

	@Override
	public IDungeonGenerator getGenerator() {
		return new VolcanoGenerator(this);
	}

	@Override
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		super.generate(x, z, world, chunk, random);

		// This generator sadly is too slow for no reason so we use the old one *sigh*
		/*
		 * System.out.println("Generating dungeon " + this.name + "... with maps");
		 * 
		 * //this.generator = new VolcanoGenerator(this);
		 * this.generator = new VolcanoGeneratorWithMaps(this);
		 * 
		 * this.generator.generate(world, chunk, x, DungeonGenUtils.getHighestYAt(chunk, x, z, true), z);
		 * 
		 * 
		 * System.out.println("Done!");
		 */

		System.out.println("Generating dungeon " + this.name + "... ");

		this.generator = getGenerator();

		this.generator.generate(world, chunk, x, DungeonGenUtils.getHighestYAt(chunk, x, z, true), z);

		System.out.println("Done!");
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
	
	public File getRoomNBTFileForType(ESpiralStrongholdRoomType type) {
		File dir = null;
		switch(type) {
		case BOSS:
			dir = bossFolder;
			break;
		case CURVE_EN:
			dir = curveENFolder;
			break;
		case CURVE_ES:
			dir = curveESFolder;
			break;
		case CURVE_NE:
			dir = curveNEFolder;
			break;
		case CURVE_NW:
			dir = curveNWFolder;
			break;
		case CURVE_SE:
			dir = curveSEFolder;
			break;
		case CURVE_SW:
			dir = curveSWFolder;
			break;
		case CURVE_WN:
			dir = curveWNFolder;
			break;
		case CURVE_WS:
			dir = curveWSFolder;
			break;
		case HALLWAY_EW:
			dir = hallEWFolder;
			break;
		case HALLWAY_NS:
			dir = hallNSFolder;
			break;
		case HALLWAY_SN:
			dir = hallSNFolder;
			break;
		case HALLWAY_WE:
			dir = hallWEFolder;
			break;
		case STAIR_EE:
			dir = stairEFolder;
			break;
		case STAIR_NN:
			dir = stairNFolder;
			break;
		case STAIR_SS:
			dir = stairSFolder;
			break;
		case STAIR_WW:
			dir = stairWFolder;
			break;
		default:
			break;
		}
		if(dir != null && dir.isDirectory() && dir.list(FileIOUtil.getNBTFileFilter()).length > 0) {
			return getStructureFileFromDirectory(dir);
		}
		return null;
	}
	
	public int getFloorCount(Random rdm) {
		return DungeonGenUtils.getIntBetweenBorders(minStrongholdFloors, maxStrongholdFloors, rdm);
	}
	public int getFloorSideLength() {
		return strongholdSideLength;
	}
	public int getStrongholdRoomCount(Random rdm) {
		return DungeonGenUtils.getIntBetweenBorders(minStrongholdRooms, maxStrongholdRooms, rdm);
	}
	public int getRoomSizeX() {
		return roomSizeX;
	}
	public int getRoomSizeY() {
		return roomSizeY;
	}
	public int getRoomSizeZ() {
		return roomSizeZ;
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

	public int[] getChestIDs() {
		return this.chestIDs;
	}

	public Block getUpperMainBlock() {
		return this.stoneBlock;
	}

	public Block getLowerMainBlock() {
		return this.lowerStoneBlock;
	}

	public Block getLavaBlock() {
		return this.lavaBlock;
	}

	public Block getMagmaBlock() {
		return this.magmaBlock;
	}

	public Block getRampBlock() {
		return this.rampBlock;
	}

	public Block getPillarBlock() {
		return this.pillarBlock;
	}

	public ResourceLocation getRampMob() {
		String[] bossString = this.rampMobName.split(":");

		return new ResourceLocation(bossString[0], bossString[1]);
	}

}
