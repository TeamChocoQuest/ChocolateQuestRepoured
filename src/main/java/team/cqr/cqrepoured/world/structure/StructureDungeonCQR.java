package team.cqr.cqrepoured.world.structure;

import java.util.Objects;

import com.google.common.base.Optional;
import com.mojang.serialization.Codec;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class StructureDungeonCQR<T extends DungeonBase> extends Structure<T> {

	private final boolean underground;
	
	public StructureDungeonCQR(Codec<T> pCodec, final boolean underground) {
		super(pCodec);
		
		this.underground = underground;
	}
	
	@Override
	public Decoration step() {
		if(this.underground) {
			return Decoration.UNDERGROUND_STRUCTURES;
		}
		return Decoration.SURFACE_STRUCTURES;
	}

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long p_230363_3_, SharedSeedRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome biome, ChunkPos chunkPos,
			T featureConfig) {
		if(!super.isFeatureChunk(chunkGenerator, biomeProvider, p_230363_3_, p_230363_5_, p_230363_6_, p_230363_7_, biome, chunkPos, featureConfig)) {
			return false;
		}
		for(ResourceLocation rs : featureConfig.getDisallowedBiomes()) {
			if(biome.getRegistryName().equals(rs)) {
				return false;
			}
		}
		for(ResourceLocation rs : featureConfig.getAllowedBiomes()) {
			if(!biome.getRegistryName().equals(rs)) {
				return false;
			}
		}
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		if(!featureConfig.isUseVanillaSpreadSystem()) {
			Optional<ServerWorld> osw = tryFindWorldForChunkGenerator(chunkGenerator);
			if(osw.isPresent()) {
				ServerWorld sw = osw.get();
				
				DungeonBase gridSelected = featureConfig.getGrids().stream().map(grid -> grid.getDungeonAt(sw, chunkX, chunkZ)).filter(Objects::nonNull).findFirst().orElse(null);
				return gridSelected.getDungeonName().equalsIgnoreCase(featureConfig.getDungeonName());
			}
		}
		return true;
	}
	
	protected static Optional<ServerWorld> tryFindWorldForChunkGenerator(ChunkGenerator cg) {
		ServerWorld sw = null;
		for(ServerWorld world : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
			if(world.getChunkSource().generator == cg) {
				sw = world;
				break;
			}
		}
		return Optional.fromNullable(sw);
	}

	// The structure start can hold data like we want, we just need to return a supplier for them here
	// Gets called in "createStart(...)" => Override that so we can inject our own data! => That gets called in generate, so we could use our own code here too
	@Override
	public IStartFactory<T> getStartFactory() {
		return StructureStartCQR::new;
	}
	
}
