package team.cqr.cqrepoured.world.structure.generation.thewall;

import com.mojang.serialization.Codec;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.world.CQRJigsawManager;

public class WallStructure extends Structure<NoFeatureConfig> {

	public WallStructure(Codec<NoFeatureConfig> pCodec) {
		super(pCodec);
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
		/*if (!CQRConfig.wall.enabled) {
			return false;
		}*/
		
		//int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		
		return chunkZ < 0 && Math.abs(chunkZ) == Math.abs(500/*CQRConfig.wall.distance*/);
	}
	
	public static class Start extends StructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
		}

		final ResourceLocation START_POOL_WALL = CQRMain.prefix("wall/start_wall");
		final ResourceLocation START_POOL_TOWER = CQRMain.prefix("wall/start_tower");
		
		@Override
		public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
			boolean tower = (chunkX % 3/*CQRConfig.wall.towerDistance*/) == 0;
			BlockPos centered = new BlockPos((chunkX * 16) -16, 140, (chunkZ * 16) -16);
			ResourceLocation startPool;
			if(tower) {
				startPool = START_POOL_TOWER;
			} else {
				startPool = START_POOL_WALL;
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
	
	

}
