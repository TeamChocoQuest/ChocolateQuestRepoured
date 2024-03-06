package team.cqr.cqrepoured.generation.world.level.levelgen.structure.generator;

import com.mojang.serialization.Codec;

public interface StructureGeneratorType<T extends StructureGenerator> {

	Codec<T> codec();

}
