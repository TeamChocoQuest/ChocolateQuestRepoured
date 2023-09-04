package team.cqr.cqrepoured.entity.trade;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.init.CQRDatapackLoaders;
import team.cqr.cqrepoured.util.CQRWeightedRandom;
import team.cqr.cqrepoured.util.CQRWeightedRandom.WeightedObject;
import team.cqr.cqrepoured.util.WeakReferenceLazyLoadField;

public class TradeProfileInstance {

	private final ResourceLocation referencedProfile;
	private WeakReference<Entity> trader;
	
	protected final WeakReferenceLazyLoadField<TradeProfile> TRADE_PROFILE = new WeakReferenceLazyLoadField<>(this::loadTradeProfile);
	protected final Map<Integer, TradeData> TRADE_OVERRIDES = new Int2ObjectArrayMap<>();
	protected final Map<Integer, Integer> TRADE_STOCK = new Int2IntArrayMap();
	
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
	
	@Nullable
	protected final TradeProfile loadTradeProfile() {
		if (this.referencedProfile == null) {
			return null;
		}
		return CQRDatapackLoaders.getTradeProfile(this.referencedProfile).get();
	}
	
	public TradeProfileInstance(final ResourceLocation referencedProfile, final Entity trader) {
		this.referencedProfile = referencedProfile;
		if (this.referencedProfile != null) {
			// Load directly
			this.TRADE_PROFILE.get();
		}
		this.trader = new WeakReference<Entity>(trader);
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
	protected TradeData getTradeAt(final int index) {
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
	
	public ItemStack getTradeResult(final int tradeIndex, final Entity customer, final ItemStack... inputs) {
		ItemStack result = ItemStack.EMPTY;
		
		TradeData tradeData = this.getTradeAt(tradeIndex);
		if (tradeData == null) {
			return result;
		}

		if (!this.isInStock(tradeIndex, customer)) {
			return result;
		}
		
		// Now, let's actually ask the trade if we can buy it...
		if (this.doesCustomerMeetRequirements(tradeIndex, customer) && this.doesInputMeetRequirements(tradeIndex, inputs)) {
			return tradeData.getResultingItem();
		}
		
		
		return result;
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
		
		Collection<WeightedObject<TradeData>> weightedObjects = new ArrayList<>();
		relevantEntries.forEach(td -> weightedObjects.add(new WeightedObject<TradeData>(td, td.getStockData().get().restockData().get().restockWeight())));
		
		CQRWeightedRandom<TradeData> weightedRandom = new CQRWeightedRandom<>(weightedObjects);
		
		TradeData toRestock = weightedRandom.next(sl.getRandom());
		int tradeIndex = this.TRADE_PROFILE.get().indexOf(toRestock);
		
		int currentStock = this.TRADE_STOCK.getOrDefault(tradeIndex, toRestock.getStockData().get().defaultStockSupplier().sample(sl.getRandom()));
		this.TRADE_STOCK.put(tradeIndex, currentStock + toRestock.getStockData().get().restockData().get().restockAmount());
	}
	
	// Saving & Loading
	
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
