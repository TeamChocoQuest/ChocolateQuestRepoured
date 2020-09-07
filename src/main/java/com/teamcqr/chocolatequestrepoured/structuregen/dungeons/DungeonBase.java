package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonDataManager;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonGeneratorThread;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.AbstractDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Loader;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public abstract class DungeonBase {

	// private CQFaction owningFaction
	protected final Random random = new Random();

	protected String name;
	protected boolean enabled = true;
	protected int iconID = 0;

	protected int weight = 0;
	protected int chance = 0;
	protected int spawnLimit = -1;
	protected int[] allowedDims = new int[0];
	protected boolean allowedInAllDims = false;
	protected ResourceLocation[] allowedBiomes = new ResourceLocation[0];
	protected String[] allowedBiomeTypes = new String[0];
	protected boolean allowedInAllBiomes = false;
	protected ResourceLocation[] disallowedBiomes = new ResourceLocation[0];
	protected String[] disallowedBiomeTypes = new String[0];
	protected BlockPos[] lockedPositions = new BlockPos[0];
	protected boolean spawnOnlyBehindWall = false;
	protected String[] modDependencies = new String[0];
	protected String[] dungeonDependencies = new String[0];

	protected String dungeonMob;
	protected boolean replaceBanners;
	protected int underGroundOffset;
	protected int yOffset;

	protected boolean buildSupportPlatform;
	protected IBlockState supportBlock;
	protected IBlockState supportTopBlock;

	protected boolean useCoverBlock;
	protected IBlockState coverBlock;

	// Protection system stuff
	protected boolean enableProtectionSystem = false;
	protected boolean preventBlockPlacing = false;
	protected boolean preventBlockBreaking = false;
	protected boolean preventExplosionsTNT = false;
	protected boolean preventExplosionsOther = false;
	protected boolean preventFireSpreading = false;
	protected boolean preventEntitySpawning = false;
	protected boolean ignoreNoBossOrNexus = false;

	public DungeonBase(String name, Properties prop) {
		this.name = name;
		this.enabled = PropertyFileHelper.getBooleanProperty(prop, "enabled", this.enabled);
		this.iconID = PropertyFileHelper.getIntProperty(prop, "icon", this.iconID);

		this.weight = PropertyFileHelper.getIntProperty(prop, "weight", this.weight);
		this.chance = PropertyFileHelper.getIntProperty(prop, "chance", this.chance);
		this.spawnLimit = PropertyFileHelper.getIntProperty(prop, "spawnLimit", this.spawnLimit);
		this.allowedDims = PropertyFileHelper.getIntArrayProperty(prop, "allowedDims", this.allowedDims);
		this.allowedInAllDims = PropertyFileHelper.getBooleanProperty(prop, "allowedInAllDims", this.allowedInAllDims);
		this.allowedBiomes = PropertyFileHelper.getResourceLocationArrayProperty(prop, "allowedBiomes", this.allowedBiomes);
		this.allowedBiomeTypes = PropertyFileHelper.getStringArrayProperty(prop, "allowedBiomeTypes", this.allowedBiomeTypes);
		this.allowedInAllBiomes = PropertyFileHelper.getBooleanProperty(prop, "allowedInAllBiomes", this.allowedInAllBiomes);
		this.disallowedBiomes = PropertyFileHelper.getResourceLocationArrayProperty(prop, "disallowedBiomes", this.disallowedBiomes);
		this.disallowedBiomeTypes = PropertyFileHelper.getStringArrayProperty(prop, "disallowedBiomeTypes", this.disallowedBiomeTypes);
		this.lockedPositions = PropertyFileHelper.getBlockPosArrayProperty(prop, "lockedPositions", this.lockedPositions);
		this.spawnOnlyBehindWall = PropertyFileHelper.getBooleanProperty(prop, "spawnOnlyBehindWall", this.spawnOnlyBehindWall);
		this.modDependencies = PropertyFileHelper.getStringArrayProperty(prop, "modDependencies", this.modDependencies);
		this.dungeonDependencies = PropertyFileHelper.getStringArrayProperty(prop, "dungeonDependencies", this.dungeonDependencies);

		this.dungeonMob = prop.getProperty("dummyReplacement", DungeonInhabitantManager.DEFAULT_INHABITANT_IDENT);
		this.replaceBanners = PropertyFileHelper.getBooleanProperty(prop, "replaceBanners", false);
		this.underGroundOffset = PropertyFileHelper.getIntProperty(prop, "undergroundoffset", 0);
		this.yOffset = PropertyFileHelper.getIntProperty(prop, "yoffset", 0);

		this.buildSupportPlatform = PropertyFileHelper.getBooleanProperty(prop, "buildsupportplatform", false);
		this.supportBlock = PropertyFileHelper.getBlockStateProperty(prop, "supportblock", Blocks.STONE.getDefaultState());
		this.supportTopBlock = PropertyFileHelper.getBlockStateProperty(prop, "supportblocktop", Blocks.GRASS.getDefaultState());

		this.useCoverBlock = PropertyFileHelper.getBooleanProperty(prop, "usecoverblock", false);
		this.coverBlock = PropertyFileHelper.getBlockStateProperty(prop, "coverblock", Blocks.AIR.getDefaultState());

		// protection system
		this.enableProtectionSystem = PropertyFileHelper.getBooleanProperty(prop, "enableProtectionSystem", false);
		this.preventBlockBreaking = PropertyFileHelper.getBooleanProperty(prop, "preventBlockBreaking", false);
		this.preventBlockPlacing = PropertyFileHelper.getBooleanProperty(prop, "preventBlockPlacing", false);
		this.preventExplosionsTNT = PropertyFileHelper.getBooleanProperty(prop, "preventExplosionsTNT", false);
		this.preventExplosionsOther = PropertyFileHelper.getBooleanProperty(prop, "preventExplosionOther", false);
		this.preventFireSpreading = PropertyFileHelper.getBooleanProperty(prop, "preventFireSpreading", false);
		this.preventEntitySpawning = PropertyFileHelper.getBooleanProperty(prop, "preventEntitySpawning", false);
		this.ignoreNoBossOrNexus = PropertyFileHelper.getBooleanProperty(prop, "ignoreNoBossOrNexus", false);
	}

	@Override
	public String toString() {
		return this.name;
	}

	public abstract AbstractDungeonGenerator<? extends DungeonBase> createDungeonGenerator(World world, int x, int y, int z);

	public void generate(World world, int x, int z) {
		Chunk chunk = world.getChunk(x >> 4, z >> 4);
		int y = 0;
		for (int ix = 0; ix < 16; ix++) {
			for (int iz = 0; iz < 16; iz++) {
				y += DungeonGenUtils.getYForPos(world, chunk.x * 16 + ix, chunk.z * 16 + iz, false);
			}
		}
		y >>= 8;
		y -= this.getUnderGroundOffset();
		y += this.getYOffset();
		this.generate(world, x, y, z);
	}

	public void generate(World world, int x, int y, int z) {
		if (CQRConfig.advanced.multithreadedDungeonPreparation) {
			new DungeonGeneratorThread(this.createDungeonGenerator(world, x, y, z)).start();
		} else {
			this.createDungeonGenerator(world, x, y, z).generate();
		}
	}

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

	public boolean canSpawnAtPos(World world, BlockPos pos, boolean behindWall) {
		if (!this.enabled) {
			return false;
		}
		if (this.weight <= 0) {
			return false;
		}
		if (this.chance <= 0) {
			return false;
		}
		if (this.isModDependencyMissing()) {
			return false;
		}
		if (!this.isValidDim(world.provider.getDimension())) {
			return false;
		}
		if (this.isDungeonDependencyMissing(world)) {
			return false;
		}
		if (DungeonDataManager.getInstance(world).isDungeonSpawnLimitMet(this)) {
			return false;
		}
		if (world.provider.getDimension() == 0 && this.spawnOnlyBehindWall && !behindWall) {
			return false;
		}
		return this.isValidBiome(world.getBiome(pos));
	}

	public boolean canSpawnInChunkWithLockedPosition(World world, int chunkX, int chunkZ) {
		if (!this.enabled) {
			return false;
		}
		if (this.isModDependencyMissing()) {
			return false;
		}
		if (!this.isValidDim(world.provider.getDimension())) {
			return false;
		}
		return this.isLockedPositionInChunk(chunkX, chunkZ);
	}

	public boolean isModDependencyMissing() {
		for (String s : this.modDependencies) {
			if (!Loader.isModLoaded(s)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidDim(int dim) {
		if (this.allowedInAllDims) {
			return true;
		}
		for (int i : this.allowedDims) {
			if (i == dim) {
				return true;
			}
		}
		return false;
	}

	public boolean isDungeonDependencyMissing(World world) {
		Set<String> spawnedDungeons = DungeonDataManager.getSpawnedDungeonNames(world);
		for (String s : this.dungeonDependencies) {
			if (!spawnedDungeons.contains(s)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidBiome(Biome biome) {
		ResourceLocation biomeName = biome.getRegistryName();
		Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biome);
		boolean flag = this.allowedInAllBiomes;

		if (!flag) {
			for (ResourceLocation rs : this.allowedBiomes) {
				if (rs.equals(biomeName)) {
					flag = true;
					break;
				}
			}
		}
		if (!flag) {
			for (BiomeDictionary.Type biomeType : biomeTypes) {
				for (String s : this.allowedBiomeTypes) {
					if (s.equals(biomeType.getName())) {
						flag = true;
						break;
					}
				}
			}
		}

		if (flag) {
			for (ResourceLocation rs : this.disallowedBiomes) {
				if (rs.equals(biomeName)) {
					flag = false;
					break;
				}
			}
		}
		if (flag) {
			for (BiomeDictionary.Type biomeType : biomeTypes) {
				for (String s : this.disallowedBiomeTypes) {
					if (s.equals(biomeType.getName())) {
						flag = false;
						break;
					}
				}
			}
		}

		return flag;
	}

	public boolean isLockedPositionInChunk(int chunkX, int chunkZ) {
		for (BlockPos p : this.lockedPositions) {
			if (p.getX() >> 4 == chunkX && p.getZ() >> 4 == chunkZ) {
				return true;
			}
		}
		return false;
	}

	public List<BlockPos> getLockedPositionsInChunk(int chunkX, int chunkZ) {
		List<BlockPos> list = new ArrayList<>();
		for (BlockPos p : this.lockedPositions) {
			if (p.getX() >> 4 == chunkX && p.getZ() >> 4 == chunkZ) {
				list.add(p);
			}
		}
		return list;
	}

	public String getDungeonName() {
		return name;
	}

	public int getIconID() {
		return iconID;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public int getWeight() {
		return weight;
	}

	public int getChance() {
		return chance;
	}

	public int getSpawnLimit() {
		return spawnLimit;
	}

	public int[] getAllowedDims() {
		return allowedDims;
	}

	public boolean isAllowedInAllDims() {
		return allowedInAllDims;
	}

	public ResourceLocation[] getAllowedBiomes() {
		return allowedBiomes;
	}

	public String[] getAllowedBiomeTypes() {
		return allowedBiomeTypes;
	}

	public boolean isAllowedInAllBiomes() {
		return allowedInAllBiomes;
	}

	public ResourceLocation[] getDisallowedBiomes() {
		return disallowedBiomes;
	}

	public String[] getDisallowedBiomeTypes() {
		return disallowedBiomeTypes;
	}

	public BlockPos[] getLockedPositions() {
		return lockedPositions;
	}

	public boolean doesSpawnOnlyBehindWall() {
		return spawnOnlyBehindWall;
	}

	public String[] getModDependencies() {
		return modDependencies;
	}

	public String[] getDungeonDependencies() {
		return dungeonDependencies;
	}

	public String getDungeonMob() {
		return dungeonMob;
	}

	public boolean replaceBanners() {
		return replaceBanners;
	}

	public int getUnderGroundOffset() {
		return underGroundOffset;
	}

	public int getYOffset() {
		return yOffset;
	}

	public boolean doBuildSupportPlatform() {
		return buildSupportPlatform;
	}

	public IBlockState getSupportBlock() {
		return supportBlock;
	}

	public IBlockState getSupportTopBlock() {
		return supportTopBlock;
	}

	public boolean isCoverBlockEnabled() {
		return useCoverBlock;
	}

	public IBlockState getCoverBlock() {
		return coverBlock;
	}

	public boolean isProtectionSystemEnabled() {
		return enableProtectionSystem;
	}

	public boolean preventBlockPlacing() {
		return preventBlockPlacing;
	}

	public boolean preventBlockBreaking() {
		return preventBlockBreaking;
	}

	public boolean preventExplosionsTNT() {
		return preventExplosionsTNT;
	}

	public boolean preventExplosionsOther() {
		return preventExplosionsOther;
	}

	public boolean preventFireSpreading() {
		return preventFireSpreading;
	}

	public boolean preventEntitySpawning() {
		return preventEntitySpawning;
	}

	public boolean ignoreNoBossOrNexus() {
		return ignoreNoBossOrNexus;
	}

}
