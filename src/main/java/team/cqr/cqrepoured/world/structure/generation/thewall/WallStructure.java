package team.cqr.cqrepoured.world.structure.generation.thewall;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.StructureType;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.CQRJigsawManager;

public class WallStructure extends Structure {

	protected final WallFeatureConfig featureConfig;
	
	public WallStructure(net.minecraft.world.level.levelgen.structure.Structure.StructureSettings settings, WallFeatureConfig featureConfig) {
		super(settings);
		this.featureConfig = featureConfig;
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
		
	}
	
	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
		pContext.
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StructureType<?> type() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
