package team.cqr.cqrepoured.entity.profile.variant;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record DamageCap(
		boolean enabled,
		float maxUncappedDamage,
		float maxDamageInMaxHPPercent
) {
	public static final Codec<DamageCap> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.BOOL.fieldOf("enabled").forGetter(DamageCap::enabled),
				Codec.FLOAT.fieldOf("max-uncapped-damage").forGetter(DamageCap::maxUncappedDamage),
				Codec.FLOAT.fieldOf("max-damage-in-percent").forGetter(DamageCap::maxDamageInMaxHPPercent)
			).apply(instance, DamageCap::new);
	});
	
	public float capDamage(float originalValue, Supplier<Float> healthProvider) {
		if (!enabled) {
			return originalValue;
		}
		float percentageHP = healthProvider.get() * this.maxDamageInMaxHPPercent;
		return Math.min(Math.max(this.maxUncappedDamage, percentageHP), originalValue);
	}
	
}