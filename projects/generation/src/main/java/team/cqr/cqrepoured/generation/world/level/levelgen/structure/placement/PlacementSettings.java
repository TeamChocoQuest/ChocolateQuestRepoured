package team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonDataManager;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.WorldDungeonGenerator;

public record PlacementSettings(double chance, double rarityFactor, List<ResourceLocation> dungeonDependencies, int spawnLimit, PositionValidator positionValidator, List<ResourceLocation> structuresPreventingGeneration, int structureCheckRadius, PositionFinder positionFinder) {

	public static final Codec<PlacementSettings> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.DOUBLE.fieldOf("chance").forGetter(PlacementSettings::chance),
				Codec.DOUBLE.fieldOf("rarity_factor").forGetter(PlacementSettings::rarityFactor),
				Codec.list(ResourceLocation.CODEC).fieldOf("dungeon_dependencies").forGetter(PlacementSettings::dungeonDependencies),
				Codec.INT.fieldOf("spawn_limit").forGetter(PlacementSettings::spawnLimit),
				PositionValidator.CODEC.fieldOf("position_validator").forGetter(PlacementSettings::positionValidator),
				Codec.list(ResourceLocation.CODEC).fieldOf("structures_preventing_generation").forGetter(PlacementSettings::structuresPreventingGeneration),
				Codec.INT.fieldOf("structure_check_radius").forGetter(PlacementSettings::structureCheckRadius),
				PositionFinder.CODEC.fieldOf("position_finder").forGetter(PlacementSettings::positionFinder))
				.apply(instance, PlacementSettings::new);
	});

	public Optional<BlockPos> findGenerationPoint(Structure structure, GenerationContext context) {
		ServerLevel level = WorldDungeonGenerator.getLevel(context.chunkGenerator());
		ResourceLocation structureName = context.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
		if (DungeonDataManager.getDungeonGenerationCount(level, structureName) >= this.spawnLimit) {
			return Optional.empty();
		}
		if (!DungeonDataManager.getSpawnedDungeonNames(level).containsAll(this.dungeonDependencies)) {
			return Optional.empty();
		}
		if (!this.positionValidator.validatePosition(context.chunkPos())) {
			return Optional.empty();
		}
		// TODO check for nearby non-cqr structures
		return Optional.of(this.positionFinder.findPosition(context, context.chunkPos()));
	}

}
