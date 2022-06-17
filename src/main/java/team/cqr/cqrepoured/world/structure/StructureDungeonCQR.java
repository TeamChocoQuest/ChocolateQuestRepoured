package team.cqr.cqrepoured.world.structure;

import java.util.Objects;

import com.google.common.base.Predicates;
import com.mojang.serialization.Codec;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.structure.Structure;
import team.cqr.cqrepoured.world.structure.config.CQRDungeonFeatureConfig;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.grid.GridRegistry;

public class StructureDungeonCQR extends Structure<CQRDungeonFeatureConfig> {

	private final boolean underground;
	private final DungeonBase dungeonConfig;
	
	public StructureDungeonCQR(Codec<CQRDungeonFeatureConfig> pCodec, final boolean underground, final DungeonBase dungeonData) {
		super(pCodec);
		
		this.underground = underground;
		this.dungeonConfig = dungeonData;
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
			CQRDungeonFeatureConfig featureConfig) {
		if(!super.isFeatureChunk(chunkGenerator, biomeProvider, p_230363_3_, p_230363_5_, p_230363_6_, p_230363_7_, biome, chunkPos, featureConfig)) {
			return false;
		}
		for(ResourceLocation rs : this.dungeonConfig.getDisallowedBiomes()) {
			if(biome.getRegistryName().equals(rs)) {
				return false;
			}
		}
		for(ResourceLocation rs : this.dungeonConfig.getAllowedBiomes()) {
			if(!biome.getRegistryName().equals(rs)) {
				return false;
			}
		}
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		if(!this.dungeonConfig.isUseVanillaSpreadSystem()) {
			// Check the grid!
			// Problem: How do we access the world?
			//DungeonBase gridSelected = GridRegistry.getInstance().getGrids().stream().filter(Predicates.alwaysTrue()).map(grid -> grid.getDungeonAt(this.level, chunkX, chunkZ)).filter(Objects::nonNull).findFirst().orElse(null);
			//return gridSelected.getDungeonName().equalsIgnoreCase(this.dungeonConfig.getDungeonName());
		}
		return true;
	}

	// The structure start can hold data like we want, we just need to return a supplier for them here
	// Gets called in "createStart(...)" => Override that so we can inject our own data! => That gets called in generate, so we could use our own code here too
	@Override
	public IStartFactory<CQRDungeonFeatureConfig> getStartFactory() {
		return null;
	}
	
}
