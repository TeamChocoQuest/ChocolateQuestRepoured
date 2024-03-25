package team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import team.cqr.cqrepoured.generation.init.CQRDataPackRegistries;

public record FixedInhabitantSelector(
		Either<DungeonInhabitant, DungeonInhabitant> inhabitant
	) implements InhabitantSelector {
	
	public static final Codec<FixedInhabitantSelector> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.either(CQRDataPackRegistries.INHABITANTS.byNameCodec(), DungeonInhabitant.CODEC).fieldOf("inhabitant").forGetter(FixedInhabitantSelector::inhabitant)
			).apply(instance, FixedInhabitantSelector::new);
	});

	@Override
	public DungeonInhabitant get(GenerationContext context, BlockPos pos) {
		if (this.inhabitant.left().isPresent()) {
			return this.inhabitant.left().get();
		} else if (this.inhabitant.right().isPresent()) {
			return this.inhabitant.right().get();
		}
		return null;
	}

	@Override
	public InhabitantSelectorType type() {
		return InhabitantSelectorType.FIXED;
	}

}
