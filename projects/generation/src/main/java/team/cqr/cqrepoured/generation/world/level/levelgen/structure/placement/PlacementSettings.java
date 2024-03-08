package team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import team.cqr.cqrepoured.common.random.RandomUtil;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.DungeonDataManager;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.WorldDungeonGenerator;

public record PlacementSettings(double chance, double rarityFactor, int spawnLimit, List<ResourceLocation> dungeonDependencies,
		List<PositionValidator> positionValidators, List<ResourceLocation> structuresPreventingGeneration, int structureCheckRadius,
		List<PositionFinder> positionFinders) {

	public static final Codec<PlacementSettings> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.DOUBLE.optionalFieldOf("chance", 1.0D).forGetter(PlacementSettings::chance),
				Codec.DOUBLE.optionalFieldOf("rarity_factor", 0.0D).forGetter(PlacementSettings::rarityFactor),
				Codec.INT.optionalFieldOf("spawn_limit", 0).forGetter(PlacementSettings::spawnLimit),
				Codec.list(ResourceLocation.CODEC).optionalFieldOf("dungeon_dependencies", List.of()).forGetter(PlacementSettings::dungeonDependencies),
				Codec.list(PositionValidator.CODEC).optionalFieldOf("position_validators", List.of()).forGetter(PlacementSettings::positionValidators),
				Codec.list(ResourceLocation.CODEC).optionalFieldOf("structures_preventing_generation", List.of()).forGetter(PlacementSettings::structuresPreventingGeneration),
				Codec.INT.optionalFieldOf("structure_check_radius", 0).forGetter(PlacementSettings::structureCheckRadius),
				Codec.list(PositionFinder.CODEC).fieldOf("position_finders").forGetter(PlacementSettings::positionFinders))
				.apply(instance, PlacementSettings::new);
	});

	public Optional<BlockPos> findGenerationPoint(Structure structure, GenerationContext context) {
		ServerLevel level = WorldDungeonGenerator.getLevel(context.chunkGenerator());
		if (this.spawnLimit > 0) {
			ResourceLocation structureName = context.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
			if (DungeonDataManager.getDungeonGenerationCount(level, structureName) >= this.spawnLimit) {
				return Optional.empty();
			}
		}
		if (!this.dungeonDependencies.isEmpty() && !DungeonDataManager.getSpawnedDungeonNames(level).containsAll(this.dungeonDependencies)) {
			return Optional.empty();
		}
		if (!this.positionValidators.isEmpty() && !this.positionValidators.stream().allMatch(v -> v.validatePosition(context.chunkPos()))) {
			return Optional.empty();
		}
		// TODO check for nearby non-cqr structures
		return findPosition(context);
	}

	private Optional<BlockPos> findPosition(GenerationContext context) {
		Set<BlockPos> positions = new ObjectOpenHashSet<>();

		if (!this.positionFinders.isEmpty()) {
			for (PositionFinder positionFinder : this.positionFinders) {
				positions = positionFinder.findPosition(context, context.chunkPos(), positions);
			}
		}

		return RandomUtil.random(positions, context.random());
	}

	public BlockPos applyOffsets(GenerationContext context, BlockPos pos) {
		if (!this.positionFinders.isEmpty()) {
			for (PositionFinder positionFinder : this.positionFinders) {
				pos = positionFinder.applyOffsets(context, pos);
			}
		}
		return pos;
	}

}
