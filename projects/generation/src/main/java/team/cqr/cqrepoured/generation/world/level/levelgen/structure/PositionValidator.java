package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import com.mojang.serialization.Codec;

import net.minecraft.world.level.ChunkPos;

public interface PositionValidator {

	Codec<PositionValidator> CODEC = PositionValidatorType.CODEC.dispatch(PositionValidator::type, PositionValidatorType::codec);

	boolean validatePosition(ChunkPos chunkPos);

	PositionValidatorType type();

}
