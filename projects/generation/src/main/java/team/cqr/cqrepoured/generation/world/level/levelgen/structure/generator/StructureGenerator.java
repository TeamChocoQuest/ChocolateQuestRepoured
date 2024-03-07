package team.cqr.cqrepoured.generation.world.level.levelgen.structure.generator;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure.GenerationContext;
import team.cqr.cqrepoured.generation.init.CQRStructureGeneratorTypes;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.CQRStructurePiece;

public interface StructureGenerator {

	Codec<StructureGenerator> CODEC = CQRStructureGeneratorTypes.REGISTRY.get()
			.getCodec()
			.dispatch(StructureGenerator::type, StructureGeneratorType::codec);

	void prepare(GenerationContext context, BlockPos pos, CQRStructurePiece.Builder dungeonBuilder);

	StructureGeneratorType<?> type();

}
