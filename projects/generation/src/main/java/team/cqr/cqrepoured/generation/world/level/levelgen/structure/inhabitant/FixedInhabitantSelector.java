package team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import team.cqr.cqrepoured.generation.init.CQRDataPackRegistries;

public record FixedInhabitantSelector(
		DungeonInhabitant inhabitant
	) implements InhabitantSelector {
	
	public static final Codec<FixedInhabitantSelector> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				CQRDataPackRegistries.INHABITANTS.byNameCodec().fieldOf("inhabitant").forGetter(FixedInhabitantSelector::inhabitant)
			).apply(instance, FixedInhabitantSelector::new);
	});

	@Override
	public DungeonInhabitant get(GenerationContext context, BlockPos pos) {
		return this.inhabitant;
	}

	@Override
	public InhabitantSelectorType type() {
		return InhabitantSelectorType.FIXED;
	}

}
