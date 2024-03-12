package team.cqr.cqrepoured.generation.world.level.levelgen.structure.generator;

import java.util.Locale;

import com.mojang.serialization.Codec;

import net.minecraft.util.StringRepresentable;

public enum StructureGeneratorType implements StringRepresentable {

	TEMPLATE(TemplateStructureGenerator.CODEC);

	public static final Codec<StructureGeneratorType> CODEC = StringRepresentable.fromEnum(StructureGeneratorType::values);
	private final Codec<? extends StructureGenerator> codec;

	private StructureGeneratorType(Codec<? extends StructureGenerator> codec) {
		this.codec = codec;
	}

	public Codec<? extends StructureGenerator> codec() {
		return codec;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
