package team.cqr.cqrepoured.entity;

import java.util.Objects;

import net.minecraft.entity.Entity;
import team.cqr.cqrepoured.config.CQRConfig;
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
					for(int i = 0; i < restocks; i++) {
						offer.getTrades().stream()
							.filter(Objects::nonNull)
							.filter(t -> t.canRestock() && t.hasLimitedStock())
							.findAny()
							.ifPresent(tradeRestock -> tradeRestock.incStock());
					}
					
				}
				this.setLastTimedRestockTime(ticksExisted);
			}
		}
	}
	
}
