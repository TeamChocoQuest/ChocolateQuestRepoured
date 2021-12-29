package team.cqr.cqrepoured.entity;

import java.util.Objects;

import net.minecraft.entity.Entity;
import team.cqr.cqrepoured.config.CQRConfig;
import team.cqr.cqrepoured.entity.trade.TraderOffer;

public interface ITradeRestockOverTime {
	
	public long getLastTimedRestockTick();
	public void setLastTimedRestockTick(final long newValue);

	public TraderOffer getTrades();
	public boolean hasTrades();
	
	public default void onTick() {
		if(CQRConfig.mobs.enableTradeRestockOverTime && this instanceof Entity) {
			Entity ent = (Entity) this;
			if(!this.hasTrades()) {
				return;
			}
			long ticksExisted = ent.ticksExisted;
			long lastTimed = this.getLastTimedRestockTick();
			final long delta = ticksExisted - lastTimed;
			final int restocks = (int) Math.floorDiv(delta, CQRConfig.mobs.tradeRestockTime);
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
				this.setLastTimedRestockTick(ticksExisted);
			}
		}
	}
	
}
