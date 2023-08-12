package team.cqr.cqrepoured.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CQRWeightedRandom<T> {
	
	public static <T> Codec<CQRWeightedRandom<T>> createCodec(final Codec<T> innerCodec) {
		return RecordCodecBuilder.create(instance -> {
			return instance.group(
					WeightedObject.<T>createCodec(innerCodec).listOf().fieldOf("entries").forGetter(CQRWeightedRandom::getEntries)
			).apply(instance, CQRWeightedRandom::new);
		});
	}

	public static record WeightedObject<T>(T object, int weight) {
		public static <T> Codec<WeightedObject<T>> createCodec(final Codec<T> innerCodec) {
			return RecordCodecBuilder.create(instance -> {
				return instance.group(
						innerCodec.fieldOf("entry").forGetter(WeightedObject::object),
						Codec.INT.fieldOf("weight").forGetter(WeightedObject::weight)
				).apply(instance, WeightedObject::new);
			});
		}
	}

	private static final Random RAND = new Random();
	private List<WeightedObject<T>> items = new ArrayList<>();
	private int totalWeight = 0;
	
	public List<WeightedObject<T>> getEntries() {
		return this.items;
	}

	public CQRWeightedRandom() {

	}

	@SafeVarargs
	public CQRWeightedRandom(WeightedObject<T>... weightedObjects) {
		for (WeightedObject<T> weightedObject : weightedObjects) {
			this.add(weightedObject);
		}
	}

	public CQRWeightedRandom(Collection<WeightedObject<T>> weightedObjects) {
		for (WeightedObject<T> weightedObject : weightedObjects) {
			this.add(weightedObject);
		}
	}
	
	public CQRWeightedRandom(List<WeightedObject<T>> entries) {
		for (WeightedObject<T> wo : entries) {
			this.add(wo);
		}
	}

	public void add(T item, int weight) {
		this.add(new WeightedObject<>(item, weight));
	}

	public void add(WeightedObject<T> weightedObject) {
		if (weightedObject.weight > 0) {
			this.totalWeight += weightedObject.weight;
			this.items.add(weightedObject);
		}
	}

	public int numItems() {
		return this.items.size();
	}

	public void clear() {
		this.totalWeight = 0;
		this.items.clear();
	}

	@Nullable
	public T next() {
		return this.next(RAND);
	}

	@Nullable
	public T next(Random rand) {
		if (this.numItems() > 0) {
			if (this.totalWeight > 0) {
				int seed = rand.nextInt(this.totalWeight);
				int total = 0;
				for (WeightedObject<T> item : this.items) {
					total += item.weight;
					if (total > seed) {
						return item.object;
					}
				}

				return null;
			} else {
				return this.items.get(0).object;
			}
		} else {
			return null;
		}
	}

	public int getTotalWeight() {
		return this.totalWeight;
	}

	public CQRWeightedRandom<T> copy() {
		return new CQRWeightedRandom<>(this.items);
	}

}
