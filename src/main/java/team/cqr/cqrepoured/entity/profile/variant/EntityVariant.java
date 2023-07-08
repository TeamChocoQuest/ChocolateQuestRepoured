package team.cqr.cqrepoured.entity.profile.variant;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EntityVariant(
		int weight,
		SizeEntry size,
		DamageEntry damageConfig,
		List<AttributeEntry> attributes,
		List<AssetEntry> assets
		) {
	
	public static final Codec<EntityVariant> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.optionalFieldOf("weight", 0).forGetter(EntityVariant::weight),
				SizeEntry.CODEC.fieldOf("size").forGetter(EntityVariant::size),
				DamageEntry.CODEC.fieldOf("damage").forGetter(EntityVariant::damageConfig),
				AttributeEntry.CODEC.listOf().fieldOf("attributes").forGetter(EntityVariant::attributes),
				//TODO: Change to weighted list!
				AssetEntry.CODEC.listOf().fieldOf("assets").forGetter(EntityVariant::assets)
				
			).apply(instance, EntityVariant::new);
	});

}
