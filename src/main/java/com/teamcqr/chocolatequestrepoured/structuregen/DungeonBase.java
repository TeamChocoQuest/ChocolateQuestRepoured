package com.teamcqr.chocolatequestrepoured.structuregen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonBase {

	protected IDungeonGenerator generator;
	private boolean replaceBanners = false;
	// private CQFaction owningFaction
	protected String name;
	private Item placeItem;
	protected UUID dunID;
	protected int underGroundOffset = 0;
	protected int chance;
	protected int yOffset = 0;
	protected int[] allowedDims = { 0 };
	protected String[] modDependencies = { Reference.MODID };
	protected boolean unique = false;
	protected boolean buildSupportPlatform = true;
	protected boolean useCoverBlock = false;
	private EDungeonMobType dungeonMob = EDungeonMobType.DEFAULT;
	private boolean spawnBehindWall = false;
	private int iconID;
	private FileInputStream fisConfigFile = null;
	private Block supportBlock = Blocks.STONE;
	private Block supportTopBlock = Blocks.GRASS;
	protected Block coverBlock = Blocks.AIR;
	private BlockPos lockedPos = null;
	private boolean isPosLocked = false;
	protected boolean registeredSuccessful = false;
	private boolean rotateDungeon = true;
	
	//Protection system stuff
	protected boolean enableProtectionSystem = false;
	protected boolean allowBlockPlacing = false;
	protected boolean allowBlockBreaking = false;
	protected boolean allowFireSpread = false;
	protected boolean allowMobSpawning = false;
	protected boolean bypassSecurityChecks = false;

	public void generate(BlockPos pos, World world) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		Random rdm = new Random();
		rdm.setSeed(WorldDungeonGenerator.getSeed(world, chunk.x, chunk.z));
		this.generate(pos.getX(), pos.getZ(), world, chunk, rdm);
	}

	protected void generate(int x, int z, World world, Chunk chunk, Random random) {
		this.dunID = MathHelper.getRandomUUID();
	}

	public DungeonBase(File configFile) {
		// DONE: read values from file
		Properties prop = this.loadConfig(configFile);

		if (prop != null) {
			this.name = configFile.getName().replace(".properties", "");
			this.chance = PropertyFileHelper.getIntProperty(prop, "chance", 20);
			this.underGroundOffset = PropertyFileHelper.getIntProperty(prop, "undergroundoffset", 0);
			this.allowedDims = PropertyFileHelper.getIntArrayProperty(prop, "allowedDims", new int[] { 0 });
			this.unique = PropertyFileHelper.getBooleanProperty(prop, "unique", false);
			this.useCoverBlock = PropertyFileHelper.getBooleanProperty(prop, "usecoverblock", false);
			this.spawnBehindWall = PropertyFileHelper.getBooleanProperty(prop, "spawnOnlyBehindWall", false);
			this.iconID = PropertyFileHelper.getIntProperty(prop, "icon", 0);
			this.yOffset = PropertyFileHelper.getIntProperty(prop, "yoffset", 0);
			this.replaceBanners = PropertyFileHelper.getBooleanProperty(prop, "replaceBanners", false);
			this.dungeonMob = EDungeonMobType.byString(prop.getProperty("dungeonMob", EDungeonMobType.DEFAULT.name().toUpperCase()).toUpperCase());
			this.modDependencies = PropertyFileHelper.getStringArrayProperty(prop, "dependencies", new String[] { Reference.MODID });
			if (this.modDependencies.length <= 0 || this.modDependencies == null) {
				this.modDependencies = new String[] { Reference.MODID };
			}

			this.buildSupportPlatform = PropertyFileHelper.getBooleanProperty(prop, "buildsupportplatform", false);
			if (this.buildSupportPlatform) {
				this.supportBlock = PropertyFileHelper.getBlockProperty(prop, "supportblock", Blocks.STONE);
				this.supportTopBlock = PropertyFileHelper.getBlockProperty(prop, "supportblocktop", Blocks.GRASS);
			}
			this.coverBlock = PropertyFileHelper.getBlockProperty(prop, "coverblock", Blocks.AIR);
			this.rotateDungeon = PropertyFileHelper.getBooleanProperty(prop, "rotateDungeon", true);
			
			//protection system
			this.enableProtectionSystem = PropertyFileHelper.getBooleanProperty(prop, "enableProtectionSystem", false);
			this.allowBlockBreaking = !PropertyFileHelper.getBooleanProperty(prop, "blockMining", false);
			this.allowBlockPlacing = !PropertyFileHelper.getBooleanProperty(prop, "blockBuilding", false);
			this.allowFireSpread = !PropertyFileHelper.getBooleanProperty(prop, "blockFireSpread", false);
			this.allowMobSpawning = !PropertyFileHelper.getBooleanProperty(prop, "blockMobSpawning", false);
			this.bypassSecurityChecks = PropertyFileHelper.getBooleanProperty(prop, "ignoreNoBossOrNexus", false);

			this.closeConfigFile();
		} else {
			this.registeredSuccessful = false;
		}
	}

	public void closeConfigFile() {
		try {
			this.fisConfigFile.close();
		} catch (IOException e) {
			this.registeredSuccessful = false;
			e.printStackTrace();
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
		return this.supportTopBlock;
	}

	public Block getSupportBlock() {
		return this.supportBlock;
	}

	public int getUnderGroundOffset() {
		return this.underGroundOffset;
	}

	public BlockPos getLockedPos() {
		return this.lockedPos;
	}

	public boolean isPosLocked() {
		return this.isPosLocked;
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

	public UUID getDungeonID() {
		return this.dunID;
	}

	public boolean isProtectedFromModifications() {
		return this.enableProtectionSystem;
	}

	public boolean isCoverBlockEnabled() {
		return this.useCoverBlock;
	}

	public boolean doesSpawnOnlyBehindWall() {
		return this.spawnBehindWall;
	}

	public boolean replaceBanners() {
		return this.replaceBanners;
	}

	public boolean rotateDungeon() {
		return this.rotateDungeon;
	}

	public Properties loadConfig(File configFile) {
		Properties prop = new Properties();
		this.fisConfigFile = null;
		this.registeredSuccessful = true;
		try {
			this.fisConfigFile = new FileInputStream(configFile);
			prop.load(this.fisConfigFile);
		} catch (FileNotFoundException e) {
			CQRMain.logger.error("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			try {
				this.fisConfigFile.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			prop = null;
			configFile = null;
			this.registeredSuccessful = false;
		} catch (IOException e) {
			CQRMain.logger.error("Unable to read config file: " + configFile.getName());
			e.printStackTrace();
			try {
				this.fisConfigFile.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			prop = null;
			configFile = null;
			this.registeredSuccessful = false;
		}
		if (prop != null && configFile != null && this.fisConfigFile != null) {
			return prop;
		}
		this.registeredSuccessful = false;
		return null;
	}

	public EDungeonMobType getDungeonMob() {
		return this.dungeonMob;
	}

	public File getStructureFileFromDirectory(File parentDir) {
		if (parentDir.isDirectory()) {
			int fileCount = parentDir.listFiles(FileIOUtil.getNBTFileFilter()).length;
			if (fileCount > 0) {
				Random rdm = new Random();
				List<File> files = this.getFilesRecursively(parentDir);
				fileCount = files.size();
				return files.get(rdm.nextInt(fileCount));
			}
		} else {
			if (parentDir.getName().contains(".nbt")) {
				return parentDir;
			}
		}
		return null;
	}

	private List<File> getFilesRecursively(File parentDir) {
		if (!parentDir.isDirectory() || parentDir.listFiles(FileIOUtil.getNBTFileFilter()).length <= 0) {
			return null;
		}
		List<File> allFiles = new ArrayList<File>();
		Queue<File> dirs = new LinkedList<File>();
		dirs.add(new File(parentDir.getAbsolutePath()));
		while (!dirs.isEmpty()) {
			for (File f : dirs.poll().listFiles(FileIOUtil.getNBTFileFilter())) {
				if (f.isDirectory()) {
					dirs.add(f);
				} else if (f.isFile()) {
					allFiles.add(f);
				}
			}
		}
		return allFiles;
	}

	public String[] getDependencies() {
		return this.modDependencies;
	}

	public int getYOffset() {
		return Math.abs(this.yOffset);
	}
	
	//Protection system
	public boolean getAllowFireSpread() {
		return allowFireSpread;
	}
	public boolean getAllowBlockPlacing() {
		return allowBlockPlacing;
	}
	public boolean getAllowBlockBreaking() {
		return allowBlockBreaking;
	}
	public boolean getSecurityBypassEnabled() {
		return bypassSecurityChecks;
	}
	public boolean getAllowMobSpawns() {
		return allowMobSpawning;
	}
}
