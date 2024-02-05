package team.cqr.cqrepoured.world.structure.generation.thewall;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import team.cqr.cqrepoured.world.structure.generation.thewall.WallFeatureConfig.StructurePools;

public record WallFeatureConfig(
		int chunkZ,
		int yPosition,
		boolean placeRoofsInSnow,
		double minBlockCoverageForRoofs,
		StructurePools normalPool,
		StructurePools snowyPool,
		TagKey<Biome> snowBiomeTag
	) {
	
	public static final Codec<WallFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.fieldOf("chunk-pos-z").forGetter(WallFeatureConfig::chunkZ),
				Codec.INT.fieldOf("block-pos-y").forGetter(WallFeatureConfig::yPosition),
				Codec.BOOL.optionalFieldOf("allow-snow-roofs", false).forGetter(WallFeatureConfig::placeRoofsInSnow),
				Codec.DOUBLE.optionalFieldOf("min-snowy-blocks-percent", 0.0D).forGetter(WallFeatureConfig::minBlockCoverageForRoofs),
				StructurePools.CODEC.fieldOf("normal-pool").forGetter(WallFeatureConfig::normalPool),
				StructurePools.CODEC.optionalFieldOf("snowy-pool", null).forGetter(WallFeatureConfig::snowyPool),
				TagKey.hashedCodec(Registries.BIOME).fieldOf("snow-biome-tag").forGetter(WallFeatureConfig::snowBiomeTag)
			).apply(instance, WallFeatureConfig::new);
	});
	
	static final int TOTAL_CHUNK_AREA = 256; // 16 x 16
	
	public boolean isChunkSnowy(ChunkGenerator generator, int chunkX, int chunkZ) {
		if (!this.placeRoofsInSnow()) {
			return false;
		}
		if (this.snowyPool() == null) {
			return false;
		}
		int startX = chunkX * 16;
		int startZ = chunkZ * 16;
		int snowyBiomeBlocks = 0;
		for(int ix = startX; ix < startX + 16; ix++) {
			for(int iz = startZ; iz < startZ + 16; iz++) {
				/*int y = generator.getFirstFreeHeight(ix, iz, Types.WORLD_SURFACE_WG);
				for(Biome b : generator.getBiomeSource().getBiomesWithin(ix, y, iz, 2)) {
					if (b.)
				}*/
			}
		}
		return (snowyBiomeBlocks / TOTAL_CHUNK_AREA) >= this.minBlockCoverageForRoofs();
	}
	
	public static record StructurePools(
			ResourceLocation wall,
			ResourceLocation tower
		) {

		public static final Codec<StructurePools> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					ResourceLocation.CODEC.fieldOf("wall").forGetter(StructurePools::wall),
					ResourceLocation.CODEC.fieldOf("tower").forGetter(StructurePools::tower)
				).apply(instance, StructurePools::new);
		});
		
	}

}
