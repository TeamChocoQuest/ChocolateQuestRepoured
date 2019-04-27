package com.teamcqr.chocolatequestrepoured.dungeongen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class DungeonBase {
	
	protected IDungeonGenerator generator;
	protected String name;
	private Item placeItem;
	protected int underGroundOffset = 0;
	protected int chance;
	protected int yOffset = 0;
	protected int[] allowedDims = {0};
	protected boolean unique = false;
	protected boolean buildSupportPlatform = true;
	private int iconID;
	private Block supportBlock = Blocks.STONE;
	private Block supportTopBlock = Blocks.GRASS;
	//TODO: add TOP BLOCK option
	protected Block coverBlock = Blocks.AIR;
	private BlockPos lockedPos = null;
	private boolean isPosLocked = false;
	protected boolean registeredSuccessful = false;
	
	public void generate(BlockPos pos, World world) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		Random rdm = new Random();
		rdm.setSeed(WorldDungeonGenerator.getSeed(world, chunk.x, chunk.z));
		generate(pos.getX(), pos.getZ(), world, chunk, rdm);
	}
	
	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		
	}
	
	public DungeonBase(File configFile) {
		//DONE: read values from file
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(configFile);
			prop.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			prop = null;
			configFile = null;
		} catch (IOException e) {
			System.out.println("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			prop = null;
			configFile = null;
		}
		if(prop != null && configFile != null && fis != null) {
			this.name = configFile.getName().replace(".properties", "");
			this.chance = PropertyFileHelper.getIntProperty(prop, "chance", 20);
			this.underGroundOffset = PropertyFileHelper.getIntProperty(prop, "undergroundoffset", 0);
			this.allowedDims = PropertyFileHelper.getIntArrayProperty(prop, "allowedDims", new int[]{0});
			this.unique = PropertyFileHelper.getBooleanProperty(prop, "unique", false);
			this.iconID = PropertyFileHelper.getIntProperty(prop, "icon", 0);
			this.yOffset = PropertyFileHelper.getIntProperty(prop, "yoffset", 0);
		
			this.buildSupportPlatform = PropertyFileHelper.getBooleanProperty(prop, "buildsupportplatform", false);
			if(this.buildSupportPlatform) {
				this.supportBlock = Blocks.STONE;
				try {
					Block tmp = Block.getBlockFromName(prop.getProperty("supportblock", "minecraft:stone"));
					if(tmp != null) {
						this.supportBlock = tmp;
					}
				} catch(Exception ex) {
					System.out.println("couldnt load supportblock block! using default value (stone block)...");
				}
				
				this.supportTopBlock = Blocks.GRASS;
				try {
					Block tmp = Block.getBlockFromName(prop.getProperty("supportblocktop", "minecraft:stone"));
					if(tmp != null) {
						this.supportTopBlock = tmp;
					}
				} catch(Exception ex) {
					System.out.println("couldnt load supportblocktop block! using default value (air block)...");
				}
			}
			this.coverBlock = Blocks.AIR;
			try {
				Block tmp = Block.getBlockFromName(prop.getProperty("coverblock", "minecraft:air"));
				if(tmp != null) {
					this.coverBlock = tmp;
				}
			} catch(Exception ex) {
				System.out.println("couldnt load cover block! using default value (air block)...");
			}
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public IDungeonGenerator getGenerator() {
		return this.generator;
	}
	public Item getDungeonPlaceItem() {
		return this.placeItem;
	}
	public String getDungeonName() {
		return this.name;
	}
	public int getSpawnChance() {
		return this.chance;
	}
	public int[] getAllowedDimensions() {
		return this.allowedDims;
	}
	public boolean isUnique() {
		return this.unique;
	}

	public Block getSupportTopBlock() {
		return supportTopBlock;
	}

	public Block getSupportBlock() {
		return supportBlock;
	}

	public int getUnderGroundOffset() {
		return underGroundOffset;
	}

	public BlockPos getLockedPos() {
		return lockedPos;
	}

	public boolean isPosLocked() {
		return isPosLocked;
	}
	public int getIconID() {
		return this.iconID;
	}

	public void setLockPos(BlockPos pos, boolean locked) {
		this.lockedPos = pos;
		this.isPosLocked = locked;
	}
	public boolean isRegisteredSuccessful() {
		return this.registeredSuccessful;
	}
	public boolean doBuildSupportPlatform() {
		return this.buildSupportPlatform;
	}
	public Block getCoverBlock() {
		return this.coverBlock;
	}
}
