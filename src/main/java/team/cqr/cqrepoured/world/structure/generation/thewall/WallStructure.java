package team.cqr.cqrepoured.world.structure.generation.thewall;

import com.mojang.serialization.Codec;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPieceTower;
import team.cqr.cqrepoured.world.structure.generation.thewall.wallparts.WallPieceWall;

public class WallStructure extends Structure<NoFeatureConfig> {

	public WallStructure(Codec<NoFeatureConfig> pCodec) {
		super(pCodec);
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return null;
	}
	
	@Override
	protected boolean isFeatureChunk(ChunkGenerator cg, BiomeProvider bp, long seed, SharedSeedRandom ssr, int p_230363_6_, int p_230363_7_, Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
		if (!CQRConfig.wall.enabled) {
			return false;
		}
		
		//int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		
		return chunkZ < 0 && Math.abs(chunkZ) == Math.abs(CQRConfig.wall.distance);
	}
	
	public static class Start extends StructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> p_i225876_1_, int p_i225876_2_, int p_i225876_3_, MutableBoundingBox p_i225876_4_, int p_i225876_5_, long p_i225876_6_) {
			super(p_i225876_1_, p_i225876_2_, p_i225876_3_, p_i225876_4_, p_i225876_5_, p_i225876_6_);
		}

		@Override
		public void generatePieces(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager p_230364_3_, int chunkX, int chunkZ, Biome p_230364_6_, NoFeatureConfig p_230364_7_) {
			boolean tower = chunkX % CQRConfig.wall.towerDistance == 0;
			if(tower) {
				this.pieces.add(new WallPieceTower());
			} else {
				this.pieces.add(new WallPieceWall());
			}
			this.calculateBoundingBox();
		}
		
	}
	
	

}
