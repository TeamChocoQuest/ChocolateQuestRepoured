package team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant;

import java.util.List;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import net.minecraft.world.phys.Vec3;
import team.cqr.cqrepoured.generation.init.CQRDataPackRegistries;

public record DistanceInhabitantSelector(
		double sectionRadius, 
		boolean randomOutsideRange, 
		List<Either<DungeonInhabitant, DungeonInhabitant>> inhabitants
	) implements InhabitantSelector {

	public static final Codec<DistanceInhabitantSelector> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.doubleRange(1, Integer.MAX_VALUE).fieldOf("changeRadius").forGetter(DistanceInhabitantSelector::sectionRadius),
				Codec.BOOL.optionalFieldOf("selectRandomWhenOutOfMaxRange", true).forGetter(DistanceInhabitantSelector::randomOutsideRange),
				Codec.either(CQRDataPackRegistries.INHABITANTS.byNameCodec(), DungeonInhabitant.CODEC).listOf().fieldOf("inhabitants").forGetter(DistanceInhabitantSelector::inhabitants)
			).apply(instance, DistanceInhabitantSelector::new);
	});
	
	@Override
	public DungeonInhabitant get(GenerationContext context, BlockPos pos) {
		if (inhabitants.size() <= 0) {
			return null;
		}
		
		// TODO: How to access the worldspawn??
		double distance = Vec3.atCenterOf(pos).length();
		int index = (int)distance % (int)sectionRadius;
		index = Math.min(index, inhabitants.size() -1);
		Either<DungeonInhabitant, DungeonInhabitant> either = this.inhabitants.get(index);
		
		if (either.left().isPresent()) {
			return either.left().get();
		} else if (either.right().isPresent()) {
			return either.right().get();
		}
		return null;
	}

	@Override
	public InhabitantSelectorType type() {
		return InhabitantSelectorType.DISTANCE;
	}

}
