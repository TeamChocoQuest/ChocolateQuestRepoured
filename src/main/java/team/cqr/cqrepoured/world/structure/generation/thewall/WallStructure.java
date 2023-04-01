package team.cqr.cqrepoured.world.structure.generation.thewall;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.CQRJigsawManager;

public class WallStructure extends Structure<NoFeatureConfig> {

	public WallStructure() {
		super(NoFeatureConfig.CODEC);
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return Start::new;
	}
	
	@Override
	public Decoration step() {
		return Decoration.SURFACE_STRUCTURES;
	}
	
	@Override
	protected boolean isFeatureChunk(ChunkGenerator cg, BiomeProvider bp, long seed, SharedSeedRandom ssr, int p_230363_6_, int p_230363_7_, Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
		if (!CQRConfig.SERVER_CONFIG.wall.enabled.get()) {
			return false;
		}
		
		//int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		
		return chunkZ < 0 && Math.abs(chunkZ) == Math.abs(CQRConfig.SERVER_CONFIG.wall.distance.get());
	}
	
	public static class Start extends StructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
		}

		final ResourceLocation START_POOL_WALL = CQRMain.prefix("wall/start_wall");
		final ResourceLocation START_POOL_TOWER = CQRMain.prefix("wall/start_tower");
		final ResourceLocation START_POOL_WALL_ROOFED = CQRMain.prefix("wall/start_wall_roofed");
		final ResourceLocation START_POOL_TOWER_ROOFED = CQRMain.prefix("wall/start_tower_roofed");
		
		@Override
		public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
			boolean tower = (chunkX % CQRConfig.SERVER_CONFIG.wall.towerDistance.get()) == 0;
			BlockPos centered = new BlockPos(chunkX * 16, 140, chunkZ * 16);
			ResourceLocation startPool;
			boolean isSnowy = this.isSnowChunk(chunkX, chunkZ, chunkGenerator);
			if(tower) {
				startPool = isSnowy ? START_POOL_TOWER_ROOFED : START_POOL_TOWER;
			} else {
				startPool = isSnowy ? START_POOL_WALL_ROOFED : START_POOL_WALL;
			}
			
			CQRJigsawManager.addPieces(
					dynamicRegistryManager, 
					new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(startPool), 10),
					AbstractVillagePiece::new,
					chunkGenerator,
					templateManagerIn,
					centered,
					pieces,
					random,
					false, 
					false,
					Rotation.NONE
			);
			
			this.calculateBoundingBox();
		}
		
		static final int TOTAL_CHUNK_AREA = 256; //16 * 16
		
		private boolean isSnowChunk(int chunkX, int chunkZ, ChunkGenerator cg) {
			int startX = chunkX * 16;
			int startZ = chunkZ * 16;
			int snowyBiomeBlocks = 0;
			for(int ix = startX; ix < startX + 16; ix++) {
				for(int iz = startZ; iz < startZ + 16; iz++) {
					int y = cg.getFirstFreeHeight(ix, iz, Type.WORLD_SURFACE);
					for(Biome b : cg.getBiomeSource().getBiomesWithin(ix, y, iz, 2)) {
						if(b.getBiomeCategory() == Category.ICY) {
							snowyBiomeBlocks++;
						}
					}
				}
			}
			return (snowyBiomeBlocks / TOTAL_CHUNK_AREA) >= 0.25;
		}
		
	}
	
	

}
