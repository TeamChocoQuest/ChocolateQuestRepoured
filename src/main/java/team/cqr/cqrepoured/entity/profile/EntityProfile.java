package team.cqr.cqrepoured.entity.profile;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import team.cqr.cqrepoured.entity.profile.variant.EntityVariant;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.LazyLoadField;

public class EntityProfile {
	
	protected final EntityBaseData baseData;
	protected final EntityVariant defaultData;
	protected final Map<String, EntityVariant> variants;
	
	private final LazyLoadField<CQRWeightedRandom<EntityVariant>> variantWeightedList = new LazyLoadField<>(this::generateWeightedVariantList);
	
	public EntityProfile(EntityBaseData baseData, EntityVariant defaultData, Map<String, EntityVariant> variants) {
		this.baseData = baseData;
		this.defaultData = defaultData;
		this.variants = variants;
	}
	
	private final CQRWeightedRandom<EntityVariant> generateWeightedVariantList() {
		CQRWeightedRandom<EntityVariant> result = new CQRWeightedRandom<>();
		for(EntityVariant variant : this.variants.values()) {
			result.add(variant, variant.weight());
		}
		result.add(this.defaultData, this.defaultData.weight());
		
		return result;
	}
	
	public static final Codec<EntityProfile> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				EntityBaseData.CODEC.fieldOf("base").forGetter(profile -> {return profile.baseData;}),
				EntityVariant.CODEC.fieldOf("default").forGetter(profile -> {return profile.defaultData;}),
				Codec.unboundedMap(Codec.STRING, EntityVariant.CODEC).optionalFieldOf("variants", Collections.emptyMap()).forGetter(profile -> {return profile.variants;})
			).apply(instance, EntityProfile::new);
	});
	
	@Nullable
	public EntityVariant getRandomVariant() {
		return this.variantWeightedList.get().next();
	}
	
	@Nullable
	public EntityVariant getRandomVariant(final Random random) {
		return this.variantWeightedList.get().next(random);
	}
	
	public EntityBaseData baseData() {
		return this.baseData;
	}
	
	public EntityVariant defaultData() {
		return this.defaultData;
	}
	
	public EntityVariant getByName(String name) {
		if (name != null && this.variants.containsKey(name)) {
			return this.variants.getOrDefault(name, this.defaultData);
		}
		return this.defaultData;
	}

}
