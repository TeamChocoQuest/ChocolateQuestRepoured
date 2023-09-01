package team.cqr.cqrepoured.entity.trade;

import java.util.List;
import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.ItemStack;
import team.cqr.cqrepoured.entity.trade.rules.input.ITradeMatchRule;
import team.cqr.cqrepoured.init.CQRTradeRules;

public record TradeInput(ItemStack stack, List<ITradeMatchRule> matchRules) implements Function<ItemStack, Boolean> { 

	public static final Codec<TradeInput> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				ItemStack.CODEC.fieldOf("item").forGetter(TradeInput::stack),
				CQRTradeRules.TRADE_MATCH_RULE_DISPATCHER.dispatchedCodec().listOf().fieldOf("match-rules").forGetter(TradeInput::matchRules)
			).apply(instance, TradeInput::new);
	});

	@Override
	public Boolean apply(ItemStack t) {
		if (this.matchRules.isEmpty()) {
			return ItemStack.isSameItem(t, this.stack);
		} else {
			if (!ItemStack.isSameItem(this.stack, t)) {
				return false;
			}
			for (ITradeMatchRule rule : this.matchRules) {
				if (!rule.matches(t, this.stack)) {
					return false;
				}
			}
			return true;
		}
	}
	
	/*public static final Comparator<TradeInput> SORT_META = (tradeInput1, tradeInput2) -> {
		boolean ignoreMeta1 = tradeInput1.ignoreMeta();
		boolean ignoreNBT1 = tradeInput1.ignoreNBT();
		boolean ignoreMeta2 = tradeInput2.ignoreMeta();
		boolean ignoreNBT2 = tradeInput2.ignoreNBT();
		if (!ignoreMeta1 && !ignoreNBT1 && (ignoreMeta2 || ignoreNBT2)) {
			return -1;
		}
		if ((ignoreMeta1 || ignoreNBT1) && !ignoreMeta2 && !ignoreNBT2) {
			return 1;
		}
		if (!ignoreMeta1 && ignoreMeta2) {
			return -1;
		}
		if (ignoreMeta1 && !ignoreMeta2) {
			return 1;
		}
		if (!ignoreNBT1 && ignoreNBT2) {
			return -1;
		}
		if (ignoreNBT1 && !ignoreNBT2) {
			return 1;
		}
		return 0;
	};

	public static final Comparator<TradeInput> SORT_NBT = (tradeInput1, tradeInput2) -> {
		boolean ignoreMeta1 = tradeInput1.ignoreMeta();
		boolean ignoreNBT1 = tradeInput1.ignoreNBT();
		boolean ignoreMeta2 = tradeInput2.ignoreMeta();
		boolean ignoreNBT2 = tradeInput2.ignoreNBT();
		if (!ignoreMeta1 && !ignoreNBT1 && (ignoreMeta2 || ignoreNBT2)) {
			return -1;
		}
		if (!ignoreMeta2 && !ignoreNBT2 && (ignoreMeta1 || ignoreNBT1)) {
			return 1;
		}
		if (!ignoreNBT1 && ignoreNBT2) {
			return -1;
		}
		if (!ignoreNBT2 && ignoreNBT1) {
			return 1;
		}
		if (!ignoreMeta1 && ignoreMeta2) {
			return -1;
		}
		if (!ignoreMeta2 && ignoreMeta1) {
			return 1;
		}
		return 0;
	};*/

}
