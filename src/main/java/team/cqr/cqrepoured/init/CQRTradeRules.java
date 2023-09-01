package team.cqr.cqrepoured.init;

import com.mojang.serialization.Codec;

import commoble.databuddy.codec.RegistryDispatcher;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.trade.rules.buyer.ITradeBuyerRule;
import team.cqr.cqrepoured.entity.trade.rules.input.ITradeMatchRule;
import team.cqr.cqrepoured.entity.trade.rules.input.MatchEnchantmentList;
import team.cqr.cqrepoured.entity.trade.rules.input.MatchEnchants;
import team.cqr.cqrepoured.entity.trade.rules.input.MatchMeta;
import team.cqr.cqrepoured.entity.trade.rules.input.MatchNBT;

public class CQRTradeRules {
	
	public static void init() {
		
	}
	
	public static final RegistryDispatcher<ITradeMatchRule> TRADE_MATCH_RULE_DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
			FMLJavaModLoadingContext.get().getModEventBus(), 
			CQRMain.prefix("registry/dispatcher/trade/ruletype/match"), 
			rule -> rule.getType(),
			builder -> {}
	); 
	
	public static final RegistryDispatcher<ITradeBuyerRule> TRADE_CUSTOMER_RULE_DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
			FMLJavaModLoadingContext.get().getModEventBus(), 
			CQRMain.prefix("registry/dispatcher/trade/ruletype/customer"), 
			rule -> rule.getType(),
			builder -> {}
	); 
	
	public static final RegistryObject<Codec<ITradeMatchRule>> MATCH_META = TRADE_MATCH_RULE_DISPATCHER.registry().register("match_meta", () -> Codec.unit(new MatchMeta()));
	public static final RegistryObject<Codec<ITradeMatchRule>> MATCH_ENCHANTS = TRADE_MATCH_RULE_DISPATCHER.registry().register("match_enchants", () -> Codec.unit(new MatchEnchants()));
	public static final RegistryObject<Codec<ITradeMatchRule>> MATCH_NBT = TRADE_MATCH_RULE_DISPATCHER.registry().register("match_nbt", () -> Codec.unit(new MatchNBT()));
	public static final RegistryObject<Codec<? extends ITradeMatchRule>> MATCH_ENCHANTMENT_LIST = TRADE_MATCH_RULE_DISPATCHER.registry().register("match_enchantment_list", () -> MatchEnchantmentList.CODEC);
	
}
