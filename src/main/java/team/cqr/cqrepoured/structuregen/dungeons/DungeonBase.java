package team.cqr.cqrepoured.structuregen.dungeons;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Loader;
import team.cqr.cqrepoured.structuregen.DungeonDataManager;
import team.cqr.cqrepoured.structuregen.DungeonGeneratorThread;
import team.cqr.cqrepoured.structuregen.DungeonSpawnPos;
import team.cqr.cqrepoured.structuregen.generators.AbstractDungeonGenerator;
import team.cqr.cqrepoured.structuregen.inhabitants.DungeonInhabitantManager;
import team.cqr.cqrepoured.util.CQRConfig;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PropertyFileHelper;

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
	protected boolean fixedY = false;
	protected int yOffsetMin = 0;
	protected int yOffsetMax = 0;

	protected String dungeonMob = DungeonInhabitantManager.DEFAULT_DUNGEON_INHABITANT.getName();
	protected boolean replaceBanners = true;

	protected boolean buildSupportPlatform = true;
	protected IBlockState supportBlock = null;
	protected IBlockState supportTopBlock = null;

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
		this.fixedY = PropertyFileHelper.getBooleanProperty(prop, "fixedY", this.fixedY);
		this.yOffsetMin = PropertyFileHelper.getIntProperty(prop, "yOffsetMin", this.yOffsetMin);
		this.yOffsetMax = PropertyFileHelper.getIntProperty(prop, "yOffsetMax", this.yOffsetMax, this.yOffsetMin, Integer.MAX_VALUE);

		this.dungeonMob = prop.getProperty("dummyReplacement", this.dungeonMob);
		this.replaceBanners = PropertyFileHelper.getBooleanProperty(prop, "replaceBanners", this.replaceBanners);

		this.buildSupportPlatform = PropertyFileHelper.getBooleanProperty(prop, "buildsupportplatform", this.buildSupportPlatform);
		this.supportBlock = PropertyFileHelper.getBlockStateProperty(prop, "supportblock", this.supportBlock);
		this.supportTopBlock = PropertyFileHelper.getBlockStateProperty(prop, "supportblocktop", this.supportTopBlock);

		this.useCoverBlock = PropertyFileHelper.getBooleanProperty(prop, "usecoverblock", false);
		this.coverBlock = PropertyFileHelper.getBlockStateProperty(prop, "coverblock", Blocks.AIR.getDefaultState());

		// protection system
		this.enableProtectionSystem = PropertyFileHelper.getBooleanProperty(prop, "enableProtectionSystem", false);
		this.preventBlockBreaking = PropertyFileHelper.getBooleanProperty(prop, "preventBlockBreaking", false);
		this.preventBlockPlacing = PropertyFileHelper.getBooleanProperty(prop, "preventBlockPlacing", false);
		this.preventExplosionsTNT = PropertyFileHelper.getBooleanProperty(prop, "preventExplosionsTNT", false);
		this.preventExplosionsOther = PropertyFileHelper.getBooleanProperty(prop, "preventExplosionsOther", false);
		this.preventFireSpreading = PropertyFileHelper.getBooleanProperty(prop, "preventFireSpreading", false);
		this.preventEntitySpawning = PropertyFileHelper.getBooleanProperty(prop, "preventEntitySpawning", false);
		this.ignoreNoBossOrNexus = PropertyFileHelper.getBooleanProperty(prop, "ignoreNoBossOrNexus", false);
	}

	@Override
	public String toString() {
		return this.name;
	}

	public abstract AbstractDungeonGenerator<? extends DungeonBase> createDungeonGenerator(World world, int x, int y, int z, Random rand, DungeonDataManager.DungeonSpawnType spawnType);

	public void generate(World world, int x, int z, Random rand, DungeonDataManager.DungeonSpawnType spawnType, boolean generateImmediately) {
		this.generate(world, x, this.getYForPos(world, x, z, rand), z, rand, spawnType, generateImmediately);
	}

	public int getYForPos(World world, int x, int z, Random rand) {
		int y = 0;
		if (!this.fixedY) {
			int chunkStartX = x >> 4 << 4;
			int chunkStartZ = z >> 4 << 4;
			for (int ix = 0; ix < 16; ix++) {
				for (int iz = 0; iz < 16; iz++) {
					y += DungeonGenUtils.getYForPos(world, chunkStartX + ix, chunkStartZ + iz, this.treatWaterAsAir);
				}
			}
			y >>= 8;
			y += DungeonGenUtils.randomBetween(this.yOffsetMin, this.yOffsetMax, rand);
		} else {
			y = DungeonGenUtils.randomBetween(this.yOffsetMin, this.yOffsetMax, rand);
		}
		y -= this.getUnderGroundOffset();
		return y;
	}

	public void generate(World world, int x, int y, int z, Random rand, DungeonDataManager.DungeonSpawnType spawnType, boolean generateImmediately) {
		AbstractDungeonGenerator<?> dungeonGenerator = this.createDungeonGenerator(world, x, y, z, rand, spawnType);

		if (!generateImmediately && CQRConfig.advanced.multithreadedDungeonPreparation) {
			DungeonGeneratorThread.add(dungeonGenerator);
		} else {
			dungeonGenerator.generate(generateImmediately);
		}
	}

	public void generateWithOffsets(World world, int x, int y, int z, Random rand, DungeonDataManager.DungeonSpawnType spawnType, boolean generateImmediately) {
		if (!this.fixedY) {
			y += DungeonGenUtils.randomBetween(this.yOffsetMin, this.yOffsetMax, rand);
		}
		y -= this.getUnderGroundOffset();
		this.generate(world, x, y, z, rand, spawnType, generateImmediately);
	}

	/*
	 * private Map<String, Integer> lastUsedFilePerDirectory = new ConcurrentHashMap<>();
	 * 
	 * public File getStructureFileFromDirectory(File parentDir, Random rand) { List<File> files = new ArrayList<>(FileUtils.listFiles(parentDir, new String[] {
	 * "nbt" }, true)); if (!files.isEmpty()) { File file =
	 * files.get(rand.nextInt(files.size())); Integer lastUsedFileHash = lastUsedFilePerDirectory.computeIfAbsent(parentDir.getAbsolutePath(), key -> new
	 * Integer(0)); if (lastUsedFileHash == 0) { lastUsedFileHash = file.hashCode(); } else if
	 * (files.size() > 1 && file.hashCode() == lastUsedFileHash) { while (file.hashCode() == lastUsedFileHash) { file = files.get(rand.nextInt(files.size())); } }
	 * return file; } return null; }
	 */

	private Map<String, Integer> lastUsedFilePerDirectory = new ConcurrentHashMap<>();

	@Nullable
	public File getStructureFileFromDirectory(File parentDir, Random rand) {
		Collection<File> files = FileUtils.listFiles(parentDir, new String[] { "nbt" }, true);
		List<File> filesL = (files instanceof List ? ((List<File>) files) : new ArrayList<>(files));
		if (filesL.isEmpty()) {
			return null;
		}
		if (CQRConfig.advanced.tryPreventingDuplicateDungeons) {
			File file;
			Integer lastUsedFileHash = this.lastUsedFilePerDirectory.computeIfAbsent(parentDir.getAbsolutePath(), key -> new Integer(0));
			do {
				file = filesL.get(rand.nextInt(files.size()));
				if (lastUsedFileHash == 0 || filesL.size() == 1) {
					lastUsedFileHash = file.hashCode();
					break;
				}
			} while (file.hashCode() == lastUsedFileHash);
			lastUsedFileHash = file.hashCode();
			return file;
		} else {
			return filesL.get(rand.nextInt(files.size()));
		}
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
		if (DungeonDataManager.isDungeonSpawnLimitMet(world, this)) {
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

	public int getYOffsetMin() {
		return this.yOffsetMin;
	}

	public int getYOffsetMax() {
		return this.yOffsetMax;
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
