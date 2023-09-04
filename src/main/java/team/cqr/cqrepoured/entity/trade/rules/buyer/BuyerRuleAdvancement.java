package team.cqr.cqrepoured.entity.trade.rules.buyer;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.trade.TradeData;

public record BuyerRuleAdvancement(
		boolean isBlacklist,
		Optional<Integer> minMatches,
		Optional<Integer> maxMatches,
		List<ResourceLocation> advancements
	) implements ITradeBuyerRule {
	
	public static final Codec<BuyerRuleAdvancement> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.BOOL.optionalFieldOf("blacklist", false).forGetter(BuyerRuleAdvancement::isBlacklist),
				Codec.INT.optionalFieldOf("min-matches").forGetter(BuyerRuleAdvancement::minMatches),
				Codec.INT.optionalFieldOf("max-matches").forGetter(BuyerRuleAdvancement::maxMatches),
				ResourceLocation.CODEC.listOf().fieldOf("advancements").forGetter(BuyerRuleAdvancement::advancements)
			).apply(instance, BuyerRuleAdvancement::new);
	});
	
	@Override
	public boolean matches(Entity customer, Entity trader, TradeData tradeRawData) {
		if (!(customer instanceof Player)) {
			return this.isBlacklist();
		}
		// TODO: Somehow make this work on client too
		Player player = (Player) customer;
		if (customer == null || customer.level() == null || customer.level().getServer() == null) {
			return this.isBlacklist();
		}
		final boolean countMatching = this.minMatches.isPresent() || this.maxMatches.isPresent();
		int matches = 0;

		for (ResourceLocation condition : this.advancements) {
			if (CQRMain.PROXY.hasAdvancement(player, condition));
			matches++;
		}
		
		if (!countMatching) {
			return !this.isBlacklist();
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
