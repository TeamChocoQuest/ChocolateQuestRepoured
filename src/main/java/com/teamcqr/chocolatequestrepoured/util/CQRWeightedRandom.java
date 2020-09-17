package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

public class CQRWeightedRandom<T> {

	public static class WeightedObject<T> {
		private T object;
		private int weight;

		public WeightedObject(T object, int weight) {
			this.object = object;
			this.weight = weight;
		}
	}

	private static final Random RAND = new Random();
	private List<WeightedObject<T>> items = new ArrayList<>();
	private int totalWeight = 0;

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

	public CQRWeightedRandom<T> copy() {
		return new CQRWeightedRandom<>(this.items);
	}

}
