package com.teamcqr.chocolatequestrepoured.util;

import net.minecraft.util.WeightedRandom;

public class WeightedItem<T> extends WeightedRandom.Item {

	public T item;

	public WeightedItem(T item, int itemWeight) {
		super(itemWeight);
		this.item = item;
	}

}
