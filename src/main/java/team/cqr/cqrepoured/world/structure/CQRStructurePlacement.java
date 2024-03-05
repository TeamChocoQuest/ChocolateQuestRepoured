package team.cqr.cqrepoured.world.structure;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.storage.LevelData;
import team.cqr.cqrepoured.generation.init.CQRStructurePlacementTypes;
import team.cqr.cqrepoured.world.structure.generation.WorldDungeonGenerator;

public class CQRStructurePlacement extends RandomSpreadStructurePlacement {

	public static final Codec<CQRStructurePlacement> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
				FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", FrequencyReductionMethod.DEFAULT)
						.forGetter(CQRStructurePlacement::frequencyReductionMethod),
        		Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(CQRStructurePlacement::frequency),
        		ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(CQRStructurePlacement::salt),
				Codec.intRange(0, 4096).fieldOf("spacing").forGetter(CQRStructurePlacement::spacing),
				Codec.intRange(0, 4096).fieldOf("separation").forGetter(CQRStructurePlacement::separation),
				RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(CQRStructurePlacement::spreadType),
				ExtraCodecs.NON_NEGATIVE_INT.fieldOf("minimum_distance_to_spawn").forGetter(CQRStructurePlacement::minimumDistanceToSpawn),
				ExtraCodecs.NON_NEGATIVE_INT.fieldOf("priority").forGetter(CQRStructurePlacement::priority))
        		.apply(instance, CQRStructurePlacement::new);
    });

	private final int minimumDistanceToSpawn;
	private final int priority;

	public CQRStructurePlacement(FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, int spacing, int separation,
			RandomSpreadType spreadType, int minDistToSpawn, int priority) {
		super(Vec3i.ZERO, frequencyReductionMethod, frequency, salt, Optional.empty(), spacing, separation, spreadType);
		this.minimumDistanceToSpawn = minDistToSpawn;
		this.priority = priority;
	}

	@Override
	public StructurePlacementType<?> type() {
		return CQRStructurePlacementTypes.CQR_STRUCTURE_PLACEMENT_TYPE;
	}

	@Deprecated
	@Override
	public boolean isStructureChunk(ChunkGeneratorStructureState structureState, int x, int z) {
		return super.isStructureChunk(structureState, x, z);
	}

	public boolean isCQRStructureChunk(ChunkGenerator chunkGenerator, ChunkPos chunkPos) {
		ServerLevel level = WorldDungeonGenerator.getLevel(chunkGenerator);
		LevelData levelData = level.getLevelData();
		if (Mth.square(chunkPos.x - levelData.getXSpawn()) + Mth.square(chunkPos.z - levelData.getZSpawn()) < Mth.square(this.minimumDistanceToSpawn)) {
			return false;
		}
		if (!this.isStructureChunk(level.getChunkSource()
				.getGeneratorState(), chunkPos.x, chunkPos.z)) {
			return false;
		}
		return StructureLocator.getStructureAt(level, chunkPos)
				.isEmpty();
	}

	public int minimumDistanceToSpawn() {
		return minimumDistanceToSpawn;
	}

	public int priority() {
		return priority;
	}

}
