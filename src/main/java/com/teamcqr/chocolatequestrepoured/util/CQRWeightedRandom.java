package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

public class CQRWeightedRandom<T> {
	private class WeightedObject {
		Object object;
		int weight;

		private WeightedObject(Object object, int weight) {
			this.object = object;
			this.weight = weight;
		}
	}

	private Random random;
	private List<WeightedObject> items;
	private int totalWeight = 0;

	public CQRWeightedRandom(Random random) {
		this.random = random;
		this.items = new ArrayList<>();
	}

	public void add(T item, int weight) {
		if (weight > 0) {
			this.totalWeight += weight;
			this.items.add(new WeightedObject(item, weight));
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
		if (this.numItems() > 0) {
			if (this.totalWeight > 0) {
				int seed = this.random.nextInt(this.totalWeight);
				int total = 0;
				for (WeightedObject item : this.items) {
					total += item.weight;
					if (total > seed) {
						return (T) item.object;
					}
				}

				return null;
			} else {
				return (T) this.items.get(0);
			}
		} else {
			return null;
		}
	}
}
