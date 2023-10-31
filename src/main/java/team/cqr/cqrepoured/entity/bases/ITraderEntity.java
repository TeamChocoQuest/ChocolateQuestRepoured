package team.cqr.cqrepoured.entity.bases;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.entity.trade.TradeData;
import team.cqr.cqrepoured.entity.trade.TradeProfileInstance;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;

public interface ITraderEntity {
	
	@Nullable
	public TradeProfileInstance getTradeProfileInstance();
	public void setTradeProfileInstance(@Nullable TradeProfileInstance profile);
	
	public default Entity getTrader() {
		if (this instanceof Entity) {
			return (Entity)this;
		}
		throw new RuntimeException("Interface must be implemented upon children of Entity!");
	}
	
	// Returns true only when the profile actually exists, the resLoc isn't empty or null and the trader exists too
	public default boolean assignTradeProfile(ResourceLocation id) {
		if (this.getTrader() == null || id == null || id.getPath().isBlank()) {
			return false;
		}
		TradeProfileInstance profile = new TradeProfileInstance(id, this.getTrader());
		if (CQRDatapackLoaders.getTradeProfile(id).isPresent()) {
			this.setTradeProfileInstance(profile);
			
			return true;
		}
		return false;
	}
	
	// Trade access methods
	public default void restockRandomTrades(int limit) {
		
	}
	
	public default void overrideTrade(int index, final TradeData override) {
		
	}
	
	@Nullable
	public default TradeData getTradeAt(int index) {
		if (this.getTradeProfileInstance() == null) {
			return null;
		}
		return this.getTradeProfileInstance().getTradeAt(index);
	}
	
	public default int getTradeCount() {
		if (this.getTradeProfileInstance() == null) {
			return 0;
		}
		return this.getTradeProfileInstance().getTradeCount();
	}
	
	public default TradeData[] getTrades() {
		TradeData[] result = new TradeData[this.getTradeCount()];
		if (result.length > 0) {
			for (int i = 0; i < result.length; i++) {
				result[i] = this.getTradeAt(i);
			}
		}
		return result;
	}
	
	public default boolean buy(final int tradeIndex, final Entity customer, Consumer<ItemStack> action, final ItemStack... input) {
		if (this.getTradeProfileInstance() == null) {
			return false;
		}
		if (!this.getTradeProfileInstance().hasAnyTrades()) {
			return false;
		}
		ItemStack result = this.getTradeProfileInstance().getTradeResult(tradeIndex, customer, input);
		if (result == null) {
			return false;
		}
		if (result.isEmpty()) {
			return false;
		}
		action.accept(result);
		return true;
	}
	
	public default void callOnReadData(CompoundTag nbt) {
		if (this.getTradeProfileInstance() != null && this.getTrader() != null) {
			this.setTradeProfileInstance(TradeProfileInstance.readFromNBT(nbt, this.getTrader()));
		}
	}
	
	public default void callOnWriteData(CompoundTag nbt) {
		if (this.getTradeProfileInstance() != null) {
			this.getTradeProfileInstance().saveToNBT(nbt);
		}
	}

}
