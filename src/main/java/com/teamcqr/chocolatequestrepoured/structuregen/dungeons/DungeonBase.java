package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public abstract class DungeonBase {

	// private CQFaction owningFaction

	protected final Random random = new Random();

	protected String name;
	protected int iconID;

	protected int[] allowedDims;
	protected int weight;
	protected int chance;
	protected String[] biomes;
	protected String[] blacklistedBiomes;
	protected boolean unique;
	protected boolean rotateDungeon;
	protected boolean spawnBehindWall;
	protected String[] modDependencies;
	protected String[] dungeonDependencies;

	protected EDungeonMobType dungeonMob;
	protected boolean replaceBanners;
	protected int underGroundOffset;
	protected int yOffset;

	protected boolean isPosLocked;
	protected BlockPos lockedPos;

	protected boolean buildSupportPlatform;
	protected Block supportBlock;
	protected Block supportTopBlock;

	protected boolean useCoverBlock;
	protected Block coverBlock;

	// Protection system stuff
	protected boolean enableProtectionSystem = false;
	protected boolean allowBlockPlacing = false;
	protected boolean allowBlockBreaking = false;
	protected boolean allowFireSpread = false;
	protected boolean allowMobSpawning = false;
	protected boolean allowExplosionTNT = true;
	protected boolean allowExplosionOther = false;
	protected boolean bypassSecurityChecks = false;

	public DungeonBase(String name, Properties prop) {
		this.name = name;
		this.iconID = PropertyFileHelper.getIntProperty(prop, "icon", 0);

		this.allowedDims = PropertyFileHelper.getIntArrayProperty(prop, "allowedDims", new int[0]);
		this.weight = PropertyFileHelper.getIntProperty(prop, "weight", 0);
		this.chance = PropertyFileHelper.getIntProperty(prop, "chance", 0);
		this.biomes = PropertyFileHelper.getStringArrayProperty(prop, "biomes", new String[0]);
		this.blacklistedBiomes = PropertyFileHelper.getStringArrayProperty(prop, "disallowedBiomes", new String[0]);
		this.unique = PropertyFileHelper.getBooleanProperty(prop, "unique", false);
		this.modDependencies = PropertyFileHelper.getStringArrayProperty(prop, "dependencies", new String[0]);
		this.dungeonDependencies = PropertyFileHelper.getStringArrayProperty(prop, "requiredDungeonsForThisToSpawn", new String[0]);
		this.spawnBehindWall = PropertyFileHelper.getBooleanProperty(prop, "spawnOnlyBehindWall", false);

		this.dungeonMob = EDungeonMobType.byString(prop.getProperty("dummyReplacement", EDungeonMobType.DEFAULT.name().toUpperCase()).toUpperCase());
		this.replaceBanners = PropertyFileHelper.getBooleanProperty(prop, "replaceBanners", false);
		this.underGroundOffset = PropertyFileHelper.getIntProperty(prop, "undergroundoffset", 0);
		this.yOffset = PropertyFileHelper.getIntProperty(prop, "yoffset", 0);
		this.rotateDungeon = PropertyFileHelper.getBooleanProperty(prop, "rotateDungeon", true);

		this.isPosLocked = PropertyFileHelper.getBooleanProperty(prop, "spawnAtCertainPosition", false);
		if (this.isPosLocked) {
			this.isPosLocked = this.handleLockedPos(prop);
		}

		this.buildSupportPlatform = PropertyFileHelper.getBooleanProperty(prop, "buildsupportplatform", false);
		this.supportBlock = PropertyFileHelper.getBlockProperty(prop, "supportblock", Blocks.STONE);
		this.supportTopBlock = PropertyFileHelper.getBlockProperty(prop, "supportblocktop", Blocks.GRASS);

		this.useCoverBlock = PropertyFileHelper.getBooleanProperty(prop, "usecoverblock", false);
		this.coverBlock = PropertyFileHelper.getBlockProperty(prop, "coverblock", Blocks.AIR);

		// protection system
		this.enableProtectionSystem = PropertyFileHelper.getBooleanProperty(prop, "enableProtectionSystem", false);
		this.allowBlockBreaking = !PropertyFileHelper.getBooleanProperty(prop, "blockMining", false);
		this.allowBlockPlacing = !PropertyFileHelper.getBooleanProperty(prop, "blockBuilding", false);
		this.allowFireSpread = !PropertyFileHelper.getBooleanProperty(prop, "blockFireSpread", false);
		this.allowMobSpawning = !PropertyFileHelper.getBooleanProperty(prop, "blockMobSpawning", false);
		this.allowExplosionTNT = !PropertyFileHelper.getBooleanProperty(prop, "blockExplosionTNT", false);
		this.allowExplosionOther = !PropertyFileHelper.getBooleanProperty(prop, "blockExplosionOther", false);
		this.bypassSecurityChecks = PropertyFileHelper.getBooleanProperty(prop, "ignoreNoBossOrNexus", false);
	}

	@Override
	public String toString() {
		return this.name;
	}

	private boolean handleLockedPos(Properties prop) {
		String[] coordinates = prop.getProperty("spawnAt", "-;-;-").split(";");
		try {
			int x = Integer.parseInt(coordinates[0]);
			int y = Integer.parseInt(coordinates[1]);
			int z = Integer.parseInt(coordinates[2]);
			this.lockedPos = new BlockPos(x, y, z);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			CQRMain.logger.error("{}: Failed to read spawn position!", this.name);
			return false;
		}
		return true;
	}

	public void generate(World world, int x, int z) {
		Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
		int y = 0;
		for (int ix = 0; ix < 16; ix++) {
			for (int iz = 0; iz < 16; iz++) {
				y += this.getYForPos(world, chunk.x * 16 + ix, chunk.z * 16 + iz, false);
			}
		}
		y /= 256;
		y -= this.getUnderGroundOffset();
		y += this.getYOffset();
		this.generate(world, x, y, z);
	}

	protected int getYForPos(World world, int x, int z, boolean ignoreWater) {
		int y = 255;
		Material material = world.getBlockState(new BlockPos(x, y, z)).getMaterial();
		while (y > 0 && (material == Material.AIR || material == Material.WOOD || material == Material.LEAVES || material == Material.PLANTS || (ignoreWater && material == Material.WATER))) {
			material = world.getBlockState(new BlockPos(x, --y, z)).getMaterial();
		}
		return y;
	}

	public abstract void generate(World world, int x, int y, int z);

	public void generateWithOffsets(World world, int x, int y, int z) {
		y -= this.getUnderGroundOffset();
		y += this.getYOffset();
		this.generate(world, x, y, z);
	}

	public File getStructureFileFromDirectory(File parentDir) {
		List<File> files = new ArrayList<>(FileUtils.listFiles(parentDir, new String[] { "nbt" }, true));
		if (!files.isEmpty()) {
			return files.get(this.random.nextInt(files.size()));
		}
		return null;
	}

	public String getDungeonName() {
		return this.name;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getChance() {
		return this.chance;
	}

	public String[] getBiomes() {
		return this.biomes;
	}

	public String[] getBlacklistedBiomes() {
		return this.blacklistedBiomes;
	}

	public int[] getAllowedDimensions() {
		return this.allowedDims;
	}

	public boolean isDimensionAllowed(int dimension) {
		for (int dim : this.getAllowedDimensions()) {
			if (dim == dimension) {
				return true;
			}
		}
		return false;
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
		return Math.abs(this.underGroundOffset);
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

	public boolean doBuildSupportPlatform() {
		return this.buildSupportPlatform;
	}

	public Block getCoverBlock() {
		return this.coverBlock;
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

	public EDungeonMobType getDungeonMob() {
		return this.dungeonMob;
	}

	public String[] getDependencies() {
		return this.modDependencies;
	}

	public int getYOffset() {
		return this.yOffset;
	}

	public boolean dependsOnOtherStructures() {
		return this.dungeonDependencies.length > 0;
	}

	public String[] getDungeonDependencies() {
		return this.dungeonDependencies;
	}

	// Protection system
	public boolean isProtectedFromModifications() {
		return this.enableProtectionSystem;
	}

	public boolean getAllowBlockPlacing() {
		return this.allowBlockPlacing;
	}

	public boolean getAllowBlockBreaking() {
		return this.allowBlockBreaking;
	}

	public boolean getAllowFireSpread() {
		return this.allowFireSpread;
	}

	public boolean getAllowMobSpawns() {
		return this.allowMobSpawning;
	}

	public boolean getAllowExplosionTNT() {
		return this.allowExplosionTNT;
	}

	public boolean getAllowExplosionOther() {
		return this.allowExplosionOther;
	}

	public boolean getSecurityBypassEnabled() {
		return this.bypassSecurityChecks;
	}

}
