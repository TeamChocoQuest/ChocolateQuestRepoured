package team.cqr.cqrepoured.entity.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RestockData(
		int restockAmount,
		int restockWeight,
		int restockRate
	) {
	
	public static final Codec<RestockData> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.INT.fieldOf("restock-amount").forGetter(RestockData::restockAmount),
				Codec.INT.fieldOf("restock-weight").forGetter(RestockData::restockWeight),
				Codec.INT.fieldOf("restock-rate").forGetter(RestockData::restockRate)
			).apply(instance, RestockData::new);
	});

}
