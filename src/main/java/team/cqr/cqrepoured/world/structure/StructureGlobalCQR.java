package team.cqr.cqrepoured.world.structure;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.mojang.serialization.Codec;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.util.DungeonGenUtils;
import team.cqr.cqrepoured.world.structure.generation.DungeonDataManager.DungeonSpawnType;
import team.cqr.cqrepoured.world.structure.generation.DungeonRegistry;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.grid.DungeonGrid;
import team.cqr.cqrepoured.world.structure.generation.grid.GridRegistry;

public class StructureGlobalCQR extends Structure<NoFeatureConfig> {

	public StructureGlobalCQR(Codec<NoFeatureConfig> pCodec) {
		super(pCodec);
	}

	@Override
	public Decoration step() {
		return Decoration.SURFACE_STRUCTURES;
	}

	@Override
	protected boolean linearSeparation() {
		return false;
	}

	private DungeonBase selectedCurrently = null;
	private ChunkGenerator chunkGeneratorTmp = null;

	@Override
	public StructureStart<?> generate(DynamicRegistries p_242785_1_, ChunkGenerator p_242785_2_, BiomeProvider p_242785_3_, TemplateManager p_242785_4_, long p_242785_5_, ChunkPos p_242785_7_, Biome p_242785_8_, int p_242785_9_,
			SharedSeedRandom p_242785_10_, StructureSeparationSettings p_242785_11_, NoFeatureConfig p_242785_12_) {
		this.chunkGeneratorTmp = p_242785_2_;

		StructureStart<?> start = super.generate(p_242785_1_, p_242785_2_, p_242785_3_, p_242785_4_, p_242785_5_, p_242785_7_, p_242785_8_, p_242785_9_, p_242785_10_, p_242785_11_, p_242785_12_);

		this.chunkGeneratorTmp = null;
		return start;
	}
	
	protected double getDistanceBetweenSqr(ChunkPos p1, ChunkPos p2) {
		return Math.pow((p2.x - p1.x), 2) + Math.pow((p2.z - p1.z), 2);
	}

	@Override
	public ChunkPos getPotentialFeatureChunk(StructureSeparationSettings pSeparationSettings, long pSeed, SharedSeedRandom pRandom, int pX, int pZ) {
		
		if (this.chunkGeneratorTmp != null) {
			Optional<ServerWorld> sw = StructureGridCQR.tryFindWorldForChunkGenerator(this.chunkGeneratorTmp);
			if (sw.isPresent()) {
				ChunkPos oldNearest = null;
				ChunkPos current = new ChunkPos(pX, pZ);
				double distanceOld = 0;
				for(DungeonGrid grid : GridRegistry.getInstance().getGrids()) {
					if(grid.isChunkOnGrid(sw.get(), pX, pZ)) {
						return current;
					} else {
						if(oldNearest == null) {
							oldNearest = grid.getPotentialChunkPosAtOrNear(sw.get(), pX, pZ);
							distanceOld = getDistanceBetweenSqr(oldNearest, current);
						} else {
							ChunkPos potNewNearest = grid.getPotentialChunkPosAtOrNear(sw.get(), pX, pZ);
							double distance = getDistanceBetweenSqr(potNewNearest, current);
							if(distance < distanceOld) {
								distanceOld = distance;
								oldNearest = potNewNearest;
							}
						}
					}
				}
				if(oldNearest != null) {
					return oldNearest;
				}
			}

		}
		int i = pSeparationSettings.spacing();
		int j = pSeparationSettings.separation();
		int k = Math.floorDiv(pX, i);
		int l = Math.floorDiv(pZ, i);
		pRandom.setLargeFeatureWithSalt(pSeed, k, l, pSeparationSettings.salt());
		int i1;
		int j1;
		if (this.linearSeparation()) {
			i1 = pRandom.nextInt(i - j);
			j1 = pRandom.nextInt(i - j);
		} else {
			i1 = (pRandom.nextInt(i - j) + pRandom.nextInt(i - j)) / 2;
			j1 = (pRandom.nextInt(i - j) + pRandom.nextInt(i - j)) / 2;
		}

		return new ChunkPos(k * i + i1, l * i + j1);
	}

	@Nullable
	protected static DungeonBase getDungeonAt(ServerWorld world, int chunkX, int chunkZ, Random rand) {
		if (!canSpawnDungeonsInWorld(world)) {
			return null;
		}

		DungeonBase locationSpecificDungeon = getLocationSpecificDungeon(world, chunkX, chunkZ);
		if (locationSpecificDungeon != null) {
			return locationSpecificDungeon;
		}

		return GridRegistry.getInstance().getGrids().stream().filter(grid -> grid.canSpawnDungeonAtCoords(world, chunkX, chunkZ, rand)).map(grid -> grid.getDungeonAt(world, chunkX, chunkZ)).filter(Objects::nonNull).findFirst().orElse(null);

		/*
		 * DungeonBase myDungeon = myGrid.getDungeonAt(world, chunkX, chunkZ); DungeonBase selected = GridRegistry.getInstance().getGrids().stream().map(grid -> grid.getDungeonAt(world, chunkX,
		 * chunkZ)).filter(Objects::nonNull).findFirst().orElse(null);
		 * 
		 * if(myDungeon == null || selected == null) { return null; }
		 * 
		 * if(myDungeon.getDungeonName().equalsIgnoreCase(selected.getDungeonName())) { return myDungeon; } return null;
		 */
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
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long p_230363_3_, SharedSeedRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
		if (!super.isFeatureChunk(chunkGenerator, biomeProvider, p_230363_3_, p_230363_5_, p_230363_6_, p_230363_7_, biome, chunkPos, featureConfig)) {
			return false;
		}
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;

		Optional<ServerWorld> osw = tryFindWorldForChunkGenerator(chunkGenerator);
		if (osw.isPresent()) {
			ServerWorld sw = osw.get();

			/*
			 * if(!this.config.isChunkOnGrid(sw, chunkX, chunkZ)) { return false; }
			 * 
			 * if(this.config.canSpawnDungeonAtCoords(sw, chunkX, chunkZ, p_230363_5_)) { this.selectedCurrently = getDungeonAt(this.config, sw, chunkX, chunkZ); return this.selectedCurrently != null; }
			 */
			this.selectedCurrently = getDungeonAt(sw, chunkX, chunkZ, p_230363_5_);
			return this.selectedCurrently != null;

		}
		return false;
	}

	protected static Optional<ServerWorld> tryFindWorldForChunkGenerator(ChunkGenerator cg) {
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
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return new IStartFactory<NoFeatureConfig>() {

			@Override
			public StructureStart<NoFeatureConfig> create(Structure<NoFeatureConfig> p_create_1_, int p_create_2_, int p_create_3_, MutableBoundingBox p_create_4_, int p_create_5_, long p_create_6_) {
				if (p_create_1_ instanceof StructureGlobalCQR) {
					return new StructureStartGlobalCQR(p_create_1_, p_create_2_, p_create_3_, p_create_4_, p_create_5_, p_create_6_, ((StructureGlobalCQR) p_create_1_).selectedCurrently);
				}
				return new StructureStartGlobalCQR(p_create_1_, p_create_2_, p_create_3_, p_create_4_, p_create_5_, p_create_6_, StructureGlobalCQR.this.selectedCurrently);
			}

		};
	}

	static class StructureStartGlobalCQR extends StructureStart<NoFeatureConfig> {

		private final DungeonBase dungeonObj;

		public StructureStartGlobalCQR(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn, final DungeonBase dungeonObj) {
			super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);

			this.dungeonObj = dungeonObj;
		}

		@Override
		public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
			if (this.dungeonObj == null) {
				return;
			}

			// Y Position => If fixed, set last bool to true, that will force the y position
			// If it is set to false => Heightmap is used
			// Offsets: At the end, shift all structures
			int shiftOffsetY = 0;
			int x = chunkX * 16;
			int z = chunkZ * 16;
			int y = 0;
			if (this.dungeonObj.isFixedY()) {
				y = DungeonGenUtils.randomBetween(this.dungeonObj.getYOffsetMin(), this.dungeonObj.getYOffsetMax(), this.random);
			} else {
				y = chunkGenerator.getBaseHeight(x, z, Type.WORLD_SURFACE_WG);
				shiftOffsetY = DungeonGenUtils.randomBetween(this.dungeonObj.getYOffsetMin(), this.dungeonObj.getYOffsetMax(), this.random);
			}
			shiftOffsetY -= this.dungeonObj.getUnderGroundOffset();
			ResourceLocation startPool = null;
			BlockPos centered = new BlockPos(x, y, z);

			// Run the dugneon generators...
			// this.pieces.add(this.dungeonObj.runGenerator(dynamicRegistryManager, chunkGenerator, templateManagerIn, centered.offset(0, shiftOffsetY, 0), random));
			System.out.println("Generating dungeon at " + centered.toString());
			Optional<ServerWorld> osw = tryFindWorldForChunkGenerator(chunkGenerator);
			if(!FMLEnvironment.production && osw.isPresent()) {
				ServerWorld sw = osw.get();
				sw.getServer().getPlayerList().broadcastMessage(new StringTextComponent("Generated dungeon at: " + centered.toString()), ChatType.SYSTEM, Util.NIL_UUID);
			}
			this.pieces.add(this.dungeonObj.generate(dynamicRegistryManager, chunkGenerator, templateManagerIn, centered.offset(0, shiftOffsetY, 0), random, DungeonSpawnType.DUNGEON_GENERATION));

			/*
			 * CQRJigsawManager.addPieces( dynamicRegistryManager, new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(startPool), 10), config.doBuildSupportPlatform() ?
			 * StructurePieceCQR::createWithSupportHill : StructurePieceCQR::createWithoutSupportHill, chunkGenerator, templateManagerIn, centered, this.pieces, this.random, false, !config.isFixedY(), Rotation.NONE );
			 */
			final int tmpInt = shiftOffsetY;
			// this.pieces.forEach(piece -> piece.move(0, tmpInt, 0));
			this.pieces.forEach(piece -> piece.getBoundingBox().y0 += tmpInt);
			this.pieces.forEach(piece -> piece.getBoundingBox().y1 += tmpInt);

			this.calculateBoundingBox();
		}

	}

}
