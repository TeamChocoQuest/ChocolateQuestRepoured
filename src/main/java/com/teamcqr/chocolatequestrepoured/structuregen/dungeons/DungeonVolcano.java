package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.init.CQRBlocks;
import com.teamcqr.chocolatequestrepoured.init.CQRLoottables;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.EStrongholdRoomType;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.volcano.GeneratorVolcano;
import com.teamcqr.chocolatequestrepoured.structuregen.oldVolcano.GeneratorVolcanoOld;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;
import com.teamcqr.chocolatequestrepoured.util.WeightedItem;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonVolcano extends DungeonBase {

	// For smoke: https://github.com/Tropicraft/Tropicraft/blob/1.12.2/src/main/java/net/tropicraft/core/common/block/tileentity/TileEntityVolcano.java
	private ResourceLocation rampMob = new ResourceLocation("minecraft", "zombie");

	private int minHeight = 80;
	private int maxHeight = 100;
	private int innerRadius = 10;
	private double steepness = 0.0000125D;
	private boolean damagedVolcano = true;
	private int maxHoleSize = 8;

	private List<WeightedItem<IBlockState>> volcanoBlocks = Arrays.asList(new WeightedItem<>(Blocks.STONE.getDefaultState(), 1000));
	private IBlockState lavaBlock = Blocks.LAVA.getDefaultState();
	private int lavaWeight = 10;
	private IBlockState rampBlock = Blocks.NETHERRACK.getDefaultState();
	private IBlockState pillarBlock = CQRBlocks.GRANITE_LARGE.getDefaultState();

	private boolean buildStairwell = true;
	private double chestChance = 0.002D;
	private ResourceLocation[] chestIDs = { CQRLoottables.CHESTS_FOOD, CQRLoottables.CHESTS_MATERIAL, CQRLoottables.CHESTS_EQUIPMENT };

	// Stronghold
	private boolean buildStronghold = true;
	private int minStrongholdFloors = 2;
	private int maxStrongholdFloors = 3;
	private int minStrongholdRadius = 1;
	private int maxStrongholdRadius = 2;
	private int minStrongholdRooms = 15;
	private int maxStrongholdRooms = 46;
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

		this.rampMob = PropertyFileHelper.getResourceLocationProperty(prop, "rampMob", this.rampMob);

		this.minHeight = PropertyFileHelper.getIntProperty(prop, "minHeight", this.minHeight);
		this.maxHeight = PropertyFileHelper.getIntProperty(prop, "maxHeight", this.maxHeight);
		this.innerRadius = PropertyFileHelper.getIntProperty(prop, "innerRadius", this.innerRadius);
		this.steepness = PropertyFileHelper.getDoubleProperty(prop, "steepness", this.steepness);
		this.damagedVolcano = PropertyFileHelper.getBooleanProperty(prop, "damagedVolcano", this.damagedVolcano);
		this.maxHoleSize = Math.max(PropertyFileHelper.getIntProperty(prop, "maxHoleSize", this.maxHoleSize), 2);

		this.volcanoBlocks = PropertyFileHelper.getWeightedBlockStateList(prop, "volcanoBlocks", this.volcanoBlocks);
		this.lavaBlock = PropertyFileHelper.getBlockStateProperty(prop, "lavaBlock", this.lavaBlock);
		this.lavaWeight = PropertyFileHelper.getIntProperty(prop, "lavaWeight", this.lavaWeight);
		this.rampBlock = PropertyFileHelper.getBlockStateProperty(prop, "rampBlock", this.rampBlock);
		this.pillarBlock = PropertyFileHelper.getBlockStateProperty(prop, "pillarBlock", this.pillarBlock);

		this.buildStairwell = PropertyFileHelper.getBooleanProperty(prop, "buildStairwell", this.buildStairwell);
		this.chestChance = PropertyFileHelper.getDoubleProperty(prop, "chestChance", this.chestChance);
		this.chestIDs = PropertyFileHelper.getResourceLocationArrayProperty(prop, "chestIDs", this.chestIDs);

		// Stronghold
		this.buildStronghold = PropertyFileHelper.getBooleanProperty(prop, "buildStronghold", this.buildStronghold);
		this.minStrongholdFloors = PropertyFileHelper.getIntProperty(prop, "minStrongholdFloors", this.minStrongholdFloors);
		this.maxStrongholdFloors = PropertyFileHelper.getIntProperty(prop, "maxStrongholdFloors", this.maxStrongholdFloors);
		this.minStrongholdRadius = PropertyFileHelper.getIntProperty(prop, "minStrongholdRadius", this.minStrongholdRadius);
		this.maxStrongholdRadius = PropertyFileHelper.getIntProperty(prop, "maxStrongholdRadius", this.maxStrongholdRadius);
		this.minStrongholdRooms = PropertyFileHelper.getIntProperty(prop, "minStrongholdRooms", this.minStrongholdRooms);
		this.maxStrongholdRooms = PropertyFileHelper.getIntProperty(prop, "maxStrongholdRooms", this.maxStrongholdRooms);
		this.roomSizeX = PropertyFileHelper.getIntProperty(prop, "roomSizeX", this.roomSizeX);
		this.roomSizeY = PropertyFileHelper.getIntProperty(prop, "roomSizeY", this.roomSizeY);
		this.roomSizeZ = PropertyFileHelper.getIntProperty(prop, "roomSizeZ", this.roomSizeZ);
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
	public AbstractDungeonGenerator<?> createDungeonGenerator(World world, int x, int y, int z) {
		return true ? new GeneratorVolcano(world, new BlockPos(x, y, z), this) : new GeneratorVolcanoOld(world, new BlockPos(x, y, z));
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

	public ResourceLocation getRampMob() {
		return rampMob;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public int getInnerRadius() {
		return innerRadius;
	}

	public double getSteepness() {
		return steepness;
	}

	public boolean isVolcanoDamaged() {
		return damagedVolcano;
	}

	public int getMaxHoleSize() {
		return maxHoleSize;
	}

	public List<WeightedItem<IBlockState>> getVolcanoBlocks() {
		return volcanoBlocks;
	}

	public IBlockState getLavaBlock() {
		return lavaBlock;
	}

	public int getLavaWeight() {
		return lavaWeight;
	}

	public IBlockState getRampBlock() {
		return rampBlock;
	}

	public IBlockState getPillarBlock() {
		return pillarBlock;
	}

	public boolean doBuildStairs() {
		return buildStairwell;
	}

	public double getChestChance() {
		return chestChance;
	}

	public ResourceLocation[] getChestIDs() {
		return chestIDs;
	}

	public boolean doBuildStronghold() {
		return buildStronghold;
	}

	public int getMinStrongholdFloors() {
		return minStrongholdFloors;
	}

	public int getMaxStrongholdFloors() {
		return maxStrongholdFloors;
	}

	public int getMinStrongholdRadius() {
		return minStrongholdRadius;
	}

	public int getMaxStrongholdRadius() {
		return maxStrongholdRadius;
	}

	public int getMinStrongholdRooms() {
		return minStrongholdRooms;
	}

	public int getMaxStrongholdRooms() {
		return maxStrongholdRooms;
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

}
