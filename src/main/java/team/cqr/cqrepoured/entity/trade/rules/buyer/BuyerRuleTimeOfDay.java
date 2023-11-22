package team.cqr.cqrepoured.entity.trade.rules.buyer;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelTimeAccess;
import team.cqr.cqrepoured.entity.trade.TradeData;

public record BuyerRuleTimeOfDay(
		boolean blacklist,
		int startTime,
		int stopTime,
		boolean moonPhaseBlackList,
		Optional<List<Integer>> moonPhases
	) implements ITradeBuyerRule {
	
	public static final Codec<BuyerRuleTimeOfDay> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.BOOL.optionalFieldOf("blacklist", false).forGetter(BuyerRuleTimeOfDay::blacklist),
				Codec.INT.fieldOf("start-time").forGetter(BuyerRuleTimeOfDay::startTime),
				Codec.INT.fieldOf("stop-time").forGetter(BuyerRuleTimeOfDay::stopTime),
				Codec.BOOL.optionalFieldOf("blacklist-moon-phases", false).forGetter(BuyerRuleTimeOfDay::moonPhaseBlackList),
				Codec.INT.listOf().optionalFieldOf("moon-phases").forGetter(BuyerRuleTimeOfDay::moonPhases)
			).apply(instance, BuyerRuleTimeOfDay::new);
	});

	@Override
	public boolean matches(Entity customer, Entity trader, TradeData tradeRawData) {
		if (customer == null || customer.level() == null || !(customer.level() instanceof LevelTimeAccess)) {
			return this.blacklist();
		}
		LevelTimeAccess timeAccess = customer.level();
		int time = Math.round(timeAccess.getTimeOfDay(1));
		
		boolean result = false;
		
		if (time >= this.startTime() && time <= this.stopTime()) {
			if (this.moonPhases.isEmpty() || this.moonPhases().get().isEmpty()) {
				result = true;
			} else {
				if (this.moonPhases.get().contains(timeAccess.getMoonPhase())) {
					result = !this.moonPhaseBlackList();
				}
			}
		}
		return !this.blacklist() && result;
	}

	@Override
	public Codec<? extends ITradeBuyerRule> getType() {
		return CODEC;
	}

}
