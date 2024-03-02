package team.cqr.cqrepoured.protection;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ProtectionSettings(boolean preventBlockPlacing, boolean preventBlockBreaking, boolean preventExplosionsTNT, boolean preventExplosionsOther, boolean preventFireSpreading, boolean preventEntitySpawning, boolean persistent) {

	public static final Codec<ProtectionSettings> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.BOOL.fieldOf("preventBlockPlacing").forGetter(ProtectionSettings::preventBlockPlacing),
				Codec.BOOL.fieldOf("preventBlockBreaking").forGetter(ProtectionSettings::preventBlockBreaking),
				Codec.BOOL.fieldOf("preventExplosionsTNT").forGetter(ProtectionSettings::preventExplosionsTNT),
				Codec.BOOL.fieldOf("preventExplosionsOther").forGetter(ProtectionSettings::preventExplosionsOther),
				Codec.BOOL.fieldOf("preventFireSpreading").forGetter(ProtectionSettings::preventFireSpreading),
				Codec.BOOL.fieldOf("preventEntitySpawning").forGetter(ProtectionSettings::preventEntitySpawning),
				Codec.BOOL.fieldOf("persistent").forGetter(ProtectionSettings::persistent))
				.apply(instance, ProtectionSettings::new);
	});

}
