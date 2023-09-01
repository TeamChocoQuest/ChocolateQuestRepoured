package team.cqr.cqrepoured.entity.trade;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.valueproviders.IntProvider;

public record StockData(
		int minStock,
		int maxStock,
		IntProvider defaultStockSupplier,
		Optional<RestockData> restockData
	) {
	
	public static final Codec<StockData> CODEC = RecordCodecBuilder.create(instance -> {
			return instance.group(
					Codec.INT.fieldOf("min-stock").forGetter(StockData::minStock),
					Codec.INT.fieldOf("max-stock").forGetter(StockData::maxStock),
					IntProvider.CODEC.fieldOf("default-stock").forGetter(StockData::defaultStockSupplier),
					Codec.optionalField("restock", RestockData.CODEC).forGetter(StockData::restockData)
				).apply(instance, StockData::new);
		});

}
