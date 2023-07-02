package team.cqr.cqrepoured.entity.profile;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public record EntityVariant(
		int weight,
		SizeEntry size,
		List<AttributeEntry> attributes,
		List<AssetEntry> assets
		) {
	
	public static final Codec<EntityVariant> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.optionalFieldOf("weight", 0).forGetter(EntityVariant::weight),
				SizeEntry.CODEC.fieldOf("size").forGetter(EntityVariant::size),
				AttributeEntry.CODEC.listOf().fieldOf("attributes").forGetter(EntityVariant::attributes),
				AssetEntry.CODEC.listOf().fieldOf("assets").forGetter(EntityVariant::assets)
			).apply(instance, EntityVariant::new);
	});
	
	public static record AttributeEntry(
			Attribute attribute, 
			double value
	) {
		public static final Codec<AttributeEntry> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					ForgeRegistries.ATTRIBUTES.getCodec().fieldOf("id").forGetter(AttributeEntry::attribute),
					Codec.DOUBLE.fieldOf("value").forGetter(AttributeEntry::value)
				).apply(instance, AttributeEntry::new);
		});
	}
	
	public static record AssetEntry(
			int weight, 
			Optional<ResourceLocation> optTexture, 
			Optional<ResourceLocation> optModel,
			Optional<ResourceLocation> optAnimations
	) {
		public static final Codec<AssetEntry> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					Codec.INT.fieldOf("weight").forGetter(AssetEntry::weight),
					ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(AssetEntry::optTexture),
					ResourceLocation.CODEC.optionalFieldOf("model").forGetter(AssetEntry::optModel),
					ResourceLocation.CODEC.optionalFieldOf("animations").forGetter(AssetEntry::optAnimations)
				).apply(instance, AssetEntry::new);
		});
	}
	
	public static record SizeEntry(
			float width, 
			float height
	) {
		public static final Codec<SizeEntry> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					Codec.FLOAT.fieldOf("width").forGetter(SizeEntry::width),
					Codec.FLOAT.fieldOf("height").forGetter(SizeEntry::height)
				).apply(instance, SizeEntry::new);
		});
	}

}
