package team.cqr.cqrepoured.common.entity.profile.variant;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;

public record AssetEntry(
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