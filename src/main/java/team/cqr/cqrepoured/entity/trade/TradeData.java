package team.cqr.cqrepoured.entity.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.entity.trade.rules.buyer.ITradeBuyerRule;
import team.cqr.cqrepoured.init.CQRTradeRules;

public class TradeData extends ArrayList<TradeInput> {

	private final ItemStack result;
	private final List<ITradeBuyerRule> customerConstraints;
	
	private final Optional<StockData> restockSettings;
	
	private static final long serialVersionUID = 8257720743528671701L;
	
	public static final Codec<TradeData> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				TradeInput.CODEC.listOf().fieldOf("inputs").forGetter((data) -> data),
				ItemStack.CODEC.fieldOf("result").forGetter((data) -> data.result),
				CQRTradeRules.TRADE_CUSTOMER_RULE_DISPATCHER.dispatchedCodec().listOf().fieldOf("customer-rules").forGetter(data -> data.customerConstraints),
				StockData.CODEC.optionalFieldOf("restock-settings").forGetter(trade -> trade.restockSettings)
			).apply(instance, TradeData::new);
	});
	
	public TradeData(final List<TradeInput> inputs, final ItemStack result, List<ITradeBuyerRule> customerConstraints, Optional<StockData> restockOpt) {
		super(inputs);
		this.result = result;
		this.customerConstraints = customerConstraints;
		this.restockSettings = restockOpt;
	}
	
	public Optional<StockData> getRestockData() {
		return this.restockSettings;
	}
	
	public final ItemStack getResultingItem() {
		return this.result;
	}
	
	public boolean canBuy(final Entity customer, final Entity trader, final ItemStack[] inputs) {
		if (trader == null) {
			return false;
		}
		if (!trader.isAlive()) {
			return false;
		}
		return this.matchesConstraints(customer, trader) && this.matches(inputs);
	}
	
	protected boolean matches(final ItemStack[] inputs) {
		if (this.size() > inputs.length) {
			// Not enough stacks
			return false;
		}
		for(int i = 0; i < inputs.length && i < this.size(); i++) {
			if (!this.get(i).apply(inputs[i])) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean matchesConstraints(final Entity customer, final Entity trader) {
		if (customer == null) {
			return false;
		}
		if (!customer.isAlive()) {
			return false;
		}
		
		// TODO: Check line of sight and distance between trader and customer! => Rule?
		
		for (ITradeBuyerRule rule : this.customerConstraints) {
			if (!rule.matches(customer, trader, this)) {
				return false;
			}
		}
		return true;
	}
	

}
