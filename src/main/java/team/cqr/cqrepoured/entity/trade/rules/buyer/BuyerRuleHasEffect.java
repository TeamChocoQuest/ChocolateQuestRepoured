package team.cqr.cqrepoured.entity.trade.rules.buyer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import team.cqr.cqrepoured.entity.trade.TradeData;

public record BuyerRuleHasEffect(
		boolean isBlacklist,
		Optional<Integer> minMatches,
		Optional<Integer> maxMatches,
		List<EffectEntry> effects
	) implements ITradeBuyerRule {
	
	public static record EffectEntry(
			Optional<Integer> minLevel,
			Optional<Integer> maxLevel,
			MobEffect effect
		) {
		
		@SuppressWarnings("deprecation")
		public static final Codec<EffectEntry> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					Codec.INT.optionalFieldOf("min-level").forGetter(EffectEntry::minLevel),
					Codec.INT.optionalFieldOf("max-level").forGetter(EffectEntry::maxLevel),
					BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("id").forGetter(EffectEntry::effect)
				).apply(instance, EffectEntry::new);
		});
		
		public boolean matches(MobEffect effect, int level) {
			if (this.effect().equals(effect)) {
				if (this.minLevel().isPresent() || this.maxLevel().isPresent()) {
					if (this.minLevel().isPresent() && this.maxLevel().isPresent()) {
						return this.minLevel().get() <= level && this.maxLevel().get() >= level;
					} else {
						if (this.minLevel().isPresent()) {
							return this.minLevel().get() <= level;
						} else if (this.maxLevel().isPresent()) {
							return this.maxLevel().get() >= level;
						}
					}
				} else {
					return true;
				}
			}
			return false;
		}
		
	}
	
	public static final Codec<BuyerRuleHasEffect> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.BOOL.optionalFieldOf("blacklist", false).forGetter(BuyerRuleHasEffect::isBlacklist),
				Codec.INT.optionalFieldOf("min-matches").forGetter(BuyerRuleHasEffect::minMatches),
				Codec.INT.optionalFieldOf("max-matches").forGetter(BuyerRuleHasEffect::maxMatches),
				EffectEntry.CODEC.listOf().fieldOf("effects").forGetter(BuyerRuleHasEffect::effects)
			).apply(instance, BuyerRuleHasEffect::new);
	});

	@Override
	public boolean matches(Entity customer, Entity trader, TradeData tradeRawData) {
		if (!(customer instanceof Mob) ) {
			return this.isBlacklist();
		}
		Mob mob = (Mob) customer;
		if (customer == null || customer.level() == null || customer.level().getServer() == null) {
			return this.isBlacklist();
		}
		final boolean countMatching = this.minMatches.isPresent() || this.maxMatches.isPresent();
		int matches = 0;
		
		for (Map.Entry<MobEffect, MobEffectInstance> entry : mob.getActiveEffectsMap().entrySet()) {
			for (EffectEntry effectEntry : this.effects) {
				// Otherwise ignore...
				if (effectEntry.matches(entry.getKey(), entry.getValue().getAmplifier())) {
					matches++;
				}
			}
		}
		
		if (!countMatching) {
			return !this.isBlacklist() && matches == effects.size();
		} else {
			if (this.minMatches.isPresent() && this.maxMatches.isPresent()) {
				return this.minMatches.get() <= matches && this.maxMatches.get() >= matches && !this.isBlacklist();
			} else {
				if (this.minMatches.isPresent()) {
					return this.minMatches.get() <= matches && !this.isBlacklist();
				} else if (this.maxMatches.isPresent()) {
					return this.maxMatches.get() >= matches && !this.isBlacklist();
				}
			}
		}
		return this.isBlacklist();
	}

	@Override
	public Codec<? extends ITradeBuyerRule> getType() {
		return CODEC;
	}

}
