package team.cqr.cqrepoured.entity.profile.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record DamageCap(
		boolean enabled,
		double maxUncappedDamage,
		double maxDamageInMaxHPPercent
) {
	public static final Codec<DamageCap> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.BOOL.fieldOf("enabled").forGetter(DamageCap::enabled),
				Codec.DOUBLE.fieldOf("max-uncapped-damage").forGetter(DamageCap::maxUncappedDamage),
				Codec.DOUBLE.fieldOf("max-damage-in-percent").forGetter(DamageCap::maxDamageInMaxHPPercent)
			).apply(instance, DamageCap::new);
	});
}