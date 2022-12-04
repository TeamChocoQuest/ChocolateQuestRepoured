package team.cqr.cqrepoured.world.structure;

import java.util.Objects;

import com.google.common.base.Optional;
import com.mojang.serialization.Codec;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;
import team.cqr.cqrepoured.world.structure.generation.grid.DungeonGrid;

public class StructureDungeonCQR<T extends DungeonBase> extends Structure<T> {

	private final boolean underground;

	public StructureDungeonCQR(Codec<T> pCodec, final boolean underground) {
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
	private ChunkGenerator chunkGeneratorTmp = null;

	@Override
	public StructureStart<?> generate(DynamicRegistries p_242785_1_, ChunkGenerator p_242785_2_, BiomeProvider p_242785_3_, TemplateManager p_242785_4_, long p_242785_5_, ChunkPos p_242785_7_, Biome p_242785_8_, int p_242785_9_,
			SharedSeedRandom p_242785_10_, StructureSeparationSettings p_242785_11_, T p_242785_12_) {
		this.config = p_242785_12_;
		this.chunkGeneratorTmp = p_242785_2_;
		
		StructureStart<?> start = super.generate(p_242785_1_, p_242785_2_, p_242785_3_, p_242785_4_, p_242785_5_, p_242785_7_, p_242785_8_, p_242785_9_, p_242785_10_, p_242785_11_, p_242785_12_);
		
		this.config = null;
		this.chunkGeneratorTmp = null;
		return start;
	}

	@Override
	public ChunkPos getPotentialFeatureChunk(StructureSeparationSettings pSeparationSettings, long pSeed, SharedSeedRandom pRandom, int pX, int pZ) {
		if (this.config != null && this.chunkGeneratorTmp != null) {
			Optional<ServerWorld> sw = StructureDungeonCQR.tryFindWorldForChunkGenerator(this.chunkGeneratorTmp);
			if (sw.isPresent()) {
				for (DungeonGrid grid : this.config.getGrids()) {
					if (grid.isChunkOnGrid(sw.get(), pX, pZ)) {
						return new ChunkPos(pX, pZ);
					}
				}
			}
			return new ChunkPos(pX +1, pZ +1);
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

	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long p_230363_3_, SharedSeedRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome biome, ChunkPos chunkPos, T featureConfig) {
		if (!super.isFeatureChunk(chunkGenerator, biomeProvider, p_230363_3_, p_230363_5_, p_230363_6_, p_230363_7_, biome, chunkPos, featureConfig)) {
			return false;
		}
		for (ResourceLocation rs : featureConfig.getDisallowedBiomes()) {
			if (biome.getRegistryName().equals(rs)) {
				return false;
			}
		}
		for (ResourceLocation rs : featureConfig.getAllowedBiomes()) {
			if (!biome.getRegistryName().equals(rs)) {
				return false;
			}
		}
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		if (!featureConfig.isUseVanillaSpreadSystem()) {
			Optional<ServerWorld> osw = tryFindWorldForChunkGenerator(chunkGenerator);
			if (osw.isPresent()) {
				ServerWorld sw = osw.get();

				DungeonBase gridSelected = featureConfig.getGrids().stream().map(grid -> grid.getDungeonAt(sw, chunkX, chunkZ)).filter(Objects::nonNull).findFirst().orElse(null);
				if(gridSelected == null) {
					return false;
				}
				return gridSelected.getDungeonName().equalsIgnoreCase(featureConfig.getDungeonName());
			}
		}
		return true;
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

	// The structure start can hold data like we want, we just need to return a supplier for them here
	// Gets called in "createStart(...)" => Override that so we can inject our own data! => That gets called in generate, so we could use our own code here too
	@Override
	public IStartFactory<T> getStartFactory() {
		return StructureStartCQR::new;
	}

}
