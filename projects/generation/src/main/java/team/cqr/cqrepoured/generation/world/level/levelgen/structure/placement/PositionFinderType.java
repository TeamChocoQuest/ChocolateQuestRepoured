package team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement;

import java.util.Locale;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

public enum PositionFinderType implements StringRepresentable {

	;

	public static final Codec<PositionFinderType> CODEC = StringRepresentable.fromEnum(PositionFinderType::values);
	private final Codec<? extends PositionFinder> codec;

	private PositionFinderType(Codec<? extends PositionFinder> codec) {
		this.codec = codec;
	}

	public Codec<? extends PositionFinder> codec() {
		return codec;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
