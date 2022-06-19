package team.cqr.cqrepoured.entity;

import net.minecraft.entity.Entity;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.trade.Trade;
import team.cqr.cqrepoured.entity.trade.TraderOffer;

import java.util.List;
import java.util.stream.Collectors;

public interface ITradeRestockOverTime {
	
	public long getLastTimedRestockTime();
	public void setLastTimedRestockTime(final long newValue);

	public TraderOffer getTrades();
	public boolean hasTrades();
	
	public default void updateTradeRestockTimer() {
		if(CQRConfig.SERVER_CONFIG.mobs.enableTradeRestockOverTime.get() && this instanceof Entity) {
			if(!this.hasTrades()) {
				return;
			}
			long ticksExisted = ((Entity)this).level.getGameTime();
			long lastTimed = this.getLastTimedRestockTime();
			if(lastTimed <= 0) {
				this.setLastTimedRestockTime(ticksExisted);
				return;
			}
			final long delta = ticksExisted - lastTimed;
			int restocks = (int) Math.floorDiv(delta, CQRConfig.SERVER_CONFIG.mobs.tradeRestockTime.get());
			restocks = Math.min(restocks, CQRConfig.SERVER_CONFIG.mobs.maxAutoRestocksOverTime.get());
			if(delta > 0 && restocks > 0) {
				if(this.getTrades() != null) {
					TraderOffer offer = this.getTrades();
					List<Trade> trades = offer.getTrades().stream().filter(Trade::canRestock).collect(Collectors.toList());
					for(int i = 0; !trades.isEmpty() && i < restocks; i++) {
						int index = ((Entity) this).level.random.nextInt(trades.size());
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
