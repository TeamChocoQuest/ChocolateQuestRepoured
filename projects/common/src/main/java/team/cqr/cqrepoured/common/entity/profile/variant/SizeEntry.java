package team.cqr.cqrepoured.common.entity.profile.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SizeEntry(
		float width, 
		float height
) {
	public static final Codec<SizeEntry> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(Codec.FLOAT.fieldOf("width").forGetter(SizeEntry::width), Codec.FLOAT.fieldOf("height").forGetter(SizeEntry::height)).apply(instance, SizeEntry::new);
	});
}
