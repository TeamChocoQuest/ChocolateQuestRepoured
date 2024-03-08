package team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import team.cqr.cqrepoured.generation.init.CQRDataPackRegistries;

public record RandomInhabitantSelector(HolderSet<DungeonInhabitant> inhabitants) implements InhabitantSelector {

	public static final Codec<RandomInhabitantSelector> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				RegistryCodecs.homogeneousList(CQRDataPackRegistries.INHABITANTS.registryKey()).optionalFieldOf("inhabitants", HolderSet.direct()).forGetter(RandomInhabitantSelector::inhabitants))
				.apply(instance, RandomInhabitantSelector::new);
	});

	@Override
	public DungeonInhabitant get(GenerationContext context, BlockPos pos) {
		return inhabitants.getRandomElement(context.random()).map(Holder::get).orElse(null);
	}

	@Override
	public InhabitantSelectorType type() {
		return InhabitantSelectorType.RANDOM;
	}

}
