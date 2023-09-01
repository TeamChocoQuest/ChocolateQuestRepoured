package team.cqr.cqrepoured.init;

import com.mojang.serialization.Codec;

import commoble.databuddy.codec.RegistryDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import team.cqr.cqrepoured.CQRConstants;
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
			FMLJavaModLoadingContext.get(), 
			CQRMain.prefix("registry/dispatcher/tradematchruletype"), 
			rule -> rule.getType(),
			builder -> {}
	); 
	
	public static final RegistryDispatcher<ITradeBuyerRule> TRADE_CUSTOMER_RULE_DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
			FMLJavaModLoadingContext.get(), 
			CQRMain.prefix("registry/dispatcher/tradecustomerruletype"), 
			rule -> rule.getType(),
			builder -> {}
	); 
	
	public static final RegistryObject<Codec<ITradeMatchRule>> MATCH_META = TRADE_MATCH_RULE_DISPATCHER.registry().register("match_meta", () -> Codec.unit(new MatchMeta()));
	public static final RegistryObject<Codec<ITradeMatchRule>> MATCH_ENCHANTS = TRADE_MATCH_RULE_DISPATCHER.registry().register("match_enchants", () -> Codec.unit(new MatchEnchants()));
	public static final RegistryObject<Codec<ITradeMatchRule>> MATCH_NBT = TRADE_MATCH_RULE_DISPATCHER.registry().register("match_nbt", () -> Codec.unit(new MatchNBT()));
	public static final RegistryObject<Codec<ITradeMatchRule>> MATCH_EnchantmentList = TRADE_MATCH_RULE_DISPATCHER.registry().register("match_enchantment_list", () -> MatchEnchantmentList.CODEC);
	
	
	public static final RegistryDispatcher<Cheese> CHEESE_DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
		FMLJavaModLoadingContext.get().getModEventBus(),
		new ResourceLocation(CQRConstants.MODID, "cheese"),
		cheese -> cheese.getType(), // using a method reference here seems to confuse eclipse
		builder->{}
	);
	
	// RegistryObjects can be created from the dispatcher's deferred registry
	public static final RegistryObject<Codec<Cheddar>> CHEDDAR = CHEESE_DISPATCHER.registry()
		.register("cheddar", () -> Codec.unit(new Cheddar()));
	
	// Base class for your data classes, instances of this could potentially be parsed from jsons or whatever
	public static interface Cheese
	{
		public Codec<? extends Cheese> getType();
		public int color();
	}
	
	// subclass of the data class, the "type" field in Cheese jsons would indicate to use e.g. the databuddy:cheddar serializer
	public static class Cheddar implements Cheese
	{
		@Override
		public Codec<? extends Cheese> getType()
		{
			return CQRTradeRules.CHEDDAR.get();
		}

		@Override
		public int color()
		{
			return 0;
		}
	}
	
}
