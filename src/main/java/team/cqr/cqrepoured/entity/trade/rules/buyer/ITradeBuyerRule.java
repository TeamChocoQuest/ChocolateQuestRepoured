package team.cqr.cqrepoured.entity.trade.rules.buyer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.Entity;
import team.cqr.cqrepoured.entity.trade.TradeData;

public interface ITradeBuyerRule {

	public boolean matches(final @Nullable Entity customer, final @Nonnull Entity trader, final @Nonnull TradeData tradeRawData);
	
	public Codec<? extends ITradeBuyerRule> getType();
	
}
