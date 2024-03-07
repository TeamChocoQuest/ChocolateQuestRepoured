package team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement;

import java.util.Locale;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

public enum PositionValidatorType implements StringRepresentable {

	;

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
