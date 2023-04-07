package team.cqr.cqrepoured.world.structure.generation.dungeons;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.ModList;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.util.PropertyFileHelper;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.DungeonSpawnPos;
import team.cqr.cqrepoured.world.structure.generation.grid.DungeonGrid;
import team.cqr.cqrepoured.world.structure.generation.grid.GridRegistry;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public abstract class DungeonBase implements IFeatureConfig {

	// TODO: Write codec that uses the old prop file to re-create the object
	// => Store the prop file somewhere in the object as string
	public static final Codec<DungeonBase> CODEC = RecordCodecBuilder.create((object) -> {
		return object.group(
				Codec.STRING.fieldOf("name").forGetter((obj) -> {
					return obj.getDungeonName();
				}),
				Codec.STRING.fieldOf("propertyfile").forGetter((obj) -> {
					return obj.getPropFileAsString();
				})
			).apply(object, (name, propString) -> {
				//If a dungeon with this name has already been registered, use the registered object
				DungeonBase dunBase = DungeonRegistry.getInstance().getDungeon(name);
				if(dunBase != null) {
					dunBase.tryAssignGrid();
					return dunBase;
				}
				//Otherwise restore the dungeon object
				Properties prop = getFromString(propString);
				if(prop != null) {
					DungeonBase db = DungeonRegistry.createDungeonFromFile(prop, name);
					db.tryAssignGrid();
					return db;
				}
				return null;
			});
	});
	
	protected final void tryAssignGrid() {
		for(DungeonGrid grid : GridRegistry.getInstance().getGrids()) {
			for(DungeonBase gdb : grid.getDungeons()) {
				if(gdb.getName().equalsIgnoreCase(this.getDungeonName())) {
					this.assignGrid(grid);
				}
			}
		}
	}
	
	String getPropFileAsString() {
		return this.SCANNED_PROPERTIES_FILE;
	}
			
	protected final String SCANNED_PROPERTIES_FILE;
	
	protected String name;
	protected boolean enabled = true;
	protected int iconID = 0;
	protected Set<DungeonGrid> grids = new HashSet<>();

	protected int weight = 0;
	protected int chance = 0;
	protected int spawnLimit = -1;
	protected ResourceLocation[] allowedDims = new ResourceLocation[0];
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
	protected String[] structuresPreventingGeneration = new String[0];
	protected int structureCheckRadius = 0;

	protected boolean treatWaterAsAir = false;
	protected int underGroundOffset = 0;
	protected boolean fixedY = false;

	protected int yOffsetMin = 0;
	protected int yOffsetMax = 0;

	protected String dungeonMob = "DEFAULT";//DungeonInhabitantManager.DEFAULT_DUNGEON_INHABITANT.getName();
	protected boolean replaceBanners = true;

	protected boolean buildSupportPlatform = true;

	protected boolean useCoverBlock;
	protected BlockState coverBlock;

	// Protection system stuff
	protected boolean enableProtectionSystem = false;
	protected boolean preventBlockPlacing = false;
	protected boolean preventBlockBreaking = false;
	protected boolean preventExplosionsTNT = false;
	protected boolean preventExplosionsOther = false;
	protected boolean preventFireSpreading = false;
	protected boolean preventEntitySpawning = false;
	protected boolean ignoreNoBossOrNexus = false;
	
	protected DungeonBase(String name, Properties prop) {
		this.SCANNED_PROPERTIES_FILE = DungeonBase.writePropertiesToString(prop);
		
		this.name = name;
		String newName = "";
		for(int i = 0; i < this.name.length(); i++) {
			char c = this.name.charAt(i);
			if(Character.isUpperCase(c)) {
				if(i > 0) {
					newName = newName + "_";
				}
				c = Character.toLowerCase(c);
			}
			newName = newName + c;
		}
		this.name = newName;
		
		this.enabled = PropertyFileHelper.getBooleanProperty(prop, "enabled", this.enabled);
		this.iconID = PropertyFileHelper.getIntProperty(prop, "icon", this.iconID, 0, 19);

		this.weight = PropertyFileHelper.getIntProperty(prop, "weight", this.weight, 0, Integer.MAX_VALUE);
		this.chance = PropertyFileHelper.getIntProperty(prop, "chance", this.chance, 0, 100);
		this.spawnLimit = PropertyFileHelper.getIntProperty(prop, "spawnLimit", this.spawnLimit, -1, Integer.MAX_VALUE);
		this.allowedDims = PropertyFileHelper.getResourceLocationArrayProperty(prop, "allowedDims", this.allowedDims, true);
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
		this.structuresPreventingGeneration = PropertyFileHelper.getStringArrayProperty(prop, "structuresPreventingGeneration", this.structuresPreventingGeneration, true);
		this.structureCheckRadius = PropertyFileHelper.getIntProperty(prop, "structureCheckRadius", this.structureCheckRadius, 0, 128);

		this.treatWaterAsAir = PropertyFileHelper.getBooleanProperty(prop, "treatWaterAsAir", this.treatWaterAsAir);
		this.underGroundOffset = PropertyFileHelper.getIntProperty(prop, "undergroundoffset", this.underGroundOffset, 0, Integer.MAX_VALUE);
		this.fixedY = PropertyFileHelper.getBooleanProperty(prop, "fixedY", this.fixedY);
		this.yOffsetMin = PropertyFileHelper.getIntProperty(prop, "yOffsetMin", this.yOffsetMin);
		this.yOffsetMax = PropertyFileHelper.getIntProperty(prop, "yOffsetMax", this.yOffsetMax, this.yOffsetMin, Integer.MAX_VALUE);

		this.dungeonMob = prop.getProperty("dummyReplacement", this.dungeonMob);
		this.replaceBanners = PropertyFileHelper.getBooleanProperty(prop, "replaceBanners", this.replaceBanners);

		this.buildSupportPlatform = PropertyFileHelper.getBooleanProperty(prop, "buildsupportplatform", this.buildSupportPlatform);

		this.useCoverBlock = PropertyFileHelper.getBooleanProperty(prop, "usecoverblock", false);
		this.coverBlock = PropertyFileHelper.getBlockStateProperty(prop, "coverblock", Blocks.AIR.defaultBlockState());

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

	public static final String writePropertiesToString(Properties prop) {
		StringWriter writer = new StringWriter();
		String text;
		try {
			prop.store(writer, "");
			text = normalizeNewLines(writer.toString());
			if(writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		List<String> lines = new ArrayList<>(Arrays.asList(text.split("\n")));
		//Remove timestamp
		lines.remove(0);
		return lines.stream().collect(Collectors.joining("\n"));
	}
	
	public static final String normalizeNewLines(String text) {
	    return text.replace("\r\n", "\n").replace("\r", "\n");
	}
	
	public static final Properties getFromString(final String s) {
		final Properties prop = new Properties();
		if(s.isEmpty()) {
			return prop;
		}
		try {
			StringReader sr = new StringReader(s);
			prop.load(sr);
			sr.close();
		} catch (IOException e) {
			e.printStackTrace();
			return new Properties();
		}
		return prop;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	public String getName() {
		return this.name;
	}

	public abstract Collection<StructurePiece> runGenerator(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random);

	public Collection<StructurePiece> generate(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random, DungeonSpawnType spawnType) {
		int x = pos.getX();
		int z = pos.getZ();
		BlockPos pos1 = new BlockPos(x, this.getYForPos(chunkGenerator, x, z, random), z);
		return this.generateAt(dynamicRegistries, chunkGenerator, templateManager, pos1, random, spawnType);
	}

	public int getYForPos(ChunkGenerator chunkGenerator, int x, int z, Random rand) {
		int y = 0;
		if (!this.fixedY) {
			// TODO make this a dungeon config option?
			int r = 16;
			int[] arr = new int[(r * 2 + 1) * (r * 2 + 1)];
			for (int ix = -r; ix <= r; ix++) {
				for (int iz = -r; iz <= r; iz++) {
					arr[(ix + r) * (r * 2 + 1) + (iz + r)] = DungeonGenUtils.getYForPos(chunkGenerator, x + ix, z + iz, this.treatWaterAsAir);
				}
			}
			Arrays.sort(arr);

			y = arr[(int) (arr.length * 0.6)];
			y += DungeonGenUtils.randomBetween(this.yOffsetMin, this.yOffsetMax, rand);
		} else {
			y = DungeonGenUtils.randomBetween(this.yOffsetMin, this.yOffsetMax, rand);
		}
		y -= this.getUnderGroundOffset();
		return y;
	}

	public Collection<StructurePiece> generateAt(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random, DungeonSpawnType spawnType) {
		return this.runGenerator(dynamicRegistries, chunkGenerator, templateManager, pos, random);
	}

	public Collection<StructurePiece> generateWithOffsets(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, BlockPos pos, Random random, DungeonSpawnType spawnType) {
		if (!this.fixedY) {
			pos = pos.above(DungeonGenUtils.randomBetween(this.yOffsetMin, this.yOffsetMax, random));
		}
		pos = pos.below(this.getUnderGroundOffset());
		return this.generateAt(dynamicRegistries, chunkGenerator, templateManager, pos, random, spawnType);
	}

	@Nullable
	public File getStructureFileFromDirectory(File parentDir, Random rand) {
		Collection<File> files = FileUtils.listFiles(parentDir, new String[] { "nbt" }, true);
		if (files.isEmpty()) {
			return null;
		}
		return files.stream().skip(rand.nextInt(files.size())).findFirst().get();
	}

	public boolean canSpawnInDim(ResourceLocation dim) {
		if (this.isModDependencyMissing()) {
			return false;
		}
		if (!this.enabled) {
			return false;
		}
		if (this.weight <= 0) {
			return false;
		}
		if (this.chance <= 0) {
			return false;
		}
		return this.isValidDim(dim);
	}

	public boolean canSpawnAt(World world, Biome biome, BlockPos pos) {
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
		if (!this.isValidDim(world.dimension())) {
			return false;
		}
		if (this.isDungeonDependencyMissing(world)) {
			return false;
		}
		if (DungeonDataManager.isDungeonSpawnLimitMet(world, this)) {
			return false;
		}
		if (this.spawnOnlyBehindWall && world.dimension() == World.OVERWORLD && CQRConfig.SERVER_CONFIG.wall.enabled.get() && pos.getZ() >> 4 < -CQRConfig.SERVER_CONFIG.wall.distance.get()) {
			return false;
		}
		if (!this.isValidBiome(biome)) {
			return false;
		}
		return !this.isStructureNearby(world, pos);
	}

	public boolean canSpawnInChunkWithLockedPosition(World level, ChunkPos chunkPos) {
		if (!this.enabled) {
			return false;
		}
		if (this.isModDependencyMissing()) {
			return false;
		}
		if (!this.isValidDim(level.dimension().location())) {
			return false;
		}
		return this.isLockedPositionInChunk(level, chunkPos);
	}

	public boolean isModDependencyMissing() {
		for (String s : this.modDependencies) {
			if (!ModList.get().isLoaded(s)) {
				return true;
			}
		}
		return false;
	}

	public boolean isValidDim(RegistryKey<World> registryKey) {
		return this.isValidDim(registryKey.location());
	}
	
	public boolean isValidDim(ResourceLocation dim) {
		for (ResourceLocation i : this.allowedDims) {
			if (i.equals(dim)) {
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
		return this.isValidBiome(biomeName);
	}
	
	public boolean isValidBiome(ResourceLocation biomeName) {
		RegistryKey<Biome> biomeKey = RegistryKey.create(Registry.BIOME_REGISTRY, biomeName);
		
		Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biomeKey);
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
					if (s.equalsIgnoreCase(biomeType.getName())) {
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
					if (s.equalsIgnoreCase(biomeType.getName())) {
						flag = false;
						break;
					}
				}
			}
		}

		return flag;
	}

	public boolean isStructureNearby(World world, BlockPos pos) {
		if (!CQRConfig.SERVER_CONFIG.advanced.generationRespectOtherStructures.get()) {
			return false;
		}
		//TODO: Reimplement
		/*for (String structure : this.structuresPreventingGeneration) {
			if (StructureHelper.isStructureInRange(world, pos, this.structureCheckRadius, structure)) {
				return true;
			}
		}*/
		return false;
	}

	public boolean isLockedPositionInChunk(IWorld level, ChunkPos chunkPos) {
		return Arrays.stream(this.lockedPositions).anyMatch(spawnPosition -> spawnPosition.isInChunk(level, chunkPos));
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

	public ResourceLocation[] getAllowedDims() {
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

	public boolean isCoverBlockEnabled() {
		return this.useCoverBlock;
	}

	public BlockState getCoverBlock() {
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
	
	public boolean isFixedY() {
		return fixedY;
	}

	public void assignGrid(DungeonGrid dungeonGrid) {
		this.grids.add(dungeonGrid);
	}

	public Collection<DungeonGrid> getGrids() {
		return Collections.unmodifiableCollection(this.grids);
	}

}
