package team.cqr.cqrepoured.entity.profile.variant;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;

public record DamageEntry(
		Optional<DamageCap> damageCap,
		double minDamage,
		boolean fireImmune,
		Map<ResourceLocation, Float> damageTypeMultipliers
) {
	public static final Codec<DamageEntry> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				DamageCap.CODEC.optionalFieldOf("damage-cap").forGetter(DamageEntry::damageCap),
				Codec.DOUBLE.fieldOf("min-damage").forGetter(DamageEntry::minDamage),
				Codec.BOOL.fieldOf("fire-immune").forGetter(DamageEntry::fireImmune),
				Codec.unboundedMap(ResourceLocation.CODEC, Codec.FLOAT).optionalFieldOf("multipliers", Collections.emptyMap()).forGetter(DamageEntry::damageTypeMultipliers)
			).apply(instance, DamageEntry::new);
	});
}