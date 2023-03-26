package team.cqr.cqrepoured.world.structure;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.mojang.serialization.Codec;
import com.sk89q.worldedit.history.changeset.ArrayListHistory;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.grid.DungeonGrid;
import team.cqr.cqrepoured.world.structure.generation.grid.GridRegistry;

public class StructureGridCQR<T extends DungeonGrid> extends Structure<T> {

	private final boolean underground;

	public StructureGridCQR(Codec<T> pCodec, final boolean underground) {
		super(pCodec);

		this.underground = underground;
	}

	@Override
	public Decoration step() {
		if (this.underground) {
			return Decoration.UNDERGROUND_STRUCTURES;
		}
		return Decoration.SURFACE_STRUCTURES;
	}

	@Override
	protected boolean linearSeparation() {
		return false;
	}

	private T config = null;
	private DungeonBase selectedCurrently = null;
	private ChunkGenerator chunkGeneratorTmp = null;
	private Biome currentBiome = null;
	private long currentSeed = -1;

	@Override
	public StructureStart<?> generate(DynamicRegistries p_242785_1_, ChunkGenerator p_242785_2_, BiomeProvider p_242785_3_, TemplateManager p_242785_4_, long p_242785_5_, ChunkPos p_242785_7_, Biome p_242785_8_, int p_242785_9_,
			SharedSeedRandom p_242785_10_, StructureSeparationSettings p_242785_11_, T p_242785_12_) {
		//return StructureStart.INVALID_START;
		this.config = p_242785_12_;
		this.chunkGeneratorTmp = p_242785_2_;
		this.selectedCurrently = null;
		this.currentBiome = p_242785_8_;
		try {
			int pX = p_242785_7_.x;
			int pZ = p_242785_7_.z;
			
			Optional<ServerWorld> sw = StructureGridCQR.tryFindWorldForChunkGenerator(this.chunkGeneratorTmp);
			if (sw.isPresent()) {
				boolean noFeatureChunk = !this.isFeatureChunk(p_242785_2_, p_242785_3_, p_242785_9_, p_242785_10_, pX, pZ, p_242785_8_, p_242785_7_, p_242785_12_);
				if(noFeatureChunk) {
					return StructureStart.INVALID_START;
				}
			}
			
			if(this.selectedCurrently == null) {
				
				return StructureStart.INVALID_START;
			}
			
			StructureStart<?> start = super.generate(p_242785_1_, p_242785_2_, p_242785_3_, p_242785_4_, p_242785_5_, p_242785_7_, p_242785_8_, p_242785_9_, p_242785_10_, p_242785_11_, p_242785_12_);
			
			return start;
		} finally {
			this.config = null;
			this.chunkGeneratorTmp = null;
			this.currentBiome = null;
			this.currentSeed = -1;
		}
		
	}

	//TODO: Currently causes problems, leading to literal littering of structures
	/*@Override
	public ChunkPos getPotentialFeatureChunk(StructureSeparationSettings pSeparationSettings, long pSeed, SharedSeedRandom pRandom, int pX, int pZ) {
		ChunkPos parentResult = super.getPotentialFeatureChunk(pSeparationSettings, pSeed, pRandom, pX, pZ);
		if (this.config != null && this.chunkGeneratorTmp != null) {
			Optional<ServerWorld> sw = StructureGridCQR.tryFindWorldForChunkGenerator(this.chunkGeneratorTmp);
			if (sw.isPresent()) {
				if(getDungeonAt(this.config, sw.get(), pX, pZ, this.currentBiome) != null) {
					//System.out.println("pos valid");
					return new ChunkPos(pX, pZ);
				}
				if(getDungeonAt(this.config, sw.get(), parentResult.x, parentResult.z, this.currentBiome) != null) {
					//System.out.println("parentpos valid");
					return parentResult;
				}
				//System.out.println("pos invalid");
				//return new ChunkPos(pX +1, pZ +1);
			}
			
		}
		return parentResult;
	}*/
	
	@Nullable
	protected DungeonBase getDungeonAt(ServerWorld world, int chunkX, int chunkZ, Biome biome, SharedSeedRandom seedRandom) {
		if(!canSpawnDungeonsInWorld(world) || this.config == null) {
			return null;
		}
		long seed = WorldDungeonGenerator.getSeed(world.getSeed(), chunkX, chunkZ);
		seedRandom.setLargeFeatureSeed(this.currentSeed, chunkX, chunkZ);
		
		DungeonBase locationSpecificDungeon = getLocationSpecificDungeon(world, chunkX, chunkZ);
		if (locationSpecificDungeon != null) {
			return locationSpecificDungeon;
		}
		
		DungeonBase myDungeon = this.config.getDungeonAt(world, chunkX, chunkZ, true, biome, seedRandom);
		//DungeonBase selected = GridRegistry.getInstance().getGrids().stream().map(grid -> grid.getDungeonAt(world, chunkX, chunkZ)).filter(Objects::nonNull).findFirst().orElse(null);
		/*
		if(myDungeon == null || selected == null) {
			return null;
		}
		
		if(myDungeon.getDungeonName().equalsIgnoreCase(selected.getDungeonName())) {
			return myDungeon;
		}*/
		return myDungeon;
	}
	
	public static boolean canSpawnDungeonsInWorld(ServerWorld world) {
		// Check if structures are enabled for this world
		if (!world.getServer().getWorldData().worldGenSettings().generateFeatures()) {
			return false;
		}
		// Check for flat world type and if dungeons may spawn there
		if (!(world.getChunkSource().getGenerator() instanceof FlatChunkGenerator)) {
			return true;
		}
		return CQRConfig.SERVER_CONFIG.general.dungeonsInFlat.get();
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long p_230363_3_, SharedSeedRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome biome, ChunkPos chunkPos, T featureConfig) {
		this.currentSeed = p_230363_3_;
		if (!super.isFeatureChunk(chunkGenerator, biomeProvider, p_230363_3_, p_230363_5_, p_230363_6_, p_230363_7_, biome, chunkPos, featureConfig)) {
			return false;
		}
		//return false;
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		
		Optional<ServerWorld> osw = tryFindWorldForChunkGenerator(chunkGenerator);
		if (osw.isPresent()) {
			ServerWorld sw = osw.get();
			
			// if(!this.config.isChunkOnGrid(sw, chunkX, chunkZ)) {
			// 	 return false;
			// }

			if(this.config.canSpawnDungeonAtCoordsIgnoreGridCheck(sw, chunkX, chunkZ, p_230363_5_, biome)) {
				if(this.selectedCurrently == null) {
					this.selectedCurrently = getDungeonAt(sw, chunkX, chunkZ, biome, p_230363_5_);
					
				}
				return this.selectedCurrently != null;
			}
			
		}
		return false;
	}

	public static Optional<ServerWorld> tryFindWorldForChunkGenerator(ChunkGenerator cg) {
		ServerWorld sw = null;
		for (ServerWorld world : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
			if (world.getChunkSource().generator == cg) {
				sw = world;
				break;
			}
		}
		return Optional.fromNullable(sw);
	}
	
	@Nullable
	public static DungeonBase getLocationSpecificDungeon(World world, int chunkX, int chunkZ) {
		// Spawn all coordinate specific dungeons for this chunk
		List<DungeonBase> locationSpecificDungeons = DungeonRegistry.getInstance().getLocationSpecificDungeonsForChunk(world, chunkX, chunkZ);
		if (locationSpecificDungeons.isEmpty()) {
			return null;
		}
		if (locationSpecificDungeons.size() > 1) {
			CQRMain.logger.warn("Found {} location specific dungeons for chunkX={}, chunkZ={}!", locationSpecificDungeons.size(), chunkX, chunkZ);
		}
		return locationSpecificDungeons.get(0);
	}

	// The structure start can hold data like we want, we just need to return a supplier for them here
	// Gets called in "createStart(...)" => Override that so we can inject our own data! => That gets called in generate, so we could use our own code here too
	@Override
	public IStartFactory<T> getStartFactory() {
		return new IStartFactory<T>() {

			@Override
			public StructureStart<T> create(Structure<T> structureObj, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int referenceIn, long seedIn) {
				return new StructureStartGridCQR<T>(structureObj, chunkX, chunkZ, boundingBox, referenceIn, seedIn, StructureGridCQR.this.selectedCurrently);
			}
			
		};
	}

}
