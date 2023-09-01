package team.cqr.cqrepoured.entity.trade.rules.input;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import team.cqr.cqrepoured.entity.trade.rules.input.MatchEnchantmentList.EnchantmentEntry;

public record MatchEnchantmentList(
		boolean isBlacklist,
		Optional<Integer> minMatches,
		Optional<Integer> maxMatches,
		List<EnchantmentEntry> enchantments
	) implements ITradeMatchRule {
	
	public static record EnchantmentEntry(
			Enchantment enchantment,
			Optional<Integer> minLevel,
			Optional<Integer> maxLevel
		) {
		
		public static final Codec<EnchantmentEntry> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					ForgeRegistries.ENCHANTMENTS.getCodec().fieldOf("id").forGetter(EnchantmentEntry::enchantment),
					Codec.INT.optionalFieldOf("min-level").forGetter(EnchantmentEntry::minLevel),
					Codec.INT.optionalFieldOf("max-level").forGetter(EnchantmentEntry::maxLevel)
				).apply(instance, EnchantmentEntry::new);
		});
		
		public boolean matches(final Enchantment ench, int level) {
			if (this.enchantment != ench) {
				return false;
			}
			if (this.minLevel.isPresent() && this.maxLevel.isPresent()) {
				return this.minLevel.get() <= level && this.maxLevel.get() >= level;
			} else {
				if (this.minLevel.isPresent()) {
					return this.minLevel.get() <= level;
				} else if (this.maxLevel.isPresent()) {
					return this.maxLevel.get() >= level;
				} else {
					return true;
				}
			}
		}
	}
	
	public static final Codec<MatchEnchantmentList> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.BOOL.optionalFieldOf("blacklist", false).forGetter(MatchEnchantmentList::isBlacklist),
				Codec.INT.optionalFieldOf("min-matches").forGetter(MatchEnchantmentList::minMatches),
				Codec.INT.optionalFieldOf("max-matches").forGetter(MatchEnchantmentList::maxMatches),
				EnchantmentEntry.CODEC.listOf().fieldOf("enchantments").forGetter(MatchEnchantmentList::enchantments)
			).apply(instance, MatchEnchantmentList::new);
	});

	@Override
	public boolean matches(ItemStack input, ItemStack toMatch) {
		final boolean countMatching = this.minMatches.isPresent() || this.maxMatches.isPresent();
		int matches = 0;

		if (input.getAllEnchantments().isEmpty()) {
			if (!countMatching) {
				return !this.isBlacklist();
			} else {
				return this.minMatches.get() <= 0 && !this.isBlacklist();
			}
		}
		
		for (EnchantmentEntry condition : this.enchantments) {
			Integer level = input.getAllEnchantments().getOrDefault(condition.enchantment(), null);
			if (level == null) {
				continue;
			}
			if (!condition.matches(condition.enchantment(), level)) {
				if (!countMatching) {
					return false;
				}
				continue;
			}
			matches++;
		}
		
		if (!countMatching) {
			return true;
		} else {
			if (this.minMatches.isPresent() && this.maxMatches.isPresent()) {
				return this.minMatches.get() <= matches && this.maxMatches.get() >= matches;
			} else {
				if (this.minMatches.isPresent()) {
					return this.minMatches.get() <= matches;
				} else if (this.maxMatches.isPresent()) {
					return this.maxMatches.get() >= matches;
				}
			}
		}
		return false;
	}

	@Override
	public Codec<? extends ITradeMatchRule> getType() {
		return CODEC;
	}

}
