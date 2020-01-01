package com.teamcqr.chocolatequestrepoured.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandom<T> {
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

	public WeightedRandom(Random random) {
		this.random = random;
		this.items = new ArrayList<>();
	}

	public void add(T item, int weight) {
		this.totalWeight += weight;
		this.items.add(new WeightedObject(item, weight));
	}

	public int numItems() {
		return this.items.size();
	}

	public void clear() {
		this.totalWeight = 0;
		this.items.clear();
	}

	public T next() {
		int seed = this.random.nextInt(this.totalWeight);
		int total = 0;
		for (WeightedObject item : this.items) {
			total += item.weight;
			if (total > seed) {
				return (T) item.object;
			}
		}

		return null;
	}
}
