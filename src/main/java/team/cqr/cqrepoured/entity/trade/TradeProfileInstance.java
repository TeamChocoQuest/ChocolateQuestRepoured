package team.cqr.cqrepoured.entity.trade;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.common.CQRConstants;
import team.cqr.cqrepoured.common.random.CQRWeightedRandom;
import team.cqr.cqrepoured.common.random.CQRWeightedRandom.WeightedObject;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.util.WeakReferenceLazyLoadField;

public class TradeProfileInstance {

	private final ResourceLocation referencedProfile;
	private WeakReference<Entity> trader;
	
	protected final WeakReferenceLazyLoadField<TradeProfile> TRADE_PROFILE = new WeakReferenceLazyLoadField<>(this::loadTradeProfile);
	protected final Map<Integer, TradeData> TRADE_OVERRIDES = new Int2ObjectArrayMap<>();
	protected final Map<Integer, Integer> TRADE_STOCK = new Int2IntArrayMap();
	
	private static final RandomSource FALLBACK_RANDOM = RandomSource.create();
	
	// Codec to save and load from/to NBT
	public static final Codec<TradeProfileInstance> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				ResourceLocation.CODEC.fieldOf("referenced-profile").forGetter(tpi -> tpi.referencedProfile),
				Codec.unboundedMap(Codec.INT, TradeData.CODEC).optionalFieldOf("trade-overrides").forGetter(tpi -> Optional.ofNullable(tpi.TRADE_OVERRIDES)),
				Codec.unboundedMap(Codec.INT, Codec.INT).optionalFieldOf("trade-stock").forGetter(tpi -> Optional.ofNullable(tpi.TRADE_STOCK))
			).apply(instance, TradeProfileInstance::new);
	});
	
	protected TradeProfileInstance(ResourceLocation referencedTradeProfile, Optional<Map<Integer, TradeData>> overrides, Optional<Map<Integer, Integer>> stockData) {
		this.referencedProfile = referencedTradeProfile;
		if (overrides.isPresent()) {
			this.TRADE_OVERRIDES.putAll(overrides.get());
		}
		if (stockData.isPresent()) {
			this.TRADE_STOCK.putAll(stockData.get());
		}
	}
	
	public int getTradeCount() {
		if (this.hasAnyTrades()) {
			return this.TRADE_PROFILE.get().size();
		}
		return 0;
	}
	
	@Nullable
	protected final TradeProfile loadTradeProfile() {
		if (this.referencedProfile == null) {
			return null;
		}
		TradeProfile result = CQRDatapackLoaders.getTradeProfile(this.referencedProfile).get();
		
		if (this.TRADE_STOCK == null || this.TRADE_STOCK.isEmpty()) {
			this.rollInitialStockUponProfileLoad(FALLBACK_RANDOM);
		}
		return result;
	}
	
	protected void rollInitialStockUponProfileLoad(final RandomSource source) {
		if (this.TRADE_PROFILE.get() == null) {
			return;
		}
		for (int i = 0; i < this.TRADE_PROFILE.get().size(); i++) {
			TradeData trade = this.getTradeAt(i);
			if (trade == null) {
				continue;
			}
			rollStockForTrade(i, trade, source);
		}
	}

	protected void rollStockForTrade(int index, final @Nonnull TradeData trade, final RandomSource source) {
		if (trade.getStockData().isEmpty()) {
			this.TRADE_STOCK.remove(index);
		} else {
			this.TRADE_STOCK.put(index, trade.getStockData().get().defaultStockSupplier().sample(source));
		}
	}

	public TradeProfileInstance(final ResourceLocation referencedProfile, final Entity trader) {
		this.referencedProfile = referencedProfile;
		if (this.referencedProfile != null) {
			// Load directly
			this.TRADE_PROFILE.get();
		}
		this.trader = new WeakReference<Entity>(trader);
	}
	
	public boolean setOverride(final int tradeIndex, final TradeData overrideData) {
		if (this.TRADE_PROFILE.get() == null) {
			return false;
		}
		if (tradeIndex >= 0 && tradeIndex < this.TRADE_PROFILE.get().size()) {
			if (overrideData == null) {
				this.TRADE_OVERRIDES.remove(tradeIndex);
			}
			else if (this.TRADE_OVERRIDES.getOrDefault(tradeIndex, null) != overrideData) {
				this.TRADE_OVERRIDES.put(tradeIndex, overrideData);
			}
			RandomSource random = FALLBACK_RANDOM;
			
			if (this.trader.get() != null) {
				random = this.trader.get().level().getRandom();
			}
			
			rollStockForTrade(tradeIndex, overrideData, random);
			
			return true;
		}
		return false;
	}
	
	// Logic
	public boolean hasAnyTrades() {
		TradeProfile tp = this.TRADE_PROFILE.get();
		if (tp == null) {
			return false;
		}
		return !tp.isEmpty();
	}
	
	@Nullable
	public TradeData getTradeAt(final int index) {
		if (!this.hasAnyTrades()) {
			return null;
		}
		if (!this.TRADE_OVERRIDES.containsKey(index) || this.TRADE_PROFILE.get() == null) {
			return null;
		}
		if (this.TRADE_PROFILE.get().size() <= index) {
			return null;
		}
		return this.TRADE_OVERRIDES.getOrDefault(index, this.TRADE_PROFILE.get().get(index));
	}
	
	public boolean isInStock(final int tradeIndex, final Entity customer) {
		TradeData tradeData = this.getTradeAt(tradeIndex);
		if (tradeData == null) {
			return false;
		}
		
		Optional<StockData> optStockData = tradeData.getStockData();
		if (optStockData.isPresent()) {
			StockData stockData = optStockData.get();
			
			// Get or compute stock count
			final RandomSource rs = customer instanceof LivingEntity le ? le.getRandom() : customer.level().getRandom();
			final int inStock = this.TRADE_STOCK.computeIfAbsent(tradeIndex, key -> stockData.defaultStockSupplier().sample(rs));
			
			if (inStock <= 0) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean doesCustomerMeetRequirements(final int tradeIndex, final Entity customer) {
		TradeData tradeData = this.getTradeAt(tradeIndex);
		if (tradeData == null) {
			return false;
		}
		
		return tradeData.matchesConstraints(customer, this.trader.get());
	}
	
	public boolean doesInputMeetRequirements(final int tradeIndex, final ItemStack... inputs) {
		TradeData tradeData = this.getTradeAt(tradeIndex);
		if (tradeData == null) {
			return false;
		}
		
		return tradeData.matches(inputs);
	}
	
	public Tuple<EBuyResult, ItemStack> getTradeResult(final int tradeIndex, final Entity customer, final ItemStack... inputs) {
		ItemStack result = ItemStack.EMPTY;
		EBuyResult actionResult = EBuyResult.SUCCESS;
		
		TradeData tradeData = this.getTradeAt(tradeIndex);
		if (tradeData == null) {
			actionResult = EBuyResult.NO_TRADE;
		}

		else if (!this.isInStock(tradeIndex, customer)) {
			actionResult = EBuyResult.NO_STOCK;
		}

		else if (inputs == null || inputs.length == 0) {
			actionResult = EBuyResult.NO_INPUT;
		}
		
		// Now, let's actually ask the trade if we can buy it...
		else if (!this.doesCustomerMeetRequirements(tradeIndex, customer)) {
			actionResult = EBuyResult.CUSTOMER_RULES_NOT_MET;
		}
		else if (!this.doesInputMeetRequirements(tradeIndex, inputs)) {
			actionResult = EBuyResult.INPUT_INVALID;
		}
		
		else if (actionResult.isSuccess()){
			// NOthing to complain
			result = tradeData.getResultingItem();
		}
		
		return new Tuple<>(actionResult, result);
	}
	
	public void onStockIncrement() {
		if (this.trader.get() == null || !this.trader.get().isAlive()) {
			return;
		}
		Level level = this.trader.get().level();
		if (level == null || level.isClientSide() || !(level instanceof ServerLevel)) {
			return;
		}
		
		ServerLevel sl = (ServerLevel)level;
		
		TradeData toRestock = this.getTradeToRestock(sl);
		if (toRestock == null) {
			return;
		}
		
		int tradeIndex = this.TRADE_PROFILE.get().indexOf(toRestock);
		
		int currentStock = this.TRADE_STOCK.getOrDefault(tradeIndex, toRestock.getStockData().get().defaultStockSupplier().sample(sl.getRandom()));
		this.TRADE_STOCK.put(tradeIndex, currentStock + toRestock.getStockData().get().restockData().get().restockAmount());
	}
	
	// Saving & Loading
	
	@Nullable
	protected TradeData getTradeToRestock(ServerLevel sl) {
		final long worldTime = sl.getGameTime();
		
		// First, find all restocking trades
		// Second, filter out those that can't restock
		// Also filter for the restock rate
		// Lastly, create a weighted list and pick a random trade
		Set<TradeData> relevantEntries = new HashSet<>();
		relevantEntries.addAll(this.TRADE_OVERRIDES.values());
		relevantEntries.addAll(this.TRADE_PROFILE.get());
		relevantEntries.removeIf(td -> {
			if (td.getStockData().isPresent()) {
				if (td.getStockData().get().restockData().isEmpty()) {
					return true;
				}
				RestockData rd = td.getStockData().get().restockData().get();
				
				if (rd.restockAmount() == 0) {
					return true;
				}
				// check rate
				return !(rd.restockRate() == 0 || worldTime % rd.restockRate() == 0);
			}
			return true;
		});

		if (relevantEntries.isEmpty()) {
			return null;
		}
		
		Collection<WeightedObject<TradeData>> weightedObjects = new ArrayList<>();
		relevantEntries.forEach(td -> weightedObjects.add(new WeightedObject<TradeData>(td, td.getStockData().get().restockData().get().restockWeight())));
		
		CQRWeightedRandom<TradeData> weightedRandom = new CQRWeightedRandom<>(weightedObjects);
		
		TradeData toRestock = weightedRandom.next(sl.getRandom());
		
		return toRestock;
	}

	public static CompoundTag saveToNBT(CompoundTag nbt, final TradeProfileInstance tpi) {
		if (tpi == null) {
			return nbt;
		}
		return tpi.saveToNBT(nbt);
	}
	
	public CompoundTag saveToNBT(CompoundTag nbt) {
		DataResult<Tag> dataResult = CODEC.encodeStart(NbtOps.INSTANCE, this);
		Optional<Tag> optResult = dataResult.result();
		if (optResult.isPresent()) {
			nbt.put(CQRConstants.NBT.KEY_TRADE_PROFILE_DATA, optResult.get());
			return nbt;
		} else {
			return nbt;
		}
	}
	
	@Nullable
	public static TradeProfileInstance readFromNBT(CompoundTag nbt, final Entity trader) {
		if (!nbt.contains(CQRConstants.NBT.KEY_TRADE_PROFILE_DATA)) {
			return null;
		}
		Tag nbtTag = nbt.get(CQRConstants.NBT.KEY_TRADE_PROFILE_DATA);
		DataResult<TradeProfileInstance> dataResult = CODEC.parse(NbtOps.INSTANCE, nbtTag);
		Optional<TradeProfileInstance> optResult = dataResult.result();
		if (optResult.isEmpty()) {
			return null;
		}
		
		TradeProfileInstance result = optResult.get();
		result.trader = new WeakReference<Entity>(trader);
		
		return result;
	}

}
