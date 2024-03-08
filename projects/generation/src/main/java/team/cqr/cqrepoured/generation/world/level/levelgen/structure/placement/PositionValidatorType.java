package team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement;

import java.util.Locale;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;

public enum PositionValidatorType implements StringRepresentable {

	EVERYWHERE(Codec.unit(new PositionValidator() {
		@Override
		public boolean validatePosition(ChunkPos chunkPos) {
			return true;
		}

		@Override
		public PositionValidatorType type() {
			return EVERYWHERE;
		}
	}));

	public static final Codec<PositionValidatorType> CODEC = StringRepresentable.fromEnum(PositionValidatorType::values);
	private final Codec<? extends PositionValidator> codec;

	private PositionValidatorType(Codec<? extends PositionValidator> codec) {
		this.codec = codec;
	}

	public Codec<? extends PositionValidator> codec() {
		return codec;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
