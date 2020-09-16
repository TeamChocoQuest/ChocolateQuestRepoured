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
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonSpawnPos;
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

	protected String name;
	protected boolean enabled = true;
	protected int iconID = 0;

	protected int weight = 0;
	protected int chance = 0;
	protected int spawnLimit = -1;
	protected int[] allowedDims = new int[0];
	protected boolean allowedDimsAsBlacklist = false;
	protected ResourceLocation[] allowedBiomes = new ResourceLocation[0];
	protected String[] allowedBiomeTypes = new String[0];
	protected boolean allowedInAllBiomes = false;
	protected ResourceLocation[] disallowedBiomes = new ResourceLocation[0];
	protected String[] disallowedBiomeTypes = new String[0];
	protected DungeonSpawnPos[] lockedPositions = new DungeonSpawnPos[0];
	protected boolean spawnOnlyBehindWall = false;
	protected String[] modDependencies = new String[0];
	protected String[] dungeonDependencies = new String[0];

	protected boolean treatWaterAsAir = false;
	protected int underGroundOffset = 0;
	protected int yOffset = 0;

	protected String dungeonMob = DungeonInhabitantManager.DEFAULT_INHABITANT_IDENT;
	protected boolean replaceBanners = true;

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
		this.iconID = PropertyFileHelper.getIntProperty(prop, "icon", this.iconID, 0, 19);

		this.weight = PropertyFileHelper.getIntProperty(prop, "weight", this.weight, 0, Integer.MAX_VALUE);
		this.chance = PropertyFileHelper.getIntProperty(prop, "chance", this.chance, 0, 100);
		this.spawnLimit = PropertyFileHelper.getIntProperty(prop, "spawnLimit", this.spawnLimit, -1, Integer.MAX_VALUE);
		this.allowedDims = PropertyFileHelper.getIntArrayProperty(prop, "allowedDims", this.allowedDims, true);
		this.allowedDimsAsBlacklist = PropertyFileHelper.getBooleanProperty(prop, "allowedDimsAsBlacklist", this.allowedDimsAsBlacklist);
		this.allowedBiomes = PropertyFileHelper.getResourceLocationArrayProperty(prop, "allowedBiomes", this.allowedBiomes, true);
		this.allowedBiomeTypes = PropertyFileHelper.getStringArrayProperty(prop, "allowedBiomeTypes", this.allowedBiomeTypes, true);
		this.allowedInAllBiomes = PropertyFileHelper.getBooleanProperty(prop, "allowedInAllBiomes", this.allowedInAllBiomes);
		this.disallowedBiomes = PropertyFileHelper.getResourceLocationArrayProperty(prop, "disallowedBiomes", this.disallowedBiomes, true);
		this.disallowedBiomeTypes = PropertyFileHelper.getStringArrayProperty(prop, "disallowedBiomeTypes", this.disallowedBiomeTypes, true);
		this.lockedPositions = PropertyFileHelper.getDungeonSpawnPosArrayProperty(prop, "lockedPositions", this.lockedPositions, true);
		this.spawnOnlyBehindWall = PropertyFileHelper.getBooleanProperty(prop, "spawnOnlyBehindWall", this.spawnOnlyBehindWall);
		this.modDependencies = PropertyFileHelper.getStringArrayProperty(prop, "modDependencies", this.modDependencies, true);
		this.dungeonDependencies = PropertyFileHelper.getStringArrayProperty(prop, "dungeonDependencies", this.dungeonDependencies, true);

		this.treatWaterAsAir = PropertyFileHelper.getBooleanProperty(prop, "treatWaterAsAir", this.treatWaterAsAir);
		this.underGroundOffset = PropertyFileHelper.getIntProperty(prop, "undergroundoffset", this.underGroundOffset, 0, Integer.MAX_VALUE);
		this.yOffset = PropertyFileHelper.getIntProperty(prop, "yoffset", this.yOffset);

		this.dungeonMob = prop.getProperty("dummyReplacement", this.dungeonMob);
		this.replaceBanners = PropertyFileHelper.getBooleanProperty(prop, "replaceBanners", this.replaceBanners);

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

	public abstract AbstractDungeonGenerator<? extends DungeonBase> createDungeonGenerator(World world, int x, int y, int z, Random rand);

	public void generate(World world, int x, int z, Random rand) {
		int chunkStartX = x >> 4 << 4;
		int chunkStartZ = z >> 4 << 4;
		int y = 0;
		for (int ix = 0; ix < 16; ix++) {
			for (int iz = 0; iz < 16; iz++) {
				y += DungeonGenUtils.getYForPos(world, chunkStartX + ix, chunkStartZ + iz, this.treatWaterAsAir);
			}
		}
		y >>= 8;
		y -= this.getUnderGroundOffset();
		y += this.getYOffset();
		this.generate(world, x, y, z, rand);
	}

	public void generate(World world, int x, int y, int z, Random rand) {
		new DungeonGeneratorThread(this.createDungeonGenerator(world, x, y, z, rand)).start();
	}

	public void generateWithOffsets(World world, int x, int y, int z, Random rand) {
		y -= this.getUnderGroundOffset();
		y += this.getYOffset();
		this.generate(world, x, y, z, rand);
	}

	public File getStructureFileFromDirectory(File parentDir, Random rand) {
		List<File> files = new ArrayList<>(FileUtils.listFiles(parentDir, new String[] { "nbt" }, true));
		if (!files.isEmpty()) {
			return files.get(rand.nextInt(files.size()));
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
		return this.isLockedPositionInChunk(world, chunkX, chunkZ);
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
		for (int i : this.allowedDims) {
			if (i == dim) {
				return !this.allowedDimsAsBlacklist;
			}
		}
		return this.allowedDimsAsBlacklist;
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

	public boolean isLockedPositionInChunk(World world, int chunkX, int chunkZ) {
		for (DungeonSpawnPos dungeonSpawnPos : this.lockedPositions) {
			if (dungeonSpawnPos.isInChunk(world, chunkX, chunkZ)) {
				return true;
			}
		}
		return false;
	}

	public List<DungeonSpawnPos> getLockedPositionsInChunk(World world, int chunkX, int chunkZ) {
		List<DungeonSpawnPos> list = new ArrayList<>();
		for (DungeonSpawnPos dungeonSpawnPos : this.lockedPositions) {
			if (dungeonSpawnPos.isInChunk(world, chunkX, chunkZ)) {
				list.add(dungeonSpawnPos);
			}
		}
		return list;
	}

	public String getDungeonName() {
		return this.name;
	}

	public int getIconID() {
		return this.iconID;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getChance() {
		return this.chance;
	}

	public int getSpawnLimit() {
		return this.spawnLimit;
	}

	public int[] getAllowedDims() {
		return this.allowedDims;
	}

	public boolean isAllowedDimsAsBlacklist() {
		return this.allowedDimsAsBlacklist;
	}

	public ResourceLocation[] getAllowedBiomes() {
		return this.allowedBiomes;
	}

	public String[] getAllowedBiomeTypes() {
		return this.allowedBiomeTypes;
	}

	public boolean isAllowedInAllBiomes() {
		return this.allowedInAllBiomes;
	}

	public ResourceLocation[] getDisallowedBiomes() {
		return this.disallowedBiomes;
	}

	public String[] getDisallowedBiomeTypes() {
		return this.disallowedBiomeTypes;
	}

	public DungeonSpawnPos[] getLockedPositions() {
		return this.lockedPositions;
	}

	public boolean doesSpawnOnlyBehindWall() {
		return this.spawnOnlyBehindWall;
	}

	public String[] getModDependencies() {
		return this.modDependencies;
	}

	public String[] getDungeonDependencies() {
		return this.dungeonDependencies;
	}

	public boolean treatWaterAsAir() {
		return this.treatWaterAsAir;
	}

	public int getUnderGroundOffset() {
		return this.underGroundOffset;
	}

	public int getYOffset() {
		return this.yOffset;
	}

	public String getDungeonMob() {
		return this.dungeonMob;
	}

	public boolean replaceBanners() {
		return this.replaceBanners;
	}

	public boolean doBuildSupportPlatform() {
		return this.buildSupportPlatform;
	}

	public IBlockState getSupportBlock() {
		return this.supportBlock;
	}

	public IBlockState getSupportTopBlock() {
		return this.supportTopBlock;
	}

	public boolean isCoverBlockEnabled() {
		return this.useCoverBlock;
	}

	public IBlockState getCoverBlock() {
		return this.coverBlock;
	}

	public boolean isProtectionSystemEnabled() {
		return this.enableProtectionSystem;
	}

	public boolean preventBlockPlacing() {
		return this.preventBlockPlacing;
	}

	public boolean preventBlockBreaking() {
		return this.preventBlockBreaking;
	}

	public boolean preventExplosionsTNT() {
		return this.preventExplosionsTNT;
	}

	public boolean preventExplosionsOther() {
		return this.preventExplosionsOther;
	}

	public boolean preventFireSpreading() {
		return this.preventFireSpreading;
	}

	public boolean preventEntitySpawning() {
		return this.preventEntitySpawning;
	}

	public boolean ignoreNoBossOrNexus() {
		return this.ignoreNoBossOrNexus;
	}

}
