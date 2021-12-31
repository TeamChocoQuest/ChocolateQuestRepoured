package team.cqr.cqrepoured.entity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.trade.Trade;
import team.cqr.cqrepoured.entity.trade.TraderOffer;

public interface ITradeRestockOverTime {
	
	public long getLastTimedRestockTime();
	public void setLastTimedRestockTime(final long newValue);

	public TraderOffer getTrades();
	public boolean hasTrades();
	
	public default void updateTradeRestockTimer() {
		if(CQRConfig.mobs.enableTradeRestockOverTime && this instanceof Entity) {
			if(!this.hasTrades()) {
				return;
			}
			long ticksExisted = ((Entity)this).world.getTotalWorldTime();
			long lastTimed = this.getLastTimedRestockTime();
			if(lastTimed <= 0) {
				this.setLastTimedRestockTime(ticksExisted);
				return;
			}
			final long delta = ticksExisted - lastTimed;
			int restocks = (int) Math.floorDiv(delta, CQRConfig.mobs.tradeRestockTime);
			restocks = Math.min(restocks, CQRConfig.mobs.maxAutoRestocksOverTime);
			if(delta > 0 && restocks > 0) {
				if(this.getTrades() != null) {
					TraderOffer offer = this.getTrades();
					List<Trade> trades = offer.getTrades().stream().filter(Trade::canRestock).collect(Collectors.toList());
					for(int i = 0; !trades.isEmpty() && i < restocks; i++) {
						int index = ((Entity) this).world.rand.nextInt(trades.size());
						Trade trade = trades.get(index);
						trade.incStock();
						if (!trade.canRestock()) {
							trades.remove(index);
						}
					}
					
				}
				this.setLastTimedRestockTime(ticksExisted);
			}
		}
	}
	
}
