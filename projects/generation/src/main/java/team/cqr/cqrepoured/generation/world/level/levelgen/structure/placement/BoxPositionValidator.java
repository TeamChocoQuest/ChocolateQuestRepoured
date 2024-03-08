package team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.ChunkPos;

public record BoxPositionValidator(int minX, int minZ, int maxX, int maxZ) implements PositionValidator {

	public static final Codec<BoxPositionValidator> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.optionalFieldOf("minX", Integer.MIN_VALUE).forGetter(BoxPositionValidator::minX),
				Codec.INT.optionalFieldOf("minZ", Integer.MIN_VALUE).forGetter(BoxPositionValidator::minZ),
				Codec.INT.optionalFieldOf("maxX", Integer.MAX_VALUE).forGetter(BoxPositionValidator::maxX),
				Codec.INT.optionalFieldOf("maxZ", Integer.MAX_VALUE).forGetter(BoxPositionValidator::maxZ))
				.apply(instance, BoxPositionValidator::new);
	});

	@Override
	public boolean validatePosition(ChunkPos chunkPos) {
		int x = chunkPos.getMiddleBlockX();
		if (x < minX) return false;
		if (x > maxX) return false;
		int z = chunkPos.getMiddleBlockZ();
		if (z < minZ) return false;
		if (z > maxZ) return false;
		return true;
	}

	@Override
	public PositionValidatorType type() {
		return PositionValidatorType.BOX;
	}

}
