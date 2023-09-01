package team.cqr.cqrepoured.entity.trade;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class TradeProfile extends ArrayList<TradeData> {

	private static final long serialVersionUID = 1248553002763142894L;
	
	public static final Codec<TradeProfile> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				TradeData.CODEC.listOf().fieldOf("entries").forGetter(profile -> profile)
			).apply(instance, TradeProfile::new);
	});
	
	public TradeProfile(final List<TradeData> trades) {
		super(trades);
	}
	
	@Override
	public TradeData remove(int index) {
		return this.get(index);
	}
	
	@Override
	public void add(int index, TradeData element) {
	}
	
	@Override
	public TradeData set(int index, TradeData element) {
		return this.get(index);
	}
	
}
