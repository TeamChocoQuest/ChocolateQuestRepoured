package team.cqr.cqrepoured.entity.profile;

import java.util.Collections;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EntityProfile(
		EntityBaseData baseData,
		EntityVariant defaultData,
		Map<String, EntityVariant> variants
	) {
	
	public static final Codec<EntityProfile> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				EntityBaseData.CODEC.fieldOf("base").forGetter(EntityProfile::baseData),
				EntityVariant.CODEC.fieldOf("default").forGetter(EntityProfile::defaultData),
				Codec.unboundedMap(Codec.STRING, EntityVariant.CODEC).optionalFieldOf("variants", Collections.emptyMap()).forGetter(EntityProfile::variants)
			).apply(instance, EntityProfile::new);
	});

}
