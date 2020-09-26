package com.teamcqr.chocolatequestrepoured.structuregen.oldVolcano;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.init.CQRBlocks;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class DungeonVolcanoOld extends DungeonBase {

	private boolean buildStairwell = true;
	private boolean buildDungeon = true;
	private boolean damagedVolcano = true;
	private boolean ores = true;
	private int oreConcentration = 2;
	private int maxHoleSize = 9;
	private int minHeight = 100;
	private int maxHeight = 130;
	private int innerRadius = 11;
	private int chestChance = 600;
	private ResourceLocation[] chestIDs = { LootTableList.CHESTS_ABANDONED_MINESHAFT, LootTableList.CHESTS_NETHER_BRIDGE, CQRLoottables.CHESTS_FOOD };
	private double steepness = 0.0025D;
	private double lavaChance = 0.005D;
	private double magmaChance = 0.075D;
	private String rampMobName = "cqrepoured:gremlin";
	private IBlockState stoneBlock = Blocks.STONE.getDefaultState();
	private IBlockState lavaBlock = Blocks.LAVA.getDefaultState();
	private IBlockState magmaBlock = Blocks.MAGMA.getDefaultState();
	private IBlockState rampBlock = Blocks.NETHERRACK.getDefaultState();
	private IBlockState lowerStoneBlock = Blocks.COBBLESTONE.getDefaultState();
	private IBlockState pillarBlock = CQRBlocks.GRANITE_LARGE.getDefaultState();
	private IBlockState[] oreBlocks = { Blocks.IRON_ORE.getDefaultState(), Blocks.GOLD_ORE.getDefaultState(), Blocks.REDSTONE_ORE.getDefaultState(), Blocks.EMERALD_ORE.getDefaultState() };

	// Stronghold
	private int minStrongholdFloors = 3;
	private int maxStrongholdFloors = 5;
	private int strongholdSideLength = 3;
	private int minStrongholdRooms = 26;
	private int maxStrongholdRooms = 32;
	private int roomSizeX = 15;
	private int roomSizeY = 10;
	private int roomSizeZ = 15;
	private File curveENFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/curves/EN");
	private File curveNEFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/curves/NE");
	private File curveSEFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/curves/SE");
	private File curveESFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/curves/ES");
	private File curveWSFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/curves/WS");
	private File curveSWFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/curves/SW");
	private File curveNWFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/curves/NW");
	private File curveWNFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/curves/WN");
	private File hallSNFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/hallway/SN");
	private File hallNSFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/hallway/NS");
	private File hallWEFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/hallway/WE");
	private File hallEWFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/hallway/EW");
	private File stairNFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/stairs/N");
	private File stairEFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/stairs/E");
	private File stairSFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/stairs/S");
	private File stairWFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/stairs/W");
	private File bossFolder = new File(CQRMain.CQ_STRUCTURE_FILES_FOLDER, "volcano/rooms/boss");

	public DungeonVolcanoOld() {
		super("fake", new Properties());
		this.dungeonMob = "GREMLIN";
		this.supportBlock = Blocks.COBBLESTONE.getDefaultState();
		this.supportTopBlock = Blocks.STONE.getDefaultState();
	}

	@Override
	public AbstractDungeonGenerator<? extends DungeonBase> createDungeonGenerator(World world, int x, int y, int z) {
		return null;
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
		return DungeonGenUtils.randomBetween(this.minStrongholdFloors, this.maxStrongholdFloors, rdm);
	}

	public int getFloorSideLength() {
		return this.strongholdSideLength;
	}

	public int getStrongholdRoomCount(Random rdm) {
		return DungeonGenUtils.randomBetween(this.minStrongholdRooms, this.maxStrongholdRooms, rdm);
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
