package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.world.structure.generation.dungeons.PositionFinder;
import team.cqr.cqrepoured.world.structure.generation.dungeons.PositionValidator;

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

}
